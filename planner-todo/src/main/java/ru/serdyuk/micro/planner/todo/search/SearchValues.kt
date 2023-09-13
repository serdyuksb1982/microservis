package ru.serdyuk.micro.planner.todo.search

import java.util.*
// в этом файле все дата классы для поиска

data class TaskSearchValues(
    var sortColumn: String,
    var sortDirection: String,
    var pageNumber: Int,
    var pageSize: Int,
    // для фильтрации значений конкретного пользователя
    var userId: Long
) {
    // поля не обязательные к заполнению

    // такое же название должно быть у объекта на frontend
    var title: String? = null
    var completed: Int? = null
    var priorityId: Long? = null
    var categoryId: Long? = null

    var dateFrom // для задания периода
            : Date? = null
    var dateTo: Date? = null
}

data class PrioritySearchValues(
    // для фильтрации значений конкретного пользователя
    var userId: Long

) {
    // такое же название должно быть у объекта на frontend
    var title: String? = null

}

data class CategorySearchValues(
    // для фильтрации значений конкретного пользователя
    var userId: Long
) {
    // такое же название должно быть у объекта на frontend
    var title: String? = null

}