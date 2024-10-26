package services;

import database.DatabaseManager;
import dto.CreateUserDto;
import dto.LoginUserDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersService {
    private Connection connection;

    public UsersService(DatabaseManager databaseManager) {
        connection = databaseManager.getConnection();
    }

    public void registerUser(CreateUserDto dto) {

        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO user_data (username, password) VALUES (?,?)");
            preparedStatement.setString(1, dto.username);
            preparedStatement.setString(2, dto.password);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public boolean loginUser(LoginUserDto dto) {
        System.out.println(dto.username);

        PreparedStatement preparedStatement;
        ResultSet result;
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM user_data WHERE username = ?");
            preparedStatement.setString(1, dto.username);
            result = preparedStatement.executeQuery();
            while (result.next()) {
                String password = result.getString(2);


                if(dto.password.equals(password)){
                    return true;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}

