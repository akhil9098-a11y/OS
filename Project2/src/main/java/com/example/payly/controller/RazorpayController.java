package com.example.payly.controller;

import com.example.payly.dto.PaymentVerificationRequest;
import com.example.payly.dto.RazorpayOrderRequest;
import com.example.payly.dto.RazorpayOrderResponse;
import com.example.payly.service.RazorpayService;
import com.razorpay.RazorpayException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class RazorpayController {

    @Autowired
    private RazorpayService razorpayService;

    @PostMapping("/create-order")
    public ResponseEntity<RazorpayOrderResponse> createOrder(@Valid @RequestBody RazorpayOrderRequest request) throws RazorpayException {
        RazorpayOrderResponse response = razorpayService.createOrder(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyPayment(@Valid @RequestBody PaymentVerificationRequest request) throws RazorpayException {
        boolean isValid = razorpayService.verifyPayment(request.getOrderId(), request.getPaymentId(), request.getSignature());
        if (isValid) {
            return ResponseEntity.ok("Payment verified successfully");
        } else {
            return ResponseEntity.badRequest().body("Payment verification failed");
        }
    }
}