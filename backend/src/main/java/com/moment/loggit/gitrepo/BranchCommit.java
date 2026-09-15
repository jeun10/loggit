package com.moment.loggit.gitrepo;

import com.moment.loggit.commit.Commit;
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
@Table(name = "branch_commits")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BranchCommit {

    @EmbeddedId
    private BranchCommitId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("branchId")
    @JoinColumn(name = "branch_id")
    private TrackedBranch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("commitId")
    @JoinColumn(name = "commit_id")
    private Commit commit;

    public BranchCommit(TrackedBranch branch, Commit commit) {
        this.branch = branch;
        this.commit = commit;
        this.id = new BranchCommitId(branch.getId(), commit.getId());
    }
}
