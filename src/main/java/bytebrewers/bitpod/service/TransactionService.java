package bytebrewers.bitpod.service;

import bytebrewers.bitpod.entity.Transaction;
import bytebrewers.bitpod.utils.dto.request.transaction.TransactionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {
    Page<Transaction> getAll(Pageable pageable, TransactionDTO req);
    Transaction create(TransactionDTO req);
    Transaction update(String id, TransactionDTO req);
    Transaction getById(String id);
    void delete(String id);
}
