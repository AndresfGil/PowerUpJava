package co.com.crediya.pragma.r2dbc;

import co.com.crediya.pragma.model.user.User;
import co.com.crediya.pragma.model.user.gateways.UserRepository;
import co.com.crediya.pragma.model.user.solicitudes.UserSimpleView;
import co.com.crediya.pragma.r2dbc.entities.UserEntity;
import co.com.crediya.pragma.r2dbc.helper.ReactiveAdapterOperations;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Repository
public class MyReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        User,
        UserEntity,
        Long,
        MyReactiveRepository
> implements UserRepository {
    private final TransactionalOperator transactionalOperator;
    private final PasswordEncoder passwordEncoder;



    public MyReactiveRepositoryAdapter(MyReactiveRepository repository, ObjectMapper mapper, TransactionalOperator transactionalOperator, PasswordEncoder passwordEncoder) {
        super(repository, mapper, d -> mapper.map(d, User.class));
        this.transactionalOperator = transactionalOperator;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Mono<User> saveUser(User user) {
        final long t0 = System.currentTimeMillis();
        final String normalized = user.getEmail() == null ? null : user.getEmail().trim().toLowerCase();

        log.debug("R2DBC saveUser start email={}", normalized);

        User userToSave = new User(
                user.getUserId(),
                normalized,
                passwordEncoder.encode(user.getPassword()),
                user.getName(),
                user.getLastname(),
                user.getRolId(),
                user.getDocumentId(),
                user.getBaseSalary(),
                user.getAddress(),
                user.getPhone(),
                user.getBirthDate()
        );

        return super.save(userToSave)
                .doOnSuccess(u -> log.info("R2DBC saveUser ok id={} email={} elapsedMs={}",
                        u.getUserId(), normalized, System.currentTimeMillis() - t0))
                .doOnError(e -> log.error("R2DBC saveUser fail email={} elapsedMs={} err={}",
                        normalized, System.currentTimeMillis() - t0, e.toString()))
                .as(transactionalOperator::transactional);
    }




    @Override
    public Mono<User> getUserByEmail(String email) {
        String normalized = email == null ? null : email.trim().toLowerCase();
        return repository.findByEmail(normalized)
                .map(e -> mapper.map(e, User.class))
                .doOnNext(u -> log.debug("R2DBC getUserByEmail hit email={}", normalized))
                .switchIfEmpty(Mono.empty());
    }


    @Override
    public Flux<UserSimpleView> getUsersByEmails(List<String> emails) {
        return super.repository.findLiteByCorreoElectronicoIn(emails);
    }

}
