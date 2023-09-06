package ru.serdyuk.micro.planner.users.search;

public class UserSearchValues {

    private String email;
    private String username;

    //
    private Integer pageNumber;
    private Integer pageSize;

    //
    private String sortColumn;
    private String sortDirection;

    public UserSearchValues() {

    }

    public UserSearchValues(String email, String username, Integer pageNumber, Integer pageSize, String sortColumn, String sortDirection) {
        this.email = email;
        this.username = username;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.sortColumn = sortColumn;
        this.sortDirection = sortDirection;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
