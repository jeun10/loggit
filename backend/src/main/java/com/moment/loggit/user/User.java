package com.moment.loggit.user;

import com.moment.loggit.global.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "github_id", unique = true)
    private Long githubId;

    @Column(name = "github_login", length = 100)
    private String githubLogin;

    @Column(length = 255)
    private String email;

    @Column(name = "password_hash", length = 255)
    private String passwordHash;

    @Column(name = "display_name", length = 100)
    private String displayName;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @Column(nullable = false, length = 50)
    private String timezone;

    public User(Long githubId, String githubLogin, String timezone) {
        this.githubId = githubId;
        this.githubLogin = githubLogin;
        this.timezone = timezone != null ? timezone : "Asia/Seoul";
    }

    public static User forEmailSignup(String email, String passwordHash) {
        User user = new User();
        user.email = email;
        user.passwordHash = passwordHash;
        user.timezone = "Asia/Seoul";
        return user;
    }

    public void updateProfile(String email, String displayName, String avatarUrl) {
        this.email = email;
        this.displayName = displayName;
        this.avatarUrl = avatarUrl;
    }

    public void changeTimezone(String timezone) {
        this.timezone = timezone;
    }
}
