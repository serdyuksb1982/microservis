package ru.serdyuk.micro.planner.entity

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "stat", schema = "todo", catalog = "planner-todo")
class Stat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(name = "completed_total", updatable = false)
    val completedTotal: Long? = null

    @Column(name = "uncompleted_total", updatable = false)
    val uncompletedTotal: Long? = null

    /*@OneToOne(fetch = FetchType.LAZY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @MapsId
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;*/
    @Column(name = "user_id")
    val userId: Long? = null
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val stat = o as Stat
        return id == stat.id
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }
}