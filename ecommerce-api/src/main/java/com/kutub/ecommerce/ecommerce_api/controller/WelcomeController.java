package com.kutub.ecommerce.ecommerce_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    @GetMapping("/welcome")
    public String welcome() {
        return "স্বাগতম! আমাদের ই-কমার্স API সফলভাবে কাজ করছে।";
    }

    @GetMapping("/api/info")
    public String info() {
        return "ই-কমার্স প্রজেক্ট শুরু হলো!";
    }
}
