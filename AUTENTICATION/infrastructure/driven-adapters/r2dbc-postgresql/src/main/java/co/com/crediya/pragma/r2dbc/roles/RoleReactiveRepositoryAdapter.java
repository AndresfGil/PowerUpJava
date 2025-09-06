package co.com.crediya.pragma.r2dbc.roles;


import co.com.crediya.pragma.model.user.Role;
import co.com.crediya.pragma.model.user.gateways.RoleRepository;
import co.com.crediya.pragma.r2dbc.entities.RoleEntity;
import co.com.crediya.pragma.r2dbc.helper.ReactiveAdapterOperations;
import co.com.crediya.pragma.r2dbc.helper.RoleEntityMapper;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;


@Repository
public class RoleReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Role,
        RoleEntity,
        Long,
        RoleReactiveRepository
        > implements RoleRepository {

    private final RoleEntityMapper roleEntityMapper;

    public RoleReactiveRepositoryAdapter(RoleReactiveRepository repository, ObjectMapper mapper, RoleEntityMapper roleEntityMapper) {
        super(repository, mapper, d -> mapper.map(d, Role.class));
        this.roleEntityMapper = roleEntityMapper;
    }
}
