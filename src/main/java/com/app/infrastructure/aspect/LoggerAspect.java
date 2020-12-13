package com.app.infrastructure.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@Aspect
@Slf4j
@Component
public class LoggerAspect {

    @Around("@annotation(com.app.infrastructure.aspect.annotations.Loggable)")
    public Mono<?> logAround(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();
        var result = joinPoint.proceed();

        if (result instanceof Mono<?> monoResult) {
            return monoResult
                    .doOnSuccess(o -> {
                        if (Objects.nonNull(o) && o instanceof ServerResponse resp) {

                            log.info("Invoking method: %s".formatted(Arrays.toString(joinPoint.getArgs())));
                            log.info("Response code: %d".formatted(resp.rawStatusCode()));
                            log.info("Execution time: %d".formatted(System.currentTimeMillis() - start));
                        }
                    });

        }

        return Mono.empty();
    }
}
