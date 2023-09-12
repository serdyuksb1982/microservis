package ru.serdyuk.micro.planner.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "activity", schema = "todo", catalog = "planner-todo")
@NoArgsConstructor
@AllArgsConstructor
@Getter@Setter
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean activated;

    @Column(updatable = false)
    private String uuid; // создается только один раз с помощью триггеров в БД

    /*@OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;*/

    @Column(name = "user_id")
    private Long userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return id.equals(activity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
