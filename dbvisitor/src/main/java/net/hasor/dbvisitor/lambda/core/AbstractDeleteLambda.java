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
package net.hasor.dbvisitor.lambda.core;
import net.hasor.dbvisitor.dialect.BoundSql;
import net.hasor.dbvisitor.dialect.SqlDialect;
import net.hasor.dbvisitor.dynamic.RegistryManager;
import net.hasor.dbvisitor.jdbc.core.JdbcTemplate;
import net.hasor.dbvisitor.lambda.segment.MergeSqlSegment;
import net.hasor.dbvisitor.mapping.def.TableMapping;

import java.sql.SQLException;
import java.util.Objects;

import static net.hasor.dbvisitor.lambda.segment.SqlKeyword.*;

/**
 * 提供 lambda delete 基础能力。
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2020-10-27
 */
public abstract class AbstractDeleteLambda<R, T, P> extends BasicQueryCompare<R, T, P> implements DeleteExecute<R> {
    private boolean allowEmptyWhere = false;

    public AbstractDeleteLambda(Class<?> exampleType, TableMapping<?> tableMapping, RegistryManager registry, JdbcTemplate jdbc) {
        super(exampleType, tableMapping, registry, jdbc);
    }

    @Override
    public int doDelete() throws SQLException {
        Objects.requireNonNull(this.jdbc, "Connection unavailable, JdbcTemplate is required.");

        BoundSql boundSql = getBoundSql();
        String sqlString = boundSql.getSqlString();

        if (logger.isDebugEnabled()) {
            logger.trace("Executing SQL statement [" + sqlString + "].");
        }

        return this.jdbc.executeUpdate(sqlString, boundSql.getArgs());
    }

    @Override
    public R allowEmptyWhere() {
        this.allowEmptyWhere = true;
        return getSelf();
    }

    @Override
    protected BoundSql buildBoundSql(SqlDialect dialect) {
        // must be clean , The getOriginalBoundSql will reinitialize.
        this.queryParam.clear();
        //
        // delete
        MergeSqlSegment updateTemplate = new MergeSqlSegment();
        updateTemplate.addSegment(DELETE);
        updateTemplate.addSegment(FROM);

        // tableName
        updateTemplate.addSegment(d -> {
            TableMapping<?> tableMapping = this.getTableMapping();
            String catalogName = tableMapping.getCatalog();
            String schemaName = tableMapping.getSchema();
            String tableName = tableMapping.getTable();
            return d.tableName(isQualifier(), catalogName, schemaName, tableName);
        });

        // WHERE
        if (!this.queryTemplate.isEmpty()) {
            updateTemplate.addSegment(WHERE);
            updateTemplate.addSegment(this.queryTemplate.sub(1));
        } else if (!this.allowEmptyWhere) {
            throw new UnsupportedOperationException("The dangerous DELETE operation, You must call `allowEmptyWhere()` to enable DELETE ALL.");
        }

        String sqlQuery = updateTemplate.getSqlSegment(dialect);
        Object[] args = this.queryParam.toArray().clone();
        return new BoundSql.BoundSqlObj(sqlQuery, args);
    }

}
