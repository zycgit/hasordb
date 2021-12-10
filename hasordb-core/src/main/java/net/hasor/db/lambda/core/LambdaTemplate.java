/*
 * Copyright 2002-2005 the original author or authors.
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
package net.hasor.db.lambda.core;
import net.hasor.db.dialect.SqlDialect;
import net.hasor.db.jdbc.core.JdbcTemplate;
import net.hasor.db.lambda.LambdaOperations;
import net.hasor.db.mapping.def.TableMapping;
import net.hasor.db.mapping.resolve.ClassTableMappingResolve;
import net.hasor.db.mapping.resolve.MappingOptions;
import net.hasor.db.types.TypeHandlerRegistry;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * 继承自 JdbcTemplate 并提供 lambda 方式生成 SQL。
 * @version : 2020-10-27
 * @author 赵永春 (zyc@hasor.net)
 */
public class LambdaTemplate extends JdbcTemplate implements LambdaOperations {
    protected final Map<Class<?>, TableMapping<?>> tableMapping = new HashMap<>();
    protected       SqlDialect                     dialect      = null;

    /**
     * Construct a new JdbcTemplate for bean usage.
     * <p>Note: The DataSource has to be set before using the instance.
     * @see #setDataSource
     */
    public LambdaTemplate() {
        super();
        this.initTypeReader();
    }

    /**
     * Construct a new JdbcTemplate, given a DataSource to obtain connections from.
     * <p>Note: This will not trigger initialization of the exception translator.
     * @param dataSource the JDBC DataSource to obtain connections from
     */
    public LambdaTemplate(final DataSource dataSource) {
        super(dataSource);
        this.initTypeReader();
    }

    /**
     * Construct a new JdbcTemplate, given a DataSource to obtain connections from.
     * <p>Note: This will not trigger initialization of the exception translator.
     * @param dataSource the JDBC DataSource to obtain connections from
     * @param typeRegistry the TypeHandlerRegistry
     */
    public LambdaTemplate(final DataSource dataSource, TypeHandlerRegistry typeRegistry) {
        super(dataSource, typeRegistry);
        this.initTypeReader();
    }

    /**
     * Construct a new JdbcTemplate, given a Connection to obtain connections from.
     * <p>Note: This will not trigger initialization of the exception translator.
     * @param conn the JDBC Connection
     */
    public LambdaTemplate(final Connection conn) {
        super(conn);
        this.initTypeReader();
    }

    /**
     * Construct a new JdbcTemplate, given a Connection to obtain connections from.
     * <p>Note: This will not trigger initialization of the exception translator.
     * @param conn the JDBC Connection
     * @param typeRegistry the TypeHandlerRegistry
     */
    public LambdaTemplate(final Connection conn, TypeHandlerRegistry typeRegistry) {
        super(conn, typeRegistry);
    }

    protected void initTypeReader() {

    }

    public void setDialect(SqlDialect dialect) {
        this.dialect = dialect;
    }

    protected <T> TableMapping<T> getTableMapping(Class<T> exampleType, MappingOptions options) {
        if (exampleType == null) {
            throw new NullPointerException("exampleType is null.");
        }
        if (exampleType == Map.class) {
            throw new UnsupportedOperationException("Map cannot be used as lambda exampleType.");
        }

        TableMapping<?> mapping = this.tableMapping.get(exampleType);
        if (mapping != null) {
            return (TableMapping<T>) mapping;
        }

        synchronized (this) {
            mapping = this.tableMapping.get(exampleType);
            if (mapping != null) {
                return (TableMapping<T>) mapping;
            }

            options = new MappingOptions(options);
            options.setCaseInsensitive(this.isResultsCaseInsensitive());
            TableMapping<?> tableMapping = new ClassTableMappingResolve().resolveTableMapping(exampleType, exampleType.getClassLoader(), this.getTypeRegistry(), options);

            return (TableMapping<T>) tableMapping;
        }
    }

    protected SqlDialect getDefaultDialect() {
        return this.dialect;
    }

    private <T, E extends AbstractExecute<T>> E configDialect(E execute) {
        SqlDialect dialect = getDefaultDialect();
        if (dialect != null) {
            execute.setDialect(dialect);
        }
        return execute;
    }

    @Override
    public <T> LambdaQuery<T> lambdaQuery(Class<T> exampleType, MappingOptions options) {
        return configDialect(new LambdaQueryWrapper<>(getTableMapping(exampleType, options), this));
    }

    @Override
    public <T> LambdaDelete<T> lambdaDelete(Class<T> exampleType, MappingOptions options) {
        return configDialect(new LambdaDeleteWrapper<>(getTableMapping(exampleType, options), this));
    }

    @Override
    public <T> LambdaUpdate<T> lambdaUpdate(Class<T> exampleType, MappingOptions options) {
        return configDialect(new LambdaUpdateWrapper<>(getTableMapping(exampleType, options), this));
    }

    @Override
    public <T> LambdaInsert<T> lambdaInsert(Class<T> exampleType, MappingOptions options) {
        return configDialect(new LambdaInsertWrapper<>(getTableMapping(exampleType, options), this));
    }
}
