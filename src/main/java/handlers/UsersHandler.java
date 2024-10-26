package handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import database.DatabaseManager;
import dto.CreateUserDto;
import dto.LoginUserDto;
import services.TokenService;
import services.UsersService;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class UsersHandler implements HttpHandler {
    private final UsersService usersService;

    public UsersHandler(DatabaseManager databaseManager) {
        usersService = new UsersService(databaseManager);
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "hello users";
        String method = exchange.getRequestMethod();
        if (method.equalsIgnoreCase("GET")) {
            handleGet(exchange);
        } else if (method.equalsIgnoreCase("PUT")) {
            handlePut(exchange);
        } else if (method.equalsIgnoreCase("POST")) {
            handlePost(exchange);
        }
        exchange.sendResponseHeaders(200, response.length());
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(response.getBytes());
        outputStream.close();
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();

        // Determine if it's a register or login endpoint
        if (path.equals("/users/register")) {
            CreateUserDto createUserDto = objectMapper.readValue(requestBody, CreateUserDto.class);
            usersService.registerUser(createUserDto);
            String response = "User registered successfully";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(response.getBytes());
            outputStream.close();
        } else if (path.equals("/users/login")) {
            LoginUserDto loginUserDto = objectMapper.readValue(requestBody, LoginUserDto.class);
            String token = usersService.loginUser(loginUserDto);
            exchange.sendResponseHeaders( 200 , token.length());
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(token.getBytes());
            outputStream.close();
        } else {
            // If the path doesn't match either, return an error response
            String response = "Invalid endpoint";
            exchange.sendResponseHeaders(404, response.length());
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(response.getBytes());
            outputStream.close();
        }
    }


    private void handleGet(HttpExchange exchange) throws IOException {

        System.out.println("get user");
        String path = exchange.getRequestURI().getPath();

        String[] pathFragments = path.split("/");
        if (pathFragments.length != 3) {
            String response = "Invalid Request";
            exchange.sendResponseHeaders(400, response.length());
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(response.getBytes());
            outputStream.close();
            return;
        }
        String username = pathFragments[2];
        System.out.println(username);
        //database userdata display

    }

    private void handlePut(HttpExchange exchange) {
        System.out.println("put user");

    }
}
