package com.example.custodian.Modals;

public class UserModal {

    String userName,phoneNumber,DOB;

    public UserModal(String userName, String phoneNumber, String DOB) {
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.DOB = DOB;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }
}
