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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MyReactiveRepositoryAdapterAdvancedTest {

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
    @DisplayName("Debe manejar transacciones correctamente al guardar usuario")
    void shouldHandleTransactionsCorrectlyWhenSavingUser() {
        when(mapper.map(any(User.class), eq(UserEntity.class))).thenReturn(testUserEntity);
        when(repository.save(any(UserEntity.class))).thenReturn(Mono.just(testUserEntity));
        when(transactionalOperator.transactional(any(Mono.class))).thenReturn(Mono.just(testUser));

        StepVerifier.create(adapter.saveUser(testUser))
                .expectNext(testUser)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe manejar errores de transacción correctamente")
    void shouldHandleTransactionErrorsCorrectly() {
        RuntimeException transactionError = new RuntimeException("Transaction failed");
        when(mapper.map(any(User.class), eq(UserEntity.class))).thenReturn(testUserEntity);
        when(repository.save(any(UserEntity.class))).thenReturn(Mono.just(testUserEntity));
        when(transactionalOperator.transactional(any(Mono.class))).thenReturn(Mono.error(transactionError));

        StepVerifier.create(adapter.saveUser(testUser))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    @DisplayName("Debe manejar múltiples usuarios en getAllUsers")
    void shouldHandleMultipleUsersInGetAllUsers() {
        UserEntity userEntity1 = testUserEntity;
        UserEntity userEntity2 = UserEntity.builder().userId(2L).name("María").email("maria@test.com").build();
        UserEntity userEntity3 = UserEntity.builder().userId(3L).name("Carlos").email("carlos@test.com").build();

        User user1 = testUser;
        User user2 = User.builder().userId(2L).name("María").email("maria@test.com").build();
        User user3 = User.builder().userId(3L).name("Carlos").email("carlos@test.com").build();

        List<UserEntity> userEntities = List.of(userEntity1, userEntity2, userEntity3);
        when(repository.findAll()).thenReturn(Flux.fromIterable(userEntities));
        when(mapper.map(userEntity1, User.class)).thenReturn(user1);
        when(mapper.map(userEntity2, User.class)).thenReturn(user2);
        when(mapper.map(userEntity3, User.class)).thenReturn(user3);

        StepVerifier.create(adapter.getAllUsers())
                .expectNext(user1)
                .expectNext(user2)
                .expectNext(user3)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe manejar lista vacía en getAllUsers")
    void shouldHandleEmptyListInGetAllUsers() {
        when(repository.findAll()).thenReturn(Flux.empty());

        StepVerifier.create(adapter.getAllUsers())
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe manejar email con espacios en blanco correctamente")
    void shouldHandleEmailWithWhitespaceCorrectly() {
        String emailWithWhitespace = "  juan.perez@test.com  ";
        String normalizedEmail = "juan.perez@test.com";
        when(repository.findByEmail(normalizedEmail)).thenReturn(Mono.just(testUserEntity));
        when(mapper.map(any(UserEntity.class), eq(User.class))).thenReturn(testUser);

        StepVerifier.create(adapter.getUserByEmail(emailWithWhitespace))
                .expectNext(testUser)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe manejar email vacío correctamente")
    void shouldHandleEmptyEmailCorrectly() {
        String emptyEmail = "";
        when(repository.findByEmail(emptyEmail)).thenReturn(Mono.empty());

        StepVerifier.create(adapter.getUserByEmail(emptyEmail))
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe manejar errores de mapeo correctamente")
    void shouldHandleMappingErrorsCorrectly() {
        RuntimeException mappingError = new RuntimeException("Mapping failed");
        when(repository.findAll()).thenReturn(Flux.just(testUserEntity));
        when(mapper.map(any(UserEntity.class), eq(User.class))).thenThrow(mappingError);

        StepVerifier.create(adapter.getAllUsers())
                .expectError(RuntimeException.class)
                .verify();
    }


}
