package com.example.payly.repository;

import com.example.payly.entity.Transaction;
import com.example.payly.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySenderOrReceiverOrderByCreatedAtDesc(User sender, User receiver);
}