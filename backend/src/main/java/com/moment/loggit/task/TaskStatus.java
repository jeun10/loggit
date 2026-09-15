package com.moment.loggit.task;

import com.moment.loggit.global.BaseTimeEntity;
import com.moment.loggit.user.User;
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
@Table(name = "task_statuses")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskStatus extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TaskCategory category;

    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    @Column(length = 7)
    private String color;

    public TaskStatus(User user, String name, TaskCategory category, int displayOrder, String color) {
        this.user = user;
        this.name = name;
        this.category = category;
        this.displayOrder = displayOrder;
        this.color = color;
    }

    public boolean isDone() {
        return category == TaskCategory.DONE;
    }

    public void rename(String name) {
        this.name = name;
    }

    public void reorder(int displayOrder) {
        this.displayOrder = displayOrder;
    }
}
