/*
 * Copyright (c) 2020 Uber Technologies, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// This file is copied and modified from https://github.com/uber-common/jvm-profiler/tree/b54894d8a6da9e5bf73da652a92121fd44882722/src/main/java_jdbc/com/uber/profiling/reporters/util/

package org.datapunch.starfish.db.framework;

import org.datapunch.starfish.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnectionProvider {
    private static final Logger logger = LoggerFactory.getLogger(DbConnectionProvider.class.getName());

    private static final String MYSQL_JDBC_DRIVER_CLASS = "com.mysql.jdbc.Driver";

    private final String jdbcDriverClass;
    private final String connectionString;
    private final String user;
    private final String password;

    private volatile static Connection connection;

    public DbConnectionProvider(String jdbcDriverClass, String connectionString) {
        this.jdbcDriverClass = jdbcDriverClass;
        this.connectionString = connectionString;
        this.user = null;
        this.password = null;
    }

    public DbConnectionProvider(String jdbcDriverClass, String connectionString, String user, String password) {
        this.jdbcDriverClass = jdbcDriverClass;
        this.connectionString = connectionString;
        this.user = user;
        this.password = password;
    }

    public String getConnectionString() {
        return connectionString;
    }

    public  Connection getConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    return connection;
                }
            } catch (SQLException e) {
                logger.warn("Failed to check whether db connection is closed", e);
            }
        }
        try {
            if (jdbcDriverClass != null && !jdbcDriverClass.isEmpty()) {
                try {
                    Class.forName(jdbcDriverClass);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException("Failed to find JDBC class " + jdbcDriverClass, e);
                }
            }
            if (StringUtil.isNullOrEmpty(user)) {
                connection = DriverManager.getConnection(connectionString);
            } else {
                connection = DriverManager.getConnection(connectionString, user, password);
            }
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create JDBC connection: " + connectionString, e);
        }
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (Throwable e) {
                connection = null;
                logger.warn("Failed to close JDBC connection", e);
            }
        }
    }
}