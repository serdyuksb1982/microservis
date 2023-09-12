package ru.serdyuk.micro.planner.users.search

//Data-class, контейнер для хранения значений без функций (методов)
data class UserSearchValues(
                            //первичный конструктор, содержит только обязательные параметры, которые должны иметь значения
                            var email: String,
                            var pageNumber: Int,
                            var pageSize: Int,
                            var sortColumn: String,
                            var sortDirection: String) {
    // поля поиска не обязательные значения

    var username: String? = null
}