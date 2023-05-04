package com.apply.banking.service.impl;

import com.apply.banking.dto.AccountDto;
import com.apply.banking.entity.Account;
import com.apply.banking.exceptions.ObjectsValidator;
import com.apply.banking.exceptions.OperationNonPermittedException;
import com.apply.banking.repository.AccountRepository;
import com.apply.banking.service.AccountService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final ObjectsValidator<AccountDto> validator;

    @Override
    public Long save(AccountDto dto) {
        validator.validate(dto);
        Account account = AccountDto.toEntity(dto);
        boolean userHasAlreadyAnAccount = accountRepository.findByUserId(account.getUser().getId()).isPresent();
        if(userHasAlreadyAnAccount && account.getUser().isActive()) {
            throw new OperationNonPermittedException(
                    "the selected user has already an active account",
                    "Create account",
                    "Account service",
                    "Account creation"
            );
        }

        if(dto.getId() == null) {
            account.setIban(generateRandomIban());
        }
        return accountRepository.save(account).getId();
    }

    @Override
    public List<AccountDto> findAll() {
        return accountRepository.findAll().stream()
                .map(AccountDto::fromEntity)
                .toList();
    }

    @Override
    public AccountDto findById(Long id) {
        return accountRepository.findById(id)
                .map(AccountDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("No account was found with the ID : " + id));
    }

    @Override
    public void delete(Long id) {
        accountRepository.deleteById(id);
    }

    private String generateRandomIban() {
        String iban = Iban.random(CountryCode.FR).toFormattedString();
        boolean ibanExists = accountRepository.findByIban(iban).isPresent();
        if(ibanExists) {
            generateRandomIban();
        }
        return iban;
    }
}
