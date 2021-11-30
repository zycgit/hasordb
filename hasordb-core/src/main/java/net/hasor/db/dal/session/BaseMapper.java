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
package net.hasor.db.dal.session;
import net.hasor.db.lambda.LambdaOperations.LambdaDelete;
import net.hasor.db.lambda.LambdaOperations.LambdaInsert;
import net.hasor.db.lambda.LambdaOperations.LambdaQuery;
import net.hasor.db.lambda.LambdaOperations.LambdaUpdate;
import net.hasor.db.lambda.core.LambdaTemplate;
import net.hasor.db.page.Page;
import net.hasor.db.page.PageResult;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

/**
 * Mapper 继承该接口后，无需编写 mapper.xml 文件，即可获得 CRUD 功能
 * @version : 2021-10-31
 * @author 赵永春 (zyc@hasor.net)
 */
public interface BaseMapper<T> extends Mapper {

    public Class<T> entityType();

    public LambdaTemplate template();

    public DalSession getSession();

    /** return LambdaInsert for insert */
    public default LambdaInsert<T> insert() {
        return template().lambdaInsert(entityType());
    }

    /** return LambdaDelete for delete */
    public default LambdaDelete<T> delete() {
        return template().lambdaDelete(entityType());
    }

    /** return LambdaUpdate for update */
    public default LambdaUpdate<T> update() {
        return template().lambdaUpdate(entityType());
    }

    /** return LambdaQuery for query */
    public default LambdaQuery<T> query() {
        return template().lambdaQuery(entityType());
    }

    /** 执行 Mapper 配置文件中的 SQL */
    public default int executeStatement(String stId, Object parameter) throws SQLException {
        return this.getSession().executeStatement(stId, parameter);
    }

    /** 执行 Mapper 配置文件中的 SQL */
    public default <E> List<E> queryStatement(String stId, Object parameter) throws SQLException {
        return this.queryStatement(stId, parameter, null);
    }

    /** 执行 Mapper 配置文件中的 SQL */
    public default <E> List<E> queryStatement(String stId, Object parameter, Page page) throws SQLException {
        return this.getSession().queryStatement(stId, parameter, page);
    }

    /**
     * 插入一条记录
     * @param entity 实体对象
     */
    public default int save(T entity) throws SQLException {
        return insert().applyEntity(entity).executeSumResult();
    }

    /**
     * 插入一组记录
     * @param entity 实体对象列表
     */
    public default int save(List<T> entity) throws SQLException {
        return insert().applyEntity(entity).executeSumResult();
    }

    /**
     * 保存或修改
     * @param entity 实体对象
     */
    public int saveOrUpdate(T entity) throws SQLException;

    /**
     * 删除
     * @param entity 实体对象
     */
    public int delete(T entity) throws SQLException;

    /**
     * 根据 ID 删除
     * @param id 主键ID
     */
    public int deleteById(Serializable id) throws SQLException;

    /**
     * 根据 ID 删除
     * @param idList 主键ID
     */
    public int deleteByIds(List<? extends Serializable> idList) throws SQLException;

    /**
     * 根据 ID 查询
     * @param id 主键ID
     */
    public T getById(Serializable id) throws SQLException;

    /**
     * 查询（根据ID 批量查询）
     * @param idList 主键ID列表
     */
    public List<T> getByIds(List<? extends Serializable> idList) throws SQLException;

    /**
     * 根据 entity 条件，作为样本 null 将不会被列入条件。
     * 如果想匹配 null 值需要使用 queryByCondition 方法
     * @param entity 实体对象
     * @return T
     */
    public List<T> listBySample(T entity) throws SQLException;

    /**
     * 根据 entity 条件，作为样本 null 将不会被列入条件。
     * 如果想匹配 null 值需要使用 queryByCondition 方法
     * @param entity 实体对象
     * @return T
     */
    public int countBySample(T entity) throws SQLException;

    /**
     * 相当于 select count(1) form xxxx
     * @return int
     */
    public int countAll() throws SQLException;

    /** 分页查询 */
    public PageResult<T> pageBySample(Object sample, Page page) throws SQLException;

    /** 初始化分页对象 */
    public default Page initPageBySample(Object sample, int pageSize) throws SQLException {
        return this.initPageBySample(sample, pageSize, 0);
    }

    /** 初始化分页对象 */
    public Page initPageBySample(Object sample, int pageSize, int pageNumberOffset) throws SQLException;
}
