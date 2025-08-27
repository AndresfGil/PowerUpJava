package co.com.crediya.pragma.r2dbc;

import co.com.crediya.pragma.model.user.User;
import co.com.crediya.pragma.r2dbc.entities.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class MyReactiveRepositoryAdapterTest {

    @Mock
    private MyReactiveRepository repository;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private TransactionalOperator transactionalOperator;

    @InjectMocks
    private MyReactiveRepositoryAdapter adapter;

    private User testUser;
    private UserEntity testUserEntity;

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

        testUserEntity = UserEntity.builder()
                .userId(1L)
                .name("Juan")
                .lastname("Pérez")
                .email("juan.perez@test.com")
                .documentId("12345678")
                .address("Calle 123")
                .birthDate("1990-01-01")
                .phone("3001234567")
                .rolId(1L)
                .baseSalary(2000000.0)
                .build();
    }

    @Test
    @DisplayName("Debe guardar un usuario exitosamente")
    void shouldSaveUserSuccessfully() {
        when(mapper.map(any(User.class), eq(UserEntity.class))).thenReturn(testUserEntity);
        when(repository.save(any(UserEntity.class))).thenReturn(Mono.just(testUserEntity));
        when(transactionalOperator.transactional(any(Mono.class))).thenReturn(Mono.just(testUser));

        StepVerifier.create(adapter.saveUser(testUser))
                .expectNext(testUser)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe obtener todos los usuarios exitosamente")
    void shouldGetAllUsersSuccessfully() {
        List<UserEntity> userEntities = List.of(testUserEntity);
        when(repository.findAll()).thenReturn(Flux.fromIterable(userEntities));
        when(mapper.map(any(UserEntity.class), eq(User.class))).thenReturn(testUser);

        StepVerifier.create(adapter.getAllUsers())
                .expectNext(testUser)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe obtener un usuario por ID exitosamente")
    void shouldGetUserByIdSuccessfully() {
        Long userId = 1L;
        when(repository.findById(userId)).thenReturn(Mono.just(testUserEntity));
        when(mapper.map(any(UserEntity.class), eq(User.class))).thenReturn(testUser);

        StepVerifier.create(adapter.getUserByIdNumber(userId))
                .expectNext(testUser)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe retornar Mono vacío cuando el usuario no existe por ID")
    void shouldReturnEmptyMonoWhenUserDoesNotExistById() {
        Long userId = 999L;
        when(repository.findById(userId)).thenReturn(Mono.empty());

        StepVerifier.create(adapter.getUserByIdNumber(userId))
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe obtener un usuario por email exitosamente")
    void shouldGetUserByEmailSuccessfully() {
        String email = "juan.perez@test.com";
        when(repository.findByEmail(email)).thenReturn(Mono.just(testUserEntity));
        when(mapper.map(any(UserEntity.class), eq(User.class))).thenReturn(testUser);

        StepVerifier.create(adapter.getUserByEmail(email))
                .expectNext(testUser)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe normalizar email a minúsculas al buscar por email")
    void shouldNormalizeEmailToLowerCaseWhenSearchingByEmail() {
        String email = "JUAN.PEREZ@TEST.COM";
        String normalizedEmail = "juan.perez@test.com";
        when(repository.findByEmail(normalizedEmail)).thenReturn(Mono.just(testUserEntity));
        when(mapper.map(any(UserEntity.class), eq(User.class))).thenReturn(testUser);

        StepVerifier.create(adapter.getUserByEmail(email))
                .expectNext(testUser)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe retornar Mono vacío cuando el usuario no existe por email")
    void shouldReturnEmptyMonoWhenUserDoesNotExistByEmail() {
        String email = "nonexistent@test.com";
        when(repository.findByEmail(email)).thenReturn(Mono.empty());

        StepVerifier.create(adapter.getUserByEmail(email))
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe manejar email null correctamente")
    void shouldHandleNullEmailCorrectly() {
        when(repository.findByEmail(null)).thenReturn(Mono.empty());

        StepVerifier.create(adapter.getUserByEmail(null))
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe eliminar un usuario exitosamente")
    void shouldDeleteUserSuccessfully() {
        Long userId = 1L;
        when(repository.deleteById(userId)).thenReturn(Mono.empty());

        StepVerifier.create(adapter.deleteUser(userId))
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe manejar errores del repositorio correctamente")
    void shouldHandleRepositoryErrorsCorrectly() {
        RuntimeException repositoryError = new RuntimeException("Database error");
        when(repository.findAll()).thenReturn(Flux.error(repositoryError));

        StepVerifier.create(adapter.getAllUsers())
                .expectError(RuntimeException.class)
                .verify();
    }
}
