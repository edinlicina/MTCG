package services;

import database.DatabaseManager;
import dto.CreateUserDto;
import dto.LoginUserDto;
import utils.TokenUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersService {
    private final Connection connection;

    public UsersService(DatabaseManager databaseManager) {
        connection = databaseManager.getConnection();
    }

    public void registerUser(CreateUserDto dto) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO user_data (username, password) VALUES (?,?)");
            preparedStatement.setString(1, dto.username);
            preparedStatement.setString(2, dto.password);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String loginUser(LoginUserDto dto) {
        PreparedStatement preparedStatement;
        ResultSet result;
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM user_data WHERE username = ?");
            preparedStatement.setString(1, dto.username);
            result = preparedStatement.executeQuery();

            if (result.next()) {
                String username = result.getString(1);
                String password = result.getString(2);
                if (dto.password.equals(password)) {
                    String token = getToken(username);
                    if (token == null) {
                        String newToken = TokenUtil.generateToken(username);
                        token = saveToken(newToken, username);
                    }
                    return token;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private String saveToken(String token, String username) {
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

    private String getToken(String username) {
        PreparedStatement preparedStatement;
        ResultSet result;
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM token WHERE username = ?");
            preparedStatement.setString(1, username);
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
}

