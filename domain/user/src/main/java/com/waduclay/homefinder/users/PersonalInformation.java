package com.waduclay.homefinder.users;


import com.waduclay.homefinder.shared.*; /**
 * Immutable class encapsulating personal information.
 */
public record PersonalInformation(
        NationalIdNumber nationalIdNumber,
        Name firstName,
        Name lastName,
        MobileNumber mobileNumber,
        Email email
) {
    public PersonalInformation(NationalIdNumber nationalIdNumber, Name firstName, Name lastName, MobileNumber mobileNumber, Email email) {
        this.nationalIdNumber = InputGuard.requireNonNull(nationalIdNumber, "national ID number");
        this.firstName = InputGuard.requireNonNull(firstName, "first name");
        this.lastName = InputGuard.requireNonNull(lastName, "last name");
        this.mobileNumber = InputGuard.requireNonNull(mobileNumber, "mobile number");
        this.email = email; // Nullable, no validation needed
    }
}
