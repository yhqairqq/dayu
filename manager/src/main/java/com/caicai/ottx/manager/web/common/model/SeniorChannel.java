/*
 * Copyright (C) 2010-2101 Alibaba Group Holding Limited.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.caicai.ottx.manager.web.common.model;

import com.alibaba.otter.shared.common.model.config.channel.Channel;
import com.caicai.ottx.service.config.channel.model.Tag;
import lombok.Data;

/**
 * @author simon 2012-1-13 下午03:34:32
 */
@Data
public class SeniorChannel extends Channel {

    private static final long serialVersionUID = -5864547001482768341L;
    private boolean           processEmpty;
    private Tag tag;
    private boolean            isReady;   //是否能启动

    public boolean isProcessEmpty() {
        return processEmpty;
    }

    public void setProcessEmpty(boolean processEmpty) {
        this.processEmpty = processEmpty;
    }
}
