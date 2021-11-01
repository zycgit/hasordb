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
package net.hasor.db.dal.dynamic.rule;
import net.hasor.db.dal.dynamic.DynamicContext;
import net.hasor.db.dal.dynamic.SqlArg;
import net.hasor.db.dal.dynamic.SqlMode;
import net.hasor.db.dialect.SqlBuilder;
import net.hasor.db.types.TypeHandler;
import net.hasor.db.types.TypeHandlerRegistry;

import java.sql.Types;
import java.util.Map;
import java.util.UUID;

/**
 * 生成一个 32位的 uuid。
 * @version : 2021-10-31
 * @author 赵永春 (zyc@hasor.net)
 */
public class Uuid32Rule implements SqlBuildRule {
    public static final  Uuid32Rule     INSTANCE    = new Uuid32Rule();
    private static final TypeHandler<?> typeHandler = TypeHandlerRegistry.DEFAULT.getTypeHandler(String.class);

    @Override
    public void executeRule(Map<String, Object> data, DynamicContext context, SqlBuilder sqlBuilder, String ruleValue) {
        String uuidValue = UUID.randomUUID().toString().replace("-", "");
        SqlArg sqlArg = new SqlArg(null, ruleValue, uuidValue, SqlMode.In, Types.VARCHAR, String.class, typeHandler);
        sqlBuilder.appendSql("?", sqlArg);
    }

    @Override
    public String toString() {
        return "MD5 [" + this.hashCode() + "]";
    }
}
