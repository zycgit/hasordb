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
package net.hasor.db.lambda;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * lambda Insert 执行器
 * @version : 2020-10-31
 * @author 赵永春 (zyc@hasor.net)
 */
public interface InsertExecute<T> extends BoundSqlBuilder {
    /** 执行插入，并且将返回的int结果相加。*/
    public default int executeSumResult() throws SQLException {
        int[] results = this.executeGetResult();
        int sumValue = 0;
        for (int result : results) {
            sumValue = sumValue + result;
        }
        return sumValue;
    }

    /** 执行插入，并返回所有结果*/
    public int[] executeGetResult() throws SQLException;

    /** insert 策略，默认策略是 {@link DuplicateKeyStrategy#Into} */
    public InsertExecute<T> onDuplicateStrategy(DuplicateKeyStrategy strategy);

    /** 批量插入记录 */
    public default InsertExecute<T> applyEntity(T entity) {
        return applyEntity(Collections.singletonList(entity));
    }

    /** 批量插入记录 */
    public InsertExecute<T> applyEntity(List<T> entity);

    /** 批量插入记录，map key 为列名 */
    public default InsertExecute<T> applyMap(Map<String, Object> columnMap) {
        return applyMap(Collections.singletonList(columnMap));
    }

    /** 批量插入记录，map key 为列名 */
    public InsertExecute<T> applyMap(List<Map<String, Object>> columnMapList);

}
