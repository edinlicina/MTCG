package services;


import database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TokenService {
    private final Connection connection;
    public TokenService(DatabaseManager databaseManager){
        connection = databaseManager.getConnection();
    }
    public String saveToken(String token, String username) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO token (username, token) VALUES (?,?)");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, token);
            preparedStatement.executeUpdate();
            return token;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public  String getToken(String username) {
        PreparedStatement preparedStatement;
        ResultSet result;
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM token WHERE username = ?");
            preparedStatement.setString(1, username);
            result = preparedStatement.executeQuery();

            if (result.next()) {
                return result.getString(2);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public  boolean isValidToken(String token) {
        PreparedStatement preparedStatement;
        ResultSet result;
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM token WHERE token = ?");
            preparedStatement.setString(1, token);
            result = preparedStatement.executeQuery();

            if (result.next()) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
