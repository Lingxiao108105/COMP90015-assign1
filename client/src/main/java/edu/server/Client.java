package edu.server;

import edu.common.utils.Json;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

public class Client extends Thread {

    private final static BlockingQueue<Request> queue = new LinkedBlockingDeque<>();
    private final static ConcurrentHashMap<Request,Response> responseMap = new ConcurrentHashMap<>();
    private static Integer logicalTime = 0;

    private String ip = "127.0.0.1";
    private Integer port = 10000;
    private Socket s1 = null;

    public Client() {
    }

    public Client(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }

    public static void enqueueRequest(Request request){
        try {
            Client.queue.put(request);
        } catch (InterruptedException e) {
            System.out.println("Fail to add request to queue!");
//            e.printStackTrace();
        }
    }

    //return the response or null if the response do not exist
    public static Response getResponse(Request request){
        return Client.responseMap.get(request);
    }

    //check whether the response is latest
    public static Boolean latest(Response response){
        return response.getLogicalTime() == logicalTime;
    }



    @Override
    public void run() {

        try {
            this.s1 = new Socket(ip ,port);
        } catch (IOException e) {
            System.out.println("Fail to connect to server " + ip + ":" + port);
            closeSocket(s1,null,null);
            connectToNewServer();
            return;
        }

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = s1.getInputStream();
            outputStream = s1.getOutputStream();
        } catch (IOException e) {
            System.out.println("Fail to open IO stream!");
            closeSocket(s1,inputStream,null);
            connectToNewServer();
            return;
        }


        while (true){
            Request request = null;
            try {
                request = Client.queue.take();
            } catch (InterruptedException e) {
                System.out.println("Fail to take request from queue!");
                continue;
//                e.printStackTrace();
            }
            Client.logicalTime++;
            request.setLogicalTime(Client.logicalTime);
            try {
                //send request and read response
                Json.getInstance().writeValue(outputStream,request);
                Response response = Json.getInstance().readValue(inputStream, Response.class);
                //ignore outdated response
                if(response.getLogicalTime() < logicalTime){
                    continue;
                }
                //save to responseMap
                responseMap.put(request,response);

            } catch (IOException e) {
                e.printStackTrace();
                closeSocket(s1,inputStream,outputStream);
                connectToNewServer();
                return;
            }
        }

    }

    private void connectToNewServer(){

    }

    private void closeSocket(Socket s1, InputStream s1in, OutputStream s1out){
        String remoteSocketAddress = s1.getRemoteSocketAddress().toString();
        try {
            if(s1in != null){
                s1in.close();
            }
            if(s1out != null){
                s1out.close();
            }
            s1.close();
        } catch (IOException ex) {
            System.out.println("Fail to close " + remoteSocketAddress);
        }
        System.out.println(remoteSocketAddress + " is closed");
    }


}
