package server;

import common.utils.CustomThreadPool;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * socket server wait for connection
 *
 * @author lingxiao li 1031146
 */
public class Server {

    private static ServerSocket s = null;
    private static final CustomThreadPool threadPool = new CustomThreadPool();

    /**
     * start the socket srver
     * @param args args of main
     */
    public static void start(String[] args){
        try {
            //Register server on input port
            Server.s = new ServerSocket(Integer.parseInt(args[0]));
            System.out.println("Successfully register server on port: " + args[0]);

            //wait for connection
            do {
                Socket s1 = Server.s.accept();
                System.out.println("Receive connection from: " + s1.getRemoteSocketAddress().toString());
                threadPool.execute(new RequestHandler(s1));
            } while (true);

        }
        catch (Exception e) {
            System.out.println("Input port is invalid or busy.");
            //e.printStackTrace();
            System.exit(-1);
        }
    }
}
