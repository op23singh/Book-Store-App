package com.op23singh.bookstore2;

public class User {
   public String FirstName,LastName,Email;
    public String FullName;
    public User(){

    }
    public User(String firstName, String lastName, String email) {
        FirstName = firstName;
        LastName = lastName;
        Email = email;
        FullName=firstName+" "+lastName;
    }
}
