package bytebrewers.bitpod.service.impl;

import bytebrewers.bitpod.entity.Transaction;
import bytebrewers.bitpod.repository.TransactionRepository;
import bytebrewers.bitpod.service.TransactionService;
import bytebrewers.bitpod.utils.dto.request.transaction.TransactionCreateDTO;
import bytebrewers.bitpod.utils.dto.request.transaction.TransactionSearchDTO;
import bytebrewers.bitpod.utils.specification.GeneralSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;


    @Override
    public Page<Transaction> getAll(Pageable pageable, TransactionSearchDTO req) {
        Specification<Transaction> specification = GeneralSpecification.getSpecification(req);
        return transactionRepository.findAll(specification, pageable);
    }

    @Override
    public Transaction create(TransactionCreateDTO req) {
        return null;
    }

    @Override
    public Transaction getById(String id) {
        return null;
    }

    @Override
    public void delete(String id) {

    }
}
