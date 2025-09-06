package co.com.crediya.pragma.api.docs;

import co.com.crediya.pragma.api.Handler;
import co.com.crediya.pragma.api.dto.*;
import co.com.crediya.pragma.model.user.solicitudes.UserSimpleView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

public interface UserControllerDocs {

    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/users",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "listenSaveUser",
                    operation = @Operation(
                            operationId = "createUser",
                            summary = "Crear usuario",
                            description = "Crea un nuevo usuario. Requiere autenticación con rol ADMIN o ASESOR.",
                            security = @SecurityRequirement(name = "Bearer Authentication"),
                            requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = SaveUserDTO.class))),
                            responses = {
                                    @ApiResponse(responseCode = "201", description = "Usuario creado",
                                            content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
                                    @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
                                    @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT requerido"),
                                    @ApiResponse(responseCode = "403", description = "Prohibido - Rol insuficiente"),
                                    @ApiResponse(responseCode = "409", description = "Usuario ya existe")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/users/batch",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "listenGetUsersByEmails",
                    operation = @Operation(
                            operationId = "Get Users",
                            summary = "Traer usuarios por emails",
                            description = "Listado de informacion de usuarios. Requiere autenticación con rol ADMIN o ASESOR.",
                            security = @SecurityRequirement(name = "Bearer Authentication"),
                            requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = BatchUsersRequestDTO.class))),
                            responses = {
                                    @ApiResponse(responseCode = "201", description = "Usuarios listados",
                                            content = @Content(schema = @Schema(implementation = UserSimpleView.class))),
                                    @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
                                    @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT requerido"),
                                    @ApiResponse(responseCode = "403", description = "Prohibido - Rol insuficiente"),
                                    @ApiResponse(responseCode = "409", description = "Lista no disponible")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/users/validate-user",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "listenValidateUser",
                    operation = @Operation(
                            operationId = "Get User",
                            summary = "Confirma existencia de usuario",
                            description = "Validacion del usuario. Requiere autenticación con rol CLIENTE.",
                            security = @SecurityRequirement(name = "Bearer Authentication"),
                            requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = RequestValidateUserDTO.class))),
                            responses = {
                                    @ApiResponse(responseCode = "201", description = "Informacion",
                                            content = @Content(schema = @Schema(implementation = UserValidateDTO.class))),
                                    @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
                                    @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT requerido"),
                                    @ApiResponse(responseCode = "403", description = "Prohibido - Rol insuficiente"),
                                    @ApiResponse(responseCode = "409", description = "Usuario no encontrado")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler);

}
