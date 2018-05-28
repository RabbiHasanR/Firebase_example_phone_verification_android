package com.example.diu.firebaseapp.ModelClass;

public class Users {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String password;
    private long date;

    public Users(){

    }
    public Users(String name,String email,String phone,String password,long date){
        this.name=name;
        this.email=email;
        this.phone=phone;
        this.password=password;
        this.date=date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
