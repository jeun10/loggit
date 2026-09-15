package com.moment.loggit.gitrepo;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class BranchCommitId implements Serializable {

    private Long branchId;
    private Long commitId;

    public BranchCommitId(Long branchId, Long commitId) {
        this.branchId = branchId;
        this.commitId = commitId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BranchCommitId that)) {
            return false;
        }
        return Objects.equals(branchId, that.branchId) && Objects.equals(commitId, that.commitId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(branchId, commitId);
    }
}
