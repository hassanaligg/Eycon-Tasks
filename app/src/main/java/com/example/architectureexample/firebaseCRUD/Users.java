package com.example.architectureexample.firebaseCRUD;

public class Users {
    private String Name;
    private String Age;

    public Users() {
    }

    @Override
    public String toString() {
        return "Users{" +
                "Name='" + Name + '\'' +
                ", Age='" + Age + '\'' +
                '}';
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }
}
