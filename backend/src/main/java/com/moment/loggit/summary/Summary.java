package com.moment.loggit.summary;

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
import java.time.Instant;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "summaries")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Summary extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Enumerated(EnumType.STRING)
    @Column(name = "period_type", nullable = false, length = 20)
    private PeriodType periodType;

    @Column(name = "period_start", nullable = false)
    private LocalDate periodStart;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SummaryScope scope;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "rule_content", columnDefinition = "jsonb")
    private String ruleContent;

    @Column(name = "llm_content", columnDefinition = "text")
    private String llmContent;

    @Column(name = "llm_model", length = 100)
    private String llmModel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SummaryStatus status;

    @Column(name = "generated_at")
    private Instant generatedAt;

    public Summary(Project project, PeriodType periodType, LocalDate periodStart, SummaryScope scope) {
        this.project = project;
        this.periodType = periodType;
        this.periodStart = periodStart;
        this.scope = scope;
        this.status = SummaryStatus.PENDING;
    }

    public void completeWithRuleContent(String ruleContentJson) {
        this.ruleContent = ruleContentJson;
        this.status = SummaryStatus.DONE;
        this.generatedAt = Instant.now();
    }

    public void completeWithLlmContent(String llmContent, String llmModel) {
        this.llmContent = llmContent;
        this.llmModel = llmModel;
        this.generatedAt = Instant.now();
    }

    public void markStale() {
        this.status = SummaryStatus.STALE;
    }

    public void markFailed() {
        this.status = SummaryStatus.FAILED;
    }
}
