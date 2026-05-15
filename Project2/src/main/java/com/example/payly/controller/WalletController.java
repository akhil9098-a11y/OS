package com.example.payly.controller;

import com.example.payly.dto.WalletAddMoneyRequest;
import com.example.payly.dto.WalletResponse;
import com.example.payly.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @GetMapping("/balance")
    public ResponseEntity<WalletResponse> getBalance(Authentication authentication) {
        String username = authentication.getName();
        WalletResponse response = walletService.getBalance(username);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add-money")
    public ResponseEntity<WalletResponse> addMoney(@Valid @RequestBody WalletAddMoneyRequest request, Authentication authentication) {
        String username = authentication.getName();
        WalletResponse response = walletService.addMoney(username, request.getAmount());
        return ResponseEntity.ok(response);
    }
}