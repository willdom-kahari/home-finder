package com.waduclay.homefinder.genericuser;


import com.waduclay.homefinder.shared.*;

import java.util.UUID;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public class User extends Entity<UUID> {
    private NationalIdNumber nationalIdNumber;
    private Name firstName;
    private Name lastName;
    private MobileNumber mobileNumber;
    private Email email;
    private Url nationalIdUrl;
    private Url currentPayslipUrl;


    private User(
            UUID id,
            NationalIdNumber nationalIdNumber,
            Name firstName,
            Name lastName,
            MobileNumber mobileNumber,
            Email email
    ) {
        super(id);
        this.nationalIdNumber = nationalIdNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;
        this.email = email;
    }


    public static User of(
            UUID baseUserId,
            NationalIdNumber nationalIdNumber,
            Name firstName,
            Name lastName,
            MobileNumber mobileNumber,
            Email email
    ) {

        return new User(baseUserId, nationalIdNumber, firstName, lastName, mobileNumber, email);
    }

    public void updateUser(
            NationalIdNumber nationalIdNumber,
            Name firstName,
            Name lastName,
            MobileNumber mobileNumber,
            Email email
    ) {
        this.nationalIdNumber = nationalIdNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;
        this.email = email;
    }

    public void addNationalIdUrl(Url nationalIdUrl) {
        if (this.nationalIdUrl != null) {
            throw new IllegalStateException("Cannot add national id. Already exists!");
        }
        this.nationalIdUrl = nationalIdUrl;
    }

    public void addPayslipUrl(Url payslipUrl) throws IllegalStateException {
        if (this.currentPayslipUrl != null) {
            throw new IllegalStateException("Cannot add payslip. Already exists!");
        }
        this.currentPayslipUrl = payslipUrl;
    }

    public String getNationalIdNumber() {
        return nationalIdNumber.getValue();
    }

    public String getFirstName() {
        return firstName.getValue();
    }

    public String getLastName() {
        return lastName.getValue();
    }

    public String getMobileNumber() {
        return mobileNumber.getValue();
    }

    public String getEmail() {
        return email.getValue();
    }

    public String getNationalIdUrl() {
        return nationalIdUrl.getValue();
    }

    public String getCurrentPayslipUrl() {
        return currentPayslipUrl.getValue();
    }
}
