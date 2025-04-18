package com.ltang.service;

import com.ltang.exception.TransactionNotFoundException;
import com.ltang.entity.Transaction;
import com.ltang.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Test
 *
 * @author tangliu
 */
class TransactionServiceTest {

    private TransactionService transactionService;
    private Transaction testTransaction;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionServiceImpl();
        testTransaction = new Transaction();
        testTransaction.setDescription("Test Transaction");
        testTransaction.setAmount(new BigDecimal("100.00"));
        testTransaction.setType(Transaction.TransactionType.DEPOSIT);
        testTransaction.setUserId(1L);
    }

    @Test
    void createTransaction_ShouldCreateNewTransaction() {
        Transaction created = transactionService.createTransaction(testTransaction);

        assertNotNull(created.getId());
        assertEquals(testTransaction.getDescription(), created.getDescription());
        assertEquals(testTransaction.getAmount(), created.getAmount());
        assertEquals(testTransaction.getType(), created.getType());
        assertEquals(testTransaction.getUserId(), created.getUserId());
        assertNotNull(created.getTimestamp());
    }

    @Test
    void getTransaction_ShouldReturnExistingTransaction() {
        Transaction created = transactionService.createTransaction(testTransaction);
        Transaction retrieved = transactionService.getTransaction(created.getId());

        assertEquals(created, retrieved);
    }

    @Test
    void getTransaction_ShouldThrowExceptionForNonExistentTransaction() {
        assertThrows(TransactionNotFoundException.class, () ->
                transactionService.getTransaction(UUID.randomUUID()));
    }

    @Test
    void updateTransaction_ShouldUpdateExistingTransaction() {
        Transaction created = transactionService.createTransaction(testTransaction);
        created.setDescription("Updated Description");

        Transaction updated = transactionService.updateTransaction(created);

        assertEquals("Updated Description", updated.getDescription());
        assertEquals(created.getId(), updated.getId());
    }

    @Test
    void deleteTransaction_ShouldRemoveTransaction() {
        Transaction created = transactionService.createTransaction(testTransaction);
        transactionService.deleteTransaction(created.getId());

        assertThrows(TransactionNotFoundException.class, () ->
                transactionService.getTransaction(created.getId()));
    }

    @Test
    void getAllTransactions_ShouldReturnPaginatedResults() {
        // Create multiple transactions
        for (int i = 0; i < 15; i++) {
            Transaction transaction = new Transaction();
            transaction.setDescription("Transaction " + i);
            transaction.setAmount(new BigDecimal("100.00"));
            transaction.setType(Transaction.TransactionType.DEPOSIT);
            transaction.setUserId(1L);
            transactionService.createTransaction(transaction);
        }

        List<Transaction> firstPage = transactionService.getAllTransactions(0, 10);
        List<Transaction> secondPage = transactionService.getAllTransactions(1, 10);

        assertEquals(10, firstPage.size());
        assertEquals(5, secondPage.size());
    }

    @Test
    void getTransactionsByUserId_ShouldReturnFilteredResults() {
        // Create transactions for different accounts
        for (int i = 0; i < 5; i++) {
            Transaction transaction = new Transaction();
            transaction.setDescription("Transaction " + i);
            transaction.setAmount(new BigDecimal("100.00"));
            transaction.setType(Transaction.TransactionType.DEPOSIT);
            transaction.setUserId(i % 2 == 0 ? 1L : 2L);
            transactionService.createTransaction(transaction);
        }

        List<Transaction> filteredTransactions = transactionService.getTransactionsByUserId(1L, 0, 10);

        assertEquals(3, filteredTransactions.size());
        assertTrue(filteredTransactions.stream().allMatch(t -> t.getUserId().equals(1L)));
    }
} 