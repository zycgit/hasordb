/*
 * Copyright 2015-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.hasor.dbvisitor.types.handler;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 读写 float 数据。
 * @author Clinton Begin
 * @author 赵永春 (zyc@hasor.net)
 */
public class FloatTypeHandler extends AbstractTypeHandler<Float> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Float parameter, Integer jdbcType) throws SQLException {
        ps.setFloat(i, parameter);
    }

    @Override
    public Float getNullableResult(ResultSet rs, String columnName) throws SQLException {
        float result = rs.getFloat(columnName);
        return result == 0 && rs.wasNull() ? null : result;
    }

    @Override
    public Float getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        float result = rs.getFloat(columnIndex);
        return result == 0 && rs.wasNull() ? null : result;
    }

    @Override
    public Float getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        float result = cs.getFloat(columnIndex);
        return result == 0 && cs.wasNull() ? null : result;
    }
}