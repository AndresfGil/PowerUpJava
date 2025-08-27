package co.com.crediya.pragma.r2dbc.helper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

class ReactiveLoggerTest {

    @Test
    @DisplayName("Debe loggear Mono exitoso correctamente")
    void shouldLogSuccessfulMonoCorrectly() {
        String testValue = "test";
        Mono<String> mono = Mono.just(testValue);
        
        StepVerifier.create(ReactiveLogger.logMono(mono, "TEST_OP", "testKey", "testValue"))
                .expectNext(testValue)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe loggear Mono con error correctamente")
    void shouldLogMonoWithErrorCorrectly() {
        RuntimeException testError = new RuntimeException("Test error");
        Mono<String> mono = Mono.error(testError);
        
        StepVerifier.create(ReactiveLogger.logMono(mono, "TEST_OP", "testKey", "testValue"))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    @DisplayName("Debe loggear Mono vacío correctamente")
    void shouldLogEmptyMonoCorrectly() {
        Mono<String> mono = Mono.empty();
        
        StepVerifier.create(ReactiveLogger.logMono(mono, "TEST_OP", "testKey", "testValue"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe loggear Flux exitoso correctamente")
    void shouldLogSuccessfulFluxCorrectly() {
        List<String> testValues = List.of("value1", "value2", "value3");
        Flux<String> flux = Flux.fromIterable(testValues);
        
        StepVerifier.create(ReactiveLogger.logFlux(flux, "TEST_FLUX_OP"))
                .expectNext("value1")
                .expectNext("value2")
                .expectNext("value3")
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe loggear Flux vacío correctamente")
    void shouldLogEmptyFluxCorrectly() {
        Flux<String> flux = Flux.empty();
        
        StepVerifier.create(ReactiveLogger.logFlux(flux, "TEST_EMPTY_FLUX_OP"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe loggear Flux con error correctamente")
    void shouldLogFluxWithErrorCorrectly() {
        RuntimeException testError = new RuntimeException("Test flux error");
        Flux<String> flux = Flux.error(testError);
        
        StepVerifier.create(ReactiveLogger.logFlux(flux, "TEST_ERROR_FLUX_OP"))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    @DisplayName("Debe loggear Flux con múltiples elementos correctamente")
    void shouldLogFluxWithMultipleElementsCorrectly() {
        Flux<Integer> flux = Flux.range(1, 5);
        
        StepVerifier.create(ReactiveLogger.logFlux(flux, "TEST_RANGE_FLUX_OP"))
                .expectNext(1, 2, 3, 4, 5)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe loggear Mono con valor null correctamente")
    void shouldLogMonoWithNullValueCorrectly() {
        Mono<String> mono = Mono.justOrEmpty(null);
        
        StepVerifier.create(ReactiveLogger.logMono(mono, "TEST_NULL_OP", "nullKey", null))
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe loggear Mono con valores extremos correctamente")
    void shouldLogMonoWithExtremeValuesCorrectly() {
        String longValue = "A".repeat(1000);
        Mono<String> mono = Mono.just(longValue);
        
        StepVerifier.create(ReactiveLogger.logMono(mono, "TEST_LONG_OP", "longKey", longValue))
                .expectNext(longValue)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe loggear Flux con operaciones complejas correctamente")
    void shouldLogFluxWithComplexOperationsCorrectly() {
        Flux<Integer> flux = Flux.range(1, 3)
                .map(i -> i * 2)
                .filter(i -> i > 2);
        
        StepVerifier.create(ReactiveLogger.logFlux(flux, "TEST_COMPLEX_FLUX_OP"))
                .expectNext(4, 6)
                .verifyComplete();
    }
}
