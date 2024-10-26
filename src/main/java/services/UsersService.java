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
    private final TokenService tokenService;

    public UsersService(DatabaseManager databaseManager) {
        connection = databaseManager.getConnection();
        tokenService = new TokenService(databaseManager);
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
                    String token = tokenService.getToken(username);
                    if (token == null) {
                        String newToken = TokenUtil.generateToken(username);
                        token = tokenService.saveToken(newToken, username);
                    }
                    return token;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }


}

