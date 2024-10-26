package handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import database.DatabaseManager;
import dto.CreateUserDto;
import dto.LoginUserDto;
import services.UsersService;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class SessionsHandler implements HttpHandler {
    private final UsersService usersService;
    private final ObjectMapper objectMapper;

    public SessionsHandler(DatabaseManager databaseManager, ObjectMapper objectMapper) {
        usersService = new UsersService(databaseManager);
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "hello users";
        String method = exchange.getRequestMethod();
        if (method.equalsIgnoreCase("POST")) {
            handlePost(exchange);
        }
        exchange.sendResponseHeaders(200, response.length());
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(response.getBytes());
        outputStream.close();
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

        LoginUserDto loginUserDto = objectMapper.readValue(requestBody, LoginUserDto.class);
        String token = usersService.loginUser(loginUserDto);
        exchange.sendResponseHeaders(200, token.length());
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(token.getBytes());
        outputStream.close();
    }
}
