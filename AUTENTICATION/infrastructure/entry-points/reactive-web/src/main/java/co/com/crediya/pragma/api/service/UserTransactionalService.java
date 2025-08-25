package co.com.crediya.pragma.api.service;

import co.com.crediya.pragma.model.user.User;
import co.com.crediya.pragma.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserTransactionalService {

    private final UserUseCase userUseCase;
    private final TransactionalOperator transactionalOperator;

    public Mono<User> saveUser(User user) {
        return userUseCase.saveUser(user)
                .as(transactionalOperator::transactional);
    }
}
