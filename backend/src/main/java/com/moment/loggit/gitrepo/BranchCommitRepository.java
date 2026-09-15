package com.moment.loggit.gitrepo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchCommitRepository extends JpaRepository<BranchCommit, BranchCommitId> {
}
