package upm.app.data.repositories.sql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class MySqlTables {
    private static final Logger LOGGER = LogManager.getLogger(MySqlTables.class);
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String DATABASE = "poodb";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private static MySqlTables instance;

    private final Connection connection;

    private MySqlTables() {
        try {
            LOGGER.info("Inicializando base de datos MySQL...");
            dropDatabase();
            createDatabase();
            this.connection = createConnectionMySql();

            LOGGER.info("Conexión establecida con éxito.");

            createUserTable();
            createArticleTable();
            createShoppingCartTable();

            LOGGER.info("Todas las tablas se han creado correctamente.");

        } catch (Exception e) {
            LOGGER.error("Error inicializando la base de datos", e);
            throw new RuntimeException(e);
        }
    }


    public static Connection getConnection() {
        if (instance == null) {
            instance = new MySqlTables();
        }
        return instance.connection;
    }

    private Connection createConnectionMySql() {
        try {
            Class.forName(MySqlTables.DRIVER);
            return DriverManager.getConnection(URL + DATABASE, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new UnsupportedOperationException("Driver error: '" + MySqlTables.DRIVER + "'. " + e.getMessage());
        } catch (SQLException e) {
            throw new UnsupportedOperationException("Connection error with '" + MySqlTables.URL + "'. " + e.getMessage());
        }
    }

    private void dropDatabase() {
        try (Connection connectionTmp = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connectionTmp.createStatement()) {
            statement.executeUpdate("DROP DATABASE IF EXISTS " + DATABASE + ";");
        } catch (SQLException e) {
            throw new UnsupportedOperationException("Drop Database Error. " + e);
        }
    }

    private void createDatabase() {
        try (Connection connectionTmp = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connectionTmp.createStatement()) {
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DATABASE + ";");
        } catch (SQLException e) {
            throw new UnsupportedOperationException("Create Database Error. " + e);
        }
    }

    public void executeUpdate(String sql) {
        LogManager.getLogger(this.getClass()).debug(() -> "Sql: " + sql);
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new UnsupportedOperationException("SQL: " + sql + " ===>>> " + e);
        }
    }

    private void createUserTable() {
        LOGGER.info("Creando tabla user...");
        executeUpdate("""
                    CREATE TABLE IF NOT EXISTS user (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        email VARCHAR(100) UNIQUE NOT NULL,
                        name VARCHAR(50),
                        first_surname VARCHAR(50),
                        second_surname VARCHAR(50),
                        dni VARCHAR(20) UNIQUE NOT NULL,
                        role VARCHAR(20)
                    )
                """);
    }

    private void createArticleTable() {
        LOGGER.info("Creando tabla article...");
        executeUpdate("""
                    CREATE TABLE IF NOT EXISTS article (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        barcode VARCHAR(50) UNIQUE NOT NULL,
                        name VARCHAR(100),
                        price DECIMAL(10,2),
                        provider VARCHAR(100),
                        stock INT,
                        registration_date DATE
                    )
                """);
    }

    private void createShoppingCartTable() {
        LOGGER.info("Creando tabla shopping_cart...");
        executeUpdate("""
                    CREATE TABLE IF NOT EXISTS shopping_cart (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        user_id INT NOT NULL,
                        article_id INT NOT NULL,
                        amount INT,
                        discount DECIMAL(5,2),
                        creation_date TIMESTAMP,
                        FOREIGN KEY (user_id) REFERENCES user(id),
                        FOREIGN KEY (article_id) REFERENCES article(id)
                    )
                """);
    }
}
