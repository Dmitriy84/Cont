package com.continuum.cucumber.utils;

import com.continuum.cucumber.shell.Application;
import com.continuum.cucumber.shell.Shell;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
public class DBUtilities {
    private static String mySQLConnString = "com.mysql.jdbc.Driver";
    private static String sqlServerConnString = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static Connection conn = null;

    static void connectDB(String DBSeverName) {
        if (!DBSeverName.equals("")) {
            String DBserverURL = null;
            String connString = null;
            final Application.TestResultsDb db = Shell.Application.getTestResultsDb();
            if (DBSeverName.equalsIgnoreCase("mysql")) {
                DBserverURL = "jdbc:mysql://" + db.getUrl();
                connString = mySQLConnString;
            } else if (DBSeverName.equalsIgnoreCase("sqlserver")) {
                DBserverURL = "jdbc:sqlserver://" + db.getUrl();
                connString = sqlServerConnString;
            }

            log.info("Creating connection to " + DBSeverName);
            createConnection(connString, db.getName(), DBserverURL, db.getUser(), db.getPassword());
        } else {
            log.info("Unknown Database Host Name.. Unable to create connection");
        }
    }

    private static Connection createConnection(String JDBCdriverString, String databaseName, String DBServerURL, String username, String password) {
        try {
            Class.forName(JDBCdriverString);
            conn = DriverManager.getConnection(DBServerURL + databaseName, username, password);
            log.info("Connection Successful to DB..");
        } catch (SQLException | ClassNotFoundException var6) {
            log.error("Attempt to connect to Database failed.", var6);
        }
        return conn;
    }

    static void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
                log.info("Connection to Database is closed");
            } catch (SQLException var1) {
                log.error("Unable to close DB connection", var1);
            }
        }
    }

    static void insertValues(String tableName, String[] ArrColumnNames, String[] ArrCoulmnValues) {
        if (conn == null) {
            log.info("Connection to Database is not present. Insert operaton failed..");
        } else {
            Statement stmt;
            try {
                if (ArrColumnNames != null && ArrCoulmnValues != null && tableName != null) {
                    StringBuilder columnNames = new StringBuilder(ArrColumnNames[0]);
                    StringBuilder columnValues = new StringBuilder("'" + ArrCoulmnValues[0] + "'");
                    if (ArrColumnNames.length > 1) {
                        for (int i = 1; i < ArrColumnNames.length; ++i) {
                            columnNames.append(", ").append(ArrColumnNames[i]);
                            columnValues.append(",'").append(ArrCoulmnValues[i]).append("'");
                        }
                    }

                    stmt = conn.createStatement();
                    stmt.executeUpdate("INSERT INTO " + tableName + " (" + columnNames + " ) VALUES( " + columnValues + " );");
                }
            } catch (SQLException var8) {
                log.error("Insert operation failed.", var8);
            }

        }
    }

    public static void updateValues(String tableName, String[] ArrColumnNames, String[] ArrCoulmnValues, String[] whereCoulmn, String[] whereValue) {
        if (conn == null) {
            log.info("Connection to database is not present. Update operation failed..");
        } else {
            String updateQuery;
            Statement stmt;

            try {
                StringBuilder valueSet;
                valueSet = new StringBuilder(ArrColumnNames[0] + "= '" + ArrCoulmnValues[0] + "'");
                String whereCondition = whereCoulmn[0] + "= '" + whereValue[0] + "'";
                int i;
                if (ArrColumnNames.length > 1) {
                    for (i = 1; i < ArrColumnNames.length; ++i) {
                        valueSet.append(", ").append(ArrColumnNames[i]).append("= '").append(ArrCoulmnValues[i]).append("'");
                    }
                }
                if (whereCoulmn.length > 1) {
                    StringBuilder whereConditionBuilder = new StringBuilder(whereCondition);
                    for (i = 1; i < whereCoulmn.length; ++i) {
                        whereConditionBuilder.append(" AND ").append(whereCoulmn[i]).append("= '").append(whereValue[i]).append("'");
                    }
                    whereCondition = whereConditionBuilder.toString();
                }
                updateQuery = "UPDATE " + tableName + " SET " + valueSet + " WHERE " + whereCondition;
                stmt = conn.createStatement();
                stmt.executeUpdate(updateQuery);
            } catch (SQLException var10) {
                log.error("Update operation failed.", var10);
            }

        }
    }

    public static void deleteValues(String tableName, String[] whereCoulmn, String[] whereValue) {
        if (conn == null) {
            log.info("Connection to database is not present. Delete operation failed..");
        } else {
            String deleteQuery;
            Statement stmt;
            try {
                StringBuilder whereCondition;
                whereCondition = new StringBuilder(whereCoulmn[0] + "= '" + whereValue[0] + "'");
                int i;
                if (whereCoulmn.length > 1) {
                    for (i = 1; i < whereCoulmn.length; ++i) {
                        whereCondition.append(" AND ").append(whereCoulmn[i]).append("= '").append(whereValue[i]).append("'");
                    }
                }
                deleteQuery = "DELETE FROM " + tableName + " WHERE " + whereCondition;
                log.info("Executing sql query: " + deleteQuery);
                stmt = conn.createStatement();
                i = stmt.executeUpdate(deleteQuery);
                log.info("Delete successfully executed. " + i + " Records deleted.");
            } catch (SQLException var7) {
                log.error("Delete operation failed.", var7);
            }
        }
    }
}