package com.example.payly.dto;

import com.example.payly.entity.TransactionStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransferResponse {

    private String transactionId;
    private BigDecimal amount;
    private String senderUsername;
    private String receiverUsername;
    private TransactionStatus status;
    private LocalDateTime timestamp;

    public TransferResponse(String transactionId, BigDecimal amount, String senderUsername, String receiverUsername, TransactionStatus status, LocalDateTime timestamp) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
        this.status = status;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}