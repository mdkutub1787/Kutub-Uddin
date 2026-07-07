package com.kutub.ecommerce.ecommerce_api.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String email;
    private String password;
    
    // This helper method allows us to get whatever the user provided
    public String getUsernameOrEmail() {
        return username != null ? username : email;
    }
}
