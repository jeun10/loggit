package com.moment.loggit.commit;

import com.moment.loggit.global.BaseCreatedAtEntity;
import com.moment.loggit.gitrepo.GitRepository;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "commits")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Commit extends BaseCreatedAtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repository_id", nullable = false)
    private GitRepository repository;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(nullable = false, length = 40)
    private String sha;

    @Column(nullable = false, columnDefinition = "text")
    private String message;

    @Column(name = "author_github_id")
    private Long authorGithubId;

    @Column(name = "author_email", length = 255)
    private String authorEmail;

    @Column(name = "author_name", length = 255)
    private String authorName;

    @Column(name = "authored_at", nullable = false)
    private Instant authoredAt;

    @Column(name = "committed_at", nullable = false)
    private Instant committedAt;

    private Integer additions;

    private Integer deletions;

    @Column(name = "html_url", length = 500)
    private String htmlUrl;

    public Commit(
            GitRepository repository,
            String sha,
            String message,
            Long authorGithubId,
            String authorEmail,
            String authorName,
            Instant authoredAt,
            Instant committedAt,
            Integer additions,
            Integer deletions,
            String htmlUrl) {
        this.repository = repository;
        this.sha = sha;
        this.message = message;
        this.authorGithubId = authorGithubId;
        this.authorEmail = authorEmail;
        this.authorName = authorName;
        this.authoredAt = authoredAt;
        this.committedAt = committedAt;
        this.additions = additions;
        this.deletions = deletions;
        this.htmlUrl = htmlUrl;
    }
}
