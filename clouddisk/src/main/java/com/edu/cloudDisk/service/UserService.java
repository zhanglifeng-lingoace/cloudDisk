package com.edu.cloudDisk.service;


import com.edu.cloudDisk.common.LingoAceUserBriefInfo;

public interface UserService {
    LingoAceUserBriefInfo getUserByName(String username);
}
