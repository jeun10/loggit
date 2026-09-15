package com.moment.loggit.gitrepo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackedBranchRepository extends JpaRepository<TrackedBranch, Long> {

    List<TrackedBranch> findByRepositoryId(Long repositoryId);
}
