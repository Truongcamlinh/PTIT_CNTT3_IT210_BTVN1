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

    @NotBlank(message = "Nội dung công việc không được để trống")
    @Column(nullable = false)
    private String content;

    @NotNull(message = "Ngày hết hạn không được để trống")
    @FutureOrPresent(message = "Ngày hết hạn phải là hôm nay hoặc trong tương lai")
    @Column(nullable = false)
    private LocalDate dueDate;

    @NotNull(message = "Vui lòng chọn trạng thái công việc")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TodoStatus status = TodoStatus.PENDING;

    @NotNull(message = "Vui lòng chọn độ ưu tiên công việc")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TodoPriority priority = TodoPriority.MEDIUM;
}
