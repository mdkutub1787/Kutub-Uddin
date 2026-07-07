package com.kutub.ecommerce.ecommerce_api.mapper;

import com.kutub.ecommerce.ecommerce_api.dto.UserDTO;
import com.kutub.ecommerce.ecommerce_api.entity.Role;
import com.kutub.ecommerce.ecommerce_api.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", expression = "java(mapRoles(user.getRoles()))")
    UserDTO toDTO(User user);

    default Set<String> mapRoles(Set<Role> roles) {
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }
}
