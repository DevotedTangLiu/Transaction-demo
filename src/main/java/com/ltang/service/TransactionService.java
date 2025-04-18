package com.ltang.service;

import com.ltang.entity.Transaction;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.UUID;

/**
 * TransactionService interface
 *
 * @author tangliu
 */
public interface TransactionService {

    /**
     * create transaction
     *
     * @param transaction
     * @return
     */
    Transaction createTransaction(Transaction transaction);

    /**
     * delete transaction by id
     *
     * @param id
     */
    void deleteTransaction(UUID id);

    /**
     * update transaction by id
     *
     * @param transaction
     * @return
     */
    Transaction updateTransaction(Transaction transaction);

    /**
     * get transaction by id
     *
     * @param id
     * @return
     */
    Transaction getTransaction(UUID id);

    /**
     * query all transactions
     *
     * @param page
     * @param size
     * @return
     */
    List<Transaction> getAllTransactions(int page, int size);

    /**
     * query transactions by userId
     *
     * @param userId
     * @param page
     * @param size
     * @return
     */
    List<Transaction> getTransactionsByUserId(Long userId, int page, int size);
} 