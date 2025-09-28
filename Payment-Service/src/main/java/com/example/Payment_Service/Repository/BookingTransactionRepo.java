package com.example.Payment_Service.Repository;

import com.example.Payment_Service.Entity.BookingTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingTransactionRepo extends JpaRepository<BookingTransaction,Integer> {
    BookingTransaction findByTransactionID(String transactionID);
}
