package com.cradletrial.cradlevhtapp.model;

import android.support.annotation.RequiresPermission;

import org.threeten.bp.ZonedDateTime;

public class Referral {
    private Long referralId;
    private Reading currentReading;
    private ZonedDateTime dateReferralSent = ZonedDateTime.now();
    private String firstName;
    private String lastName;
    private Integer ageYears;
    private Integer bpSystolic;
    private Integer bpDiastolic;
    private Integer heartRateBPM;
    private String vhtName = null;
    private String referredHealthCentre = null;
    private String otherMessage = null;

    public Referral() {
    }


    public Referral(Long referralId, Reading currentReading, ZonedDateTime zonedDateTime, String vhtName, String referredHealthCentre, String otherMessage) {
        this.referralId = referralId;
        this.currentReading = currentReading;
        this.dateReferralSent = zonedDateTime;
        this.firstName = currentReading.patientFirstName;
        this.lastName = currentReading.patientLastName;
        this.ageYears = currentReading.ageYears;
        this.bpSystolic = currentReading.bpSystolic;
        this.bpDiastolic = currentReading.bpDiastolic;
        this.heartRateBPM = currentReading.heartRateBPM;
        this.vhtName = vhtName;
        this.referredHealthCentre = referredHealthCentre;
        this.otherMessage = otherMessage;
    }

    public Referral(Reading currentReading) {
        this.currentReading = currentReading;
        this.firstName = currentReading.patientFirstName;
        this.lastName = currentReading.patientLastName;
        this.ageYears = currentReading.ageYears;
        this.bpSystolic = currentReading.bpSystolic;
        this.bpDiastolic = currentReading.bpDiastolic;
        this.heartRateBPM = currentReading.heartRateBPM;
    }

    public Long getReferralId() {
        return referralId;
    }

    public void setReferralId(Long referralId) {
        this.referralId = referralId;
    }

    public ZonedDateTime getDateReferralSent() {
        if (dateReferralSent == null) {
            return ZonedDateTime.now();
        }
        return dateReferralSent;
    }

    public void setDateReferralSent(ZonedDateTime zonedDateTime) {
        if (zonedDateTime == null) {
            this.dateReferralSent = ZonedDateTime.now();
        }
        this.dateReferralSent = zonedDateTime;
    }

    public Reading getCurrentReading() {
        return currentReading;
    }

    public void setCurrentReading(Reading currentReading) {
        this.currentReading = currentReading;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(Reading currentReading) {
        this.firstName = currentReading.patientFirstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(Reading currentReading) {
        this.lastName = currentReading.patientLastName;
    }

    public Integer getAgeYears() {
        return ageYears;
    }

    public void setAgeYears(Reading currentReading) {
        this.ageYears = currentReading.ageYears;
    }

    public Integer getBpSystolic() {
        return bpSystolic;
    }

    public void setBpSystolic(Reading currentReading) {
        this.bpSystolic = currentReading.bpSystolic;
    }

    public Integer getBpDiastolic() {
        return bpDiastolic;
    }

    public void setBpDiastolic(Reading currentReading) {
        this.bpDiastolic = currentReading.bpDiastolic;
    }

    public Integer getHeartRateBPM() {
        return heartRateBPM;
    }

    public void setHeartRateBPM(Reading currentReading) {
        this.heartRateBPM = currentReading.heartRateBPM;
    }

    public String getVhtName() {
        if (vhtName == null) {
            return "NA";
        }
        return vhtName;
    }

    public void setVhtName(String vhtName) {
        if (vhtName == null) {
            this.vhtName = "NA";
        }
        this.vhtName = vhtName;
    }

    public String getReferredHealthCentre() {
        if (referredHealthCentre == null) {
            return "NA";
        }
        return referredHealthCentre;
    }

    public void setReferredHealthCentre(String referredHealthCentre) {
        if (referredHealthCentre == null) {
            this.referredHealthCentre = "NA";
        }
        this.referredHealthCentre = referredHealthCentre;
    }

    public String getOtherMessage() {
        if (otherMessage == null) {
            return "NA";
        }
        return otherMessage;
    }

    public void setOtherMessage(String otherMessage) {
        if (otherMessage == null) {
            this.otherMessage = "NA";
        }
        this.otherMessage = otherMessage;
    }

    @Override
    public String toString() {
        return "Referral{" +
                "referralId=" + referralId +
                ", currentReading=" + currentReading +
                ", dateReferralSent=" + dateReferralSent +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", ageYears=" + ageYears +
                ", bpSystolic=" + bpSystolic +
                ", bpDiastolic=" + bpDiastolic +
                ", heartRateBPM=" + heartRateBPM +
                ", vhtName='" + vhtName + '\'' +
                ", referredHealthCentre='" + referredHealthCentre + '\'' +
                ", otherMessage='" + otherMessage + '\'' +
                '}';
    }

}
