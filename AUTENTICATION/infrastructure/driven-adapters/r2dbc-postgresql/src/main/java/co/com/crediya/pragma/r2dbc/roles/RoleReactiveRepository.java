package co.com.crediya.pragma.r2dbc.roles;

import co.com.crediya.pragma.r2dbc.entities.RoleEntity;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface RoleReactiveRepository extends ReactiveCrudRepository<RoleEntity, Long>, ReactiveQueryByExampleExecutor<RoleEntity> {


}
