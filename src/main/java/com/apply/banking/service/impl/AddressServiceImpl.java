package com.apply.banking.service.impl;

import com.apply.banking.dto.AddressDto;
import com.apply.banking.entity.Address;
import com.apply.banking.exceptions.ObjectsValidator;
import com.apply.banking.repository.AddressRepository;
import com.apply.banking.service.AddressService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {


    private final AddressRepository addressRepository;
    private final ObjectsValidator<AddressDto> validator;

    @Override
    public Long save(AddressDto dto) {
        validator.validate(dto);
        Address address = AddressDto.toEntity(dto);
        return addressRepository.save(address).getId();
    }

    @Override
    public List<AddressDto> findAll() {
        return addressRepository.findAll().stream()
                .map(AddressDto::fromEntity)
                .toList();
    }

    @Override
    public AddressDto findById(Long id) {
        return addressRepository.findById(id)
                .map(AddressDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("No address found with the ID : " + id));
    }

    @Override
    public void delete(Long id) {
        addressRepository.deleteById(id);
    }
}
