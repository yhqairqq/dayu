package com.caicai.ottx.manager.controller.canal;

import com.alibaba.otter.canal.instance.manager.model.Canal;
import com.alibaba.otter.canal.instance.manager.model.CanalParameter;
import com.alibaba.otter.manager.biz.common.exceptions.RepeatConfigureException;
import com.alibaba.otter.manager.web.common.model.SeniorCanal;
import com.alibaba.otter.shared.common.model.config.pipeline.Pipeline;
import com.caicai.ottx.common.ApiResult;
import com.caicai.ottx.common.utils.BeanUtils;
import com.caicai.ottx.common.utils.EnumBeanUtils;
import com.caicai.ottx.common.vo.PageResult;
import com.caicai.ottx.manager.controller.canal.form.CanalForm;
import com.caicai.ottx.service.config.canal.CanalService;
import com.caicai.ottx.service.config.pipeline.PipelineService;
import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetSocketAddress;
import java.util.*;

/**
 * Created by huaseng on 2019/9/2.
 */
@RequestMapping("/canal")
@RestController
@Slf4j
public class CanalController {
    @Autowired
    private CanalService canalService;
    @Autowired
    private PipelineService pipelineService;
    @RequestMapping(value = "/getByPage",method = RequestMethod.POST)
    public ApiResult<PageResult> getByPage(@RequestBody CanalForm canalForm){
        try{
            Map<String, Object> condition = BeanUtils.objectToMap(canalForm);
            List<Canal> canals = canalService.listByCondition(condition);
            List<SeniorCanal> seniorCanals = new ArrayList<SeniorCanal>();

            for (Canal canal : canals) {
                SeniorCanal seniorCanal = new SeniorCanal();
                seniorCanal.setId(canal.getId());
                seniorCanal.setName(canal.getName());
                seniorCanal.setStatus(canal.getStatus());
                seniorCanal.setDesc(canal.getDesc());
                seniorCanal.setCanalParameter(canal.getCanalParameter());
                seniorCanal.setGmtCreate(canal.getGmtCreate());
                seniorCanal.setGmtModified(canal.getGmtModified());

                List<Pipeline> pipelines = pipelineService.listByDestinationWithoutOther(canal.getName());
                seniorCanal.setPipelines(pipelines);
                seniorCanal.setUsed(!pipelines.isEmpty());
                seniorCanals.add(seniorCanal);
            }
            return ApiResult.success(new PageResult(seniorCanals,(Page)canals));

        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            return  ApiResult.failed(e.getMessage());
        }
    }

    @RequestMapping(value = "/getAll",method = RequestMethod.POST)
    public ApiResult<List<SeniorCanal>> getAll(@RequestBody CanalForm canalForm){
      try{
          List<Canal> canalList =  canalService.listAll();
          List<SeniorCanal> seniorCanals = new ArrayList<SeniorCanal>();
          for (Canal canal : canalList) {
              SeniorCanal seniorCanal = new SeniorCanal();
              seniorCanal.setId(canal.getId());
              seniorCanal.setName(canal.getName());
              seniorCanal.setStatus(canal.getStatus());
              seniorCanal.setDesc(canal.getDesc());
              seniorCanal.setCanalParameter(canal.getCanalParameter());
              seniorCanal.setGmtCreate(canal.getGmtCreate());
              seniorCanal.setGmtModified(canal.getGmtModified());

              List<Pipeline> pipelines = pipelineService.listByDestinationWithoutOther(canal.getName());
              seniorCanal.setPipelines(pipelines);
              seniorCanal.setUsed(!pipelines.isEmpty());
              seniorCanals.add(seniorCanal);
          }
          return ApiResult.success(seniorCanals);
      }catch (Exception e){
          return  ApiResult.failed(e.getMessage());
      }
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public ApiResult<String> update(@RequestBody CanalForm canalForm){
        Canal canal = new Canal();
        CanalParameter canalParameter = new CanalParameter();
       try{
           EnumBeanUtils.copyProperties(canalForm,canalParameter);
           EnumBeanUtils.copyProperties(canalForm,canal);
       } catch (Exception e){
           e.printStackTrace();
       }
        canalParameter.setZkClusters(new ArrayList<>());
        String dbAddressesString = canalForm.getGroupDbAddresses();
        convert(dbAddressesString,canalParameter);
        String positionsString = canalForm.getPositions();
        if (StringUtils.isNotEmpty(positionsString)) {
            String positions[] = StringUtils.split(positionsString, ";");
            canalParameter.setPositions(Arrays.asList(positions));
        }

        if (canalParameter.getDetectingEnable() && StringUtils.startsWithIgnoreCase(canalParameter.getDetectingSQL(), "select")) {
        }
        canal.setCanalParameter(canalParameter);
        canalService.modify(canal);
        return ApiResult.success("更新成功");
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ApiResult<String> add(@RequestBody CanalForm form){
        Canal canal = new Canal();
        CanalParameter canalParameter = new CanalParameter();
        try{
            EnumBeanUtils.copyProperties(form,canal);
            EnumBeanUtils.copyProperties(form,canalParameter);
            canalParameter.setZkClusters(new ArrayList<>());
            Long zkClusterId = form.getZkClusterId();
            canalParameter.setZkClusterId(zkClusterId);
            canal.setCanalParameter(canalParameter);
            convert(form.getGroupDbAddresses(),canalParameter);
            String positionsString = form.getPositions();
            if (StringUtils.isNotEmpty(positionsString)) {
                String positions[] = StringUtils.split(positionsString, ";");
                canalParameter.setPositions(Arrays.asList(positions));
            }
            canalService.create(canal);
            if (canalParameter.getSourcingType().isMysql() && canalParameter.getSlaveId() == null) {
                canalParameter.setSlaveId(10000 + canal.getId());
                // 再次更新一下slaveId
                try {
                    canalService.modify(canal);
                } catch (RepeatConfigureException rce) {
                    rce.printStackTrace();
                }
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return ApiResult.success("添加成功");
    }
    @RequestMapping(value = "/getByName",method = RequestMethod.POST)
    public ApiResult<SeniorCanal> getByName(@RequestBody CanalForm form){
      try{
          Canal canal =  canalService.findByName(form.getDestinationName());
          SeniorCanal seniorCanal = canalToSensor(canal);
          return ApiResult.success(seniorCanal);
      }catch (Exception e){
          e.printStackTrace();
         return ApiResult.failed(e.getMessage());
      }
    }
    @RequestMapping(value = "/remove",method = RequestMethod.POST)
    public ApiResult remove(@RequestBody CanalForm form){
        canalService.remove(form.getId());
        return ApiResult.success("删除成功");
    }

    private SeniorCanal canalToSensor(Canal canal){
        SeniorCanal seniorCanal = new SeniorCanal();
        seniorCanal.setId(canal.getId());
        seniorCanal.setName(canal.getName());
        seniorCanal.setStatus(canal.getStatus());
        seniorCanal.setDesc(canal.getDesc());
        seniorCanal.setCanalParameter(canal.getCanalParameter());
        seniorCanal.setGmtCreate(canal.getGmtCreate());
        seniorCanal.setGmtModified(canal.getGmtModified());

        List<Pipeline> pipelines = pipelineService.listByDestinationWithoutOther(canal.getName());
        seniorCanal.setPipelines(pipelines);
        seniorCanal.setUsed(!pipelines.isEmpty());
        return seniorCanal;
    }

    private  void convert(String dbAddressesString,CanalParameter parameter){
        if (StringUtils.isNotEmpty(dbAddressesString)) {
            List<List<CanalParameter.DataSourcing>> dbSocketAddress = new ArrayList<List<CanalParameter.DataSourcing>>();
            String[] dbAddresses = StringUtils.split(dbAddressesString, ";");
            for (String dbAddressString : dbAddresses) {
                List<CanalParameter.DataSourcing> groupDbSocketAddress = new ArrayList<CanalParameter.DataSourcing>();
                //多库合并配置 (逗号分隔) 多库合并配置： 10.20.144.34:3306,10.20.144.35:3306; (逗号分隔)
                String[] groupDbAddresses = StringUtils.split(dbAddressString, ",");
                for (String groupDbAddress : groupDbAddresses) {
                    String strs[] = StringUtils.split(groupDbAddress, ":");
                    InetSocketAddress address = new InetSocketAddress(strs[0].trim(), Integer.valueOf(strs[1]));
                    CanalParameter.SourcingType type = parameter.getSourcingType();
                    if (strs.length > 2) {
                        type = CanalParameter.SourcingType.valueOf(strs[2]);
                    }
                    groupDbSocketAddress.add(new CanalParameter.DataSourcing(type, address));
                }
                dbSocketAddress.add(groupDbSocketAddress);
            }

            parameter.setGroupDbAddresses(dbSocketAddress);
        }
    }
}
