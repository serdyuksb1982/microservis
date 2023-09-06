package ru.serdyuk.micro.planner.todo.search;


public class CategorySearchValues {
    private String title;// такое же название должно быть у объекта на frontend
    private Long userId;// для фильтрации значений конкретного пользователя

    public CategorySearchValues() {
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
