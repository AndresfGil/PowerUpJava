package co.com.crediya.pragma.usecase.user;

import co.com.crediya.pragma.model.user.User;
import co.com.crediya.pragma.model.user.exception.EmailAlreadyExistsException;
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
class UserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserUseCase userUseCase;

    private User testUser;
    private User existingUser;

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

        existingUser = User.builder()
                .userId(2L)
                .name("María")
                .lastname("García")
                .email("juan.perez@test.com")
                .documentId("87654321")
                .address("Calle 456")
                .birthDate("1985-05-15")
                .phone("3007654321")
                .rolId(1L)
                .baseSalary(BigDecimal.valueOf(2500000))
                .build();
    }

    @Test
    @DisplayName("Debe guardar un usuario exitosamente cuando el email no existe")
    void shouldSaveUserSuccessfullyWhenEmailDoesNotExist() {
        when(userRepository.getUserByEmail(anyString())).thenReturn(Mono.empty());
        when(userRepository.saveUser(any(User.class))).thenReturn(Mono.just(testUser));

        StepVerifier.create(userUseCase.saveUser(testUser))
                .expectNext(testUser)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe lanzar EmailAlreadyExistsException cuando el email ya existe")
    void shouldThrowEmailAlreadyExistsExceptionWhenEmailExists() {
        when(userRepository.getUserByEmail(anyString())).thenReturn(Mono.just(existingUser));
        when(userRepository.saveUser(any(User.class))).thenReturn(Mono.just(testUser));

        StepVerifier.create(userUseCase.saveUser(testUser))
                .expectError(EmailAlreadyExistsException.class)
                .verify();
    }

    @Test
    @DisplayName("Debe obtener todos los usuarios exitosamente")
    void shouldGetAllUsersSuccessfully() {
        List<User> users = List.of(testUser, existingUser);
        when(userRepository.getAllUsers()).thenReturn(Flux.fromIterable(users));

        StepVerifier.create(userUseCase.getAllUsers())
                .expectNext(testUser)
                .expectNext(existingUser)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe obtener un usuario por ID exitosamente")
    void shouldGetUserByIdSuccessfully() {
        Long userId = 1L;
        when(userRepository.getUserByIdNumber(userId)).thenReturn(Mono.just(testUser));

        StepVerifier.create(userUseCase.getUserByIdNumber(userId))
                .expectNext(testUser)
                .verifyComplete();
    }


    @Test
    @DisplayName("Debe eliminar un usuario exitosamente")
    void shouldDeleteUserSuccessfully() {
        Long userId = 1L;
        when(userRepository.deleteUser(userId)).thenReturn(Mono.empty());

        StepVerifier.create(userUseCase.deleteUser(userId))
                .verifyComplete();
    }

}
