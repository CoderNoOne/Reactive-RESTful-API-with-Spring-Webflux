package com.app.application.service;

import com.app.application.dto.CreateUserDto;
import com.app.application.exception.UsersServiceException;
import com.app.application.mapper.Mappers;
import com.app.application.validator.CreateUserDtoValidator;
import com.app.domain.security.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UserRepository userRepository;
    private final CreateUserDtoValidator createUserDtoValidator;

    public Mono<String> register(final CreateUserDto createUserDto) {
        var errors = createUserDtoValidator.validate(createUserDto);
        if (!errors.isEmpty()) {
            var errorMessage = errors
                    .entrySet()
                    .stream()
                    .map(e -> e.getKey() + ": " + e.getValue())
                    .collect(Collectors.joining("\n"));
            return Mono.error(new UsersServiceException(errorMessage));
        }

        return userRepository
                .findByUsername(createUserDto.getUsername())
                .map(userFromDb -> "User with username " + userFromDb.getUsername() + " already exist")
                .switchIfEmpty(createUser(createUserDto));
    }

    private Mono<String> createUser(final CreateUserDto createUserDto) {
        var user = Mappers.fromCreateUserDtoToRegularUser(createUserDto);
        return userRepository.addOrUpdate(user).map(User::getId);
    }

    public Flux<User> getAll() {
        return userRepository.findAll();
    }
}
