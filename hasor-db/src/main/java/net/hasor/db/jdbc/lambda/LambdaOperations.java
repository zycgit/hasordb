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
package net.hasor.db.jdbc.lambda;
import net.hasor.db.jdbc.lambda.mapping.ColumnMeta;
import net.hasor.db.jdbc.lambda.mapping.MetaManager;
import net.hasor.utils.ClassUtils;
import net.hasor.utils.reflect.MethodUtils;
import net.hasor.utils.reflect.SFunction;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Predicate;

/**
 * 提供 lambda 方式生成 SQL。
 * @version : 2020-10-27
 * @author 赵永春 (zyc@hasor.net)
 */
public interface LambdaOperations {
    /** 相当于 select * form */
    public default <T> LambdaQuery<T> lambdaSelect() {
        return lambdaSelect((SFunction<T, ?>[]) null);
    }

    /** 相当于 select * form */
    public default <T> LambdaQuery<T> lambdaSelect(Class<T> exampleType) {
        return lambdaSelect(exampleType, (SFunction<T, ?>[]) null);
    }

    /** 相当于 select xxx,xxx,xxx form */
    public default <T> LambdaQuery<T> lambdaSelect(SFunction<T, ?>... columns) {
        Predicate<T> temp = t -> false;
        Class<T> targetType = (Class<T>) ClassUtils.getSuperClassGenricType(temp.getClass(), 0);
        return lambdaSelect(targetType, columns);
    }

    /** 相当于 select xxx,xxx,xxx form */
    public default <T> LambdaQuery<T> lambdaSelect(Class<T> exampleType, SFunction<T, ?>... columns) {
        if (columns == null || columns.length == 0) {
            return lambdaSelect(exampleType, new ColumnMeta[0]);
        }
        ColumnMeta[] toArray = Arrays.stream(columns).map(columnName -> {
            Method lambdaMethod = MethodUtils.lambdaMethodName(columnName);
            return MetaManager.toColumnMeta(lambdaMethod);
        }).toArray(ColumnMeta[]::new);
        return lambdaSelect(exampleType, toArray);
    }

    /** 相当于 select xxx,xxx,xxx form */
    public <T> LambdaQuery<T> lambdaSelect(Class<T> exampleType, ColumnMeta... columns);

    /** 封装 */
    public interface LambdaQuery<T> extends AbstractLambdaQuery<T, LambdaQuery<T>>, BoundSql, QueryExecute<T> {
    }

    public interface AbstractLambdaQuery<T, R extends AbstractLambdaQuery<T, R>> extends //
            Compare<T, R>, //
            Nested<NestedQuery<T>, AbstractLambdaQuery<T, R>>,//
            Func<T, R> {
    }

    /** 嵌套 */
    public interface NestedQuery<T> extends AbstractNestedQuery<T, NestedQuery<T>> {
    }

    public interface AbstractNestedQuery<T, R extends AbstractNestedQuery<T, R>> extends //
            Compare<T, R>, //
            Nested<AbstractNestedQuery<T, R>, AbstractNestedQuery<T, R>> {
    }

    public interface BoundSql {
        public String getSqlString();

        public Map<String, Object> getArgs();
    }
}