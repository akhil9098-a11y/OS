package com.example.payly.controller;

import com.example.payly.dto.TransferRequest;
import com.example.payly.dto.TransferResponse;
import com.example.payly.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transfer")
public class TransferController {

    @Autowired
    private TransferService transferService;

    @PostMapping("/send")
    public ResponseEntity<TransferResponse> sendMoney(@Valid @RequestBody TransferRequest request, Authentication authentication) {
        String username = authentication.getName();
        TransferResponse response = transferService.sendMoney(username, request);
        return ResponseEntity.ok(response);
    }
}