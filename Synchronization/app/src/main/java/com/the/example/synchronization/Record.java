package com.the.example.synchronization;

import java.util.UUID;


 class Record {


    

    private int id;
    private String firstName;
    private String lastName;
    private int age;
    private String sex;
    private String comment;
    private boolean isSync;
     Record()
    {

    }

     public int getId() {
         return id;
     }

     void setId(int id) {
         this.id = id;
     }
    String getFirstName() {
        return firstName;
    }

     void setFirstName(String firstName) {
        this.firstName = firstName;
    }

     String getLastName() {
        return lastName;
    }

     void setLastName(String lastName) {
        this.lastName = lastName;
    }

     int getAge() {
        return age;
    }

     void setAge(int age) {
        this.age = age;
    }

     String getSex() {
        return sex;
    }

     void setSex(String sex) {
        this.sex = sex;
    }

     String getComment() {
        return comment;
    }

     void setComment(String comment) {
        this.comment = comment;
    }

     boolean isSynchronized() {
        return isSync;
    }

     void setSynchronized(boolean aSync) {
        isSync = aSync;
    }
}
