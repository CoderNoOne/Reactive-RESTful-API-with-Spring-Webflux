package com.app.application.service;

import com.app.application.dto.CreateUserDto;
import com.app.application.exception.RegistrationUserException;
import com.app.application.exception.UsersServiceException;
import com.app.application.mapper.Mappers;
import com.app.application.validator.CreateUserDtoValidator;
import com.app.domain.security.User;
import com.app.domain.security.UserRegistrationResponseDto;
import com.app.domain.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsersService {

    private final UserRepository userRepository;
    private final CreateUserDtoValidator createUserDtoValidator;
    private final PasswordEncoder passwordEncoder;

    public Mono<UserRegistrationResponseDto> register(final CreateUserDto createUserDto) {
        var errors = createUserDtoValidator.validate(createUserDto);
        if (!errors.isEmpty()) {
            var errorMessage = errors
                    .entrySet()
                    .stream()
                    .map(e -> e.getKey() + ": " + e.getValue())
                    .collect(Collectors.joining("\n"));
            return Mono.error(() -> new RegistrationUserException(errorMessage));
        }

        return userRepository
                .findByUsername(createUserDto.getUsername())
                .map(userFromDb -> UserRegistrationResponseDto.builder()
                        .errorMessage("User with username %s already exist".formatted(userFromDb.getUsername()))
                        .build())
                .switchIfEmpty(createUser(createUserDto).map(id -> UserRegistrationResponseDto.builder().id(id).build()));
    }

    private Mono<String> createUser(final CreateUserDto createUserDto) {
        createUserDto.setPassword(createUserDto.getPassword() != null ?
                passwordEncoder.encode(createUserDto.getPassword()) : null);
        var user = Mappers.fromCreateUserDtoToRegularUser(createUserDto);
        return userRepository.addOrUpdate(user).map(User::getId);
    }

    public Flux<User> getAll() {
        return userRepository.findAll();
    }
}
