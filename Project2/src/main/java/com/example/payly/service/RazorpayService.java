package com.example.payly.service;

import com.example.payly.dto.RazorpayOrderRequest;
import com.example.payly.dto.RazorpayOrderResponse;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class RazorpayService {

    @Value("${razorpay.key_id}")
    private String keyId;

    @Value("${razorpay.key_secret}")
    private String keySecret;

    public RazorpayOrderResponse createOrder(RazorpayOrderRequest request) throws RazorpayException {
        RazorpayClient razorpay = new RazorpayClient(keyId, keySecret);

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", request.getAmount().multiply(BigDecimal.valueOf(100)).intValue()); // Amount in paisa
        orderRequest.put("currency", request.getCurrency());
        orderRequest.put("receipt", "txn_" + System.currentTimeMillis());

        Order order = razorpay.orders.create(orderRequest);

        return new RazorpayOrderResponse(
                order.get("id"),
                request.getAmount(),
                request.getCurrency(),
                order.get("status")
        );
    }

    public boolean verifyPayment(String orderId, String paymentId, String signature) throws RazorpayException {
        RazorpayClient razorpay = new RazorpayClient(keyId, keySecret);

        JSONObject attributes = new JSONObject();
        attributes.put("razorpay_order_id", orderId);
        attributes.put("razorpay_payment_id", paymentId);
        attributes.put("razorpay_signature", signature);

        return razorpay.orders.fetch(orderId).get("status").equals("paid") &&
               com.razorpay.Utils.verifyPaymentSignature(attributes, keySecret);
    }
}