package ru.serdyuk.micro.planner.entity

import org.hibernate.annotations.Type
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "activity", schema = "todo", catalog = "planner-todo")
class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Type(type = "org.hibernate.type.NumericBooleanType")
    val activated: Boolean? = null

    @Column(updatable = false)
    val uuid // создается только один раз с помощью триггеров в БД
            : String? = null

    /*@OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;*/
    @Column(name = "user_id")
    val userId: Long? = null
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val activity = o as Activity
        return id == activity.id
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }
}