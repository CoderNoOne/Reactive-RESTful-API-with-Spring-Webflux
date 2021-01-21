package com.app.application.service;

import com.app.application.dto.CreateUserDto;
import com.app.application.dto.UserDto;
import com.app.application.exception.RegistrationUserException;
import com.app.application.exception.UserServiceException;
import com.app.application.validator.CreateUserDtoValidator;
import com.app.application.validator.util.Validations;
import com.app.domain.security.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsersService {

    private final UserRepository userRepository;
    private final CreateUserDtoValidator createUserDtoValidator;
    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;

    public Mono<UserRegistrationResponseDto> register(final CreateUserDto createUserDto) {
        var errors = createUserDtoValidator.validate(createUserDto);

        if (Validations.hasErrors(errors)) {
            return Mono.error(() -> new RegistrationUserException(Validations.createErrorMessage(errors)));
        }

        return userRepository
                .findByUsername(createUserDto.getUsername())
                .map(userFromDb -> UserRegistrationResponseDto.builder()
                        .errorMessage("User with username %s already exist".formatted(userFromDb.getUsername()))
                        .build())
                .switchIfEmpty(createUser(createUserDto).map(id -> UserRegistrationResponseDto.builder().id(id).build()));
    }

    private Mono<String> createUser(final CreateUserDto createUserDto) {

        return userRepository
                .addOrUpdate(createUserDto.setPassword(nonNull(createUserDto.getPassword()) ? passwordEncoder.encode(createUserDto.getPassword()) : null).toEntity())
                .map(User::getId);
    }

    public Flux<UserDto> getAll() {
        return userRepository
                .findAll()
                .map(User::toDto);
    }

    public Mono<UserDto> getByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .switchIfEmpty(Mono.error(() -> new UserServiceException("No user with username: %s".formatted(username))))
                .map(User::toDto);
    }

    public Mono<UserDto> promoteUserToAdminRole(String username) {

        return userRepository
                .findByUsername(username)
                .switchIfEmpty(Mono.error(() -> new UserServiceException("No user with username: %s".formatted(username))))
                .flatMap(user -> userRepository.deleteById(user.getId()))
                .flatMap(user -> adminRepository
                        .addOrUpdate(promoteUserToAdmin(user))
                        .map(Admin::toUserDto)
                );
    }

    private Admin promoteUserToAdmin(User user) {
        return Optional.ofNullable(user)
                .map(userVal -> new Admin(user.getUsername(), userVal.getPassword()))
                .orElse(null);
    }
}
