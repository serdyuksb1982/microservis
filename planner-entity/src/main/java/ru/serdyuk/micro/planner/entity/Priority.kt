package ru.serdyuk.micro.planner.entity

import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter
import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "priority", schema = "todo", catalog = "planner-todo")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
class Priority : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var title: String? = null
    var color: String? = null

    /*@ManyToOne(fetch = FetchType.LAZY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;*/
    @Column(name = "user_id")
    var userId: Long? = null
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val priority = o as Priority
        return id == priority.id
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }

    override fun toString(): String {
        return "Priority{" +
                "title='" + title + '\'' +
                '}'
    }
}