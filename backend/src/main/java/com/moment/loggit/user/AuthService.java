package com.moment.loggit.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(
            UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public AuthResponse signup(String email, String rawPassword) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new DuplicateEmailException();
        }
        User user = User.forEmailSignup(email, passwordEncoder.encode(rawPassword));
        userRepository.save(user);
        return issueAuthResponse(user);
    }

    public AuthResponse login(String email, String rawPassword) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("이메일 또는 비밀번호가 올바르지 않습니다"));
        if (user.getPasswordHash() == null || !passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new AuthenticationException("이메일 또는 비밀번호가 올바르지 않습니다");
        }
        return issueAuthResponse(user);
    }

    public UserResponse me(String token) {
        Long userId = jwtTokenProvider.parseUserId(token);
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new AuthenticationException("존재하지 않는 사용자입니다"));
        return UserResponse.from(user);
    }

    private AuthResponse issueAuthResponse(User user) {
        String token = jwtTokenProvider.createToken(user.getId());
        return new AuthResponse(token, UserResponse.from(user));
    }
}
