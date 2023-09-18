package ru.serdyuk.micro.planner.todo.service

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.annotation.KafkaListeners
import org.springframework.stereotype.Service
import ru.serdyuk.micro.planner.entity.Category
import ru.serdyuk.micro.planner.entity.Priority
import ru.serdyuk.micro.planner.entity.Task
import java.util.*

@Service
class TestDataService(
    private val taskService: TaskService,
    private val priorityService: PriorityService,
    private val categoryService: CategoryService
) {

    @KafkaListener(topics = ["sergey-sb"])
    fun listenKafka(userId: Long) {
        println("new userId =$userId")
        initTestData(userId)
    }

    fun initTestData(userId: Long) {
        val prior1 = Priority()
        prior1.color = "#fff"
        prior1.title = "Важный"
        prior1.userId = userId
        val prior2 = Priority()
        prior2.color = "#ffe"
        prior2.title = "Не важный"
        prior2.userId = userId
        priorityService.add(prior1)
        priorityService.add(prior2)
        val cat1 = Category()
        cat1.title = "Work"
        cat1.userId = userId
        val cat2 = Category()
        cat2.title = "Test"
        cat2.userId = userId
        categoryService.add(cat1)
        categoryService.add(cat2)

        ///
        var tomorrow = Date()
        val c = Calendar.getInstance()
        c.time = tomorrow
        c.add(Calendar.DATE, 1)
        tomorrow = c.time

        //
        var oneWeek = Date()
        val c2 = Calendar.getInstance()
        c2.time = oneWeek
        c2.add(Calendar.DATE, 7)
        oneWeek = c2.time
        val task1 = Task()
        task1.title = "Поесть....."
        task1.category = cat1
        task1.priority = prior1
        task1.completed = true
        task1.taskDate = tomorrow
        task1.userId = userId
        val task2 = Task()
        task2.title = "Поспать..."
        task2.category = cat2
        task2.completed = false
        task2.priority = prior2
        task2.taskDate = oneWeek
        task2.userId = userId
        taskService.add(task1)
        taskService.add(task2)
    }
}