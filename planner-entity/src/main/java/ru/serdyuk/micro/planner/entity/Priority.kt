package ru.serdyuk.micro.planner.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "priority", schema = "todo", catalog = "planner-todo")
@NoArgsConstructor
@AllArgsConstructor
@Getter@Setter
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Priority implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String color;

    /*@ManyToOne(fetch = FetchType.LAZY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;*/

    @Column(name = "user_id")
    private Long userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Priority priority = (Priority) o;
        return id.equals(priority.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Priority{" +
                "title='" + title + '\'' +
                '}';
    }
}
