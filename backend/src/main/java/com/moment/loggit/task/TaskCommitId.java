package com.moment.loggit.task;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class TaskCommitId implements Serializable {

    private Long taskId;
    private Long commitId;

    public TaskCommitId(Long taskId, Long commitId) {
        this.taskId = taskId;
        this.commitId = commitId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaskCommitId that)) {
            return false;
        }
        return Objects.equals(taskId, that.taskId) && Objects.equals(commitId, that.commitId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, commitId);
    }
}
