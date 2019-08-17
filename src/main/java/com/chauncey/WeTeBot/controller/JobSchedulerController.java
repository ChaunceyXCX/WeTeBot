package com.chauncey.WeTeBot.controller;

import com.chauncey.WeTeBot.config.Result;
import com.chauncey.WeTeBot.model.job.WeJob;
import com.chauncey.WeTeBot.service.IJobService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName JobSchedulerController
 * @Description TODO
 * @Author https://github.com/ChaunceyCX
 * @Date 2019/8/10 下午5:21
 * @Version 1.0
 **/
@Controller
@RequestMapping("/job")
@Log4j2
public class JobSchedulerController {

    @Autowired
    private IJobService jobService;

    @PostMapping("/add")
    public Result save(WeJob weJob) {
        log.info("新任务: {}",weJob);
        return jobService.saveJob(weJob);
    }

}
