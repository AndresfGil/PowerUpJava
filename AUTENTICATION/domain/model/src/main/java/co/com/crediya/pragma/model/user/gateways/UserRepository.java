package co.com.crediya.pragma.model.user.gateways;

import co.com.crediya.pragma.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository {

    Mono<User> saveUser(User user);

    Flux<User> getAllUsers();

    Mono<User> getUserByIdNumber(Long number);

    Mono<User> getUserByEmail(String email);

    Mono<Void> deleteUser(Long idNumber);

}
