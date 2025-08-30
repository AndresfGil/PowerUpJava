package co.com.crediya.pragma.model.user.gateways;

import co.com.crediya.pragma.model.user.User;
import reactor.core.publisher.Mono;

public interface AuthenticationGateway {

    Mono<String> generateToken(User user);

    Mono<User> validateToken(String token);
}
