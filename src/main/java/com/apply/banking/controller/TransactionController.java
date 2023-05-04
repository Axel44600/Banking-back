package com.apply.banking.controller;

import com.apply.banking.dto.TransactionDto;
import com.apply.banking.service.TransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@Tag(name = "transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/")
    public ResponseEntity<Long> save(@RequestBody TransactionDto transactionDto) {
        return ResponseEntity.ok(transactionService.save(transactionDto));
    }

    @GetMapping("/")
    public ResponseEntity<List<TransactionDto>> findAll() {
        return ResponseEntity.ok(transactionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDto> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(transactionService.findById(id));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<List<TransactionDto>> findAllByUserId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(transactionService.findAllByUserId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        transactionService.delete(id);
        return ResponseEntity.accepted().build();
    }
}
