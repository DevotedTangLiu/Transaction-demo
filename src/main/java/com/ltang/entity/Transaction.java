package com.ltang.entity;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Transaction entity
 *
 * @author tangliu
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    private UUID id;

    @Positive(message = "userId is required")
    private Long userId;

    @NotBlank(message = "Description cannot be blank")
    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotNull(message = "Type is required")
    private TransactionType type;

    private LocalDateTime timestamp;

    /**
     * hash code, in case of duplicate record
     */
    private String hashCode;

    /**
     * different types of transaction
     */
    public enum TransactionType {
        DEPOSIT,
        WITHDRAWAL
    }
} 