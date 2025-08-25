package co.com.crediya.pragma.usecase.user;

import co.com.crediya.pragma.model.user.User;
import co.com.crediya.pragma.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;

    public Mono<User> saveUser(User user){
        return userRepository.saveUser(user);
    }
    public Flux<User> getAllUsers(){
        return userRepository.getAllUsers();
    }

    public Mono<User> getUserByIdNumber(Long idNumber){
        return userRepository.getUserByIdNumber(idNumber);
    }

    public Mono<Void> deleteUser(Long idNumber){
        return userRepository.deleteUser(idNumber);
    }
}
