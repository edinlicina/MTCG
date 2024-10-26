package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import database.DatabaseManager;
import services.TokenService;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class AuthorizationInterceptorHandler implements HttpHandler {
    private final HttpHandler httpHandler;
    private final TokenService tokenService;

    public AuthorizationInterceptorHandler(HttpHandler httpHandler, DatabaseManager databaseManager) {
        this.httpHandler = httpHandler;
        this.tokenService = new TokenService(databaseManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        List<String> authorizationHeader = exchange.getRequestHeaders().get("Authorization");
        if (authorizationHeader == null || authorizationHeader.isEmpty()) {
            sendUnauthorizedResponse(exchange);
            return;
        }

        authorizationHeader.forEach((header) -> {
            if (!header.startsWith("Bearer ")) {
                try {
                    sendUnauthorizedResponse(exchange);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            String headerToken = header.substring(7);
            boolean isValidToken = tokenService.isValidToken(headerToken);
            if (!isValidToken) {
                try {
                    sendUnauthorizedResponse(exchange);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        httpHandler.handle(exchange);
    }

    private void sendUnauthorizedResponse(HttpExchange exchange) throws IOException {
        String unauthorizedResponse = "Unauthorized";
        exchange.sendResponseHeaders(401, unauthorizedResponse.length());
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(unauthorizedResponse.getBytes());
    }
}
