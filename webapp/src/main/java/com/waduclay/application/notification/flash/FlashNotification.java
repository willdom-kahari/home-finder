package com.waduclay.application.notification.flash;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.FlashMapManager;
import org.springframework.web.servlet.support.SessionFlashMapManager;

/**
 * Utility class for sending flash notifications.
 *
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public final class FlashNotification {

    private FlashNotification() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    /**
     * Sends a single flash alert notification.
     *
     * @param type     The category/type of notification
     * @param message  The message content to display
     * @param request  The HTTP servlet request
     * @param response The HTTP servlet response
     * @throws IllegalArgumentException if any parameter is null
     */
    public static void sendSingleAlert(NotificationCategory type, String message,
                                       HttpServletRequest request, HttpServletResponse response) {
        validateParameters(type, message, request, response);

        FlashMap flashMap = createFlashMap(type, message);
        getFlashMapManager().saveOutputFlashMap(flashMap, request, response);
    }

    private static void validateParameters(NotificationCategory type, String message,
                                           HttpServletRequest request, HttpServletResponse response) {
        if (type == null || message == null || request == null || response == null) {
            throw new IllegalArgumentException("None of the parameters can be null");
        }
    }

    private static FlashMap createFlashMap(NotificationCategory type, String message) {
        String infoType = type.name().toLowerCase();
        FlashMap flashMap = new FlashMap();
        flashMap.put(infoType, message);
        return flashMap;
    }

    private static FlashMapManager getFlashMapManager() {
        return new SessionFlashMapManager();
    }
}
