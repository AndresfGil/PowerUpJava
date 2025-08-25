package co.com.crediya.pragma.r2dbc;

import co.com.crediya.pragma.model.user.User;
import co.com.crediya.pragma.model.user.exception.EmailAlreadyExistsException;
import co.com.crediya.pragma.model.user.gateways.UserRepository;
import co.com.crediya.pragma.r2dbc.entities.UserEntity;
import co.com.crediya.pragma.r2dbc.helper.ReactiveAdapterOperations;
import co.com.crediya.pragma.r2dbc.helper.ReactiveLogger;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class MyReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        User,
        UserEntity,
        Long,
    MyReactiveRepository
> implements UserRepository
{
    public MyReactiveRepositoryAdapter(MyReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, User.class));
    }

    @Override
    public Mono<User> saveUser(User user) {
        final long t0 = System.currentTimeMillis();
        final String normalized = user.getEmail() == null ? null : user.getEmail().trim().toLowerCase();
        log.debug("R2DBC saveUser start email={}", normalized);

        return repository.findByEmail(normalized)
                .flatMap(existing -> {
                    log.info("R2DBC saveUser duplicate email={} detected in {} ms",
                            normalized, System.currentTimeMillis() - t0);
                    return Mono.error(new EmailAlreadyExistsException(normalized));
                })
                .switchIfEmpty(
                        super.save(user)
                                .doOnSuccess(u -> log.info("R2DBC saveUser ok id={} elapsedMs={}",
                                        u.getUserId(), System.currentTimeMillis() - t0))
                                .doOnError(e -> log.warn("R2DBC saveUser fail email={} elapsedMs={} err={}",
                                        normalized, System.currentTimeMillis() - t0, e.toString()))
                )
                .cast(User.class);
    }


    @Override
    public Flux<User> getAllUsers() {
        return ReactiveLogger.logFlux(super.findAll(), "R2DBC getAllUsers");
    }

    @Override
    public Mono<User> getUserByIdNumber(Long number) {
        return super.findById(number);
    }

    @Override
    public Mono<User> getUserByEmail(String email) {
        return null;
    }

    @Override
    public Mono<Void> deleteUser(Long idNumber) {
        return repository.deleteById(idNumber);
    }
}
