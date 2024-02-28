package bytebrewers.bitpod.service;

import bytebrewers.bitpod.entity.Transaction;
import bytebrewers.bitpod.utils.dto.request.transaction.TransactionCreateDTO;
import bytebrewers.bitpod.utils.dto.request.transaction.TransactionSearchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {
    Page<Transaction> getAll(Pageable pageable, TransactionSearchDTO req);
    Transaction create(TransactionCreateDTO req);
    Transaction getById(String id);
    void delete(String id);
}
