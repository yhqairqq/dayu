package com.caicai.ottx.dal.handler;

import com.alibaba.otter.shared.common.model.config.parameter.SystemParameter;
import com.alibaba.otter.shared.common.utils.JsonUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by huaseng on 2019/8/26.
 */
@MappedTypes(SystemParameter.class)
@MappedJdbcTypes(JdbcType.LONGVARBINARY)
public class SystemParameterTypeHandler extends BaseTypeHandler<SystemParameter>{
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, SystemParameter parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, JsonUtils.marshalToString(parameter));
    }

    @Override
    public SystemParameter getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return JsonUtils.unmarshalFromString(rs.getString(columnName),SystemParameter.class);
    }

    @Override
    public SystemParameter getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return JsonUtils.unmarshalFromString(rs.getString(columnIndex),SystemParameter.class);
    }

    @Override
    public SystemParameter getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return JsonUtils.unmarshalFromString(cs.getString(columnIndex),SystemParameter.class);
    }
}
