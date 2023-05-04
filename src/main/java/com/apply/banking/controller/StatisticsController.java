package com.apply.banking.controller;

import com.apply.banking.dto.TransactionSumDetails;
import com.apply.banking.service.StatisticsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
@Tag(name = "statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/sum-by-date/{id}")
    public ResponseEntity<List<TransactionSumDetails>> findSumTractionsByDate(
            @PathVariable("id") Long id,
            @RequestParam("start-date") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate startDate,
            @RequestParam("end-date") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate endDate) {
        return ResponseEntity.ok(statisticsService.findSumTransactionsByDate(startDate, endDate, id));
    }

    @GetMapping("/account-balance/{id}")
    public ResponseEntity<BigDecimal> getAccountBalance(@PathVariable("id") Long id) {
        return ResponseEntity.ok(statisticsService.getAccountBalance(id));
    }

    @GetMapping("/highest-transfer/{id}")
    public ResponseEntity<BigDecimal> highestTransfert(@PathVariable("id") Long id) {
        return ResponseEntity.ok(statisticsService.highestTransfert(id));
    }

    @GetMapping("highest-deposit/{id}")
    public ResponseEntity<BigDecimal> highestDeposit(@PathVariable("id") Long id) {
        return ResponseEntity.ok(statisticsService.highestDeposit(id));
    }
}
