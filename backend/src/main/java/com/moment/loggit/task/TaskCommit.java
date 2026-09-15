package com.moment.loggit.task;

import com.moment.loggit.commit.Commit;
import com.moment.loggit.global.BaseCreatedAtEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "task_commits")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskCommit extends BaseCreatedAtEntity {

    @EmbeddedId
    private TaskCommitId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("taskId")
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("commitId")
    @JoinColumn(name = "commit_id")
    private Commit commit;

    public TaskCommit(Task task, Commit commit) {
        this.task = task;
        this.commit = commit;
        this.id = new TaskCommitId(task.getId(), commit.getId());
    }
}
