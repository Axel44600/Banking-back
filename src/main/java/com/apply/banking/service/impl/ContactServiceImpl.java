package com.apply.banking.service.impl;

import com.apply.banking.dto.ContactDto;
import com.apply.banking.entity.Contact;
import com.apply.banking.exceptions.ObjectsValidator;
import com.apply.banking.repository.ContactRepository;
import com.apply.banking.service.ContactService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    private final ObjectsValidator<ContactDto> validator;

    @Override
    public Long save(ContactDto dto) {
        validator.validate(dto);
        Contact contact = ContactDto.toEntity(dto);
        return contactRepository.save(contact).getId();
    }

    @Override
    public List<ContactDto> findAll() {
        return contactRepository.findAll().stream()
                .map(ContactDto::fromEntity)
                .toList();
    }

    @Override
    public ContactDto findById(Long id) {
        return contactRepository.findById(id)
                .map(ContactDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("No contact was found with the ID : " + id));
    }

    @Override
    public void delete(Long id) {
        contactRepository.deleteById(id);
    }

    @Override
    public List<ContactDto> findAllByUserId(Long userId) {
        return contactRepository.findAllByUserId(userId).stream()
                .map(ContactDto::fromEntity)
                .toList();
    }
}
