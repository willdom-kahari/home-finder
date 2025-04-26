package com.waduclay.homefinder.genericuser;


import com.waduclay.homefinder.shared.*;

import java.util.UUID;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public class User extends Entity<UUID> {
    private final UUID baseUserId;
    private NationalIdNumber nationalIdNumber;
    private String firstName;
    private String lastName;
    private MobileNumber mobileNumber;
    private Email email;
    private Url nationalIdUrl;
    private Url currentPayslipUrl;


    private User(UUID id) {
        super(id);
        this.baseUserId = id;
    }

    public static User of(
            NationalIdNumber nationalIdNumber,
            Name firstName,
            Name lastName,
            MobileNumber mobileNumber,
            Email email
    ) {
        return new User(UUID.randomUUID());
    }
}
