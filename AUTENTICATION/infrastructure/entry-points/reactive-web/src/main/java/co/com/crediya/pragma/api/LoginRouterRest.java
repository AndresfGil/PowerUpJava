package co.com.crediya.pragma.api;

import co.com.crediya.pragma.api.docs.LoginControllerDocs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class LoginRouterRest implements LoginControllerDocs {

    @Bean
    public RouterFunction<ServerResponse> routerFunctionLogin(LoginHandler handler) {
        return route(POST("/api/v1/login"), handler::listenLogin);

    }


}