package com.chauncey.WeTeBot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @ClassName SyncKey
 * @Description TODO
 * @Author https://github.com/ChaunceyCX
 * @Date 2019/6/28 下午5:39
 * @Version 1.0
 **/
@Data
public class SyncKey {
    @JsonProperty
    private Long Count;
    @JsonProperty
    private List<Map<String, Long>> List;
}
