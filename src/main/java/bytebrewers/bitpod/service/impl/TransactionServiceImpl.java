package bytebrewers.bitpod.service.impl;

import bytebrewers.bitpod.entity.*;
import bytebrewers.bitpod.repository.TransactionRepository;
import bytebrewers.bitpod.security.JwtUtils;
import bytebrewers.bitpod.service.*;
import bytebrewers.bitpod.utils.dto.request.portfolio.PortfolioDTO;
import bytebrewers.bitpod.utils.dto.request.transaction.TransactionDTO;
import bytebrewers.bitpod.utils.dto.response.user.JwtClaim;
import bytebrewers.bitpod.utils.enums.ETransactionType;
import bytebrewers.bitpod.utils.specification.GeneralSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final StockService stockService;
    private final PortfolioService portfolioService;
    private final BankService bankService;
    private final JwtUtils jwt;
    private final UserService userService;


    @Override
    public Page<Transaction> getAll(Pageable pageable, TransactionDTO req) {
        Specification<Transaction> specification = GeneralSpecification.getSpecification(req);
        return transactionRepository.findAll(specification, pageable);
    }

    @Transactional
    @Override
    public Transaction create(TransactionDTO req, String token) {
        User user = getUserDetails(token);
        Stock stock = stockService.getById(req.getStockId());
        req.setPrice(stock.getPrice());

        Portfolio portfolio = portfolioService.getByUser(user);
        if(portfolio == null) {
            PortfolioDTO portfolioDTO = new PortfolioDTO();
            portfolio = portfolioService.create(portfolioDTO, user);
        }

        Bank bank = bankService.getById(req.getBankId());
        Transaction newTransaction = req.toEntity(stock, portfolio, bank);
        transactionRepository.save(newTransaction);
        // handle user balance
        userBalance(user, BigDecimal.valueOf(req.getPrice() * req.getLot() * 100), req.getTransactionType());
        updatePortfolio(portfolio);
        return newTransaction;
    }

    @Override
    public Transaction getById(String id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));
    }

    @Override
    public void delete(String id) {
        transactionRepository.deleteById(id);
    }

    private String parseJwt(String token) {
        if(token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }
    private User getUserDetails(String token) {
        String parsedToken = parseJwt(token);
        if(parsedToken != null) {
            JwtClaim user = jwt.getUserInfoByToken(parsedToken);
            return userService.loadByUserId(user.getUserId());
        }
        return null;
    }

    private void updatePortfolio(Portfolio portfolio) {
        // Retrieve all transactions associated with the portfolio
        List<Transaction> transactions = transactionRepository.findByPortfolio(portfolio);

        // Calculate avgBuy and returns based on the transactions
        BigDecimal totalValue = BigDecimal.ZERO;
        BigDecimal totalQuantity = BigDecimal.valueOf(transactions.size());

        for (Transaction t : transactions) {
            if (t.getTransactionType() == ETransactionType.BUY) {
                // Calculate total value
                BigDecimal transactionValue = BigDecimal.valueOf(t.getPrice());
                totalValue = totalValue.add(transactionValue);
            }
        }

        // Update avgBuy
        BigDecimal avgBuy = totalQuantity.compareTo(BigDecimal.ZERO) == 0
                ? BigDecimal.ZERO
                : totalValue.divide(totalQuantity, 2, RoundingMode.HALF_UP);

        portfolio.setAvgBuy(avgBuy);

        PortfolioDTO portfolioDTO = new PortfolioDTO(portfolio.getAvgBuy(), null);
        portfolioService.update(portfolio.getId(), portfolioDTO, portfolio.getUser());
    }

    private void userBalance(User user, BigDecimal amount, String type) {
        BigDecimal initialBalance = user.getBalance();
        if (ETransactionType.BUY.name().equalsIgnoreCase(type)) {
            if (initialBalance.compareTo(amount) < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient balance");
            }
            user.setBalance(initialBalance.subtract(amount));
        } else if (ETransactionType.SELL.name().equalsIgnoreCase(type)) {
            user.setBalance(initialBalance.add(amount));
        }
    }
}
