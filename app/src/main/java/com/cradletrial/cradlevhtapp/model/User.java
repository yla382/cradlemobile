package com.cradletrial.cradlevhtapp.model;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class User {

    private static User currentUser;

    private User(){}

    public static User getCurrentUser() {

        if(currentUser == null){
            currentUser = new User();
        }

        return currentUser;
    }

    public enum Role {
        VHT,
        HEALTH_WORKER,
        ADMIN;
    }

    private Integer userId;
    private String email = null;
    private String phoneNumber = null;
    private String password = null;
    private String firstName = null;
    private String lastName = null;
    private String roles = null;


    public User(String email, String password){
        this.email = email;
        this.password = password;
    }


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }

//    public void setEncodedPassword(String password) {
//        this.password = encodePassword(password);
//    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getRole() {
        return roles;
    }

    public void setRole(String roles) {
        this.roles = roles;
    }

    public String getRolesAsString() {
        return this.roles;
    }

//    public String encodePassword(String password) {
//        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//        return bCryptPasswordEncoder.encode(password);
//    }
//
//    public boolean isVHT(){
//        return getRoles().stream().anyMatch(str -> str.trim().equals(Role.VHT.toString()));
//    }
//
//    public boolean isHealthWorker(){
//        return getRoles().stream().anyMatch(str -> str.trim().equals(Role.HEALTH_WORKER.toString()));
//    }
//
//    public boolean isAdmin(){
//        return getRoles().stream().anyMatch(str -> str.trim().equals(Role.ADMIN.toString()));
//    }

    @Override
    public String toString(){
        return "Name: " + firstName + " " +  lastName + "\nEmail: " + email + "\nRoles:" + roles;
    }

}
