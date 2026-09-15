package com.moment.loggit.summary;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SummaryRepository extends JpaRepository<Summary, Long> {

    Optional<Summary> findByProjectIdAndPeriodTypeAndPeriodStartAndScope(
            Long projectId, PeriodType periodType, LocalDate periodStart, SummaryScope scope);

    List<Summary> findByStatus(SummaryStatus status);
}
