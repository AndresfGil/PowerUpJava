package co.com.crediya.pragma.r2dbc;

import co.com.crediya.pragma.model.user.solicitudes.UserSimpleView;
import co.com.crediya.pragma.r2dbc.entities.UserEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface MyReactiveRepository extends ReactiveCrudRepository<UserEntity, Long>, ReactiveQueryByExampleExecutor<UserEntity> {

    Mono<UserEntity> findByEmail(String email);

    @Query("""
      SELECT 
        name,
        base_salary,
        email
      FROM users
      WHERE email IN (:correosElectronicos)
    """)
    Flux<UserSimpleView> findLiteByCorreoElectronicoIn(List<String> correosElectronicos);
}
