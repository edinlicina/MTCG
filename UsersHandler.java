import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class UsersHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response ="hello users";
        String method = exchange.getRequestMethod();
        if(method.equalsIgnoreCase("GET")){
            getUserByUsername(exchange);
        } else if (method.equalsIgnoreCase("PUT")) {
            putUserByUsername(exchange);
        } else if (method.equalsIgnoreCase("POST")) {
            postUser(exchange);
        }
        exchange.sendResponseHeaders(200, response.length());
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(response.getBytes());
        outputStream.close();
    }

    private void postUser(HttpExchange exchange){
        System.out.println("post user");
    }
    private void getUserByUsername(HttpExchange exchange){

        System.out.println("get user");
    }
    private void putUserByUsername(HttpExchange exchange){
        System.out.println("put user");

    }
}
