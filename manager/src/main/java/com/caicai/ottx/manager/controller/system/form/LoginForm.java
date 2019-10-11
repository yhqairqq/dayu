package com.caicai.ottx.manager.controller.system.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2019-06-10 00:03.
 * Modify:
 */
@Data
public class LoginForm implements Serializable {
    @NotBlank(message = "登录名不能为空", groups = LoginCheck.class)
    private String userName;
    @NotBlank(message = "密码不能为空", groups = LoginCheck.class)
    private String password;

    public interface LoginCheck {
    }
}
