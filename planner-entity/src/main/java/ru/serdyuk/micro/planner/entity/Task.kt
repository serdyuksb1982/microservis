package ru.serdyuk.micro.planner.entity

import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import org.hibernate.annotations.Type
import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "task", schema = "todo", catalog = "planner-todo")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
class Task : Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    var id: Long? = null
    var title: String? = null

    @Type(type = "org.hibernate.type.NumericBooleanType") // для конвертации числа в true/false

    var completed // 1 = true, 0 = false
            : Boolean? = null

    @Column(name = "task_date") // в БД поле называется task_date, т.к. нельзя использовать системое имя date

    var taskDate: Date? = null

    // задача может иметь только один приоритет
    @ManyToOne
    @JoinColumn(name = "priority_id", referencedColumnName = "id") // по каким полям связывать

    var priority: Priority? = null

    // задача может иметь только одну категорию (с обратной стороны - одна и та же категория может быть изпользоваться не однократно)
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id") // по каким полям связывать (foreign key)

    var category // для какого пользователя задача
            : Category? = null

    /*@ManyToOne(fetch = FetchType.LAZY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "user_id", referencedColumnName = "id") // по каким полям связывать
    private User user; */
    // для какого пользователя
    @Column(name = "user_id")
    var userId: Long? = null
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val task = o as Task
        return id == task.id
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }

    override fun toString(): String {
        return "Task{" +
                "title='" + title + '\'' +
                '}'
    }
}