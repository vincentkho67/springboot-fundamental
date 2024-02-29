package bytebrewers.bitpod.service;

import bytebrewers.bitpod.entity.Portfolio;
import bytebrewers.bitpod.entity.Transaction;
import bytebrewers.bitpod.utils.dto.request.transaction.TransactionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransactionService {
    Page<Transaction> getAll(Pageable pageable, TransactionDTO req);
    Transaction create(TransactionDTO req, String token);
    Transaction getById(String id);
    void delete(String id);
    Page<Transaction> getAllByUser(Pageable pageable,String token);
    Transaction getTransactionByCurrentUser(String token, String id);
}
