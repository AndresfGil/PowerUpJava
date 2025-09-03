package co.com.crediya.pragma.api;

import co.com.crediya.pragma.api.dto.LoginDTO;
import co.com.crediya.pragma.api.dto.LoginResponseDTO;
import co.com.crediya.pragma.api.dto.SaveUserDTO;
import co.com.crediya.pragma.api.dto.UserResponseDTO;
import co.com.crediya.pragma.api.exception.GlobalExceptionFilter;
import co.com.crediya.pragma.api.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
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
                    path = "/api/v1/login",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "listenLogin",
                    operation = @Operation(
                            operationId = "login",
                            summary = "User login",
                            description = "Autentica un usuario con email y contraseña. Retorna un token JWT que debe usarse en endpoints protegidos.",
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = LoginDTO.class))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Login successful",
                                            content = @Content(schema = @Schema(implementation = LoginResponseDTO.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "401",
                                            description = "Invalid credentials",
                                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                                    )
                            }
                    )
            ),

            @RouterOperation(
                    path = "/api/v1/users",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "listenSaveUser",
                    operation = @Operation(
                            operationId = "saveUser",
                            summary = "Add new user",
                            description = "Crea un nuevo usuario. Solo ADMIN y ASESOR pueden crear usuarios.",
                            security = @SecurityRequirement(name = "bearerAuth"),
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = SaveUserDTO.class))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "User created successfully",
                                            content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Validation error",
                                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "401",
                                            description = "Unauthorized - Invalid token",
                                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "403",
                                            description = "Forbidden - Insufficient permissions",
                                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                                    )
                            }
                    )
            ),

            @RouterOperation(
                    path = "/api/v1/users",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "listenGetAllUsers",
                    operation = @Operation(
                            operationId = "getAllUsers",
                            summary = "Get all users",
                            description = "Obtiene la lista de todos los usuarios. Requiere autenticación.",
                            security = @SecurityRequirement(name = "bearerAuth"),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "List of users",
                                            content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "401",
                                            description = "Unauthorized - Invalid token",
                                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/users/me",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "listenGetUserByEmail",
                    operation = @Operation(
                            operationId = "getCurrentUser",
                            summary = "Get current user information",
                            description = "Obtiene la información del usuario autenticado usando su token. No requiere parámetros adicionales.",
                            security = @SecurityRequirement(name = "bearerAuth"),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "User information retrieved",
                                            content = @Content(schema = @Schema(implementation = Object.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "401",
                                            description = "Unauthorized - Invalid token",
                                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Internal server error",
                                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler, GlobalExceptionFilter filter) {
        return route(POST("/api/v1/login"), handler::listenLogin)
                .andRoute(GET("/api/v1/login"), handler::listenLogin)
                .andRoute(GET("/api/v1/validate-token"), handler::listenValidateToken)
                .andRoute(POST("/api/v1/users"), handler::listenSaveUser)
                .andRoute(GET("/api/v1/users"), handler::listenGetAllUsers)
                .andRoute(GET("/api/v1/users/me"), handler::listenGetUserByEmail)
                .filter(filter);
    }
}
