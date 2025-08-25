package co.com.crediya.pragma.r2dbc;

import co.com.crediya.pragma.model.user.User;
import co.com.crediya.pragma.model.user.gateways.UserRepository;
import co.com.crediya.pragma.r2dbc.entities.UserEntity;
import co.com.crediya.pragma.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
        String normalized = user.getEmail() == null ? null : user.getEmail().trim().toLowerCase();

        return repository.findByEmail(normalized)
                .flatMap(existing -> Mono.<User>error(new IllegalStateException("email_duplicado")))
                .switchIfEmpty(
                        super.save(user)
                )
                .cast(User.class);
    }

    @Override
    public Flux<User> getAllUsers() {
        return super.findAll();
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
