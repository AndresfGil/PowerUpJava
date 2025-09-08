package co.com.crediya.pragma.usecase.login;


import co.com.crediya.pragma.model.user.login.AuthTokens;
import co.com.crediya.pragma.model.user.login.gateway.LoginService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
public class LoginUseCase {

    private final LoginService loginService;

    public Mono<AuthTokens> login(String email, String password){
        return loginService.login(email, password);
    }

}



