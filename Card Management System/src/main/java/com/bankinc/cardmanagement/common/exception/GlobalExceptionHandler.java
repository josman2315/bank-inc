package com.bankinc.cardmanagement.common.exception;

import com.bankinc.cardmanagement.card.infrastructure.exception.CardAlreadyActivatedException;
import com.bankinc.cardmanagement.card.infrastructure.exception.CardBlockedException;
import com.bankinc.cardmanagement.card.infrastructure.exception.CardNotFoundException;
import com.bankinc.cardmanagement.transaction.infrastructure.exception.InsufficientBalanceException;
import com.bankinc.cardmanagement.transaction.infrastructure.exception.TransactionAlreadyAnnulledException;
import com.bankinc.cardmanagement.transaction.infrastructure.exception.TransactionAnnulationPeriodExceededException;
import com.bankinc.cardmanagement.transaction.infrastructure.exception.TransactionNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CardNotFoundException.class)
    public ResponseEntity<String> handleCardNotFoundException(CardNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(CardAlreadyActivatedException.class)
    public ResponseEntity<String> handleCardAlreadyActivatedException(CardAlreadyActivatedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(CardBlockedException.class)
    public ResponseEntity<String> handleCardBlockedException(CardBlockedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<String> handleInsufficientBalanceException(InsufficientBalanceException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<String> handleInsufficientBalanceException(TransactionNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(TransactionAlreadyAnnulledException.class)
    public ResponseEntity<String> handleInsufficientBalanceException(TransactionAlreadyAnnulledException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(TransactionAnnulationPeriodExceededException.class)
    public ResponseEntity<String> handleInsufficientBalanceException(TransactionAnnulationPeriodExceededException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}