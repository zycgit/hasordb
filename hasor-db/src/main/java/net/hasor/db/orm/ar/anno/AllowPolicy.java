/*
 * Copyright 2008-2009 the original 赵永春(zyc@hasor.net).
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
package net.hasor.db.orm.ar.anno;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 字段的授权策略
 * @version : 2014年11月26日
 * @author 赵永春(zyc@hasor.net)
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowPolicy {
    /**是否允许用于数据新增*/
    public boolean insert() default true;
    /**是否允许用于数据更新*/
    public boolean update() default true;
    /**是否允许作为删除条件*/
    public boolean deleteWhere() default true;
    /**是否允许作为更新条件*/
    public boolean updateWhere() default true;
    /**一个值它定义之后，再没有给它设置任何值的情况下忽略它作为查询条件（默认策略：true）<p>
     * --引用类型对象，默认值为null<br>
     * --short,int,long,byte,char 的默认值为  0<br>
     * --float,double 的默认值为 0.0<br>
     * --boolean 的默认值为 false*/
    public boolean ignoreUnset() default true;
}