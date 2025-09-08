package co.com.crediya.pragma.api;
import co.com.crediya.pragma.api.docs.UserControllerDocs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@Configuration
public class RouterRest implements UserControllerDocs {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST("/api/v1/users"), handler::listenSaveUser)
                .andRoute(GET("/api/v1/validate-user"), handler::listenValidateUser)
                .andRoute(POST("/api/v1/users/batch"), handler::listenGetUsersByEmails);

    }
}
