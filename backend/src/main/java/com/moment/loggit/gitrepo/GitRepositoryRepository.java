package com.moment.loggit.gitrepo;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GitRepositoryRepository extends JpaRepository<GitRepository, Long> {

    List<GitRepository> findByProjectId(Long projectId);

    Optional<GitRepository> findByIdAndProject_UserId(Long id, Long userId);
}
