package server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static ServerSocket s = null;

    public static void start(String[] args){
        try {
            //Register server on input port
            ServerSocket s = new ServerSocket(Integer.parseInt(args[0]));
            Server.s = s;

            Socket s1 = s.accept();

            OutputStream s1out = s1.getOutputStream();
            DataOutputStream dos = new DataOutputStream(s1out);
            System.out.println(dos);

            //Close the connection, but not the server socket

        }
        catch (IOException e) {
            System.out.println("Input port is invalid or busy.");
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
