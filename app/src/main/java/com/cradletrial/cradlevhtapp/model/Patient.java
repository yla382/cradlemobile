package com.cradletrial.cradlevhtapp.model;

import android.os.Parcelable;
import android.os.Parcel;

import java.io.Serializable;

public class Patient implements Parcelable {
    private Integer patientId;
    private String attestationID;
    private String firstName = null;
    private String lastName = null;
    private Integer ageYears = null;
    private String country = null;
    private String location = null;



    public Patient(){
    }

    public Patient(Integer patientId, String attestationID, String firstName, String lastName, String country, String location) {
        this.patientId = patientId;
        this.attestationID = attestationID;
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


    protected Patient(Parcel in) {
        if (in.readByte() == 0) {
            patientId = null;
        } else {
            patientId = in.readInt();
        }
        attestationID = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        if (in.readByte() == 0) {
            ageYears = null;
        } else {
            ageYears = in.readInt();
        }
        country = in.readString();
        location = in.readString();
    }

    public static final Creator<Patient> CREATOR = new Creator<Patient>() {
        @Override
        public Patient createFromParcel(Parcel in) {
            return new Patient(in);
        }

        @Override
        public Patient[] newArray(int size) {
            return new Patient[size];
        }
    };

    public Integer getPatientId() {
        return patientId;
    }

    public String getAttestationID() {
        return attestationID;
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

    public void setAttestationID(String attestationID) {
        this.attestationID = attestationID;
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
        return "Patient{" +
                "patientId=" + patientId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", ageYears=" + ageYears +
                ", country='" + country + '\'' +
                ", location='" + location + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (patientId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(patientId);
        }
        dest.writeString(attestationID);
        dest.writeString(firstName);
        dest.writeString(lastName);
        if (ageYears == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(ageYears);
        }
        dest.writeString(country);
        dest.writeString(location);
    }
}
