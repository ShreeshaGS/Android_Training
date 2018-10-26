package com.example.shrisha.lIstviewexperiment;

public class Contacts {
    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public long getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(long contactNumber) {
        this.contactNumber = contactNumber;
    }

    String contactName;
    long contactNumber;

    public Contacts(String contactName, long contactNumber) {
        this.contactName = contactName;
        this.contactNumber = contactNumber;
    }
}
