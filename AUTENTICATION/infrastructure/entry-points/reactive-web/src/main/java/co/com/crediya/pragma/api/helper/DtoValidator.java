package co.com.crediya.pragma.api.helper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DtoValidator {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public <T> Mono<T> validate(T dto) {
        Set<ConstraintViolation<T>> violations = validator.validate(dto);
        if (violations.isEmpty()) return Mono.just(dto);
        String message = violations.stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.joining("; "));
        return Mono.error(new IllegalArgumentException(message));
    }
}
