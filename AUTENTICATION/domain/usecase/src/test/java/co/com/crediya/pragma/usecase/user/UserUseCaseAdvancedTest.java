package co.com.crediya.pragma.usecase.user;

import co.com.crediya.pragma.model.user.User;
import co.com.crediya.pragma.model.user.gateways.UserRepository;
import co.com.crediya.pragma.usecase.user.user.UserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserUseCaseAdvancedTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserUseCase userUseCase;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .userId(1L)
                .name("Juan")
                .lastname("Pérez")
                .email("juan.perez@test.com")
                .documentId("12345678")
                .address("Calle 123")
                .birthDate("1990-01-01")
                .phone("3001234567")
                .rolId(1L)
                .baseSalary(BigDecimal.valueOf(2000000))
                .build();
    }

    @Test
    @DisplayName("Debe manejar operaciones concurrentes correctamente")
    void shouldHandleConcurrentOperationsCorrectly() {
        when(userRepository.getUserByEmail(anyString())).thenReturn(Mono.empty());
        when(userRepository.saveUser(any(User.class))).thenReturn(Mono.just(testUser));

        User user1 = testUser.toBuilder().email("user1@test.com").build();
        User user2 = testUser.toBuilder().email("user2@test.com").build();
        User user3 = testUser.toBuilder().email("user3@test.com").build();

        StepVerifier.create(
                Flux.merge(
                        userUseCase.saveUser(user1),
                        userUseCase.saveUser(user2),
                        userUseCase.saveUser(user3)
                )
        )
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe manejar lista vacía en getAllUsers")
    void shouldHandleEmptyListInGetAllUsers() {
        when(userRepository.getAllUsers()).thenReturn(Flux.empty());

        StepVerifier.create(userUseCase.getAllUsers())
                .verifyComplete();
    }


    @Test
    @DisplayName("Debe manejar múltiples usuarios en getAllUsers")
    void shouldHandleMultipleUsersInGetAllUsers() {
        List<User> users = List.of(
                testUser,
                testUser.toBuilder().userId(2L).name("María").email("maria@test.com").build(),
                testUser.toBuilder().userId(3L).name("Carlos").email("carlos@test.com").build()
        );
        
        when(userRepository.getAllUsers()).thenReturn(Flux.fromIterable(users));

        StepVerifier.create(userUseCase.getAllUsers())
                .expectNextCount(3)
                .verifyComplete();
    }
}
