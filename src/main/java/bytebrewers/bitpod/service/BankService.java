package bytebrewers.bitpod.service;

import bytebrewers.bitpod.entity.Bank;
import bytebrewers.bitpod.utils.dto.request.bank.BankDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface BankService {
    Page<Bank> getAll(Pageable pageable, BankDTO bankDTO);
    Bank create(BankDTO bankDTO);
    Bank getById(Integer id);
    Bank update(Integer id, BankDTO bankDTO);
    void delete(Integer id);
}
