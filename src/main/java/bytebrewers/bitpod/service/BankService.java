package bytebrewers.bitpod.service;

import bytebrewers.bitpod.utils.dto.request.bank.BankSearchDTO;
import org.springframework.http.ResponseEntity;

public interface BankService {
    ResponseEntity<?> createBank(BankSearchDTO bankSearchDTO);

    ResponseEntity<?>  getById(Integer id);

    ResponseEntity<?> update(Integer id, BankSearchDTO bankSearchDTO);

    ResponseEntity<?> deleteById(Integer id);

    ResponseEntity<?> getCustomerPerPage(Integer page, Integer size, String sortBy, String direction, BankSearchDTO bankSearchDTO);


}
