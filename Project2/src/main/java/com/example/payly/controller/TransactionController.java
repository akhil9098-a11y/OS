package com.example.payly.controller;

import com.example.payly.dto.TransactionHistoryResponse;
import com.example.payly.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/history")
    public ResponseEntity<TransactionHistoryResponse> getTransactionHistory(Authentication authentication) {
        String username = authentication.getName();
        TransactionHistoryResponse response = transactionService.getTransactionHistory(username);
        return ResponseEntity.ok(response);
    }
}