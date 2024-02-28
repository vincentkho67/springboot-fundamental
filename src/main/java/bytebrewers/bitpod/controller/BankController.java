package bytebrewers.bitpod.controller;

import bytebrewers.bitpod.entity.Bank;
import bytebrewers.bitpod.service.BankService;
import bytebrewers.bitpod.utils.constant.ApiUrl;
import bytebrewers.bitpod.utils.dto.PageResponseWrapper;
import bytebrewers.bitpod.utils.dto.Res;
import bytebrewers.bitpod.utils.dto.request.bank.BankDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiUrl.BASE_URL + ApiUrl.BASE_BANK)
@RequiredArgsConstructor
@Slf4j
public class BankController {
    private final BankService bankService;

    @GetMapping
    public ResponseEntity<?> index(
            @PageableDefault(page = 0, size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
            @ModelAttribute BankDTO bankDTO
    ) {
        Page<Bank> result = bankService.getAll(pageable, bankDTO);
        PageResponseWrapper<Bank> responseWrapper = new PageResponseWrapper<>(result);
        return Res.renderJson(responseWrapper, "Bank found", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable Integer id) {
        Bank bank = bankService.getById(id);
        return Res.renderJson(bank, "Bank found", HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody BankDTO bankDTO) {
        log.info("BankDTO: {}", bankDTO.getName());
        Bank newBank = bankService.create(bankDTO);
        return Res.renderJson(newBank, "Bank created", HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody BankDTO bankDTO) {
        Bank updatedBank = bankService.update(id, bankDTO);
        return Res.renderJson(updatedBank, "Bank updated", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        bankService.delete(id);
        return Res.renderJson(null, "Bank deleted", HttpStatus.OK);
    }

}
