package com.apply.banking.service;

import com.apply.banking.dto.AuthenticationRequest;
import com.apply.banking.dto.AuthenticationResponse;
import com.apply.banking.dto.UserDto;

public interface UserService extends AbstractService<UserDto> {

    Long validateAccount(Long id);

    Long invalidateAccount(Long id);

    AuthenticationResponse register(UserDto userDto);

    AuthenticationResponse authenticate(AuthenticationRequest request);
}
