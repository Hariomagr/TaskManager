package com.example.dellpc.taskmanagers;

public class Person {
    private String mail;
    private String status;
    private String date;
    private String time;
    private String title;
    private String delete;
    public Person(){}
    public Person(String mail, String status,String date,String time,String title,String delete){
        this.mail=mail;
        this.status=status;
        this.date=date;
        this.time=time;
        this.title=title;
        this.delete=delete;
    }

    public String getDelete() {
        return delete;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}