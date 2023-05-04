package com.apply.banking.service.impl;

import com.apply.banking.config.JwtUtils;
import com.apply.banking.dto.AccountDto;
import com.apply.banking.dto.AuthenticationRequest;
import com.apply.banking.dto.AuthenticationResponse;
import com.apply.banking.dto.UserDto;
import com.apply.banking.entity.Account;
import com.apply.banking.entity.Role;
import com.apply.banking.entity.User;
import com.apply.banking.exceptions.ObjectsValidator;
import com.apply.banking.repository.RoleRepository;
import com.apply.banking.repository.UserRepository;
import com.apply.banking.service.AccountService;
import com.apply.banking.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.apply.banking.entity.Roles.ROLE_USER;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AccountService accountService;
    private final ObjectsValidator<UserDto> validator;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;

    @Override
    public Long save(UserDto dto) {
        validator.validate(dto);
        User user = UserDto.toEntity(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user).getId();
    }

    @Override
    @Transactional
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(UserDto::fromEntity)
                .toList();
    }

    @Override
    public UserDto findById(Long id) {
        return userRepository.findById(id)
                .map(UserDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("Not user was found with the provided ID : " + id));
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Long validateAccount(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No user was found for user account validation"));

        if(user.getAccount() == null) {
            AccountDto accountDto = AccountDto.builder().user(UserDto.fromEntity(user)).build();
            var savedAccount = accountService.save(accountDto);
            user.setAccount(Account.builder().id(savedAccount).build());
        }
        user.setActive(true);
        userRepository.save(user);
        return user.getId();
    }

    @Override
    public Long invalidateAccount(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No user was found fo user account validation"));
        user.setActive(false);
        userRepository.save(user);
        return user.getId();
    }

    @Override
    @Transactional
    public AuthenticationResponse register(UserDto userDto) {
        validator.validate(userDto);
        User user = UserDto.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(findOrCreateRole(String.valueOf(ROLE_USER)));
        var savedUser = userRepository.save(user);
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", savedUser.getId());
        claims.put("fullname", savedUser.getFirstname() + " " + savedUser.getLastname());

        String token = jwtUtils.generateToken(savedUser, claims);
        return AuthenticationResponse.builder().token(token).build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        final User user = userRepository.findByEmail(request.getEmail()).get();

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("fullname", user.getFirstname() + " " + user.getLastname());

        final String token = jwtUtils.generateToken(user, claims);
        return AuthenticationResponse.builder().token(token).build();
    }

    private Role findOrCreateRole(String roleName) {
        Role role = roleRepository.findByName(roleName).orElse(null);
        if (role == null) {
            return roleRepository.save(Role.builder().name(roleName).build());
        }
        return role;
    }
}
