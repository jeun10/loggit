package com.moment.loggit.gitrepo;

import com.moment.loggit.global.BaseTimeEntity;
import com.moment.loggit.project.Project;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "git_repositories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GitRepository extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(name = "github_repo_id", nullable = false)
    private Long githubRepoId;

    @Column(nullable = false, length = 100)
    private String owner;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "default_branch", nullable = false)
    private String defaultBranch;

    @Column(name = "is_private", nullable = false)
    private boolean isPrivate;

    @Enumerated(EnumType.STRING)
    @Column(name = "connection_status", nullable = false, length = 20)
    private ConnectionStatus connectionStatus;

    public GitRepository(
            Project project,
            Long githubRepoId,
            String owner,
            String name,
            String defaultBranch,
            boolean isPrivate) {
        this.project = project;
        this.githubRepoId = githubRepoId;
        this.owner = owner;
        this.name = name;
        this.defaultBranch = defaultBranch;
        this.isPrivate = isPrivate;
        this.connectionStatus = ConnectionStatus.CONNECTED;
    }

    public void renameRepository(String owner, String name) {
        this.owner = owner;
        this.name = name;
    }

    public void markDisconnected() {
        this.connectionStatus = ConnectionStatus.DISCONNECTED;
    }

    public void markNotFound() {
        this.connectionStatus = ConnectionStatus.NOT_FOUND;
    }

    public void markConnected() {
        this.connectionStatus = ConnectionStatus.CONNECTED;
    }
}
