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
package net.hasor.dbvisitor.lambda;
import net.hasor.dbvisitor.dialect.BatchBoundSql;
import net.hasor.dbvisitor.dialect.BoundSql;
import net.hasor.dbvisitor.dialect.provider.MySqlDialect;
import net.hasor.dbvisitor.dynamic.MacroRegistry;
import net.hasor.dbvisitor.dynamic.RegistryManager;
import net.hasor.dbvisitor.dynamic.rule.RuleRegistry;
import net.hasor.dbvisitor.lambda.dto.AnnoUserInfoDTO;
import net.hasor.dbvisitor.mapping.MappingOptions;
import net.hasor.dbvisitor.mapping.MappingRegistry;
import net.hasor.dbvisitor.types.SqlArg;
import net.hasor.dbvisitor.types.TypeHandlerRegistry;
import org.junit.Test;

import javax.sql.DataSource;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2021-3-22
 */
public class BuildEntUpdateTest {
    private LambdaTemplate newLambda() {
        MappingRegistry registry = new MappingRegistry(null, new TypeHandlerRegistry(), MappingOptions.buildNew().defaultDialect(new MySqlDialect()));
        RegistryManager manager = new RegistryManager(registry, new RuleRegistry(), new MacroRegistry());
        return new LambdaTemplate((DataSource) null, manager);
    }

    @Test
    public void updateBuilder_bad_1() {
        try {
            EntityUpdateOperation<AnnoUserInfoDTO> lambdaUpdate = newLambda().updateBySpace(AnnoUserInfoDTO.class);
            assert lambdaUpdate.getBoundSql() == null;
            lambdaUpdate.doUpdate();
            assert false;
        } catch (Exception e) {
            assert e.getMessage().startsWith("there nothing to update.");
        }

        try {
            new LambdaTemplate().updateBySpace(AnnoUserInfoDTO.class).updateToSample(null);
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("newValue is null.");
        }

        try {
            EntityUpdateOperation<AnnoUserInfoDTO> lambdaUpdate = new LambdaTemplate()//
                    .updateBySpace(AnnoUserInfoDTO.class)//
                    .updateRow(new AnnoUserInfoDTO());
            lambdaUpdate.doUpdate();
            assert false;
        } catch (Exception e) {
            assert e.getMessage().startsWith("The dangerous UPDATE operation, You must call `allowEmptyWhere()` to enable UPDATE ALL.");
        }
    }

    @Test
    public void updateBuilder_bad_1_2map() {
        try {
            MapUpdateOperation lambdaUpdate = newLambda().updateBySpace(AnnoUserInfoDTO.class).asMap();
            assert lambdaUpdate.getBoundSql() == null;
            lambdaUpdate.doUpdate();
            assert false;
        } catch (Exception e) {
            assert e.getMessage().startsWith("there nothing to update.");
        }

        try {
            new LambdaTemplate().updateBySpace(AnnoUserInfoDTO.class).updateToSample(null);
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("newValue is null.");
        }

        try {
            MapUpdateOperation lambdaUpdate = new LambdaTemplate().updateBySpace(AnnoUserInfoDTO.class).asMap()//
                    .updateRow(new HashMap<>());
            lambdaUpdate.doUpdate();
            assert false;
        } catch (Exception e) {
            assert e.getMessage().startsWith("The dangerous UPDATE operation,");
        }
    }

    @Test
    public void updateBuilder_bad_2() {
        EntityUpdateOperation<AnnoUserInfoDTO> lambdaUpdate = newLambda().updateBySpace(AnnoUserInfoDTO.class);
        lambdaUpdate.allowEmptyWhere();

        try {
            lambdaUpdate.getBoundSql();
            assert false;
        } catch (Exception e) {
            assert e.getMessage().startsWith("there nothing to update.");
        }
    }

    @Test
    public void updateBuilder_bad_2_2map() {
        MapUpdateOperation lambdaUpdate = newLambda().updateBySpace(AnnoUserInfoDTO.class).asMap();
        lambdaUpdate.allowEmptyWhere();

        try {
            lambdaUpdate.getBoundSql();
            assert false;
        } catch (Exception e) {
            assert e.getMessage().startsWith("there nothing to update.");
        }
    }

    @Test
    public void updateBuilder_1_1() {
        AnnoUserInfoDTO data = new AnnoUserInfoDTO();
        data.setLoginName("acc");
        data.setPassword("pwd");
        EntityUpdateOperation<AnnoUserInfoDTO> lambdaUpdate = new LambdaTemplate().updateBySpace(AnnoUserInfoDTO.class);
        lambdaUpdate.and(qb -> qb.eq(AnnoUserInfoDTO::getSeq, 123)).updateToSample(data);

        BoundSql boundSql1 = lambdaUpdate.getBoundSql();
        assert !(boundSql1 instanceof BatchBoundSql);
        assert boundSql1.getSqlString().equals("UPDATE user_info SET login_name = ? , login_password = ? WHERE ( seq = ? )");
        assert boundSql1.getArgs()[0].equals("acc");
        assert boundSql1.getArgs()[1].equals("pwd");
        assert boundSql1.getArgs()[2].equals(123);
    }

    @Test
    public void updateBuilder_1_1_2map() {
        Map<String, Object> map = new HashMap<>();
        map.put("loginName", "acc");
        map.put("password", "pwd");
        map.put("abc", "pwd");

        MapUpdateOperation lambdaUpdate = new LambdaTemplate().updateBySpace(AnnoUserInfoDTO.class).asMap();
        lambdaUpdate.and(qb -> qb.eq("seq", 123)).updateToSample(map);

        BoundSql boundSql1 = lambdaUpdate.getBoundSql();
        assert !(boundSql1 instanceof BatchBoundSql);
        assert boundSql1.getSqlString().equals("UPDATE user_info SET login_name = ? , login_password = ? WHERE ( seq = ? )");
        assert boundSql1.getArgs()[0].equals("acc");
        assert boundSql1.getArgs()[1].equals("pwd");
        assert boundSql1.getArgs()[2].equals(123);
    }

    @Test
    public void updateBuilder_1_2() {
        AnnoUserInfoDTO data = new AnnoUserInfoDTO();
        data.setLoginName("acc");
        data.setPassword("pwd");
        EntityUpdateOperation<AnnoUserInfoDTO> lambdaUpdate = new LambdaTemplate().updateBySpace(AnnoUserInfoDTO.class);
        lambdaUpdate.and(qb -> qb.eq(AnnoUserInfoDTO::getSeq, 123)).updateToSample(data);

        BoundSql boundSql1 = lambdaUpdate.getBoundSql();
        assert !(boundSql1 instanceof BatchBoundSql);
        assert boundSql1.getSqlString().equals("UPDATE user_info SET login_name = ? , login_password = ? WHERE ( seq = ? )");
        assert boundSql1.getArgs()[0].equals("acc");
        assert boundSql1.getArgs()[1].equals("pwd");
        assert boundSql1.getArgs()[2].equals(123);
    }

    @Test
    public void updateBuilder_1_2_2map() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("loginName", "acc");
        map.put("password", "pwd");
        map.put("abc", "pwd");

        MapUpdateOperation lambdaUpdate = new LambdaTemplate().updateBySpace(AnnoUserInfoDTO.class).asMap();
        lambdaUpdate.and(qb -> qb.eq("seq", 123)).updateToSample(map);

        BoundSql boundSql1 = lambdaUpdate.getBoundSql();
        assert !(boundSql1 instanceof BatchBoundSql);
        assert boundSql1.getSqlString().equals("UPDATE user_info SET login_name = ? , login_password = ? WHERE ( seq = ? )");
        assert boundSql1.getArgs()[0].equals("acc");
        assert boundSql1.getArgs()[1].equals("pwd");
        assert boundSql1.getArgs()[2].equals(123);
    }

    @Test
    public void updateBuilder_2_1() {
        AnnoUserInfoDTO data = new AnnoUserInfoDTO();
        data.setLoginName("acc");
        data.setPassword("pwd");

        EntityUpdateOperation<AnnoUserInfoDTO> lambdaUpdate = new LambdaTemplate().updateBySpace(AnnoUserInfoDTO.class);
        lambdaUpdate.eq(AnnoUserInfoDTO::getLoginName, "admin").and().eq(AnnoUserInfoDTO::getPassword, "pass").updateRow(data);

        BoundSql boundSql1 = lambdaUpdate.getBoundSql();
        assert boundSql1.getSqlString().equals("UPDATE user_info SET user_name = ? , login_name = ? , login_password = ? , email = ? , seq = ? , register_time = ? WHERE login_name = ? AND login_password = ?");
    }

    @Test
    public void updateBuilder_2_1_map() {
        Map<String, Object> map = new HashMap<>();
        map.put("loginName", "acc");
        map.put("password", "pwd");
        map.put("abc", "pwd");

        MapUpdateOperation lambdaUpdate = new LambdaTemplate().updateBySpace(AnnoUserInfoDTO.class).asMap();
        lambdaUpdate.eq("loginName", "admin").and().eq("password", "pass").updateRow(map);

        BoundSql boundSql1 = lambdaUpdate.getBoundSql();
        assert boundSql1.getSqlString().equals("UPDATE user_info SET user_name = ? , login_name = ? , login_password = ? , email = ? , seq = ? , register_time = ? WHERE login_name = ? AND login_password = ?");
    }

    @Test
    public void updateBuilder_by_sample_1() {
        LambdaTemplate lambdaTemplate = new LambdaTemplate();

        Map<String, Object> whereValue = new HashMap<>();
        whereValue.put("id", 1);
        whereValue.put("user_name", "mali1");
        whereValue.put("name", "123");
        whereValue.put("abc", "abc");

        Map<String, Object> setValue = new HashMap<>();
        setValue.put("user_name", "mali2");
        setValue.put("name", "321");
        setValue.put("abc", "abc");
        setValue.put("create_time", new Date());

        // delete from user where id = 1 and name = 'mail';
        BoundSql boundSql1 = lambdaTemplate.updateBySpace(AnnoUserInfoDTO.class)//
                .eqBySampleMap(whereValue)//
                .updateToSampleMap(setValue)//
                .getBoundSql();
        assert boundSql1.getSqlString().equals("UPDATE user_info SET user_name = ? WHERE ( user_name = ? )");
        assert ((SqlArg) boundSql1.getArgs()[0]).getValue().equals("321");
        assert ((SqlArg) boundSql1.getArgs()[1]).getValue().equals("123");
    }

    @Test
    public void updateBuilder_by_sample_1_2map() {
        LambdaTemplate lambdaTemplate = new LambdaTemplate();

        Map<String, Object> whereValue = new HashMap<>();
        whereValue.put("id", 1);
        whereValue.put("user_name", "mali1");
        whereValue.put("name", "123");
        whereValue.put("abc", "abc");

        Map<String, Object> setValue = new HashMap<>();
        setValue.put("user_name", "mali2");
        setValue.put("name", "321");
        setValue.put("abc", "abc");
        setValue.put("create_time", new Date());

        // delete from user where id = 1 and name = 'mail';
        BoundSql boundSql1 = lambdaTemplate.updateBySpace(AnnoUserInfoDTO.class).asMap().eqBySampleMap(whereValue).updateToSampleMap(setValue).getBoundSql();
        assert boundSql1.getSqlString().equals("UPDATE user_info SET user_name = ? WHERE ( user_name = ? )");
        assert ((SqlArg) boundSql1.getArgs()[0]).getValue().equals("321");
        assert ((SqlArg) boundSql1.getArgs()[1]).getValue().equals("123");
    }
}
