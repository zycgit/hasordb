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
package net.hasor.scene.connection;
import net.hasor.dbvisitor.jdbc.DynamicConnection;
import net.hasor.test.utils.DsUtils;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @version : 2013-12-10
 * @author 赵永春 (zyc@hasor.net)
 */
public class H2DynamicConnection implements DynamicConnection {
    @Override
    public Connection getConnection() throws SQLException {
        return DsUtils.h2Conn();
    }

    @Override
    public void releaseConnection(Connection conn) throws SQLException {
        conn.close();
    }
}