

import com.sun.net.httpserver.HttpServer;
import database.DatabaseManager;
import handlers.HelloWorldHandler;
import handlers.UsersHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main{

    public static void main(String[] args) throws IOException {
        DatabaseManager databaseManager = new DatabaseManager();
        System.out.println("Hello world!");
        InetSocketAddress socketAddress = new InetSocketAddress(10000);
        HttpServer server = HttpServer.create(socketAddress, 0);
        server.createContext("/hello-world", new HelloWorldHandler());
        server.createContext("/users", new UsersHandler(databaseManager));

        server.start();
    }
}