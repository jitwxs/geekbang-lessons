package org.geektimes.projects.user.service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class OauthService {
    private static final String oauthKey = "adfafajfrlawda";

    private static final Set<String> stateSet = new HashSet<>();

    /**
     * 生成并保存state入缓存
     */
    public static String genState() {
        String state;

        while(!stateSet.add(state = UUID.randomUUID().toString())) {
        }

        return state;
    }

    /**
     * 校验state
     */
    public static Boolean checkState(String state) {
        return stateSet.contains(state);
    }
}