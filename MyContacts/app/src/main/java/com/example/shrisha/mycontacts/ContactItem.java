package com.example.shrisha.mycontacts;

class ContactItem{

    public String getContactName() {
        return contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    private String contactName;
    private String contactNumber;

    public ContactItem(String contactName, String contactNumber) {
        this.contactName = contactName;
        this.contactNumber = contactNumber;
    }
}
