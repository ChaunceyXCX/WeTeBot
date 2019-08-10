package com.chauncey.WeTeBot.repository;

import com.chauncey.WeTeBot.model.wechat.WechatMember;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author https://github.com/ChaunceyCX
 * @version 1.0
 * @className MemberRepository
 * @description TODO
 * @date 2019/6/28 上午11:08
 **/
public interface MemberRepository extends JpaRepository<WechatMember, Long> {
    WechatMember findByNickName(String NickName);

    WechatMember findByUserName(String UserName);
}
