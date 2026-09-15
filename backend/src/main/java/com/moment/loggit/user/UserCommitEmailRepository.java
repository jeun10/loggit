package com.moment.loggit.user;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCommitEmailRepository extends JpaRepository<UserCommitEmail, Long> {

    List<UserCommitEmail> findByUserId(Long userId);
}
