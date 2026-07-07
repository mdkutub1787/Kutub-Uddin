package com.kutub.ecommerce.ecommerce_api.dto;

import lombok.Data;
import java.util.Set;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private Set<String> roles;
}
