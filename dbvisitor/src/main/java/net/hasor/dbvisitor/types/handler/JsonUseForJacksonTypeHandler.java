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
package net.hasor.dbvisitor.types.handler;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.hasor.cobble.logging.Logger;
import net.hasor.dbvisitor.types.NoCache;

/**
 * 读写 {@link Object}  类型数据
 * @author 赵永春 (zyc@hasor.net)
 */
@NoCache
public class JsonUseForJacksonTypeHandler extends AbstractJsonTypeHandler<Object> {
    private static final Logger       logger = Logger.getLogger(JsonUseForJacksonTypeHandler.class);
    private final        ObjectMapper objectMapper;

    public JsonUseForJacksonTypeHandler(Class<?> type) {
        if (logger.isTraceEnabled()) {
            logger.trace("JsonUseForJacksonTypeHandler(" + type + ")");
        }
        this.rawType = type;
        this.objectMapper = this.createObjectMapper(type);
    }

    protected ObjectMapper createObjectMapper(Class<?> type) {
        return new ObjectMapper();
    }

    @Override
    public String toString() {
        return "JsonUseForJacksonTypeHandler[" + this.rawType + "]@" + super.hashCode();
    }

    @Override
    protected Object parse(String json) throws Exception {
        return this.objectMapper.readValue(json, this.rawType);
    }

    @Override
    protected String toJson(Object obj) throws Exception {
        return this.objectMapper.writeValueAsString(obj);
    }
}
