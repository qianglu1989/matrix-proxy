package com.matrix.proxy.controller;

import com.matrix.proxy.service.JdkCommandService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author luqiang
 */
@RestController
@RequestMapping("/command")
public class CommandController {

    @Resource
    private JdkCommandService jdkCommandService;

    @ResponseBody
    @RequestMapping("/getMetric")
    public String getMetric(@RequestParam final String instanceUuid,Integer type) {
        return jdkCommandService.getMetric(instanceUuid,type);
    }


}
