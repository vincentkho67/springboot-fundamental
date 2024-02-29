package bytebrewers.bitpod.service.impl;

import bytebrewers.bitpod.entity.Bank;
import bytebrewers.bitpod.entity.Portfolio;
import bytebrewers.bitpod.entity.Stock;
import bytebrewers.bitpod.entity.Transaction;
import bytebrewers.bitpod.repository.TransactionRepository;
import bytebrewers.bitpod.service.BankService;
import bytebrewers.bitpod.service.PortfolioService;
import bytebrewers.bitpod.service.StockService;
import bytebrewers.bitpod.service.TransactionService;
import bytebrewers.bitpod.utils.dto.request.transaction.TransactionDTO;
import bytebrewers.bitpod.utils.specification.GeneralSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final StockService stockService;
    private final PortfolioService portfolioService;
    private final BankService bankService;


    @Override
    public Page<Transaction> getAll(Pageable pageable, TransactionDTO req) {
        Specification<Transaction> specification = GeneralSpecification.getSpecification(req);
        return transactionRepository.findAll(specification, pageable);
    }

    @Transactional
    @Override
    public Transaction create(TransactionDTO req) {
        Stock stock = stockService.getById(req.getStockId());
        Portfolio portfolio = portfolioService.getById(req.getPortfolioId());
        Bank bank = bankService.getById(req.getBankId());
        Transaction newTransaction = req.toEntity(stock, portfolio, bank);
        return transactionRepository.save(newTransaction);
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
}
