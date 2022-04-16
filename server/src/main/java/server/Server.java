package server;

import com.fasterxml.jackson.core.JsonParser;
import common.utils.Json;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static ServerSocket s = null;

    public static void start(String[] args){
        try {
            //Register server on input port
            Server.s = new ServerSocket(Integer.parseInt(args[0]));

            Socket s1 = Server.s.accept();
            while (true){
                InputStream s1In = s1.getInputStream();
                Request request = Json.getInstance().readValue(s1In, Request.class);
                System.out.println(request);
            }


            //Close the connection, but not the server socket

        }
        catch (IOException e) {
            System.out.println("Input port is invalid or busy.");
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
