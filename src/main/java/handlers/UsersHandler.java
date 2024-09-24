package handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dto.CreateUserDto;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class UsersHandler implements HttpHandler {


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
        System.out.println("post user");
        String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();
        CreateUserDto createUserDto = objectMapper.readValue(requestBody, CreateUserDto.class);
        System.out.println(createUserDto.username);
    }

    private void handleGet(HttpExchange exchange) throws IOException{

        System.out.println("get user");
        String path = exchange.getRequestURI().getPath();

        String[] pathFragments = path.split("/");
        if(pathFragments.length != 3){
            String response = "Invalid Request";
            exchange.sendResponseHeaders(400, response.length());
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(response.getBytes());
            outputStream.close();
            return;
        }
       String username = pathFragments[2];
        System.out.println(username);
        //datadase userdata display

    }

    private void handlePut(HttpExchange exchange) {
        System.out.println("put user");

    }
}
