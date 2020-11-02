/*
 * Copyright 2002-2010 the original author or authors.
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
package net.hasor.db.jdbc.lambda.mapping;
/**
 * 映射到数据库的表信息。
 * @version : 2020-10-31
 * @author 赵永春 (zyc@hasor.net)
 */
public class TableMeta {
    private final String tableName;   // 表名

    public TableMeta(String tableName) {
        this.tableName = tableName;
    }

    /** 表名 */
    public String getTable() {
        return this.tableName;
    }
}