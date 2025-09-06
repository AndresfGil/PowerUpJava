package co.com.crediya.pragma.r2dbc.helper;

import co.com.crediya.pragma.model.user.Role;
import co.com.crediya.pragma.r2dbc.entities.RoleEntity;
import org.mapstruct.Mapper;



@Mapper(componentModel = "spring")
public interface RoleEntityMapper {


    RoleEntity toEntity(Role role);


    Role toDomain(RoleEntity entity);


}
