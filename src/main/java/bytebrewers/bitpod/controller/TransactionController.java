package bytebrewers.bitpod.controller;

import bytebrewers.bitpod.entity.Transaction;
import bytebrewers.bitpod.service.TransactionService;
import bytebrewers.bitpod.utils.constant.ApiUrl;
import bytebrewers.bitpod.utils.constant.Messages;
import bytebrewers.bitpod.utils.dto.PageResponseWrapper;
import bytebrewers.bitpod.utils.dto.Res;
import bytebrewers.bitpod.utils.dto.request.transaction.TransactionDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiUrl.BASE_URL + ApiUrl.BASE_TRANSACTION)
@RequiredArgsConstructor
@Tag(name = "Transaction", description = "Transaction API")
public class TransactionController {
    private final TransactionService transactionService;

    @Operation(
        description = "Get all transaction",
        summary = "Get all transaction (ADMIN ONLY)"
    )
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @GetMapping
    public ResponseEntity<?> index(
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @ModelAttribute TransactionDTO transactionDTO
    ) {
        Page<Transaction> res = transactionService.getAll(pageable, transactionDTO);
        PageResponseWrapper<Transaction> responseWrapper = new PageResponseWrapper<>(res);
        return Res.renderJson(responseWrapper, Messages.TRANSACTION_FOUND, HttpStatus.OK);
    }

    @Operation(
        description = "Get transaction by id",
        summary = "Get transaction by id"
    )
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable String id) {
        Transaction transaction = transactionService.getById(id);
        return Res.renderJson(transaction, Messages.TRANSACTION_FOUND, HttpStatus.OK);
    }

    @Operation(
        description = "Get transaction history",
        summary = "Get transaction history"
    )
    @GetMapping("/history")
    public ResponseEntity<?> transactionHistory(@PageableDefault(page = 0, size = 2, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                @RequestHeader(name = "Authorization") String token) {
        Page<Transaction> res = transactionService.getAllByUser(pageable, token);
        PageResponseWrapper<Transaction> responseWrapper = new PageResponseWrapper<>(res);
        return Res.renderJson(responseWrapper, Messages.TRANSACTION_FOUND, HttpStatus.OK);
    }

    @Operation(
        description = "Get transaction history by id",
        summary = "Get transaction history by id"
    )
    @GetMapping("/history/{id}")
    public ResponseEntity<?> transactionHistoryById(@PathVariable String id, @RequestHeader(name = "Authorization") String token) {
        return Res.renderJson(transactionService.getTransactionByCurrentUser(token, id), Messages.TRANSACTION_FOUND, HttpStatus.OK);
    }

    @Operation(
        description = "Create transaction",
        summary = "Create transaction"
    )
    @PostMapping("/buy-or-sell")
    public ResponseEntity<?> create(@RequestBody TransactionDTO transactionDTO,
                                    @RequestHeader(name = "Authorization") String token) {
        Transaction newTransaction = transactionService.create(transactionDTO, token);
        return Res.renderJson(newTransaction, Messages.TRANSACTION_CREATED, HttpStatus.CREATED);
    }

    @Operation(
        description = "Delete transaction",
        summary = "Delete transaction (ADMIN ONLY)"
    )
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
   @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        transactionService.delete(id);
        return Res.renderJson(null, Messages.TRANSACTION_DELETED, HttpStatus.OK);
    }
}
