package co.com.crediya.pragma.usecase.user.user;

import co.com.crediya.pragma.model.user.Role;
import co.com.crediya.pragma.model.user.User;
import co.com.crediya.pragma.model.user.exception.EmailAlreadyExistsException;
import co.com.crediya.pragma.model.user.exception.RolNotFoundException;
import co.com.crediya.pragma.model.user.gateways.RoleRepository;
import co.com.crediya.pragma.model.user.gateways.UserRepository;
import co.com.crediya.pragma.model.user.solicitudes.UserSimpleView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    private UserUseCase userUseCase;

    @BeforeEach
    void setUp() {
        userUseCase = new UserUseCase(userRepository, roleRepository);
    }

    @Test
    void testSaveUserSuccess() {
        User user = createTestUser();
        Role role = createTestRole();

        when(userRepository.getUserByEmail(user.getEmail())).thenReturn(Mono.empty());
        when(roleRepository.findById(user.getRolId())).thenReturn(Mono.just(role));
        when(userRepository.saveUser(user)).thenReturn(Mono.just(user));

        StepVerifier.create(userUseCase.saveUser(user))
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void testSaveUserEmailAlreadyExists() {
        User user = createTestUser();
        User existingUser = createTestUser();

        when(userRepository.getUserByEmail(user.getEmail())).thenReturn(Mono.just(existingUser));

        StepVerifier.create(userUseCase.saveUser(user))
                .expectError(EmailAlreadyExistsException.class)
                .verify();
    }

    @Test
    void testSaveUserRoleNotFound() {
        User user = createTestUser();

        when(userRepository.getUserByEmail(user.getEmail())).thenReturn(Mono.empty());
        when(roleRepository.findById(user.getRolId())).thenReturn(Mono.empty());

        StepVerifier.create(userUseCase.saveUser(user))
                .expectError(RolNotFoundException.class)
                .verify();
    }

    @Test
    void testGetUserByEmailSuccess() {
        String email = "test@example.com";
        User user = createTestUser();

        when(userRepository.getUserByEmail(email)).thenReturn(Mono.just(user));

        StepVerifier.create(userUseCase.getUserByEmail(email))
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void testGetUserByEmailNotFound() {
        String email = "nonexistent@example.com";

        when(userRepository.getUserByEmail(email)).thenReturn(Mono.empty());

        StepVerifier.create(userUseCase.getUserByEmail(email))
                .verifyComplete();
    }

    @Test
    void testGetUsersByEmailsSuccess() {
        List<String> emails = List.of("test1@example.com", "test2@example.com");
        UserSimpleView user1 = createTestUserSimpleView("User1", "test1@example.com", 50000L);
        UserSimpleView user2 = createTestUserSimpleView("User2", "test2@example.com", 60000L);

        when(userRepository.getUsersByEmails(emails)).thenReturn(Flux.just(user1, user2));

        StepVerifier.create(userUseCase.getUsersByEmails(emails))
                .expectNext(user1, user2)
                .verifyComplete();
    }

    @Test
    void testGetUsersByEmailsEmptyList() {
        List<String> emails = List.of();

        when(userRepository.getUsersByEmails(emails)).thenReturn(Flux.empty());

        StepVerifier.create(userUseCase.getUsersByEmails(emails))
                .verifyComplete();
    }

    @Test
    void testGetUsersByEmailsWithNullList() {
        when(userRepository.getUsersByEmails(null)).thenReturn(Flux.empty());

        StepVerifier.create(userUseCase.getUsersByEmails(null))
                .verifyComplete();
    }

    @Test
    void testGetUserByEmailWithNullEmail() {
        when(userRepository.getUserByEmail(null)).thenReturn(Mono.empty());

        StepVerifier.create(userUseCase.getUserByEmail(null))
                .verifyComplete();
    }

    @Test
    void testGetUserByEmailWithEmptyEmail() {
        when(userRepository.getUserByEmail("")).thenReturn(Mono.empty());

        StepVerifier.create(userUseCase.getUserByEmail(""))
                .verifyComplete();
    }

    private User createTestUser() {
        return User.builder()
                .userId(1L)
                .name("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .password("password123")
                .documentId("12345678")
                .address("Test Address")
                .birthDate("1990-01-01")
                .phone("1234567890")
                .rolId(1L)
                .baseSalary(new BigDecimal("50000"))
                .build();
    }

    private Role createTestRole() {
        return Role.builder()
                .rolId(1L)
                .name("USER")
                .description("Regular user role")
                .build();
    }

    private UserSimpleView createTestUserSimpleView(String name, String email, Long baseSalary) {
        return new UserSimpleView() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public Long getBaseSalary() {
                return baseSalary;
            }

            @Override
            public String getEmail() {
                return email;
            }
        };
    }
}

