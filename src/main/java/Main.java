

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.sun.net.httpserver.HttpServer;
import database.DatabaseManager;
import handlers.AuthorizationInterceptorHandler;
import handlers.HelloWorldHandler;
import handlers.SessionsHandler;
import handlers.UsersHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {

    public static void main(String[] args) throws IOException {
        DatabaseManager databaseManager = new DatabaseManager();
        ObjectMapper objectMapper = JsonMapper.builder().configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true).build();
        System.out.println("Hello world!");
        InetSocketAddress socketAddress = new InetSocketAddress(10000);
        HttpServer server = HttpServer.create(socketAddress, 0);
        server.createContext("/hello-world", new AuthorizationInterceptorHandler(new HelloWorldHandler(), databaseManager));
        server.createContext("/users", new UsersHandler(databaseManager, objectMapper));
        server.createContext("/sessions", new SessionsHandler(databaseManager, objectMapper));
        server.start();
    }

}