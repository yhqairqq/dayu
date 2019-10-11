package com.caicai.ottx.common.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huaseng on 2019/8/20.
 */
@Data
@NoArgsConstructor
public class MenuVo implements Serializable {
    private String path;
    private String name;
    private String icon;
    private String redirect;
    private Map<String, String> props = new HashMap<>(); // 节点额外属性值
    private Boolean             exact = true;
    private List<MenuVo> children;

    public MenuVo(String path, String name, String icon) {
        this.path = path;
        this.name = name;
        this.icon = icon;
    }
}
