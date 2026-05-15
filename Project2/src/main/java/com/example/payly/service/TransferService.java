package com.example.payly.service;

import com.example.payly.dto.TransferRequest;
import com.example.payly.dto.TransferResponse;
import com.example.payly.entity.*;
import com.example.payly.repository.TransactionRepository;
import com.example.payly.repository.UserRepository;
import com.example.payly.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class TransferService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public TransferResponse sendMoney(String senderUsername, TransferRequest request) {
        User sender = userRepository.findByUsername(senderUsername)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findByUsername(request.getReceiverUsername())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        if (sender.getId().equals(receiver.getId())) {
            throw new RuntimeException("Cannot send money to yourself");
        }

        Wallet senderWallet = walletRepository.findByUser(sender)
                .orElseThrow(() -> new RuntimeException("Sender wallet not found"));
        Wallet receiverWallet = walletRepository.findByUser(receiver)
                .orElseThrow(() -> new RuntimeException("Receiver wallet not found"));

        if (senderWallet.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        // Deduct from sender
        senderWallet.setBalance(senderWallet.getBalance().subtract(request.getAmount()));
        // Add to receiver
        receiverWallet.setBalance(receiverWallet.getBalance().add(request.getAmount()));

        walletRepository.save(senderWallet);
        walletRepository.save(receiverWallet);

        // Create transaction
        String transactionId = UUID.randomUUID().toString();
        Transaction transaction = new Transaction(sender, receiver, request.getAmount(), TransactionStatus.SUCCESS);
        transaction.setTransactionId(transactionId);
        transactionRepository.save(transaction);

        return new TransferResponse(transactionId, request.getAmount(), senderUsername, request.getReceiverUsername(), TransactionStatus.SUCCESS, transaction.getCreatedAt());
    }
}