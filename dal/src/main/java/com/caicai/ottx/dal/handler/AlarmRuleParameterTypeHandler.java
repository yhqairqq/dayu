package com.caicai.ottx.dal.handler;

import com.alibaba.otter.canal.instance.manager.model.CanalParameter;
import com.alibaba.otter.shared.common.utils.JsonUtils;
import com.caicai.ottx.dal.entity.AlarmRuleParameter;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by huaseng on 2019/9/2.
 */
@MappedTypes(AlarmRuleParameter.class)
@MappedJdbcTypes(JdbcType.LONGNVARCHAR)
public class AlarmRuleParameterTypeHandler extends BaseTypeHandler<AlarmRuleParameter>{
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, AlarmRuleParameter parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, JsonUtils.marshalToString(parameter));
    }

    @Override
    public AlarmRuleParameter getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return JsonUtils.unmarshalFromString(rs.getString(columnName), AlarmRuleParameter.class);
    }

    @Override
    public AlarmRuleParameter getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return JsonUtils.unmarshalFromString(rs.getString(columnIndex), AlarmRuleParameter.class);
    }

    @Override
    public AlarmRuleParameter getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return JsonUtils.unmarshalFromString(cs.getString(columnIndex), AlarmRuleParameter.class);
    }
}
