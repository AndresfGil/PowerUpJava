package co.com.crediya.pragma.model.user.gateways;

import co.com.crediya.pragma.model.user.Role;
import reactor.core.publisher.Mono;

public interface RoleRepository {
    Mono<Role> findById(Long id);
}
