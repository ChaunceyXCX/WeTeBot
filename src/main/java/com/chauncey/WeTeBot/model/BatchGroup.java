package com.chauncey.WeTeBot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName BatchContact
 * @Description 会话列表
 * @Author https://github.com/ChaunceyCX
 * @Date 2019/6/27 下午2:57
 * @Version 1.0
 **/
@Data
@Component
public class BatchGroup {
    @JsonProperty
    private BaseResponse BaseResponse;
    @JsonProperty
    private int Count;
    @JsonProperty
    private List<Group> ContactList;
}
