package com.example.shrisha.firsttask;

public class Student {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    private String name;
    private String dept;
    private int year;

    public Student(String name, String dept, int year) {
        this.name = name;
        this.dept = dept;
        this.year = year;
    }

    @Override
    public String toString() {
        return String.format("%s %s %d", name, dept, year);
    }
}
