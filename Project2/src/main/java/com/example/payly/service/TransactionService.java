package com.example.payly.service;

import com.example.payly.dto.TransactionHistoryResponse;
import com.example.payly.entity.Transaction;
import com.example.payly.entity.User;
import com.example.payly.repository.TransactionRepository;
import com.example.payly.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    public TransactionHistoryResponse getTransactionHistory(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Transaction> transactions = transactionRepository.findBySenderOrReceiverOrderByCreatedAtDesc(user, user);

        List<TransactionHistoryResponse.TransactionDto> dtos = transactions.stream()
                .map(t -> new TransactionHistoryResponse.TransactionDto(
                        t.getTransactionId(),
                        t.getAmount(),
                        t.getSender() != null ? t.getSender().getUsername() : null,
                        t.getReceiver().getUsername(),
                        t.getStatus(),
                        t.getCreatedAt()
                ))
                .collect(Collectors.toList());

        return new TransactionHistoryResponse(dtos);
    }
}