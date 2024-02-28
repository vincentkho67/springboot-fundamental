package bytebrewers.bitpod.service.impl;

import bytebrewers.bitpod.entity.Bank;
import bytebrewers.bitpod.repository.BankRepository;
import bytebrewers.bitpod.service.BankService;
import bytebrewers.bitpod.utils.dto.Res;
import bytebrewers.bitpod.utils.dto.request.bank.BankSearchDTO;
import bytebrewers.bitpod.utils.specification.GeneralSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class BankServiceImpl implements BankService {

    private final BankRepository bankRepository;

    public BankServiceImpl(BankRepository bankRepository) {
        this.bankRepository = bankRepository;

    }



    @Override
    public ResponseEntity<?> createBank(BankSearchDTO bankSearchDTO) {
        Bank newBank = Bank.builder()
                .name(bankSearchDTO.getName())
                .address(bankSearchDTO.getAddress())
                .build();
        return Res.renderJson(newBank, "succes", HttpStatus.CREATED);

    }

    @Override
    public ResponseEntity<?> getById(Integer id) {
        Optional<Bank> optionalBank = bankRepository.findById(id);
        if (optionalBank.isPresent()) {
            return Res.renderJson(bankToBankDto(optionalBank.get()), "succes", HttpStatus.OK);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bank not found");
    }

    @Override
    public ResponseEntity<?> update(Integer id, BankSearchDTO bankSearchDTO) {
        Optional<Bank> optionalBank = validateId(id);
        Bank bank = optionalBank.get();
        bank.setName(bankSearchDTO.getName());
        bank.setAddress(bankSearchDTO.getAddress());
        bankRepository.save(bank);
        return Res.renderJson(bankToBankDto(bank), "succes", HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> deleteById(Integer id) {
        Optional<Bank> optionalBank = validateId(id);
        Bank bank = optionalBank.get();
        bankRepository.delete(bank);
        return Res.renderJson(bankToBankDto(bank), "succes", HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> getCustomerPerPage(Integer page, Integer size, String sortBy, String direction, BankSearchDTO bankSearchDTO) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Bank> allDataBank = bankRepository.findAll(GeneralSpecification.getSpecification(bankSearchDTO), pageable);

       return Res.renderJson(allDataBank,"Succes get all data",HttpStatus.OK);
    }

    private Optional<Bank> validateId(Integer id) {
        Optional<Bank> customerOptional = bankRepository.findById(id);
        if (customerOptional.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found");
        return customerOptional;
    }

    public BankSearchDTO bankToBankDto(Bank bank) {
        BankSearchDTO bankSearchDTO = new BankSearchDTO();
        bankSearchDTO.setName(bank.getName());
        bankSearchDTO.setAddress(bank.getAddress());
        return bankSearchDTO;
    }
}
