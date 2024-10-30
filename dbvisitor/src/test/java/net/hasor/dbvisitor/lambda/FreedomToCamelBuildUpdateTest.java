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
import net.hasor.dbvisitor.dynamic.MacroRegistry;
import net.hasor.dbvisitor.dynamic.RegistryManager;
import net.hasor.dbvisitor.dynamic.rule.RuleRegistry;
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
public class FreedomToCamelBuildUpdateTest {
    private LambdaTemplate newLambda() {
        MappingOptions opt = MappingOptions.buildNew().mapUnderscoreToCamelCase(true);
        MappingRegistry registry = new MappingRegistry(null, new TypeHandlerRegistry(), opt);
        RegistryManager manager = new RegistryManager(registry, new RuleRegistry(), new MacroRegistry());
        return new LambdaTemplate((DataSource) null, manager);
    }

    @Test
    public void updateBuilder_bad_1() {
        try {
            MapUpdateOperation lambdaUpdate = newLambda().freedomUpdate("user_info");
            assert lambdaUpdate.getBoundSql() == null;
            lambdaUpdate.doUpdate();
            assert false;
        } catch (Exception e) {
            assert e.getMessage().startsWith("there nothing to update.");
        }

        try {
            newLambda().freedomUpdate("user_info").updateToSample(null);
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("newValue is null.");
        }

        try {
            MapUpdateOperation lambdaUpdate = newLambda().freedomUpdate("user_info")//
                    .updateRow(new HashMap<>());
            lambdaUpdate.doUpdate();
            assert false;
        } catch (Exception e) {
            assert e.getMessage().startsWith("there nothing to update.");
        }
    }

    @Test
    public void updateBuilder_bad_2() {
        MapUpdateOperation lambdaUpdate = newLambda().freedomUpdate("user_info");
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
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("loginName", "acc");
        map.put("password", "pwd");
        map.put("abc", "pwd2");

        MapUpdateOperation lambdaUpdate = newLambda().freedomUpdate("user_info");
        lambdaUpdate.and(qb -> qb.eq("seq", 123)).updateToSample(map);

        BoundSql boundSql1 = lambdaUpdate.getBoundSql();
        assert !(boundSql1 instanceof BatchBoundSql);
        assert boundSql1.getSqlString().equals("UPDATE user_info SET login_name = ? , password = ? , abc = ? WHERE ( seq = ? )");
        assert boundSql1.getArgs()[0].equals("acc");
        assert boundSql1.getArgs()[1].equals("pwd");
        assert boundSql1.getArgs()[2].equals("pwd2");
        assert boundSql1.getArgs()[3].equals(123);
    }

    @Test
    public void updateBuilder_1_2() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("loginName", "acc");
        map.put("password", "pwd");
        map.put("abc", "pwd2");

        MapUpdateOperation lambdaUpdate = newLambda().freedomUpdate("user_info");
        lambdaUpdate.and(qb -> qb.eq("seq", 123)).updateToSample(map);

        BoundSql boundSql1 = lambdaUpdate.getBoundSql();
        assert !(boundSql1 instanceof BatchBoundSql);
        assert boundSql1.getSqlString().equals("UPDATE user_info SET login_name = ? , password = ? , abc = ? WHERE ( seq = ? )");
        assert boundSql1.getArgs()[0].equals("acc");
        assert boundSql1.getArgs()[1].equals("pwd");
        assert boundSql1.getArgs()[2].equals("pwd2");
        assert boundSql1.getArgs()[3].equals(123);
    }

    @Test
    public void updateBuilder_2_1() {
        Map<String, Object> map = new HashMap<>();
        map.put("loginName", "acc");
        map.put("password", "pwd");
        map.put("abc", "pwd");

        MapUpdateOperation lambdaUpdate = newLambda().freedomUpdate("user_info");
        lambdaUpdate.eq("loginName", "admin").and().eq("password", "pass").updateRow(map);

        BoundSql boundSql1 = lambdaUpdate.getBoundSql();
        assert boundSql1.getSqlString().equals("UPDATE user_info SET password = ? , abc = ? , login_name = ? WHERE login_name = ? AND password = ?");
    }

    @Test
    public void updateBuilder_by_sample_1() {
        LambdaTemplate lambdaTemplate = newLambda();

        Map<String, Object> whereValue = new LinkedHashMap<>();
        whereValue.put("id", 1);
        whereValue.put("userName", "mali1");
        whereValue.put("name", "123");
        whereValue.put("abc", "abc");

        Map<String, Object> setValue = new LinkedHashMap<>();
        setValue.put("userName", "mali2");
        setValue.put("name", "321");
        setValue.put("abc", "abc");
        setValue.put("createTime", new Date());

        // delete from user where id = 1 and name = 'mail';
        BoundSql boundSql1 = lambdaTemplate.freedomUpdate("user_info").eqBySampleMap(whereValue).updateToSampleMap(setValue).getBoundSql();
        assert boundSql1.getSqlString().equals("UPDATE user_info SET user_name = ? , name = ? , abc = ? , create_time = ? WHERE ( id = ? AND user_name = ? AND name = ? AND abc = ? )");
        assert ((SqlArg) boundSql1.getArgs()[0]).getValue().equals("mali2");
        assert ((SqlArg) boundSql1.getArgs()[1]).getValue().equals("321");
        assert ((SqlArg) boundSql1.getArgs()[2]).getValue().equals("abc");
        assert ((SqlArg) boundSql1.getArgs()[3]).getValue() != null;
        assert ((SqlArg) boundSql1.getArgs()[4]).getValue().equals(1);
        assert ((SqlArg) boundSql1.getArgs()[5]).getValue().equals("mali1");
        assert ((SqlArg) boundSql1.getArgs()[6]).getValue().equals("123");
        assert ((SqlArg) boundSql1.getArgs()[7]).getValue().equals("abc");
    }
}
