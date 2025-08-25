package co.com.crediya.pragma.usecase.user;

import co.com.crediya.pragma.model.user.User;
import co.com.crediya.pragma.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;

    public Mono<Void> saveUser(User user){
        userRepository.saveUser(user);
        return null;
    }

    public Flux<User> getAllUsers(){
        return userRepository.getAllUsers();
    }

    public Mono<User> getUserByIdNumber(Long idNumber){
        return userRepository.getUserByIdNumber(idNumber);
    }

    public Mono<User> editUser(User user){
        return userRepository.editUser(user);
    }

    public Mono<Void> deleteUser(Long idNumber){
        userRepository.deleteUser(idNumber);
        return null;
    }

}
