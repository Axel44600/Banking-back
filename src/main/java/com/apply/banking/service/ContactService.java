package com.apply.banking.service;

import com.apply.banking.dto.ContactDto;

import java.util.List;

public interface ContactService extends AbstractService<ContactDto> {

    List<ContactDto> findAllByUserId(Long userId);
}
