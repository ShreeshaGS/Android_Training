package com.example.shrisha.helloworld;

import android.os.Parcel;
import android.os.Parcelable;

//import java.io.Serializable;

public class Student implements Parcelable {

    private  String name;
    private  String mobilenumber;
    private  String dept;


    public Student(String name,String mobilenumber,String dept){
        this.name = name;
        this.dept = dept;
        this.mobilenumber = mobilenumber;
    }


    protected Student(Parcel in) {
        name = in.readString();
        mobilenumber = in.readString();
        dept = in.readString();
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    public String getDept() {
        return dept;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(mobilenumber);
        dest.writeString(dept);
    }
}
