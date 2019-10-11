package com.caicai.ottx.service.config.channel;

import com.caicai.ottx.service.common.baseservice.GenericService;
import com.caicai.ottx.service.config.channel.model.Tag;

/**
 * Created by huaseng on 2019/9/20.
 */
public interface TagChannelRelationService extends GenericService<Tag> {

    Tag findByChannelId(Long channelId);
}
