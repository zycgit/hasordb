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
package net.hasor.dbvisitor.faker.generator;
import net.hasor.cobble.CollectionUtils;
import net.hasor.cobble.RandomUtils;
import net.hasor.dbvisitor.dialect.SqlDialect;
import net.hasor.dbvisitor.faker.FakerConfig;
import net.hasor.dbvisitor.faker.OpsType;
import net.hasor.dbvisitor.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * GeneratorTrans
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
public class GeneratorTrans {
    private final FakerConfig          fakerConfig;
    private final FakerFactory         fakerFactory;
    private final List<GeneratorTable> generatorTables;

    public GeneratorTrans(FakerFactory fakerFactory) {
        this.fakerConfig = fakerFactory.getFakerConfig();
        this.fakerFactory = fakerFactory;
        this.generatorTables = new CopyOnWriteArrayList<>();
    }

    public List<BoundQuery> generator() throws SQLException {
        return generator(null);
    }

    public List<BoundQuery> generator(OpsType opsType) throws SQLException {
        GeneratorTable table = randomTable();
        if (table == null) {
            return Collections.emptyList();
        }

        List<BoundQuery> events = new LinkedList<>();
        int opsCountPerTransaction = this.fakerConfig.randomOpsCountPerTrans();
        for (int i = 0; i < opsCountPerTransaction; i++) {
            List<BoundQuery> dataSet = this.generatorOps(randomTable(), opsType);
            events.addAll(dataSet);
        }
        return events;
    }

    public List<BoundQuery> generatorOneTable() throws SQLException {
        return generatorOneTable(null);
    }

    public List<BoundQuery> generatorOneTable(OpsType opsType) throws SQLException {
        GeneratorTable table = randomTable();
        if (table == null) {
            return Collections.emptyList();
        }

        List<BoundQuery> events = new LinkedList<>();
        int opsCountPerTransaction = this.fakerConfig.randomOpsCountPerTrans();
        for (int i = 0; i < opsCountPerTransaction; i++) {
            List<BoundQuery> dataSet = this.generatorOps(table, opsType);
            events.addAll(dataSet);
        }
        return events;
    }

    protected GeneratorTable randomTable() {
        if (!CollectionUtils.isEmpty(this.generatorTables)) {
            return this.generatorTables.get(RandomUtils.nextInt(0, this.generatorTables.size()));
        }
        return null;
    }

    protected List<BoundQuery> generatorOps(GeneratorTable fakerTable, OpsType opsType) throws SQLException {
        opsType = opsType != null ? opsType : this.fakerConfig.randomOps();
        if (opsType == null) {
            throw new IllegalStateException("no any boundary were declared, please init one.");
        }

        int batchSize = this.fakerConfig.randomBatchSizePerOps();

        switch (opsType) {
            case Insert:
                return fakerTable.buildInsert(batchSize);
            case Update:
                return fakerTable.buildUpdate(batchSize);
            case Delete:
                return fakerTable.buildDelete(batchSize);
            default:
                return Collections.emptyList();
        }
    }

    public FakerTable addTable(String catalog, String schema, String table) throws SQLException {
        try {
            GeneratorTable fetchTable = this.fakerFactory.fetchTable(catalog, schema, table);
            this.addTable(fetchTable);
            return fetchTable.getTableInfo();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("init table failed : " + e.getMessage(), e);
        }
    }

    public FakerTable addTable(FakerTable table) {
        SqlDialect dialect = this.fakerFactory.getFakerConfig().getDialect();
        JdbcTemplate jdbcTemplate = this.fakerFactory.getJdbcTemplate();
        this.addTable(new GeneratorTable(table, dialect, jdbcTemplate));
        return table;
    }

    public FakerTable addTable(GeneratorTable table) {
        if (table != null) {
            this.generatorTables.add(table);
            return table.getTableInfo();
        }
        return null;
    }
}
