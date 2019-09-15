package com.chauncey.WeTeBot.controller;

import com.chauncey.WeTeBot.config.Result;
import com.chauncey.WeTeBot.model.job.WeJob;
import com.chauncey.WeTeBot.service.IJobService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName JobSchedulerController
 * @Description TODO
 * @Author https://github.com/ChaunceyCX
 * @Date 2019/8/10 下午5:21
 * @Version 1.0
 **/
@RestController
@RequestMapping("/job")
@Log4j2
@CrossOrigin
public class JobSchedulerController {

    @Autowired
    private IJobService jobService;
    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/add")
    public Result save(@RequestBody WeJob weJob) {
        log.info("新任务: {}",weJob);
        return jobService.saveJob(weJob);
    }

    @PostMapping("/update")
    public Result update(@RequestBody WeJob weJob) {
        log.info("新任务: {}",weJob);
        return jobService.updateJob(weJob);
    }

    @PostMapping("/remove")
    public Result remove(@RequestBody WeJob weJob) {
        log.info("新任务: {}",weJob);
        return jobService.removeJob(weJob);
    }


    @GetMapping("/list")
    public String getAll() {
        log.info("获取任务列表");
        try {
            return objectMapper.writeValueAsString(jobService.getAllJob());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.getStackTrace().toString();
        }
    }



    @PostMapping("/switch")
    public Result startJob(@RequestBody WeJob weJob) {
        if (weJob.getTriggerState().equals("RUNNING")){
            log.info("启动任务: {}",weJob);
            return jobService.pauseJob(weJob);
        }else {
            log.info("暂停任务: {}",weJob);
            return jobService.resumeJob(weJob);
        }

    }
}
