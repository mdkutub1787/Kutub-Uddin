package com.kutub.ecommerce.ecommerce_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private UserDTO user; // রেজিস্ট্রেশন বা লগইনের পর ইউজারের তথ্যও সাথে যাবে

    public AuthResponse(String accessToken, UserDTO user) {
        this.accessToken = accessToken;
        this.user = user;
    }
}
