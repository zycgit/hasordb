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
package net.hasor.dbvisitor.mapper;

import net.hasor.dbvisitor.mapper.def.QueryType;
import net.hasor.dbvisitor.mapper.dto.ResultType4Mapper;
import org.h2.value.CaseInsensitiveMap;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2013-12-10
 */
public class ResultType4MapperTest {
    @Test
    public void selectBool_1() throws IOException {
        MapperRegistry registry = new MapperRegistry();
        registry.loadMapper(ResultType4Mapper.class);

        StatementDef def = registry.findStatement(ResultType4Mapper.class, "selectBool_1");
        assert def != null;
        assert def.getNamespace().equals(ResultType4Mapper.class.getName());
        assert def.getMappingType() == Boolean.class;
        assert def.getConfig().getType() == QueryType.Select;
    }

    @Test
    public void selectBytes_1() throws IOException {
        MapperRegistry registry = new MapperRegistry();
        registry.loadMapper(ResultType4Mapper.class);

        StatementDef def = registry.findStatement(ResultType4Mapper.class, "selectBytes_1");
        assert def != null;
        assert def.getNamespace().equals(ResultType4Mapper.class.getName());
        assert def.getMappingType() == byte[].class;
        assert def.getConfig().getType() == QueryType.Select;
    }

    @Test
    public void selectByte_1() throws IOException {
        MapperRegistry registry = new MapperRegistry();
        registry.loadMapper(ResultType4Mapper.class);

        StatementDef def = registry.findStatement(ResultType4Mapper.class, "selectByte_1");
        assert def != null;
        assert def.getNamespace().equals(ResultType4Mapper.class.getName());
        assert def.getMappingType() == Byte.class;
        assert def.getConfig().getType() == QueryType.Select;
    }

    @Test
    public void selectShort_1() throws IOException {
        MapperRegistry registry = new MapperRegistry();
        registry.loadMapper(ResultType4Mapper.class);

        StatementDef def = registry.findStatement(ResultType4Mapper.class, "selectShort_1");
        assert def != null;
        assert def.getNamespace().equals(ResultType4Mapper.class.getName());
        assert def.getMappingType() == Short.class;
        assert def.getConfig().getType() == QueryType.Select;
    }

    @Test
    public void selectInt_1() throws IOException {
        MapperRegistry registry = new MapperRegistry();
        registry.loadMapper(ResultType4Mapper.class);

        StatementDef def = registry.findStatement(ResultType4Mapper.class, "selectInt_1");
        assert def != null;
        assert def.getNamespace().equals(ResultType4Mapper.class.getName());
        assert def.getMappingType() == Integer.class;
        assert def.getConfig().getType() == QueryType.Select;
    }

    @Test
    public void selectLong_1() throws IOException {
        MapperRegistry registry = new MapperRegistry();
        registry.loadMapper(ResultType4Mapper.class);

        StatementDef def = registry.findStatement(ResultType4Mapper.class, "selectLong_1");
        assert def != null;
        assert def.getNamespace().equals(ResultType4Mapper.class.getName());
        assert def.getMappingType() == Long.class;
        assert def.getConfig().getType() == QueryType.Select;
    }

    @Test
    public void selectFloat_1() throws IOException {
        MapperRegistry registry = new MapperRegistry();
        registry.loadMapper(ResultType4Mapper.class);

        StatementDef def = registry.findStatement(ResultType4Mapper.class, "selectFloat_1");
        assert def != null;
        assert def.getNamespace().equals(ResultType4Mapper.class.getName());
        assert def.getMappingType() == Float.class;
        assert def.getConfig().getType() == QueryType.Select;
    }

    @Test
    public void selectDouble_1() throws IOException {
        MapperRegistry registry = new MapperRegistry();
        registry.loadMapper(ResultType4Mapper.class);

        StatementDef def = registry.findStatement(ResultType4Mapper.class, "selectDouble_1");
        assert def != null;
        assert def.getNamespace().equals(ResultType4Mapper.class.getName());
        assert def.getMappingType() == Double.class;
        assert def.getConfig().getType() == QueryType.Select;
    }

    @Test
    public void selectBigInt_1() throws IOException {
        MapperRegistry registry = new MapperRegistry();
        registry.loadMapper(ResultType4Mapper.class);

        StatementDef def = registry.findStatement(ResultType4Mapper.class, "selectBigInt_1");
        assert def != null;
        assert def.getNamespace().equals(ResultType4Mapper.class.getName());
        assert def.getMappingType() == BigInteger.class;
        assert def.getConfig().getType() == QueryType.Select;
    }

    @Test
    public void selectDecimal_1() throws IOException {
        MapperRegistry registry = new MapperRegistry();
        registry.loadMapper(ResultType4Mapper.class);

        StatementDef def = registry.findStatement(ResultType4Mapper.class, "selectDecimal_1");
        assert def != null;
        assert def.getNamespace().equals(ResultType4Mapper.class.getName());
        assert def.getMappingType() == BigDecimal.class;
        assert def.getConfig().getType() == QueryType.Select;
    }

    @Test
    public void selectNumber_1() throws IOException {
        MapperRegistry registry = new MapperRegistry();
        registry.loadMapper(ResultType4Mapper.class);

        StatementDef def = registry.findStatement(ResultType4Mapper.class, "selectNumber_1");
        assert def != null;
        assert def.getNamespace().equals(ResultType4Mapper.class.getName());
        assert def.getMappingType() == Number.class;
        assert def.getConfig().getType() == QueryType.Select;
    }

    @Test
    public void selectChar_1() throws IOException {
        MapperRegistry registry = new MapperRegistry();
        registry.loadMapper(ResultType4Mapper.class);

        StatementDef def = registry.findStatement(ResultType4Mapper.class, "selectChar_1");
        assert def != null;
        assert def.getNamespace().equals(ResultType4Mapper.class.getName());
        assert def.getMappingType() == Character.class;
        assert def.getConfig().getType() == QueryType.Select;
    }

    @Test
    public void selectString_1() throws IOException {
        MapperRegistry registry = new MapperRegistry();
        registry.loadMapper(ResultType4Mapper.class);

        StatementDef def = registry.findStatement(ResultType4Mapper.class, "selectString_1");
        assert def != null;
        assert def.getNamespace().equals(ResultType4Mapper.class.getName());
        assert def.getMappingType() == String.class;
        assert def.getConfig().getType() == QueryType.Select;
    }

    @Test
    public void selectUrl_1() throws IOException {
        MapperRegistry registry = new MapperRegistry();
        registry.loadMapper(ResultType4Mapper.class);

        StatementDef def = registry.findStatement(ResultType4Mapper.class, "selectUrl_1");
        assert def != null;
        assert def.getNamespace().equals(ResultType4Mapper.class.getName());
        assert def.getMappingType() == URL.class;
        assert def.getConfig().getType() == QueryType.Select;
    }

    @Test
    public void selectUri_1() throws IOException {
        MapperRegistry registry = new MapperRegistry();
        registry.loadMapper(ResultType4Mapper.class);

        StatementDef def = registry.findStatement(ResultType4Mapper.class, "selectUri_1");
        assert def != null;
        assert def.getNamespace().equals(ResultType4Mapper.class.getName());
        assert def.getMappingType() == URI.class;
        assert def.getConfig().getType() == QueryType.Select;
    }

    @Test
    public void selectVoid_1() throws IOException {
        MapperRegistry registry = new MapperRegistry();
        registry.loadMapper(ResultType4Mapper.class);

        StatementDef def = registry.findStatement(ResultType4Mapper.class, "selectVoid_1");
        assert def != null;
        assert def.getNamespace().equals(ResultType4Mapper.class.getName());
        assert def.getMappingType() == null;
        assert def.getConfig().getType() == QueryType.Select;
    }

    @Test
    public void selectMap_1() throws IOException {
        MapperRegistry registry = new MapperRegistry();
        registry.loadMapper(ResultType4Mapper.class);

        StatementDef def = registry.findStatement(ResultType4Mapper.class, "selectMap_1");
        assert def != null;
        assert def.getNamespace().equals(ResultType4Mapper.class.getName());
        assert def.getMappingType() == Map.class;
        assert def.getConfig().getType() == QueryType.Select;
    }

    @Test
    public void selectMap_2() throws IOException {
        MapperRegistry registry = new MapperRegistry();
        registry.loadMapper(ResultType4Mapper.class);

        StatementDef def = registry.findStatement(ResultType4Mapper.class, "selectMap_2");
        assert def != null;
        assert def.getNamespace().equals(ResultType4Mapper.class.getName());
        assert def.getMappingType() == HashMap.class;
        assert def.getConfig().getType() == QueryType.Select;
    }

    @Test
    public void selectMap_3() throws IOException {
        MapperRegistry registry = new MapperRegistry();
        registry.loadMapper(ResultType4Mapper.class);

        StatementDef def = registry.findStatement(ResultType4Mapper.class, "selectMap_3");
        assert def != null;
        assert def.getNamespace().equals(ResultType4Mapper.class.getName());
        assert def.getMappingType() == CaseInsensitiveMap.class;
        assert def.getConfig().getType() == QueryType.Select;
    }

    @Test
    public void selectDate_1() throws IOException {
        MapperRegistry registry = new MapperRegistry();
        registry.loadMapper(ResultType4Mapper.class);

        StatementDef def = registry.findStatement(ResultType4Mapper.class, "selectDate_1");
        assert def != null;
        assert def.getNamespace().equals(ResultType4Mapper.class.getName());
        assert def.getMappingType() == java.util.Date.class;
        assert def.getConfig().getType() == QueryType.Select;
    }

    @Test
    public void selectSqlDate_1() throws IOException {
        MapperRegistry registry = new MapperRegistry();
        registry.loadMapper(ResultType4Mapper.class);

        StatementDef def = registry.findStatement(ResultType4Mapper.class, "selectSqlDate_1");
        assert def != null;
        assert def.getNamespace().equals(ResultType4Mapper.class.getName());
        assert def.getMappingType() == java.sql.Date.class;
        assert def.getConfig().getType() == QueryType.Select;
    }

    @Test
    public void selectSqlTime_1() throws IOException {
        MapperRegistry registry = new MapperRegistry();
        registry.loadMapper(ResultType4Mapper.class);

        StatementDef def = registry.findStatement(ResultType4Mapper.class, "selectSqlTime_1");
        assert def != null;
        assert def.getNamespace().equals(ResultType4Mapper.class.getName());
        assert def.getMappingType() == java.sql.Time.class;
        assert def.getConfig().getType() == QueryType.Select;
    }

    @Test
    public void selectSqlTimestamp_1() throws IOException {
        MapperRegistry registry = new MapperRegistry();
        registry.loadMapper(ResultType4Mapper.class);

        StatementDef def = registry.findStatement(ResultType4Mapper.class, "selectSqlTimestamp_1");
        assert def != null;
        assert def.getNamespace().equals(ResultType4Mapper.class.getName());
        assert def.getMappingType() == java.sql.Timestamp.class;
        assert def.getConfig().getType() == QueryType.Select;
    }

    @Test
    public void selectOffsetDateTime_1() throws IOException {
        MapperRegistry registry = new MapperRegistry();
        registry.loadMapper(ResultType4Mapper.class);

        StatementDef def = registry.findStatement(ResultType4Mapper.class, "selectOffsetDateTime_1");
        assert def != null;
        assert def.getNamespace().equals(ResultType4Mapper.class.getName());
        assert def.getMappingType() == java.time.OffsetDateTime.class;
        assert def.getConfig().getType() == QueryType.Select;
    }

    @Test
    public void selectOffsetTime_1() throws IOException {
        MapperRegistry registry = new MapperRegistry();
        registry.loadMapper(ResultType4Mapper.class);

        StatementDef def = registry.findStatement(ResultType4Mapper.class, "selectOffsetTime_1");
        assert def != null;
        assert def.getNamespace().equals(ResultType4Mapper.class.getName());
        assert def.getMappingType() == java.time.OffsetTime.class;
        assert def.getConfig().getType() == QueryType.Select;
    }

    @Test
    public void selectLocalDate_1() throws IOException {
        MapperRegistry registry = new MapperRegistry();
        registry.loadMapper(ResultType4Mapper.class);

        StatementDef def = registry.findStatement(ResultType4Mapper.class, "selectLocalDate_1");
        assert def != null;
        assert def.getNamespace().equals(ResultType4Mapper.class.getName());
        assert def.getMappingType() == java.time.LocalDate.class;
        assert def.getConfig().getType() == QueryType.Select;
    }

    @Test
    public void selectLocalTime_1() throws IOException {
        MapperRegistry registry = new MapperRegistry();
        registry.loadMapper(ResultType4Mapper.class);

        StatementDef def = registry.findStatement(ResultType4Mapper.class, "selectLocalTime_1");
        assert def != null;
        assert def.getNamespace().equals(ResultType4Mapper.class.getName());
        assert def.getMappingType() == java.time.LocalTime.class;
        assert def.getConfig().getType() == QueryType.Select;
    }

    @Test
    public void selectLocalDateTime_1() throws IOException {
        MapperRegistry registry = new MapperRegistry();
        registry.loadMapper(ResultType4Mapper.class);

        StatementDef def = registry.findStatement(ResultType4Mapper.class, "selectLocalDateTime_1");
        assert def != null;
        assert def.getNamespace().equals(ResultType4Mapper.class.getName());
        assert def.getMappingType() == java.time.LocalDateTime.class;
        assert def.getConfig().getType() == QueryType.Select;
    }

    @Test
    public void selectMonthDay_1() throws IOException {
        MapperRegistry registry = new MapperRegistry();
        registry.loadMapper(ResultType4Mapper.class);

        StatementDef def = registry.findStatement(ResultType4Mapper.class, "selectMonthDay_1");
        assert def != null;
        assert def.getNamespace().equals(ResultType4Mapper.class.getName());
        assert def.getMappingType() == java.time.MonthDay.class;
        assert def.getConfig().getType() == QueryType.Select;
    }

    @Test
    public void selectMonth_1() throws IOException {
        MapperRegistry registry = new MapperRegistry();
        registry.loadMapper(ResultType4Mapper.class);

        StatementDef def = registry.findStatement(ResultType4Mapper.class, "selectMonth_1");
        assert def != null;
        assert def.getNamespace().equals(ResultType4Mapper.class.getName());
        assert def.getMappingType() == java.time.Month.class;
        assert def.getConfig().getType() == QueryType.Select;
    }

    @Test
    public void selectYearMonth_1() throws IOException {
        MapperRegistry registry = new MapperRegistry();
        registry.loadMapper(ResultType4Mapper.class);

        StatementDef def = registry.findStatement(ResultType4Mapper.class, "selectYearMonth_1");
        assert def != null;
        assert def.getNamespace().equals(ResultType4Mapper.class.getName());
        assert def.getMappingType() == java.time.YearMonth.class;
        assert def.getConfig().getType() == QueryType.Select;
    }

    @Test
    public void selectYear_1() throws IOException {
        MapperRegistry registry = new MapperRegistry();
        registry.loadMapper(ResultType4Mapper.class);

        StatementDef def = registry.findStatement(ResultType4Mapper.class, "selectYear_1");
        assert def != null;
        assert def.getNamespace().equals(ResultType4Mapper.class.getName());
        assert def.getMappingType() == java.time.Year.class;
        assert def.getConfig().getType() == QueryType.Select;
    }
}
