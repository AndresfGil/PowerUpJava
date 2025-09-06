package co.com.crediya.pragma.model.user.gateways;

import co.com.crediya.pragma.model.user.User;
import co.com.crediya.pragma.model.user.solicitudes.UserSimpleView;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;

public interface UserRepository {

    Mono<User> saveUser(User user);

    Mono<User> getUserByEmail(String email);

    Flux<UserSimpleView>getUsersByEmails(List<String> emails);

}
