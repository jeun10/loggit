package com.moment.loggit.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GithubCredentialRepository extends JpaRepository<GithubCredential, Long> {

    Optional<GithubCredential> findByUserId(Long userId);
}
