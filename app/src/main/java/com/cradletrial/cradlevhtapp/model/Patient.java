package com.cradletrial.cradlevhtapp.model;

public class Patient {
    private Integer patientId;
    private String firstName = null;
    private String lastName = null;
    private Integer ageYears = null;
    private String country = null;
    private String location = null;


    public Patient() {
    }

    public Patient(Integer patientId, String firstName, String lastName, String country, String location) {
        this.patientId = patientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.location = location;
    }

    public Patient(String firstName, String lastName, Integer ageYears) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.ageYears = ageYears;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public String getCountry() {
        return country;
    }

    public String getLocation() {
        return location;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Integer getAgeYears() {
        return ageYears;
    }

    public void setAgeYears(Integer ageYears) {
        this.ageYears = ageYears;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    @Override
    public String toString() {
        return this.getFirstName();
    }

}
