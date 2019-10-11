package com.caicai.ottx.service.config.channel.impl;

import com.caicai.ottx.dal.entity.TagChannelRelationDO;
import com.caicai.ottx.dal.entity.TagChannelRelationDOExample;
import com.caicai.ottx.dal.entity.TagDO;
import com.caicai.ottx.dal.entity.TagDOExample;
import com.caicai.ottx.dal.mapper.TagChannelRelationDOMapperExt;
import com.caicai.ottx.dal.mapper.TagDOMapperExt;
import com.caicai.ottx.service.config.channel.TagService;
import com.caicai.ottx.service.config.channel.model.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.ecs.html.Col;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by huaseng on 2019/9/20.
 */
@Service
@Slf4j
public class TagServiceImpl implements TagService {
    @Autowired
    private SqlSession sqlSession;
    @Autowired
    private TagDOMapperExt tagDOMapperExt;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private TagChannelRelationDOMapperExt tagChannelRelationDOMapperExt;

    @Override
    public void create(Tag tag) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                TagDO tagDo = new TagDO();
                BeanUtils.copyProperties(tag,tagDo);
                tagDOMapperExt.insertSelective(tagDo);
                TagChannelRelationDO tagChannelRelationDO = new TagChannelRelationDO();
                tagChannelRelationDO.setChannelId(tag.getChannelId());
                tagChannelRelationDO.setTagId(tagDo.getId());
                tagChannelRelationDO.setGmtCreate(new Date());
                tagChannelRelationDO.setGmtModified(new Date());
                tagChannelRelationDOMapperExt.insertSelective(tagChannelRelationDO);
            }
        });
    }

    @Override
    public void remove(Long identity) {
        tagDOMapperExt.deleteByPrimaryKey(identity);
        TagChannelRelationDOExample example = new TagChannelRelationDOExample();
        example.createCriteria().andChannelIdEqualTo(identity);
        tagChannelRelationDOMapperExt.deleteByExample(example);
    }

    @Override
    public void modify(Tag entityObj) {
        TagDO tagDO = new TagDO();
        TagDOExample example = new TagDOExample();
        TagChannelRelationDOExample tagChannelRelationDOExample  = new TagChannelRelationDOExample();
        tagChannelRelationDOExample.createCriteria().andChannelIdEqualTo(entityObj.getChannelId());
        List<TagChannelRelationDO> tagChannelRelationDOList =  tagChannelRelationDOMapperExt.selectByExample(tagChannelRelationDOExample);
        if(CollectionUtils.isEmpty(tagChannelRelationDOList)){
            BeanUtils.copyProperties(entityObj,tagDO);
            int count = tagDOMapperExt.insertSelective(tagDO);
            if(count > 0){
               TagChannelRelationDO channelRelationDO = new TagChannelRelationDO();
               channelRelationDO.setChannelId(entityObj.getChannelId());
               channelRelationDO.setTagId(tagDO.getId());
               tagChannelRelationDOMapperExt.insertSelective(channelRelationDO);
           }
            return;
        }else{
            TagChannelRelationDO channelRelationDO = tagChannelRelationDOList.get(0);
            entityObj.setId(channelRelationDO.getTagId());
            BeanUtils.copyProperties(entityObj,tagDO);
            tagDOMapperExt.updateByPrimaryKeySelective(tagDO);
        }
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
}
