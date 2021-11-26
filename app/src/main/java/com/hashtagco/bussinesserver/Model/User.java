package com.hashtagco.bussinesserver.Model;


//مودل كلاس بيانات المستخدم  لتخزينها على الفيربيز
public class User
{
    private String Name;
    private String Password;
    private String Phone;
    private String IsStaff;
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    private  String uid;
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;

    private String homeAddress;
    private double balance;



    public User() {

    }



    public User( String email ,String name, String password , String phone) {
        Phone=phone;

        Name = name;
        Password=password;
        IsStaff="true";
        this.email = email ;

    }


    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }


    public String getIsStaff() {
        return IsStaff;
    }

    public void setIsStaff(String isStaff) {
        IsStaff = isStaff;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }


    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }
}
