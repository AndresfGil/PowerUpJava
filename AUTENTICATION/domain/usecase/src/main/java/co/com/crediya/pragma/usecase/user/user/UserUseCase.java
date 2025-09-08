package co.com.crediya.pragma.usecase.user.user;

import co.com.crediya.pragma.model.user.User;
import co.com.crediya.pragma.model.user.exception.EmailAlreadyExistsException;
import co.com.crediya.pragma.model.user.gateways.RoleRepository;
import co.com.crediya.pragma.model.user.gateways.UserRepository;
import co.com.crediya.pragma.model.user.solicitudes.UserSimpleView;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import co.com.crediya.pragma.model.user.exception.RolNotFoundException;
import java.util.List;


@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public Mono<User> saveUser(User user) {
        return userRepository.getUserByEmail(user.getEmail()).hasElement()
                .flatMap(exist ->
                        exist
                                ? Mono.error(new EmailAlreadyExistsException(user.getEmail()))
                                : roleRepository.findById(user.getRolId()) )
                .switchIfEmpty(Mono.error(new RolNotFoundException()
                ))
                .then(Mono.defer(() -> userRepository.saveUser(user)));
    }


    public Mono<User> getUserByEmail(String email){
        return userRepository.getUserByEmail(email);
    }

    public Flux<UserSimpleView> getUsersByEmails(List<String> correosElectronicos) {
        return userRepository.getUsersByEmails(correosElectronicos);
    }


}
