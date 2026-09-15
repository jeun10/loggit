package com.moment.loggit.project;

import com.moment.loggit.global.BaseTimeEntity;
import com.moment.loggit.global.Visibility;
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
@Table(name = "projects")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column(length = 7)
    private String color;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Visibility visibility;

    public Project(User user, String name, String description, String color) {
        this.user = user;
        this.name = name;
        this.description = description;
        this.color = color;
        this.visibility = Visibility.PRIVATE;
    }

    public void rename(String name) {
        this.name = name;
    }

    public void updateDetails(String description, String color) {
        this.description = description;
        this.color = color;
    }

    public void changeVisibility(Visibility visibility) {
        this.visibility = visibility;
    }
}
