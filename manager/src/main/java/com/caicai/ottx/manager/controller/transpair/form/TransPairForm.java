package com.caicai.ottx.manager.controller.transpair.form;

import com.caicai.ottx.manager.controller.BaseForm;
import lombok.Data;

/**
 * Created by huaseng on 2019/10/18.
 */
@Data
public class TransPairForm extends BaseForm{
    private long id;
    private long  sourceMediaId;
    private long  targetMediaId;
}
