package bytebrewers.bitpod.controller;

import bytebrewers.bitpod.service.BankService;
import bytebrewers.bitpod.utils.constant.ApiUrl;
import bytebrewers.bitpod.utils.dto.request.bank.BankSearchDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiUrl.BASE_URL + ApiUrl.BASE_BANK)
@RequiredArgsConstructor
public class BankController {

    private final BankService bankService;


    @PostMapping
    public ResponseEntity<?> createNewBank(@RequestBody BankSearchDTO bankSearchDTO) {
        return bankService.createBank(bankSearchDTO);
    }

    @GetMapping
    public ResponseEntity<?> getPerPage(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                        @RequestParam(name = "size", defaultValue = "5") Integer size,
                                        @RequestParam(name = "sort-by", defaultValue = "name") String sortBy,
                                        @RequestParam(name = "direction", defaultValue = "ASC") String direction,
                                        @ModelAttribute BankSearchDTO bankSearchDTO){
        return bankService.getCustomerPerPage(page, size, sortBy, direction, bankSearchDTO);
    }
    @GetMapping("{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Integer id) {
        return bankService.getById(id);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Integer id, @RequestBody BankSearchDTO bankSearchDTO) {
        return bankService.update(id, bankSearchDTO);

    }

    @DeleteMapping("{id}")
    public void deleteCustomerById(@PathVariable Integer id) {
        bankService.deleteById(id);
    }

}
