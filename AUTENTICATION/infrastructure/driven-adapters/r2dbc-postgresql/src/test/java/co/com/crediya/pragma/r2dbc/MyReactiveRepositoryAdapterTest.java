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
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

    private UserEntity userEntity1;
    private UserEntity userEntity2;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        userEntity1 = new UserEntity();
        userEntity1.setUserId(1L);
        userEntity1.setEmail("user1@test.com");
        userEntity1.setName("User 1");

        userEntity2 = new UserEntity();
        userEntity2.setUserId(2L);
        userEntity2.setEmail("user2@test.com");
        userEntity2.setName("User 2");

        user1 = User.builder()
                .userId(1L)
                .email("user1@test.com")
                .name("User 1")
                .build();

        user2 = User.builder()
                .userId(2L)
                .email("user2@test.com")
                .name("User 2")
                .build();
    }

//    @Test
//    @DisplayName("getUsersByEmails debe retornar usuarios cuando se proporcionan emails válidos")
//    void getUsersByEmails_ShouldReturnUsers_WhenValidEmailsProvided() {
//        // Arrange
//        List<String> emails = Arrays.asList("user1@test.com", "user2@test.com");
//        List<String> normalizedEmails = Arrays.asList("user1@test.com", "user2@test.com");
//
//        when(repository.findByEmailIn(normalizedEmails))
//                .thenReturn(Flux.just(userEntity1, userEntity2));
//        when(mapper.map(userEntity1, User.class)).thenReturn(user1);
//        when(mapper.map(userEntity2, User.class)).thenReturn(user2);
//
//        // Act & Assert
//        StepVerifier.create(adapter.getUsersByEmails(emails))
//                .expectNext(user1, user2)
//                .verifyComplete();
//    }
//
//    @Test
//    @DisplayName("getUsersByEmails debe normalizar emails (trim y lowercase)")
//    void getUsersByEmails_ShouldNormalizeEmails() {
//        // Arrange
//        List<String> emails = Arrays.asList("  USER1@TEST.COM  ", "  User2@Test.com  ");
//        List<String> normalizedEmails = Arrays.asList("user1@test.com", "user2@test.com");
//
//        when(repository.findByEmailIn(normalizedEmails))
//                .thenReturn(Flux.just(userEntity1, userEntity2));
//        when(mapper.map(userEntity1, User.class)).thenReturn(user1);
//        when(mapper.map(userEntity2, User.class)).thenReturn(user2);
//
//        // Act & Assert
//        StepVerifier.create(adapter.getUsersByEmails(emails))
//                .expectNext(user1, user2)
//                .verifyComplete();
//    }

//    @Test
//    @DisplayName("getUsersByEmails debe eliminar emails duplicados")
//    void getUsersByEmails_ShouldRemoveDuplicateEmails() {
//        // Arrange
//        List<String> emails = Arrays.asList("user1@test.com", "user1@test.com", "user2@test.com");
//        List<String> normalizedEmails = Arrays.asList("user1@test.com", "user2@test.com");
//
//        when(repository.findByEmailIn(normalizedEmails))
//                .thenReturn(Flux.just(userEntity1, userEntity2));
//        when(mapper.map(userEntity1, User.class)).thenReturn(user1);
//        when(mapper.map(userEntity2, User.class)).thenReturn(user2);
//
//        // Act & Assert
//        StepVerifier.create(adapter.getUsersByEmails(emails))
//                .expectNext(user1, user2)
//                .verifyComplete();
//    }
//
//    @Test
//    @DisplayName("getUsersByEmails debe filtrar emails null y vacíos")
//    void getUsersByEmails_ShouldFilterNullAndEmptyEmails() {
//        // Arrange
//        List<String> emails = Arrays.asList("user1@test.com", null, "", "  ", "user2@test.com");
//        List<String> normalizedEmails = Arrays.asList("user1@test.com", "user2@test.com");
//
//        when(repository.findByEmailIn(normalizedEmails))
//                .thenReturn(Flux.just(userEntity1, userEntity2));
//        when(mapper.map(userEntity1, User.class)).thenReturn(user1);
//        when(mapper.map(userEntity2, User.class)).thenReturn(user2);
//
//        // Act & Assert
//        StepVerifier.create(adapter.getUsersByEmails(emails))
//                .expectNext(user1, user2)
//                .verifyComplete();
//    }

    @Test
    @DisplayName("getUsersByEmails debe retornar Flux vacío cuando la lista de emails está vacía")
    void getUsersByEmails_ShouldReturnEmptyFlux_WhenEmailsListIsEmpty() {
        // Arrange
        List<String> emails = Collections.emptyList();

        // Act & Assert
        StepVerifier.create(adapter.getUsersByEmails(emails))
                .verifyComplete();
    }

    @Test
    @DisplayName("getUsersByEmails debe retornar Flux vacío cuando todos los emails son null o vacíos")
    void getUsersByEmails_ShouldReturnEmptyFlux_WhenAllEmailsAreNullOrEmpty() {
        // Arrange
        List<String> emails = Arrays.asList(null, "", "  ");

        // Act & Assert
        StepVerifier.create(adapter.getUsersByEmails(emails))
                .verifyComplete();
    }

    @Test
    @DisplayName("getUsersByEmails debe retornar Flux vacío cuando se pasa null")
    void getUsersByEmails_ShouldReturnEmptyFlux_WhenNullProvided() {
        // Act & Assert
        StepVerifier.create(adapter.getUsersByEmails(null))
                .verifyComplete();
    }
}
