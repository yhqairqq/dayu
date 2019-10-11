package com.caicai.ottx.service.config.channel.impl;

import com.caicai.ottx.dal.entity.TagDO;
import com.caicai.ottx.dal.mapper.TagChannelRelationDOMapperExt;
import com.caicai.ottx.service.config.channel.TagChannelRelationService;
import com.caicai.ottx.service.config.channel.model.Tag;
import com.caicai.ottx.service.config.channel.model.TagChannelRelation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by huaseng on 2019/9/20.
 */
@Slf4j
@Service
public class TagChannelRelationServiceImpl implements TagChannelRelationService {

    @Autowired
    private SqlSession sqlSession;
    @Autowired
    private TagChannelRelationDOMapperExt tagChannelRelationDOMapperExt;


    @Override
    public void create(Tag entityObj) {

    }

    @Override
    public void remove(Long identity) {

    }

    @Override
    public void modify(Tag entityObj) {

    }

    @Override
    public Tag findById(Long identity) {
        return null;
    }

    @Override
    public List<Tag> listByIds(Long... identities) {
        return null;
    }

    @Override
    public List<Tag> listAll() {
        return null;
    }

    @Override
    public List<Tag> listByCondition(Map condition) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public int getCount(Map condition) {
        return 0;
    }

    @Override
    public Tag findByChannelId(Long channelId) {
        List<TagDO> tagDOList =  sqlSession.selectList("findTagByChannelId",channelId);
        if(!CollectionUtils.isEmpty(tagDOList)){
            TagDO tagDO = tagDOList.get(0);
            Tag tag = new Tag();
            BeanUtils.copyProperties(tagDO,tag);
            return tag;
        }
        return null;
    }
}
