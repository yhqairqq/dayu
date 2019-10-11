package com.caicai.ottx.service.config.datamedia;

import com.alibaba.otter.manager.biz.common.baseservice.GenericService;
import com.alibaba.otter.shared.common.model.config.data.DataMedia;

import java.util.List;

/**
 * Created by huaseng on 2019/8/23.
 */
public interface DataMediaService   extends GenericService<DataMedia> {

     List<DataMedia> listByDataMediaSourceId(Long dataMediaSourceId);

     Long createReturnId(DataMedia dataMedia);

     List<String> queryColumnByMedia(DataMedia dataMedia);

     List<String> queryColumnByMediaId(Long dataMediaId);


}
