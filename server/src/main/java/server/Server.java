package server;

import com.fasterxml.jackson.core.JsonParser;
import common.utils.CustomThreadPool;
import common.utils.Json;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static ServerSocket s = null;
    private static CustomThreadPool threadPool = new CustomThreadPool();

    public static void start(String[] args){
        try {
            //Register server on input port
            Server.s = new ServerSocket(Integer.parseInt(args[0]));

            do {
                Socket s1 = Server.s.accept();
                threadPool.execute(new RequestHandler(s1));
            } while (true);

        }
        catch (IOException e) {
            System.out.println("Input port is invalid or busy.");
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
