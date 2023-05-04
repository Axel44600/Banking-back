package com.apply.banking.repository;

import com.apply.banking.dto.TransactionSumDetails;
import com.apply.banking.entity.Transaction;
import com.apply.banking.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByUserId(Long userId);

    @Query("select sum(t.amount) from Transaction t where t.user.id = :userId")
    BigDecimal findAccountBalance(@Param("userId") Long userId);

    @Query("select max(abs(t.amount)) as amount from Transaction  t where t.user.id = :userId and t.type = :transactiontype")
    BigDecimal findHighestAmountByTransactionType(@Param("userId") Long userId, @Param("transactiontype") TransactionType transactionType);

    @Query("select t.transactionDate as transactionDate, sum(t.amount) as amount from Transaction t where t.user.id = :userId and t.creationDate "
            + "between :start and :end "
            + "group by t.transactionDate")
    List<TransactionSumDetails> findSumTransactionsByDate(LocalDateTime start, LocalDateTime end, Long userId);
}
