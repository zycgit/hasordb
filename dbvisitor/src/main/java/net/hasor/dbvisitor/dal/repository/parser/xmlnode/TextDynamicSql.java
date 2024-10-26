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
package net.hasor.dbvisitor.dal.repository.parser.xmlnode;
import net.hasor.cobble.StringUtils;
import net.hasor.dbvisitor.dynamic.*;

import java.sql.SQLException;

/**
 * 文本块
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2021-05-24
 */
public class TextDynamicSql implements DynamicSql {
    private final StringBuilder textBuilder = new StringBuilder();
    private       DynamicSql    dynamicSql;

    public TextDynamicSql(String text) {
        this.appendText(StringUtils.isBlank(text) ? "" : text);
    }

    public void appendText(String text) {
        if (StringUtils.isNotBlank(text)) {
            this.textBuilder.append(text);
        }
        this.dynamicSql = parserQuery(this.textBuilder.toString());
    }

    @Override
    public boolean isHaveInjection() {
        return this.dynamicSql.isHaveInjection();
    }

    @Override
    public void buildQuery(SqlArgSource data, RegistryManager context, SqlBuilder sqlBuilder) throws SQLException {
        this.dynamicSql.buildQuery(data, context, sqlBuilder);
    }

    protected DynamicSql parserQuery(String fragmentString) {
        return DynamicParsed.getParsedSql(fragmentString);
    }
}