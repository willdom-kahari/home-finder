package com.waduclay.properties.user;


/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public record UserAccountStatus(boolean credentialsExpired, boolean accountLocked, boolean accountActive) {
}
