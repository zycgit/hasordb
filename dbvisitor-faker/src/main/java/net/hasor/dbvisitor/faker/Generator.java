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
package net.hasor.dbvisitor.faker;

import java.util.List;

/**
 * 数据生成
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
public interface Generator {

    List<BoundSql> buildInsert(FakerTable fakerTable, int batchSize);

    List<BoundSql> buildDelete(FakerTable fakerTable, int batchSize);

    List<BoundSql> buildUpdate(FakerTable fakerTable, int batchSize);
}