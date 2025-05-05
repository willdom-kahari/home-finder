package com.waduclay.application.notification;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.FlashMapManager;
import org.springframework.web.servlet.support.SessionFlashMapManager;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public class Notifier {
    private Notifier() {
    }

    public static void sendSingleAlert(NotificationCategory type, String message, HttpServletRequest request, HttpServletResponse response) {
        /*
         * Create flash attributes manually and save them before redirect.
         * The attributes are necessary during redirects to transfer messages
         * from one request to the other. Alternatively, inside controllers, you can
         * inject the RedirectAttributes interface and call it's addFlashAttribute() method.
         */
        String infoType = type.name().toLowerCase();
        FlashMap flashMap = new FlashMap();
        flashMap.put(infoType, message);
        final FlashMapManager flashMapManager = new SessionFlashMapManager();
        flashMapManager.saveOutputFlashMap(flashMap, request, response);
    }
}
