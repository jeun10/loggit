package com.moment.loggit.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.moment.loggit.global.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "github_credentials")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GithubCredential extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "encrypted_access_token", nullable = false)
    @JsonIgnore
    private String encryptedAccessToken;

    @Column(name = "key_version", nullable = false)
    private int keyVersion;

    @Column(length = 500)
    private String scopes;

    public GithubCredential(User user, String encryptedAccessToken, String scopes) {
        this.user = user;
        this.encryptedAccessToken = encryptedAccessToken;
        this.keyVersion = 1;
        this.scopes = scopes;
    }

    public void rotateToken(String encryptedAccessToken, int keyVersion) {
        this.encryptedAccessToken = encryptedAccessToken;
        this.keyVersion = keyVersion;
    }

    @Override
    public String toString() {
        return "GithubCredential{id=" + id + "}";
    }
}
