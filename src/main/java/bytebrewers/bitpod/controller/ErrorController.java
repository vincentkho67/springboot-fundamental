package bytebrewers.bitpod.controller;

import bytebrewers.bitpod.utils.dto.Res;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ErrorController {
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e) {
        return Res.renderJson(null, e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleResponseStatusException(ResponseStatusException e) {
        return Res.renderJson(null, e.getReason(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> handleNullPointerException(NullPointerException e) {
        String message = e.getMessage();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        if (e.getMessage().contains("Portfolio.getTransactions()")) {
            message = "No Transaction or Portfolio Found";
            status = HttpStatus.NOT_FOUND;
        }
        if (e.getMessage().contains("java.util.List.iterator()")) {
            message = "No Portfolio Found";
            status = HttpStatus.NOT_FOUND;
        }
        return Res.renderJson(null, message, status);
    }

}
