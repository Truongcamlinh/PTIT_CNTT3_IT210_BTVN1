package com.holiday.ex01.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{todo.validation.content.notBlank}")
    @Column(nullable = false)
    private String content;

    @NotNull(message = "{todo.validation.dueDate.notNull}")
    @FutureOrPresent(message = "{todo.validation.dueDate.futureOrPresent}")
    @Column(nullable = false)
    private LocalDate dueDate;

    @NotNull(message = "{todo.validation.status.notNull}")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TodoStatus status;

    @NotNull(message = "{todo.validation.priority.notNull}")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TodoPriority priority;
}
