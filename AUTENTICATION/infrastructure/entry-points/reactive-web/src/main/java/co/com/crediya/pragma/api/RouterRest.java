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
        return route(POST("/api/users"), handler::listenSaveUser)       // Crear usuario
                .andRoute(GET("/api/users"), handler::listenGetAllUsers) // Listar usuarios
                .andRoute(GET("/api/users/{id}"), handler::listenGetUserById) // Buscar por id
                .andRoute(DELETE("/api/users/{id}"), handler::listenDeleteUser); // Eliminar usuario
    }
}