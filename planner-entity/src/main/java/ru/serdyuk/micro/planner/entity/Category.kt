package ru.serdyuk.micro.planner.entity

import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "category", schema = "todo", catalog = "planner-todo")
class Category : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var title: String? = null

    @Column(name = "completed_count", updatable = false)
    val completedCount: Long? = null

    @Column(name = "uncompleted_count", updatable = false)
    val uncompletedCount: Long? = null

    /*@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;*/
    @Column(name = "user_id")
    val userId: Long? = null
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val category = o as Category
        return id == category.id
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }

    override fun toString(): String {
        return title!!
    }
}