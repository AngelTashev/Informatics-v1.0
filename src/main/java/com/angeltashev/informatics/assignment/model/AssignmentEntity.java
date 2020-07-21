package com.angeltashev.informatics.assignment.model;

import com.angeltashev.informatics.user.model.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "assignments")
public class AssignmentEntity {

    @Id
    @GeneratedValue(generator = "uuid-string")
    @GenericGenerator(name = "uuid-string", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne (
            fetch = FetchType.EAGER
    )
    private UserEntity user;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    @Column(name = "due_date", nullable = false)
    private LocalDateTime dueDate;

    @Column(name = "submission")
    private byte[] submission;

    @Column(name = "resource")
    private byte[] resource;

}
