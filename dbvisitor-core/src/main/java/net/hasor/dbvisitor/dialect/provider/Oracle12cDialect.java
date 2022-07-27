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
package net.hasor.dbvisitor.dialect.provider;
import net.hasor.dbvisitor.dialect.BoundSql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Oracle12c 的 SqlDialect 实现
 * @author 廖仑辉 (form mybatis-plus-extension-3.3.0.jar)
 * @author 赵永春 (zyc@hasor.net)
 * @since 2019-11-29
 */
public class Oracle12cDialect extends OracleDialect {
    @Override
    public BoundSql countSql(BoundSql boundSql) {
        String sqlBuilder = "SELECT COUNT(*) FROM (" + boundSql.getSqlString() + ") TEMP_T";
        return new BoundSql.BoundSqlObj(sqlBuilder, boundSql.getArgs());
    }

    @Override
    public BoundSql pageSql(BoundSql boundSql, long start, long limit) {
        StringBuilder sqlBuilder = new StringBuilder(boundSql.getSqlString());
        List<Object> paramArrays = new ArrayList<>(Arrays.asList(boundSql.getArgs()));

        sqlBuilder.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        paramArrays.add(start);
        paramArrays.add(limit);
        return new BoundSql.BoundSqlObj(sqlBuilder.toString(), paramArrays.toArray());
    }
}
