package co.com.crediya.pragma.api;

import co.com.crediya.pragma.api.dto.SaveUserDTO;
import co.com.crediya.pragma.api.exception.GlobalExceptionFilter;
import co.com.crediya.pragma.api.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/users",
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "listenSaveUser",
                    operation = @Operation(
                            summary = "Crear un usuario",
                            description = "Registra un nuevo usuario en el sistema",
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = SaveUserDTO.class))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Usuario creado exitosamente",
                                            content = @Content(schema = @Schema(implementation = SaveUserDTO.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Error de validación",
                                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/users",
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "listenGetAllUsers",
                    operation = @Operation(summary = "Listar todos los usuarios")
            ),
            @RouterOperation(
                    path = "/api/v1/users/{id}",
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "listenGetUserById",
                    operation = @Operation(
                            summary = "Buscar usuario por ID",
                            parameters = {
                                    @Parameter(
                                            name = "id",
                                            description = "Identificador del usuario",
                                            required = true,
                                            in = ParameterIn.PATH,
                                            schema = @Schema(type = "integer")
                                    )
                            },
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
                                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/users/{id}",
                    method = RequestMethod.DELETE,
                    beanClass = Handler.class,
                    beanMethod = "listenDeleteUser",
                    operation = @Operation(
                            summary = "Eliminar usuario por ID",
                            parameters = {
                                    @Parameter(
                                            name = "id",
                                            description = "Identificador del usuario",
                                            required = true,
                                            in = ParameterIn.PATH,
                                            schema = @Schema(type = "integer")
                                    )
                            },
                            responses = {
                                    @ApiResponse(responseCode = "204", description = "Usuario eliminado"),
                                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler, GlobalExceptionFilter filter) {
        return route(POST("/api/v1/users"), handler::listenSaveUser)
                .andRoute(GET("/api/v1/users"), handler::listenGetAllUsers)
                .andRoute(GET("/api/v1/users/{id}"), handler::listenGetUserById)
                .andRoute(DELETE("/api/v1/users/{id}"), handler::listenDeleteUser)
                .filter(filter);
    }
}