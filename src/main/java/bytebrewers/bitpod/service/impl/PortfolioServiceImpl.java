package bytebrewers.bitpod.service.impl;

import bytebrewers.bitpod.entity.Auditable;
import bytebrewers.bitpod.entity.Portfolio;
import bytebrewers.bitpod.entity.Transaction;
import bytebrewers.bitpod.entity.User;
import bytebrewers.bitpod.repository.PortfolioRepository;
import bytebrewers.bitpod.security.JwtUtils;
import bytebrewers.bitpod.service.PortfolioService;
import bytebrewers.bitpod.service.StockService;
import bytebrewers.bitpod.service.TransactionService;
import bytebrewers.bitpod.service.UserService;
import bytebrewers.bitpod.utils.constant.Messages;
import bytebrewers.bitpod.utils.dto.request.portfolio.PortfolioDTO;
import bytebrewers.bitpod.utils.dto.request.stock.StockDTO;
import bytebrewers.bitpod.utils.dto.response.user.JwtClaim;
import bytebrewers.bitpod.utils.enums.ETransactionType;
import bytebrewers.bitpod.utils.helper.EntityUpdater;
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
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PortfolioServiceImpl implements PortfolioService {
    private final PortfolioRepository portfolioRepository;
    private final JwtUtils jwt;
    private final UserService userService;
    private final StockService stockService;
    @Override
    public Page<Portfolio> getAll(Pageable pageable, PortfolioDTO portfolioDTO) {
        Specification<Portfolio> specification = GeneralSpecification.getSpecification(portfolioDTO);
        return portfolioRepository.findAll(specification, pageable);
    }
    @Override
    public Portfolio create(PortfolioDTO portfolioDTO, User cred) {
        return portfolioRepository.save(portfolioDTO.toEntity(cred));
    }
    @Override
    public Portfolio getById(String id) {
        return Auditable.searchById(portfolioRepository.findById(id), "Portfolio not found");
    }
    @Override
    public Portfolio update(String id, PortfolioDTO portfolioDTO, User cred) {
        Portfolio existingPortfolio =  Auditable.searchById(portfolioRepository.findById(id), "Portfolio not found");
        EntityUpdater.updateEntity(existingPortfolio, portfolioDTO.toEntity(cred));
        return portfolioRepository.save(existingPortfolio);
    }
    @Override
    public void delete(String id) {
        Auditable.searchById(portfolioRepository.findById(id), "Portfolio not found");
        portfolioRepository.deleteById(id);
    }

    @Override
    public Portfolio getByUser(User user) {
        return portfolioRepository.findByUser(user);
    }

    @Override
    public Portfolio currentUser(String token) {
        User user = userService.getUserDetails(token);
        Portfolio port = getByUser(user);
        String returns = setReturns(port.getTransactions());
        port.setReturns(returns);
        return port;
    }

    private String setReturns(List<Transaction> transactions) {
        List<StockDTO> stocks = stockService.fetch();
        BigDecimal totalGain = BigDecimal.ZERO;
        for (Transaction t : transactions) {
            if (t.getTransactionType() == ETransactionType.BUY) {
                String stockName = t.getStock().getName();
                Optional<StockDTO> matchingStock = stocks.stream()
                        .filter(s -> s.getName().equals(stockName))
                        .findFirst();

                if (matchingStock.isPresent()) {
                    BigDecimal transactionPrice = BigDecimal.valueOf(t.getPrice() / 100);
                    BigDecimal stockClose = BigDecimal.valueOf(matchingStock.get().getPrice());

                    // calculation for percentages
                    BigDecimal percentageGain = calculatePercentGain(transactionPrice, stockClose);
                    totalGain = totalGain.add(percentageGain);
                }
            }
        }
        if (totalGain.compareTo(BigDecimal.ZERO) >= 0) {
            return "Gain " + totalGain + "%";
        } else {
            return "Loss " + totalGain.abs() + "%";
        }
    }

    private BigDecimal calculatePercentGain(BigDecimal latestValue, BigDecimal currentValue) {
        return currentValue.subtract(latestValue).divide(latestValue, 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }
}
