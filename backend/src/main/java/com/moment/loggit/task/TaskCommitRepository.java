package com.moment.loggit.task;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskCommitRepository extends JpaRepository<TaskCommit, TaskCommitId> {
}
