/*
 * Copyright PT Len Industri (Persero)
 *
 * THIS SOFTWARE SOURCE CODE AND ANY EXECUTABLE DERIVED THEREOF ARE PROPRIETARY
 * TO PT LEN INDUSTRI (PERSERO), AS APPLICABLE, AND SHALL NOT BE USED IN ANY WAY
 * OTHER THAN BEFOREHAND AGREED ON BY PT LEN INDUSTRI (PERSERO), NOR BE REPRODUCED
 * OR DISCLOSED TO THIRD PARTIES WITHOUT PRIOR WRITTEN AUTHORIZATION BY
 * PT LEN INDUSTRI (PERSERO), AS APPLICABLE.
 */

/*
 * @author Hendra
 * */

/*
 * the class for database driver
 */

package org.len.db;

import org.len.DotenvLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class DatabaseConnector {
    private static Connection connection;

    public static Connection connectDatabase() throws SQLException, ClassNotFoundException {
        Map<String, String> envVariables = DotenvLoader.loadEnvVariables();

        String host = envVariables.get("DB_HOST");
        String port = envVariables.get("DB_PORT");
        String user = envVariables.get("DB_USER");
        String password = envVariables.get("DB_PASSWORD");
        String dbName = envVariables.get("DB_NAME");
        String dbType = envVariables.get("DB_TYPE");
        Class.forName("org.postgresql.Driver");
        String jdbcUrl;

        /* flexible changes db connection by env dbType var

         */

        if ("mysql".equals(dbType)) {
            jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + dbName;
        } else if ("postgres".equals(dbType)) {
            jdbcUrl = "jdbc:postgresql://" + host + ":" + port + "/" + dbName;
        } else if ("sqlite".equals(dbType)) {
            jdbcUrl = "jdbc:sqlite:" + dbName;
        } else if ("mssql".equals(dbType)) {
            jdbcUrl = "jdbc:sqlserver://" + host + ":" + port + ";databaseName=" + dbName;
        } else {
            throw new IllegalArgumentException("Unsupported database type: " + dbType);
        }

        connection = DriverManager.getConnection(jdbcUrl, user, password);
        System.out.println("Database Connected");
        return connection;
    }
}
