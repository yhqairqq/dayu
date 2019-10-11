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

package com.caicai.ottx.dal.handler;

import com.alibaba.otter.canal.instance.manager.model.CanalParameter;
import com.alibaba.otter.shared.common.utils.JsonUtils;
import org.apache.ibatis.type.*;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
@MappedTypes(CanalParameter.class)
@MappedJdbcTypes(JdbcType.LONGNVARCHAR)
public class CanalParameterTypeHandler extends BaseTypeHandler<CanalParameter> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, CanalParameter parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, JsonUtils.marshalToString(parameter));
    }

    @Override
    public CanalParameter getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return JsonUtils.unmarshalFromString(rs.getString(columnName), CanalParameter.class);
    }

    @Override
    public CanalParameter getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return JsonUtils.unmarshalFromString(rs.getString(columnIndex), CanalParameter.class);
    }

    @Override
    public CanalParameter getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return JsonUtils.unmarshalFromString(cs.getString(columnIndex), CanalParameter.class);
    }
}
