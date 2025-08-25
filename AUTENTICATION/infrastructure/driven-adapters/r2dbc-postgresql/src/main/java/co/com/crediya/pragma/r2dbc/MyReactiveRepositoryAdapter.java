package co.com.crediya.pragma.r2dbc;

import co.com.crediya.pragma.model.user.User;
import co.com.crediya.pragma.model.user.gateways.UserRepository;
import co.com.crediya.pragma.r2dbc.entities.UserEntity;
import co.com.crediya.pragma.r2dbc.exception.UserNotFoundException;
import co.com.crediya.pragma.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.attribute.UserPrincipalNotFoundException;

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
        return super.save(user);
    }

    @Override
    public Flux<User> getAllUsers() {
        return super.findAll();
    }

    @Override
    public Mono<User> getUserByIdNumber(Long Number) {
        return super.findById(Number);
    }

    @Override
    public Mono<User> editUser(User user) {
        return super.findById(user.getIdNumber().longValue())
                .switchIfEmpty(Mono.error(new UserNotFoundException("User not found with id " + user.getIdNumber())))
                .flatMap(existingUser -> super.save(user))
                .onErrorMap(e -> new RuntimeException("Error updating user: " + e.getMessage(), e));
    }


    @Override
    public Mono<Void> deleteUser(Long idNumber) {
        return repository.deleteById(idNumber);
    }
}
