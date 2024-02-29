package bytebrewers.bitpod.controller;

import bytebrewers.bitpod.entity.Transaction;
import bytebrewers.bitpod.service.TransactionService;
import bytebrewers.bitpod.utils.constant.ApiUrl;
import bytebrewers.bitpod.utils.constant.Messages;
import bytebrewers.bitpod.utils.dto.PageResponseWrapper;
import bytebrewers.bitpod.utils.dto.Res;
import bytebrewers.bitpod.utils.dto.request.transaction.TransactionDTO;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiUrl.BASE_URL + ApiUrl.BASE_TRANSACTION)
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    @GetMapping
    public ResponseEntity<?> index(
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @ModelAttribute TransactionDTO transactionDTO
    ) {
        Page<Transaction> res = transactionService.getAll(pageable, transactionDTO);
        PageResponseWrapper<Transaction> responseWrapper = new PageResponseWrapper<>(res);
        return Res.renderJson(responseWrapper, Messages.TRANSACTION_FOUND, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable String id) {
        Transaction transaction = transactionService.getById(id);
        return Res.renderJson(transaction, Messages.TRANSACTION_FOUND, HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<?> create(@RequestBody TransactionDTO transactionDTO,
                                    @RequestHeader(name = "Authorization") String token) {
        Transaction newTransaction = transactionService.create(transactionDTO, token);
        return Res.renderJson(newTransaction, Messages.TRANSACTION_CREATED, HttpStatus.CREATED);
    }
   @DeleteMapping
    public ResponseEntity<?> delete(@PathVariable String id) {
        transactionService.delete(id);
        return Res.renderJson(null, Messages.TRANSACTION_DELETED, HttpStatus.OK);
    }
}
