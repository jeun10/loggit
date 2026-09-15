package com.moment.loggit.user;

public record AuthResponse(String token, UserResponse user) {
}
