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
