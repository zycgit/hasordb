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
package net.hasor.dbvisitor.dynamic.segment;
import net.hasor.dbvisitor.dynamic.DynamicContext;
import net.hasor.dbvisitor.dynamic.SqlArgSource;
import net.hasor.dbvisitor.dynamic.SqlBuilder;

import java.sql.SQLException;

public class TextSqlSegment implements SqlSegment {
    private final StringBuilder textString;

    public TextSqlSegment(String exprString) {
        this.textString = new StringBuilder(exprString);
    }

    public void append(String append) {
        this.textString.append(append);
    }

    @Override
    public void buildQuery(SqlArgSource data, DynamicContext context, SqlBuilder sqlBuilder) throws SQLException {
        sqlBuilder.appendSql(this.textString.toString());
    }

    @Override
    public TextSqlSegment clone() {
        return new TextSqlSegment(this.textString.toString());
    }

    @Override
    public String toString() {
        return "Text [" + this.textString + "]";
    }
}
