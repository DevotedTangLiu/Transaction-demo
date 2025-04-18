package com.ltang.service.impl;

import com.ltang.entity.Transaction;
import com.ltang.exception.BusinessException;
import com.ltang.exception.TransactionNotFoundException;
import com.ltang.service.TransactionService;
import com.ltang.util.HashCodeHelper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Implementation of transaction service interface
 *
 * @author tangliu
 */
@Service
public class TransactionServiceImpl implements TransactionService {
    /**
     * user map to store transactions in the memory, just demo
     * should never use store in memory in real situation
     */
    private final Map<UUID, Transaction> transactions = new ConcurrentHashMap<>();

    private final Integer MAX_SIZE = 1000000;

    /**
     * make sure no duplicate transaction created
     * simplify the model, assume that same MD5(userId, amount, transaction type, desc) means identical
     *
     * @param transaction
     */
    private void checkDuplicate(Transaction transaction) {
        String hashCode = HashCodeHelper.calculateHashCode(transaction);
        if (transactions.values().stream()
                .anyMatch(t -> t.getHashCode() != null && t.getHashCode().equals(hashCode))) {
            throw new BusinessException("Transaction duplicated!");
        }
    }


    @Override
    public Transaction createTransaction(Transaction transaction) {

        // make sure do not exceed the volume limit, it might not happen in real world, but since we use memory to
        // hold all the datas, just in case
        if (transactions.size() > MAX_SIZE) {
            throw new BusinessException("System busy, please try later");
        }
        checkDuplicate(transaction);

        // other business logic, like check balance before withdraw, make sure have enough amount, calculate balance, etc

        transaction.setId(UUID.randomUUID());
        transaction.setTimestamp(LocalDateTime.now());
        HashCodeHelper.updateHashCode(transaction);
        transactions.put(transaction.getId(), transaction);
        return transaction;
    }

    @Override
    @CacheEvict(value = "transactions", key = "#id")
    public void deleteTransaction(UUID id) {
        if (!transactions.containsKey(id)) {
            throw new TransactionNotFoundException("Transaction not found with id: " + id);
        }
        transactions.remove(id);
    }

    /**
     * update transaction
     * <p>
     * actually we should not update some properties of a transaction in practice
     *
     * @param transaction
     * @return
     */
    @Override
    @CacheEvict(value = "transactions", key = "#transaction.id")
    public Transaction updateTransaction(Transaction transaction) {
        if (!transactions.containsKey(transaction.getId())) {
            throw new TransactionNotFoundException("Transaction not found with id: " + transaction.getId());
        }
        checkDuplicate(transaction);

        transaction.setTimestamp(LocalDateTime.now());
        HashCodeHelper.updateHashCode(transaction);
        transactions.put(transaction.getId(), transaction);
        return transaction;
    }

    @Override
    @Cacheable(value = "transactions", key = "#id")
    public Transaction getTransaction(UUID id) {
        return Optional.ofNullable(transactions.get(id))
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with id: " + id));
    }

    @Override
    public List<Transaction> getAllTransactions(int page, int size) {
        return transactions.values().stream()
                .sorted(Comparator.comparing(Transaction::getTimestamp).reversed())
                .skip((long) page * size)
                .limit(size)
                .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> getTransactionsByUserId(Long userId, int page, int size) {
        return transactions.values().stream()
                .filter(t -> t.getUserId().equals(userId))
                .sorted(Comparator.comparing(Transaction::getTimestamp).reversed())
                .skip((long) page * size)
                .limit(size)
                .collect(Collectors.toList());
    }
} 