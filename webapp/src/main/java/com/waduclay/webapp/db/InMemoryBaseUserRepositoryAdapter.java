package com.waduclay.webapp.db;


import com.waduclay.homefinder.baseuser.BaseUser;
import com.waduclay.homefinder.baseuser.BaseUserRepositoryPort;

import java.util.Map;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public class InMemoryBaseUserRepositoryAdapter implements BaseUserRepositoryPort {
    private final Map<String, BaseUser> userMap;

    public InMemoryBaseUserRepositoryAdapter(Map<String, BaseUser> userMap) {
        this.userMap = userMap;
    }


    @Override
    public void save(BaseUser user) {
        userMap.put(user.getUsername(), user);
    }
}
