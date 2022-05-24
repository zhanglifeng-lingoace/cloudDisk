package com.edu.cloudDisk.controller;
import com.edu.common.response.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/cloudDisk/check")
public class CheckController  {

    @ResponseBody
    @GetMapping("/alive")
    public CommonResult appVersion() {
        Map<String, String> versionMap = new HashMap<>(1);
        versionMap.put("version", "edu cloud disk 1.0");
        return CommonResult.success(versionMap);
    }

}
