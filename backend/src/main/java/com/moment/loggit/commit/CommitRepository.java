package com.moment.loggit.commit;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommitRepository extends JpaRepository<Commit, Long> {

    List<Commit> findByRepositoryIdAndAuthoredAtBetween(Long repositoryId, Instant from, Instant to);

    Optional<Commit> findByRepositoryIdAndSha(Long repositoryId, String sha);
}
