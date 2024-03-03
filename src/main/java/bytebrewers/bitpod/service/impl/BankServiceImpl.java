package bytebrewers.bitpod.service.impl;

import bytebrewers.bitpod.entity.Auditable;
import bytebrewers.bitpod.entity.Bank;
import bytebrewers.bitpod.repository.BankRepository;
import bytebrewers.bitpod.service.BankService;
import bytebrewers.bitpod.utils.dto.request.bank.BankDTO;
import bytebrewers.bitpod.utils.helper.EntityUpdater;
import bytebrewers.bitpod.utils.specification.GeneralSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BankServiceImpl implements BankService {
    private final BankRepository bankRepository;

    @Override
    public Page<Bank> getAll(Pageable pageable, BankDTO bankDTO) {
        Specification<Bank> specification = GeneralSpecification.getSpecification(bankDTO);
        return bankRepository.findAll(specification, pageable);
    }

    @Override
    public Bank create(BankDTO bankDTO) {
        return bankRepository.save(bankDTO.toEntity());
    }

    @Override
    public Bank getById(Integer id) {
        return Auditable.searchById(bankRepository.findById(id), "Bank not found");
    }

    @Override
    public Bank update(Integer id, BankDTO bankDTO) {
        Bank existingBank = Auditable.searchById(bankRepository.findById(id), "Bank not found");
        EntityUpdater.updateEntity(existingBank, bankDTO.toEntity());
        return bankRepository.save(existingBank);
    }

    @Override
    public void delete(Integer id) {
//        Auditable.searchById(bankRepository.findById(id), "Bank not found");
        bankRepository.deleteById(id);
    }
}
