package com.apply.banking.service.impl;

import com.apply.banking.dto.TransactionDto;
import com.apply.banking.entity.Transaction;
import com.apply.banking.entity.TransactionType;
import com.apply.banking.exceptions.ObjectsValidator;
import com.apply.banking.repository.TransactionRepository;
import com.apply.banking.service.TransactionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final ObjectsValidator<TransactionDto> validator;

    @Override
    public Long save(TransactionDto dto) {
        validator.validate(dto);
        Transaction transaction = TransactionDto.toEntity(dto);
        BigDecimal transactionMultiplier = BigDecimal.valueOf(getTransactionMultiplier(transaction.getType()));
        BigDecimal amount = transaction.getAmount().multiply(transactionMultiplier);
        transaction.setAmount(amount);
        return transactionRepository.save(transaction).getId();
    }

    @Override
    public List<TransactionDto> findAll() {
        return transactionRepository.findAll().stream()
                .map(TransactionDto::fromEntity)
                .toList();
    }

    @Override
    public TransactionDto findById(Long id) {
        return transactionRepository.findById(id)
                .map(TransactionDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("No transaction was found with the ID : " + id));
    }

    @Override
    public void delete(Long id) {
        transactionRepository.deleteById(id);
    }

    private int getTransactionMultiplier(TransactionType type) {
        return TransactionType.TRANSFERT == type ?  -1 : 1;
    }

    @Override
    public List<TransactionDto> findAllByUserId(Long userId) {
        return transactionRepository.findAllByUserId(userId).stream()
                .map(TransactionDto::fromEntity).toList();
    }
}
