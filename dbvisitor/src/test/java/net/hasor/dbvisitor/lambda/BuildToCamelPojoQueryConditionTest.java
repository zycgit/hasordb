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
import net.hasor.dbvisitor.dialect.BoundSql;
import net.hasor.dbvisitor.dynamic.MacroRegistry;
import net.hasor.dbvisitor.dynamic.RegistryManager;
import net.hasor.dbvisitor.dynamic.rule.RuleRegistry;
import net.hasor.dbvisitor.lambda.dto.UserInfo;
import net.hasor.dbvisitor.mapping.MappingOptions;
import net.hasor.dbvisitor.mapping.MappingRegistry;
import net.hasor.dbvisitor.types.TypeHandlerRegistry;
import org.junit.Test;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

/***
 * @version : 2021-3-22
 * @author 赵永春 (zyc@hasor.net)
 */
public class BuildToCamelPojoQueryConditionTest {

    private LambdaTemplate newLambda() {
        MappingOptions opt = MappingOptions.buildNew().mapUnderscoreToCamelCase(true);
        MappingRegistry registry = new MappingRegistry(null, new TypeHandlerRegistry(), opt);
        RegistryManager manager = new RegistryManager(registry, new RuleRegistry(), new MacroRegistry());

        return new LambdaTemplate((DataSource) null, manager);
    }

    @Test
    public void queryBuild_0() {
        BoundSql boundSql1 = newLambda().queryBySpace(UserInfo.class).getBoundSql();
        assert boundSql1.getSqlString().equals("SELECT * FROM user_info");
    }

    @Test
    public void queryBuild_0_2map() {
        BoundSql boundSql1 = newLambda().queryBySpace(UserInfo.class).asMap().getBoundSql();
        assert boundSql1.getSqlString().equals("SELECT * FROM user_info");
    }

    @Test
    public void queryBuild_1() {
        BoundSql boundSql1 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getLoginName, "abc").getBoundSql();
        assert boundSql1.getSqlString().equals("SELECT * FROM user_info WHERE login_name = ?");
        assert boundSql1.getArgs()[0].equals("abc");
    }

    @Test
    public void queryBuild_1_2map() {
        BoundSql boundSql1 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("loginName", "abc").getBoundSql();
        assert boundSql1.getSqlString().equals("SELECT * FROM user_info WHERE login_name = ?");
        assert boundSql1.getArgs()[0].equals("abc");
    }

    @Test
    public void queryBuild_and_1() {
        BoundSql boundSql1 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).eq(UserInfo::getLoginName, "abc").getBoundSql();
        assert boundSql1.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name = ?");
        assert boundSql1.getArgs()[0].equals(1);
        assert boundSql1.getArgs()[1].equals("abc");

        BoundSql boundSql2 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, "a").eq(UserInfo::getLoginName, "b").getBoundSql();
        assert boundSql2.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name = ?");
        assert boundSql2.getArgs()[0].equals("a");
        assert boundSql2.getArgs()[1].equals("b");
    }

    @Test
    public void queryBuild_and_1_2map() {
        BoundSql boundSql1 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).eq("loginName", "abc").getBoundSql();
        assert boundSql1.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name = ?");
        assert boundSql1.getArgs()[0].equals(1);
        assert boundSql1.getArgs()[1].equals("abc");

        BoundSql boundSql2 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", "a").eq("loginName", "b").getBoundSql();
        assert boundSql2.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name = ?");
        assert boundSql2.getArgs()[0].equals("a");
        assert boundSql2.getArgs()[1].equals("b");
    }

    @Test
    public void queryBuild_or_1() {
        BoundSql boundSql1 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).or().eq(UserInfo::getLoginName, "abc").getBoundSql();
        assert boundSql1.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? OR login_name = ?");
        assert boundSql1.getArgs()[0].equals(1);
        assert boundSql1.getArgs()[1].equals("abc");
    }

    @Test
    public void queryBuild_or_1_2map() {
        BoundSql boundSql1 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).or().eq("loginName", "abc").getBoundSql();
        assert boundSql1.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? OR login_name = ?");
        assert boundSql1.getArgs()[0].equals(1);
        assert boundSql1.getArgs()[1].equals("abc");
    }

    @Test
    public void queryBuild_ne_1() {
        BoundSql boundSql1 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).ne(UserInfo::getLoginName, "abc").getBoundSql();
        assert boundSql1.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name <> ?");
        assert boundSql1.getArgs()[0].equals(1);
        assert boundSql1.getArgs()[1].equals("abc");

        BoundSql boundSql2 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).or().ne(UserInfo::getLoginName, "abc").getBoundSql();
        assert boundSql2.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? OR login_name <> ?");
        assert boundSql2.getArgs()[0].equals(1);
        assert boundSql2.getArgs()[1].equals("abc");

        BoundSql boundSql3 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).and().ne(UserInfo::getLoginName, "abc").getBoundSql();
        assert boundSql3.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name <> ?");
        assert boundSql3.getArgs()[0].equals(1);
        assert boundSql3.getArgs()[1].equals("abc");
    }

    @Test
    public void queryBuild_ne_1_2map() {
        BoundSql boundSql1 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).ne("loginName", "abc").getBoundSql();
        assert boundSql1.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name <> ?");
        assert boundSql1.getArgs()[0].equals(1);
        assert boundSql1.getArgs()[1].equals("abc");

        BoundSql boundSql2 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).or().ne("loginName", "abc").getBoundSql();
        assert boundSql2.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? OR login_name <> ?");
        assert boundSql2.getArgs()[0].equals(1);
        assert boundSql2.getArgs()[1].equals("abc");

        BoundSql boundSql3 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).and().ne("loginName", "abc").getBoundSql();
        assert boundSql3.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name <> ?");
        assert boundSql3.getArgs()[0].equals(1);
        assert boundSql3.getArgs()[1].equals("abc");
    }

    @Test
    public void queryBuild_gt_1() {
        BoundSql boundSql1 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).gt(UserInfo::getLoginName, "abc").getBoundSql();
        assert boundSql1.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name > ?");
        assert boundSql1.getArgs()[0].equals(1);
        assert boundSql1.getArgs()[1].equals("abc");

        BoundSql boundSql2 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).or().gt(UserInfo::getLoginName, "abc").getBoundSql();
        assert boundSql2.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? OR login_name > ?");
        assert boundSql2.getArgs()[0].equals(1);
        assert boundSql2.getArgs()[1].equals("abc");

        BoundSql boundSql3 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).and().gt(UserInfo::getLoginName, "abc").getBoundSql();
        assert boundSql3.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name > ?");
        assert boundSql3.getArgs()[0].equals(1);
        assert boundSql3.getArgs()[1].equals("abc");
    }

    @Test
    public void queryBuild_gt_1_2map() {
        BoundSql boundSql1 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).gt("loginName", "abc").getBoundSql();
        assert boundSql1.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name > ?");
        assert boundSql1.getArgs()[0].equals(1);
        assert boundSql1.getArgs()[1].equals("abc");

        BoundSql boundSql2 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).or().gt("loginName", "abc").getBoundSql();
        assert boundSql2.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? OR login_name > ?");
        assert boundSql2.getArgs()[0].equals(1);
        assert boundSql2.getArgs()[1].equals("abc");

        BoundSql boundSql3 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).and().gt("loginName", "abc").getBoundSql();
        assert boundSql3.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name > ?");
        assert boundSql3.getArgs()[0].equals(1);
        assert boundSql3.getArgs()[1].equals("abc");
    }

    @Test
    public void queryBuild_ge_1() {
        BoundSql boundSql1 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).ge(UserInfo::getLoginName, "abc").getBoundSql();
        assert boundSql1.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name >= ?");
        assert boundSql1.getArgs()[0].equals(1);
        assert boundSql1.getArgs()[1].equals("abc");

        BoundSql boundSql2 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).or().ge(UserInfo::getLoginName, "abc").getBoundSql();
        assert boundSql2.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? OR login_name >= ?");
        assert boundSql2.getArgs()[0].equals(1);
        assert boundSql2.getArgs()[1].equals("abc");

        BoundSql boundSql3 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).and().ge(UserInfo::getLoginName, "abc").getBoundSql();
        assert boundSql3.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name >= ?");
        assert boundSql3.getArgs()[0].equals(1);
        assert boundSql3.getArgs()[1].equals("abc");
    }

    @Test
    public void queryBuild_ge_1_2map() {
        BoundSql boundSql1 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).ge("loginName", "abc").getBoundSql();
        assert boundSql1.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name >= ?");
        assert boundSql1.getArgs()[0].equals(1);
        assert boundSql1.getArgs()[1].equals("abc");

        BoundSql boundSql2 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).or().ge("loginName", "abc").getBoundSql();
        assert boundSql2.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? OR login_name >= ?");
        assert boundSql2.getArgs()[0].equals(1);
        assert boundSql2.getArgs()[1].equals("abc");

        BoundSql boundSql3 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).and().ge("loginName", "abc").getBoundSql();
        assert boundSql3.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name >= ?");
        assert boundSql3.getArgs()[0].equals(1);
        assert boundSql3.getArgs()[1].equals("abc");
    }

    @Test
    public void queryBuild_lt_1() {
        BoundSql boundSql1 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).lt(UserInfo::getLoginName, "abc").getBoundSql();
        assert boundSql1.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name < ?");
        assert boundSql1.getArgs()[0].equals(1);
        assert boundSql1.getArgs()[1].equals("abc");

        BoundSql boundSql2 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).or().lt(UserInfo::getLoginName, "abc").getBoundSql();
        assert boundSql2.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? OR login_name < ?");
        assert boundSql2.getArgs()[0].equals(1);
        assert boundSql2.getArgs()[1].equals("abc");

        BoundSql boundSql3 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).and().lt(UserInfo::getLoginName, "abc").getBoundSql();
        assert boundSql3.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name < ?");
        assert boundSql3.getArgs()[0].equals(1);
        assert boundSql3.getArgs()[1].equals("abc");
    }

    @Test
    public void queryBuild_lt_1_2map() {
        BoundSql boundSql1 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).lt("loginName", "abc").getBoundSql();
        assert boundSql1.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name < ?");
        assert boundSql1.getArgs()[0].equals(1);
        assert boundSql1.getArgs()[1].equals("abc");

        BoundSql boundSql2 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).or().lt("loginName", "abc").getBoundSql();
        assert boundSql2.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? OR login_name < ?");
        assert boundSql2.getArgs()[0].equals(1);
        assert boundSql2.getArgs()[1].equals("abc");

        BoundSql boundSql3 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).and().lt("loginName", "abc").getBoundSql();
        assert boundSql3.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name < ?");
        assert boundSql3.getArgs()[0].equals(1);
        assert boundSql3.getArgs()[1].equals("abc");
    }

    @Test
    public void queryBuild_le_1() {
        BoundSql boundSql1 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).le(UserInfo::getLoginName, "abc").getBoundSql();
        assert boundSql1.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name <= ?");
        assert boundSql1.getArgs()[0].equals(1);
        assert boundSql1.getArgs()[1].equals("abc");

        BoundSql boundSql2 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).or().le(UserInfo::getLoginName, "abc").getBoundSql();
        assert boundSql2.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? OR login_name <= ?");
        assert boundSql2.getArgs()[0].equals(1);
        assert boundSql2.getArgs()[1].equals("abc");

        BoundSql boundSql3 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).and().le(UserInfo::getLoginName, "abc").getBoundSql();
        assert boundSql3.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name <= ?");
        assert boundSql3.getArgs()[0].equals(1);
        assert boundSql3.getArgs()[1].equals("abc");
    }

    @Test
    public void queryBuild_le_1_2map() {
        BoundSql boundSql1 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).le("loginName", "abc").getBoundSql();
        assert boundSql1.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name <= ?");
        assert boundSql1.getArgs()[0].equals(1);
        assert boundSql1.getArgs()[1].equals("abc");

        BoundSql boundSql2 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).or().le("loginName", "abc").getBoundSql();
        assert boundSql2.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? OR login_name <= ?");
        assert boundSql2.getArgs()[0].equals(1);
        assert boundSql2.getArgs()[1].equals("abc");

        BoundSql boundSql3 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).and().le("loginName", "abc").getBoundSql();
        assert boundSql3.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name <= ?");
        assert boundSql3.getArgs()[0].equals(1);
        assert boundSql3.getArgs()[1].equals("abc");
    }

    @Test
    public void queryBuild_is_null_1() {
        BoundSql boundSql1 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).isNull(UserInfo::getLoginName).getBoundSql();
        assert boundSql1.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name IS NULL");
        assert boundSql1.getArgs()[0].equals(1);

        BoundSql boundSql2 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).or().isNull(UserInfo::getLoginName).getBoundSql();
        assert boundSql2.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? OR login_name IS NULL");
        assert boundSql2.getArgs()[0].equals(1);

        BoundSql boundSql3 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).or().isNull(UserInfo::getLoginName).getBoundSql();
        assert boundSql3.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? OR login_name IS NULL");
        assert boundSql3.getArgs()[0].equals(1);
    }

    @Test
    public void queryBuild_is_null_1_2map() {
        BoundSql boundSql1 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).isNull("loginName").getBoundSql();
        assert boundSql1.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name IS NULL");
        assert boundSql1.getArgs()[0].equals(1);

        BoundSql boundSql2 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).or().isNull("loginName").getBoundSql();
        assert boundSql2.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? OR login_name IS NULL");
        assert boundSql2.getArgs()[0].equals(1);

        BoundSql boundSql3 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).or().isNull("loginName").getBoundSql();
        assert boundSql3.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? OR login_name IS NULL");
        assert boundSql3.getArgs()[0].equals(1);
    }

    @Test
    public void queryBuild_is_not_null_1() {
        BoundSql boundSql1 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).isNotNull(UserInfo::getLoginName).getBoundSql();
        assert boundSql1.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name IS NOT NULL");
        assert boundSql1.getArgs()[0].equals(1);

        BoundSql boundSql2 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).or().isNotNull(UserInfo::getLoginName).getBoundSql();
        assert boundSql2.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? OR login_name IS NOT NULL");
        assert boundSql2.getArgs()[0].equals(1);

        BoundSql boundSql3 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).and().isNotNull(UserInfo::getLoginName).getBoundSql();
        assert boundSql3.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name IS NOT NULL");
        assert boundSql3.getArgs()[0].equals(1);
    }

    @Test
    public void queryBuild_is_not_null_1_2map() {
        BoundSql boundSql1 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).isNotNull("loginName").getBoundSql();
        assert boundSql1.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name IS NOT NULL");
        assert boundSql1.getArgs()[0].equals(1);

        BoundSql boundSql2 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).or().isNotNull("loginName").getBoundSql();
        assert boundSql2.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? OR login_name IS NOT NULL");
        assert boundSql2.getArgs()[0].equals(1);

        BoundSql boundSql3 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).and().isNotNull("loginName").getBoundSql();
        assert boundSql3.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name IS NOT NULL");
        assert boundSql3.getArgs()[0].equals(1);
    }

    @Test
    public void queryBuild_in_1() {
        List<String> inData = Arrays.asList("a", "b", "c");
        BoundSql boundSql1 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).in(UserInfo::getLoginName, inData).getBoundSql();
        assert boundSql1.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name IN ( ? , ? , ? )");
        assert boundSql1.getArgs()[0].equals(1);
        assert boundSql1.getArgs()[1].equals("a");
        assert boundSql1.getArgs()[2].equals("b");
        assert boundSql1.getArgs()[3].equals("c");

        BoundSql boundSql2 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).or().in(UserInfo::getLoginName, inData).getBoundSql();
        assert boundSql2.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? OR login_name IN ( ? , ? , ? )");
        assert boundSql2.getArgs()[0].equals(1);
        assert boundSql2.getArgs()[1].equals("a");
        assert boundSql2.getArgs()[2].equals("b");
        assert boundSql2.getArgs()[3].equals("c");

        BoundSql boundSql3 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).and().in(UserInfo::getLoginName, inData).getBoundSql();
        assert boundSql3.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name IN ( ? , ? , ? )");
        assert boundSql3.getArgs()[0].equals(1);
        assert boundSql3.getArgs()[1].equals("a");
        assert boundSql3.getArgs()[2].equals("b");
        assert boundSql3.getArgs()[3].equals("c");
    }

    @Test
    public void queryBuild_in_1_2map() {
        List<String> inData = Arrays.asList("a", "b", "c");
        BoundSql boundSql1 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).in("loginName", inData).getBoundSql();
        assert boundSql1.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name IN ( ? , ? , ? )");
        assert boundSql1.getArgs()[0].equals(1);
        assert boundSql1.getArgs()[1].equals("a");
        assert boundSql1.getArgs()[2].equals("b");
        assert boundSql1.getArgs()[3].equals("c");

        BoundSql boundSql2 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).or().in("loginName", inData).getBoundSql();
        assert boundSql2.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? OR login_name IN ( ? , ? , ? )");
        assert boundSql2.getArgs()[0].equals(1);
        assert boundSql2.getArgs()[1].equals("a");
        assert boundSql2.getArgs()[2].equals("b");
        assert boundSql2.getArgs()[3].equals("c");

        BoundSql boundSql3 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).and().in("loginName", inData).getBoundSql();
        assert boundSql3.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name IN ( ? , ? , ? )");
        assert boundSql3.getArgs()[0].equals(1);
        assert boundSql3.getArgs()[1].equals("a");
        assert boundSql3.getArgs()[2].equals("b");
        assert boundSql3.getArgs()[3].equals("c");
    }

    @Test
    public void queryBuild_not_in_1() {
        List<String> notInData = Arrays.asList("a", "b", "c");
        BoundSql boundSql1 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).notIn(UserInfo::getLoginName, notInData).getBoundSql();
        assert boundSql1.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name NOT IN ( ? , ? , ? )");
        assert boundSql1.getArgs()[0].equals(1);
        assert boundSql1.getArgs()[1].equals("a");
        assert boundSql1.getArgs()[2].equals("b");
        assert boundSql1.getArgs()[3].equals("c");

        BoundSql boundSql2 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).or().notIn(UserInfo::getLoginName, notInData).getBoundSql();
        assert boundSql2.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? OR login_name NOT IN ( ? , ? , ? )");
        assert boundSql2.getArgs()[0].equals(1);
        assert boundSql2.getArgs()[1].equals("a");
        assert boundSql2.getArgs()[2].equals("b");
        assert boundSql2.getArgs()[3].equals("c");

        BoundSql boundSql3 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).and().notIn(UserInfo::getLoginName, notInData).getBoundSql();
        assert boundSql3.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name NOT IN ( ? , ? , ? )");
        assert boundSql3.getArgs()[0].equals(1);
        assert boundSql3.getArgs()[1].equals("a");
        assert boundSql3.getArgs()[2].equals("b");
        assert boundSql3.getArgs()[3].equals("c");
    }

    @Test
    public void queryBuild_not_in_1_2map() {
        List<String> notInData = Arrays.asList("a", "b", "c");
        BoundSql boundSql1 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).notIn("loginName", notInData).getBoundSql();
        assert boundSql1.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name NOT IN ( ? , ? , ? )");
        assert boundSql1.getArgs()[0].equals(1);
        assert boundSql1.getArgs()[1].equals("a");
        assert boundSql1.getArgs()[2].equals("b");
        assert boundSql1.getArgs()[3].equals("c");

        BoundSql boundSql2 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).or().notIn("loginName", notInData).getBoundSql();
        assert boundSql2.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? OR login_name NOT IN ( ? , ? , ? )");
        assert boundSql2.getArgs()[0].equals(1);
        assert boundSql2.getArgs()[1].equals("a");
        assert boundSql2.getArgs()[2].equals("b");
        assert boundSql2.getArgs()[3].equals("c");

        BoundSql boundSql3 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).and().notIn("loginName", notInData).getBoundSql();
        assert boundSql3.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name NOT IN ( ? , ? , ? )");
        assert boundSql3.getArgs()[0].equals(1);
        assert boundSql3.getArgs()[1].equals("a");
        assert boundSql3.getArgs()[2].equals("b");
        assert boundSql3.getArgs()[3].equals("c");
    }

    @Test
    public void queryBuild_between_1() {
        BoundSql boundSql1 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).between(UserInfo::getLoginName, 2, 3).getBoundSql();
        assert boundSql1.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name BETWEEN ? AND ?");
        assert boundSql1.getArgs()[0].equals(1);
        assert boundSql1.getArgs()[1].equals(2);
        assert boundSql1.getArgs()[2].equals(3);

        BoundSql boundSql2 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).or().between(UserInfo::getLoginName, 2, 3).getBoundSql();
        assert boundSql2.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? OR login_name BETWEEN ? AND ?");
        assert boundSql2.getArgs()[0].equals(1);
        assert boundSql2.getArgs()[1].equals(2);
        assert boundSql2.getArgs()[2].equals(3);

        BoundSql boundSql3 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).and().between(UserInfo::getLoginName, 2, 3).getBoundSql();
        assert boundSql3.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name BETWEEN ? AND ?");
        assert boundSql3.getArgs()[0].equals(1);
        assert boundSql3.getArgs()[1].equals(2);
        assert boundSql3.getArgs()[2].equals(3);
    }

    @Test
    public void queryBuild_between_1_2map() {
        BoundSql boundSql1 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).between("loginName", 2, 3).getBoundSql();
        assert boundSql1.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name BETWEEN ? AND ?");
        assert boundSql1.getArgs()[0].equals(1);
        assert boundSql1.getArgs()[1].equals(2);
        assert boundSql1.getArgs()[2].equals(3);

        BoundSql boundSql2 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).or().between("loginName", 2, 3).getBoundSql();
        assert boundSql2.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? OR login_name BETWEEN ? AND ?");
        assert boundSql2.getArgs()[0].equals(1);
        assert boundSql2.getArgs()[1].equals(2);
        assert boundSql2.getArgs()[2].equals(3);

        BoundSql boundSql3 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).and().between("loginName", 2, 3).getBoundSql();
        assert boundSql3.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name BETWEEN ? AND ?");
        assert boundSql3.getArgs()[0].equals(1);
        assert boundSql3.getArgs()[1].equals(2);
        assert boundSql3.getArgs()[2].equals(3);
    }

    @Test
    public void queryBuild_not_between_1() {
        BoundSql boundSql1 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).notBetween(UserInfo::getLoginName, 2, 3).getBoundSql();
        assert boundSql1.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name NOT BETWEEN ? AND ?");
        assert boundSql1.getArgs()[0].equals(1);
        assert boundSql1.getArgs()[1].equals(2);
        assert boundSql1.getArgs()[2].equals(3);

        BoundSql boundSql2 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).or().notBetween(UserInfo::getLoginName, 2, 3).getBoundSql();
        assert boundSql2.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? OR login_name NOT BETWEEN ? AND ?");
        assert boundSql2.getArgs()[0].equals(1);
        assert boundSql2.getArgs()[1].equals(2);
        assert boundSql2.getArgs()[2].equals(3);

        BoundSql boundSql3 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).and().notBetween(UserInfo::getLoginName, 2, 3).getBoundSql();
        assert boundSql3.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name NOT BETWEEN ? AND ?");
        assert boundSql3.getArgs()[0].equals(1);
        assert boundSql3.getArgs()[1].equals(2);
        assert boundSql3.getArgs()[2].equals(3);
    }

    @Test
    public void queryBuild_not_between_1_2map() {
        BoundSql boundSql1 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).notBetween("loginName", 2, 3).getBoundSql();
        assert boundSql1.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name NOT BETWEEN ? AND ?");
        assert boundSql1.getArgs()[0].equals(1);
        assert boundSql1.getArgs()[1].equals(2);
        assert boundSql1.getArgs()[2].equals(3);

        BoundSql boundSql2 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).or().notBetween("loginName", 2, 3).getBoundSql();
        assert boundSql2.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? OR login_name NOT BETWEEN ? AND ?");
        assert boundSql2.getArgs()[0].equals(1);
        assert boundSql2.getArgs()[1].equals(2);
        assert boundSql2.getArgs()[2].equals(3);

        BoundSql boundSql3 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).and().notBetween("loginName", 2, 3).getBoundSql();
        assert boundSql3.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name NOT BETWEEN ? AND ?");
        assert boundSql3.getArgs()[0].equals(1);
        assert boundSql3.getArgs()[1].equals(2);
        assert boundSql3.getArgs()[2].equals(3);
    }

    @Test
    public void queryBuild_like_1() {
        BoundSql boundSql1 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).like(UserInfo::getLoginName, "abc").getBoundSql();
        assert boundSql1.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name LIKE CONCAT('%', ? ,'%')");
        assert boundSql1.getArgs()[0].equals(1);
        assert boundSql1.getArgs()[1].equals("abc");

        BoundSql boundSql2 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).or().like(UserInfo::getLoginName, "abc").getBoundSql();
        assert boundSql2.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? OR login_name LIKE CONCAT('%', ? ,'%')");
        assert boundSql2.getArgs()[0].equals(1);
        assert boundSql2.getArgs()[1].equals("abc");

        BoundSql boundSql3 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).and().like(UserInfo::getLoginName, "abc").getBoundSql();
        assert boundSql3.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name LIKE CONCAT('%', ? ,'%')");
        assert boundSql3.getArgs()[0].equals(1);
        assert boundSql3.getArgs()[1].equals("abc");
    }

    @Test
    public void queryBuild_like_1_2map() {
        BoundSql boundSql1 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).like("loginName", "abc").getBoundSql();
        assert boundSql1.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name LIKE CONCAT('%', ? ,'%')");
        assert boundSql1.getArgs()[0].equals(1);
        assert boundSql1.getArgs()[1].equals("abc");

        BoundSql boundSql2 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).or().like("loginName", "abc").getBoundSql();
        assert boundSql2.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? OR login_name LIKE CONCAT('%', ? ,'%')");
        assert boundSql2.getArgs()[0].equals(1);
        assert boundSql2.getArgs()[1].equals("abc");

        BoundSql boundSql3 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).and().like("loginName", "abc").getBoundSql();
        assert boundSql3.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name LIKE CONCAT('%', ? ,'%')");
        assert boundSql3.getArgs()[0].equals(1);
        assert boundSql3.getArgs()[1].equals("abc");
    }

    @Test
    public void queryBuild_not_like_1() {
        BoundSql boundSql1 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).notLike(UserInfo::getLoginName, "abc").getBoundSql();
        assert boundSql1.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name NOT LIKE CONCAT('%', ? ,'%')");
        assert boundSql1.getArgs()[0].equals(1);
        assert boundSql1.getArgs()[1].equals("abc");

        BoundSql boundSql2 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).or().notLike(UserInfo::getLoginName, "abc").getBoundSql();
        assert boundSql2.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? OR login_name NOT LIKE CONCAT('%', ? ,'%')");
        assert boundSql2.getArgs()[0].equals(1);
        assert boundSql2.getArgs()[1].equals("abc");

        BoundSql boundSql3 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).and().notLike(UserInfo::getLoginName, "abc").getBoundSql();
        assert boundSql3.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name NOT LIKE CONCAT('%', ? ,'%')");
        assert boundSql3.getArgs()[0].equals(1);
        assert boundSql3.getArgs()[1].equals("abc");
    }

    @Test
    public void queryBuild_not_like_1_2map() {
        BoundSql boundSql1 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).notLike("loginName", "abc").getBoundSql();
        assert boundSql1.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name NOT LIKE CONCAT('%', ? ,'%')");
        assert boundSql1.getArgs()[0].equals(1);
        assert boundSql1.getArgs()[1].equals("abc");

        BoundSql boundSql2 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).or().notLike("loginName", "abc").getBoundSql();
        assert boundSql2.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? OR login_name NOT LIKE CONCAT('%', ? ,'%')");
        assert boundSql2.getArgs()[0].equals(1);
        assert boundSql2.getArgs()[1].equals("abc");

        BoundSql boundSql3 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).and().notLike("loginName", "abc").getBoundSql();
        assert boundSql3.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name NOT LIKE CONCAT('%', ? ,'%')");
        assert boundSql3.getArgs()[0].equals(1);
        assert boundSql3.getArgs()[1].equals("abc");
    }

    @Test
    public void queryBuild_like_right_1() {
        BoundSql boundSql1 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).likeRight(UserInfo::getLoginName, "abc").getBoundSql();
        assert boundSql1.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name LIKE CONCAT( ? ,'%')");
        assert boundSql1.getArgs()[0].equals(1);
        assert boundSql1.getArgs()[1].equals("abc");

        BoundSql boundSql2 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).or().likeRight(UserInfo::getLoginName, "abc").getBoundSql();
        assert boundSql2.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? OR login_name LIKE CONCAT( ? ,'%')");
        assert boundSql2.getArgs()[0].equals(1);
        assert boundSql2.getArgs()[1].equals("abc");

        BoundSql boundSql3 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).and().likeRight(UserInfo::getLoginName, "abc").getBoundSql();
        assert boundSql3.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name LIKE CONCAT( ? ,'%')");
        assert boundSql3.getArgs()[0].equals(1);
        assert boundSql3.getArgs()[1].equals("abc");
    }

    @Test
    public void queryBuild_like_right_1_2map() {
        BoundSql boundSql1 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).likeRight("loginName", "abc").getBoundSql();
        assert boundSql1.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name LIKE CONCAT( ? ,'%')");
        assert boundSql1.getArgs()[0].equals(1);
        assert boundSql1.getArgs()[1].equals("abc");

        BoundSql boundSql2 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).or().likeRight("loginName", "abc").getBoundSql();
        assert boundSql2.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? OR login_name LIKE CONCAT( ? ,'%')");
        assert boundSql2.getArgs()[0].equals(1);
        assert boundSql2.getArgs()[1].equals("abc");

        BoundSql boundSql3 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).and().likeRight("loginName", "abc").getBoundSql();
        assert boundSql3.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name LIKE CONCAT( ? ,'%')");
        assert boundSql3.getArgs()[0].equals(1);
        assert boundSql3.getArgs()[1].equals("abc");
    }

    @Test
    public void queryBuild_not_like_right_1() {
        BoundSql boundSql1 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).notLikeRight(UserInfo::getLoginName, "abc").getBoundSql();
        assert boundSql1.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name NOT LIKE CONCAT( ? ,'%')");
        assert boundSql1.getArgs()[0].equals(1);
        assert boundSql1.getArgs()[1].equals("abc");

        BoundSql boundSql2 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).or().notLikeRight(UserInfo::getLoginName, "abc").getBoundSql();
        assert boundSql2.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? OR login_name NOT LIKE CONCAT( ? ,'%')");
        assert boundSql2.getArgs()[0].equals(1);
        assert boundSql2.getArgs()[1].equals("abc");

        BoundSql boundSql3 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).and().notLikeRight(UserInfo::getLoginName, "abc").getBoundSql();
        assert boundSql3.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name NOT LIKE CONCAT( ? ,'%')");
        assert boundSql3.getArgs()[0].equals(1);
        assert boundSql3.getArgs()[1].equals("abc");
    }

    @Test
    public void queryBuild_not_like_right_1_map() {
        BoundSql boundSql1 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).notLikeRight("loginName", "abc").getBoundSql();
        assert boundSql1.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name NOT LIKE CONCAT( ? ,'%')");
        assert boundSql1.getArgs()[0].equals(1);
        assert boundSql1.getArgs()[1].equals("abc");

        BoundSql boundSql2 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).or().notLikeRight("loginName", "abc").getBoundSql();
        assert boundSql2.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? OR login_name NOT LIKE CONCAT( ? ,'%')");
        assert boundSql2.getArgs()[0].equals(1);
        assert boundSql2.getArgs()[1].equals("abc");

        BoundSql boundSql3 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).and().notLikeRight("loginName", "abc").getBoundSql();
        assert boundSql3.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name NOT LIKE CONCAT( ? ,'%')");
        assert boundSql3.getArgs()[0].equals(1);
        assert boundSql3.getArgs()[1].equals("abc");
    }

    @Test
    public void queryBuild_like_left_1() {
        BoundSql boundSql1 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).likeLeft(UserInfo::getLoginName, "abc").getBoundSql();
        assert boundSql1.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name LIKE CONCAT('%', ? )");
        assert boundSql1.getArgs()[0].equals(1);
        assert boundSql1.getArgs()[1].equals("abc");

        BoundSql boundSql2 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).or().likeLeft(UserInfo::getLoginName, "abc").getBoundSql();
        assert boundSql2.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? OR login_name LIKE CONCAT('%', ? )");
        assert boundSql2.getArgs()[0].equals(1);
        assert boundSql2.getArgs()[1].equals("abc");

        BoundSql boundSql3 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).and().likeLeft(UserInfo::getLoginName, "abc").getBoundSql();
        assert boundSql3.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name LIKE CONCAT('%', ? )");
        assert boundSql3.getArgs()[0].equals(1);
        assert boundSql3.getArgs()[1].equals("abc");
    }

    @Test
    public void queryBuild_like_left_1_map() {
        BoundSql boundSql1 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).likeLeft("loginName", "abc").getBoundSql();
        assert boundSql1.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name LIKE CONCAT('%', ? )");
        assert boundSql1.getArgs()[0].equals(1);
        assert boundSql1.getArgs()[1].equals("abc");

        BoundSql boundSql2 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).or().likeLeft("loginName", "abc").getBoundSql();
        assert boundSql2.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? OR login_name LIKE CONCAT('%', ? )");
        assert boundSql2.getArgs()[0].equals(1);
        assert boundSql2.getArgs()[1].equals("abc");

        BoundSql boundSql3 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).and().likeLeft("loginName", "abc").getBoundSql();
        assert boundSql3.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name LIKE CONCAT('%', ? )");
        assert boundSql3.getArgs()[0].equals(1);
        assert boundSql3.getArgs()[1].equals("abc");
    }

    @Test
    public void queryBuild_not_like_left_1() {
        BoundSql boundSql1 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).notLikeLeft(UserInfo::getLoginName, "abc").getBoundSql();
        assert boundSql1.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name NOT LIKE CONCAT('%', ? )");
        assert boundSql1.getArgs()[0].equals(1);
        assert boundSql1.getArgs()[1].equals("abc");

        BoundSql boundSql2 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).or().notLikeLeft(UserInfo::getLoginName, "abc").getBoundSql();
        assert boundSql2.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? OR login_name NOT LIKE CONCAT('%', ? )");
        assert boundSql2.getArgs()[0].equals(1);
        assert boundSql2.getArgs()[1].equals("abc");

        BoundSql boundSql3 = newLambda().queryBySpace(UserInfo.class)//
                .eq(UserInfo::getSeq, 1).and().notLikeLeft(UserInfo::getLoginName, "abc").getBoundSql();
        assert boundSql3.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name NOT LIKE CONCAT('%', ? )");
        assert boundSql3.getArgs()[0].equals(1);
        assert boundSql3.getArgs()[1].equals("abc");
    }

    @Test
    public void queryBuild_not_like_left_1_map() {
        BoundSql boundSql1 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).notLikeLeft("loginName", "abc").getBoundSql();
        assert boundSql1.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name NOT LIKE CONCAT('%', ? )");
        assert boundSql1.getArgs()[0].equals(1);
        assert boundSql1.getArgs()[1].equals("abc");

        BoundSql boundSql2 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).or().notLikeLeft("loginName", "abc").getBoundSql();
        assert boundSql2.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? OR login_name NOT LIKE CONCAT('%', ? )");
        assert boundSql2.getArgs()[0].equals(1);
        assert boundSql2.getArgs()[1].equals("abc");

        BoundSql boundSql3 = newLambda().queryBySpace(UserInfo.class).asMap()//
                .eq("seq", 1).and().notLikeLeft("loginName", "abc").getBoundSql();
        assert boundSql3.getSqlString().equals("SELECT * FROM user_info WHERE seq = ? AND login_name NOT LIKE CONCAT('%', ? )");
        assert boundSql3.getArgs()[0].equals(1);
        assert boundSql3.getArgs()[1].equals("abc");
    }
}
