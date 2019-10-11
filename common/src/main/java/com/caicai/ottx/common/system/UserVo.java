package com.caicai.ottx.common.system;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huaseng on 2019/8/20.
 */
@Data
public class UserVo implements Serializable {
    private static final long serialVersionUID = 8331110200724759149L;


    private Long         id;         // 用户Id
    private String       nickname;   // 昵称
    private String       username;   // 登录邮箱
    private String       department; // 部门
    private String       position;   // 职位
    private String       avatar;     // 头像
    private String       phone;      // 手机号
    private String       signature;  // 个性签名
    private String       email;      // 邮箱号
    private String       address;    // 地址
    private List<String> tags;       // 用户标签
    private String   token;     // 会话token
}
