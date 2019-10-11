package com.caicai.ottx.manager.controller.analysis.form;

import com.caicai.ottx.manager.controller.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by huaseng on 2019/9/2.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AnalysisForm extends BaseForm {

    private int topN = 15;
    private int minute = 1;
    private long pipelineId;
    private long dataMediaPairId;

}
