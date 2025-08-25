package co.com.crediya.pragma.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST("/api/v1/users"), handler::listenSaveUser)
                .andRoute(GET("/api/v1/users"), handler::listenGetAllUsers)
                .andRoute(GET("/api/v1/users/{id}"), handler::listenGetUserById)
                .andRoute(DELETE("/api/v1/users/{id}"), handler::listenDeleteUser);
    }
}