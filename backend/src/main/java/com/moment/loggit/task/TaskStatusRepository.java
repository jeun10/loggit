package com.moment.loggit.task;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long> {

    List<TaskStatus> findByUserIdOrderByDisplayOrder(Long userId);
}
