package com.chauncey.WeTeBot.repository;

import com.chauncey.WeTeBot.model.job.WeJob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @ClassName WeJobRepository
 * @Description TODO
 * @Author https://github.com/ChaunceyCX
 * @Date 2019/8/8 下午6:47
 * @Version 1.0
 **/
public interface WeJobRepository extends JpaRepository<WeJob, Long> {
    WeJob getWeJobByJobName(String jobName);

    WeJob getWeJobById(Integer id);

}
