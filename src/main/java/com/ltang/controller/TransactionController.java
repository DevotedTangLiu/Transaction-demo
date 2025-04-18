package com.ltang.controller;

import com.ltang.entity.Transaction;
import com.ltang.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Transaction operation api
 *
 * @author tangliu
 */
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;


    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * create transaction
     * 1. validate parameters
     * 2. guarantee idempotence (just show the idea)
     *
     * @param transaction
     * @return
     */
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@Valid @RequestBody Transaction transaction) {
        return new ResponseEntity<>(transactionService.createTransaction(transaction), HttpStatus.CREATED);
    }

    /**
     * delete transaction by id
     * <p>
     * actually we should never delete a transaction in the db, that might cause problem,
     * we should maybe update the visible/disabled/deleted status
     * but nevertheless just a demo
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable UUID id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * update transaction by id
     *
     * @param id
     * @param transaction
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable UUID id, @Valid @RequestBody Transaction transaction) {
        transaction.setId(id);
        return ResponseEntity.ok(transactionService.updateTransaction(transaction));
    }

    /**
     * query transaction by id
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable UUID id) {
        return ResponseEntity.ok(transactionService.getTransaction(id));
    }

    /**
     * query all transactions
     *
     * @param page
     * @param size
     * @return
     */
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(transactionService.getAllTransactions(page, size));
    }

    /**
     * query transactions by userId
     *
     * @param userId
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/account/{userId}")
    public ResponseEntity<List<Transaction>> getTransactionsByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(transactionService.getTransactionsByUserId(userId, page, size));
    }
} 