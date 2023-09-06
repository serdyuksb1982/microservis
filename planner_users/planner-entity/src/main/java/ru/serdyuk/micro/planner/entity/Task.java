package ru.serdyuk.micro.planner.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


@Entity
@Table(name = "task", schema = "todo", catalog = "planner-todo")
@NoArgsConstructor
@AllArgsConstructor
@Getter@Setter
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Task implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String title;

    @Type(type = "org.hibernate.type.NumericBooleanType") // для конвертации числа в true/false
    private Boolean completed; // 1 = true, 0 = false

    @Column(name = "task_date")// в БД поле называется task_date, т.к. нельзя использовать системое имя date
    private Date taskDate;

    // задача может иметь только один приоритет
    @ManyToOne
    @JoinColumn(name = "priority_id", referencedColumnName = "id") // по каким полям связывать
    private Priority priority;

    // задача может иметь только одну категорию (с обратной стороны - одна и та же категория может быть изпользоваться не однократно)
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")// по каким полям связывать (foreign key)
    private Category category; // для какого пользователя задача

    /*@ManyToOne(fetch = FetchType.LAZY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "user_id", referencedColumnName = "id") // по каким полям связывать
    private User user; */// для какого пользователя

    @Column(name = "user_id")
    private Long userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id.equals(task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                '}';
    }
}
