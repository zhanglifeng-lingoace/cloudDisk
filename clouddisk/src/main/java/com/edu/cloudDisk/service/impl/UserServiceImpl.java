package com.edu.cloudDisk.service.impl;

import com.edu.cloudDisk.common.Constants;
import com.edu.cloudDisk.common.LingoAceUserBriefInfo;
import com.edu.cloudDisk.service.UserService;
import com.lingoace.pub.studentcenter.clients.UserParentInfoClient;
import com.lingoace.pub.studentcenter.domain.response.UserParentInfoResp;
import com.lingoace.teacher.clients.TutorClient;
import com.lingoace.teacher.domain.request.TutorConditionReq;
import com.lingoace.teacher.domain.response.UserTutorInfoResp;
import com.lingoace.usercenter.clients.UserInfoClient;
import com.lingoace.usercenter.domain.response.UserInfoResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserParentInfoClient parentInfoClient;
    @Autowired
    private TutorClient tutorClient;
    @Autowired
    private UserInfoClient opsInfoClient;

    @Override
    public LingoAceUserBriefInfo getUserByName(String username) {
        log.info("接口请求过来的原始username={}",username);
        if (username.contains(Constants.USER_PASSWORD_SPLIT)) {
            //进入C端的校验查询逻辑
            String[] run = username.split(Constants.USER_PASSWORD_SPLIT);
            Integer role = "null".equals(run[0]) ? Constants.USER_ROLE_PARENT : Integer.parseInt(run[0]);
            String username1 = run[1];
            if (role == Constants.USER_ROLE_TUTOR) {
                TutorConditionReq tutorConditionReq = new TutorConditionReq();
                tutorConditionReq.setUsername(username1);
                tutorConditionReq.setOrderBy("create_time desc");
                List<UserTutorInfoResp> tutorInfoRespList = tutorClient.selectUserTutorInfoByExample(tutorConditionReq).getData();
                if (CollectionUtils.isEmpty(tutorInfoRespList)) {
                    return null;
                }
                UserTutorInfoResp userTutorInfo = tutorInfoRespList.get(0);
                LingoAceUserBriefInfo lingoAceUserBriefInfo = new LingoAceUserBriefInfo();
                BeanUtils.copyProperties(userTutorInfo, lingoAceUserBriefInfo);
                lingoAceUserBriefInfo.setUsername(username1);
                return lingoAceUserBriefInfo;
            } else if ((role == Constants.USER_ROLE_PARENT) || (role == Constants.USER_ROLE_STUDENT)) {
                List<UserParentInfoResp> parentInfoRespList = parentInfoClient.selectByUsernameAndOrder(username1, "create_time desc").getData();
                if (CollectionUtils.isEmpty(parentInfoRespList)) {
                    return null;
                }
                UserParentInfoResp userParentInfoResp = parentInfoRespList.get(0);
                LingoAceUserBriefInfo lingoAceUserBriefInfo = new LingoAceUserBriefInfo();
                BeanUtils.copyProperties(userParentInfoResp, lingoAceUserBriefInfo);
                lingoAceUserBriefInfo.setUsername(username1);
                return lingoAceUserBriefInfo;
            } else {
                return null;
            }
        } else {
            //进入ops端查询逻辑
            UserInfoResp userInfo = opsInfoClient.queryByUsername(username).getData();
            LingoAceUserBriefInfo userBriefInfo = new LingoAceUserBriefInfo();
            BeanUtils.copyProperties(userInfo, userBriefInfo);
            return userBriefInfo;
        }
    }
}
