package ru.serdyuk.micro.planner.todo.search;

import java.util.Date;

public class TaskSearchValues {

    // поля поиска (все типы - объекты. чтобы можно было передавать через null)
    private String title;
    private Integer completed;
    private Long priorityId;
    private Long categoryId;
    private Long userId;
    private Date dateFrom; // для задания периода
    private Date dateTo;

    // постраничность
    private Integer pageNumber;
    private Integer pageSize;

    //Сортировка
    private String sortColumn;
    private String sortDirection;

    public TaskSearchValues() {

    }

    public TaskSearchValues(String title,
                            Integer completed,
                            Long priorityId,
                            Long categoryId,
                            Long userId,
                            Date dateFrom,
                            Date dateTo,
                            Integer pageNumber,
                            Integer pageSize,
                            String sortColumn,
                            String sortDirection) {
        this.title = title;
        this.completed = completed;
        this.priorityId = priorityId;
        this.categoryId = categoryId;
        this.userId = userId;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.sortColumn = sortColumn;
        this.sortDirection = sortDirection;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCompleted() {
        return completed;
    }

    public void setCompleted(Integer completed) {
        this.completed = completed;
    }

    public Long getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(Long priorityId) {
        this.priorityId = priorityId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }
}
