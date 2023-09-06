package ru.serdyuk.micro.planner.todo.search;

public class PrioritySearchValues {
    private String title;// такое же название должно быть у объекта на frontend
    private Long userId;// для фильтрации значений конкретного пользователя

    public PrioritySearchValues() {

    }

    public PrioritySearchValues(String title, Long userId) {
        this.title = title;
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
