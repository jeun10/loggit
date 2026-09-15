package com.moment.loggit.note;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByUserIdAndNoteDate(Long userId, LocalDate noteDate);

    Optional<Note> findByIdAndUserId(Long id, Long userId);
}
