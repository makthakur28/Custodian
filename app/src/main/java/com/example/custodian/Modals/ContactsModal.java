package com.example.custodian.Modals;

public class ContactsModal {

    String name , phone_number ;
    boolean isInvite;
    public ContactsModal(String name, String phone_number,boolean isInvite) {
        this.name = name;
        this.phone_number = phone_number;
        this.isInvite = isInvite;
    }

    public boolean isInvite() {
        return isInvite;
    }

    public void setInvite(boolean invite) {
        isInvite = invite;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
