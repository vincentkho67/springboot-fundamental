package bytebrewers.bitpod.service.impl;

import bytebrewers.bitpod.entity.*;
import bytebrewers.bitpod.repository.PortfolioRepository;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.PageImpl;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final StockService stockService;
    private final PortfolioService portfolioService;
    private final BankService bankService;
    private final UserService userService;
    private final PortfolioRepository portfolioRepository;

    @Override
    public Page<Transaction> getAll(Pageable pageable, TransactionDTO req) {
        Specification<Transaction> specification = GeneralSpecification.getSpecification(req);
        return transactionRepository.findAll(specification, pageable);
    }

    @Transactional
    @Override
    public Transaction create(TransactionDTO req, String token) {
        User user = userService.getUserDetails(token);
        Stock stock = stockService.getById(req.getStockId());
        req.setPrice(stock.getPrice());
        Bank bank = bankService.getById(req.getBankId());

        Portfolio portfolio = portfolioService.getByUser(user);
        if(portfolio == null) {
            PortfolioDTO portfolioDTO = new PortfolioDTO();
            portfolio = portfolioService.create(portfolioDTO, user);
        }

        Transaction newTransaction = req.toEntity(stock, portfolio, bank);
        transactionRepository.save(newTransaction);
        // handle user balance
        assert user != null;
        userBalance(user, BigDecimal.valueOf(req.getPrice() * req.getLot() * 100), req.getTransactionType());
        updatePortfolio(portfolio);
        if (newTransaction.getTransactionType() == ETransactionType.SELL) {
            handleSell(portfolio, req);
        }
        if (portfolio.getTransactions() != null) {
            reCheckPortfolio(portfolio);
        }
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

    @Override
    public Page<Transaction> getAllByUser(Pageable pageable, String token) {
        List<Transaction> res = portfolioService.currentUser(token).getTransactions();
        return wrap(res, pageable);
    }

    @Override
    public Transaction getTransactionByCurrentUser(String token, String id) {
        User user = userService.getUserDetails(token);
        List<Transaction> t = portfolioService.getByUser(user).getTransactions();

        return t.stream().filter(transaction -> transaction.getId().equals(id)).findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));
    }

    private void updatePortfolio(Portfolio portfolio) {
        // Retrieve all transactions associated with the portfolio
        List<Transaction> transactions = transactionRepository.findByPortfolio(portfolio);

        // Calculate avgBuy and returns based on the transactions
        BigDecimal totalValue = BigDecimal.ZERO;
        int totalQuantity = 0;

        for (Transaction t : transactions) {
            if (t.getTransactionType() == ETransactionType.BUY && t.getLot() > 0) {
                totalQuantity++;
                // Calculate total value
                BigDecimal transactionValue = BigDecimal.valueOf(t.getPrice());
                totalValue = totalValue.add(transactionValue);
            }
        }
        // Update avgBuy
        BigDecimal avgBuy = totalQuantity == 0
                ? BigDecimal.ZERO
                : totalValue.divide(BigDecimal.valueOf(totalQuantity), 2, RoundingMode.HALF_UP);

        PortfolioDTO portfolioDTO = new PortfolioDTO(avgBuy, null);
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
    private void handleSell(Portfolio portfolio,TransactionDTO req) {
        List<Transaction> trans = portfolio.getTransactions();
        String stockId = req.getStockId();
        List<Transaction> buyTransactions = new ArrayList<>();


        for (Transaction t : trans) {
            if (t.getTransactionType() == ETransactionType.BUY && t.getStock().getId().equals(stockId)) {
                buyTransactions.add(t);
            }
        }
        updateBuyTransactions(buyTransactions, req.getLot());
    }
    private void updateBuyTransactions(List<Transaction> req, int remainingLotAmount) {
        int toBeRemoved = remainingLotAmount;
        for (Transaction t : req) {
            while (toBeRemoved > 0 && t.getLot() > 0) {
                if (toBeRemoved >= t.getLot()) {
                    toBeRemoved -= t.getLot();
                    t.setLot(0);
                } else {
                    t.setLot(t.getLot() - toBeRemoved);
                    toBeRemoved = 0;
                    break;
                }
            }
            if (toBeRemoved == 0) {
                break;
            }
        }

        if (toBeRemoved > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient lot amount to sell");
        }
        req.forEach(transactionRepository::save);
    }
    private void reCheckPortfolio(Portfolio req) {
        List<Transaction> trans = req.getTransactions();
        List<Transaction> toBeUpdated = new ArrayList<>();

        for (Transaction t : trans) {
            if (t.getTransactionType() == ETransactionType.BUY && t.getLot() > 0) {
                toBeUpdated.add(t);
            }
        }

        if (toBeUpdated.isEmpty()) {
            portfolioService.update(req.getId(), new PortfolioDTO(BigDecimal.valueOf(0), null), req.getUser());
        }
    }
    private Page<Transaction> wrap(List<Transaction> transactions, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<Transaction> pageList;

        if (startItem < transactions.size()) {
            int endItem = Math.min(startItem + pageSize, transactions.size());
            pageList = transactions.subList(startItem, endItem);
        } else {
            pageList = Collections.emptyList();
        }

        return new PageImpl<>(pageList, pageable, transactions.size());
    }
}
