package org.simplebank.util;

import org.h2.tools.RunScript;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import static org.simplebank.util.Configs.getProperty;

public class DataSetup {
    private static final Logger log = Logger.getLogger(DataSetup.class);

    public static void populateData() {
        log.info("Populating Test User Table and data ..... ");
        try (Connection conn = DriverManager.getConnection(getProperty("url"), "", "")) {
            String filePath = new File("").getAbsolutePath();
            String SQL_FILE_PATH = "/src/main/resources/demo.sql";
            RunScript.execute(conn, new FileReader(filePath + SQL_FILE_PATH));
        } catch (SQLException e) {
            log.error("Error populating user data: ", e);
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            log.error("Error finding test script file ", e);
            throw new RuntimeException(e);
        }
    }

    public static void clearDataSet(){
        log.info("Populating Test User Table and data ..... ");
        try (Connection conn = DriverManager.getConnection(getProperty("url"), "", "")) {
            String filePath = new File("").getAbsolutePath();
            String SQL_FILE_PATH = "/src/main/resources/cleanUp.sql";
            RunScript.execute(conn, new FileReader(filePath + SQL_FILE_PATH));
        } catch (SQLException e) {
            log.error("Error populating user data: ", e);
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            log.error("Error finding test script file ", e);
            throw new RuntimeException(e);
        }
    }
}
