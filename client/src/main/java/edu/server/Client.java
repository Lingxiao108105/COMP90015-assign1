package edu.server;

import edu.DictionaryClient;
import edu.common.utils.Json;
import edu.javafx.ReconnectGUIController;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

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

    public String getIp() {
        return ip;
    }

    public Integer getPort() {
        return port;
    }

    public Socket getS1() {
        return s1;
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
        } catch (Exception e) {
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
            //end the thread
            if(Thread.currentThread().isInterrupted()){
                closeSocket(s1,inputStream,outputStream);
                System.out.println(Thread.currentThread().getName() + " is stop! ");
                return;
            }
            Request request = null;
            try {
                request = Client.queue.take();
            } catch (InterruptedException e) {
                closeSocket(s1,inputStream,outputStream);
                System.out.println(Thread.currentThread().getName() + " is stop! ");
                return;
            }
            //normal request
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

            } catch (Exception e) {
                //e.printStackTrace();
                closeSocket(s1,inputStream,outputStream);
                connectToNewServer();
                return;
            }
        }

    }

    private void connectToNewServer(){
        //start reconnect GUI
        if(ReconnectGUIController.stage == null){
            Platform.runLater(() ->{
                FXMLLoader fxmlLoader = new FXMLLoader(DictionaryClient.class.getResource("reconnect.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load(), 514.0, 295.0);
                } catch (IOException e) {
                    System.out.println("Fail to load the scene from reconnect.fxml");
                    Thread.currentThread().interrupt();
                }
                Stage stage = new Stage();
                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent windowEvent) {
                        Platform.exit();
                    }
                });
                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
                ReconnectGUIController.stage = stage;
            });
        } else {
            Platform.runLater(() ->{
                ReconnectGUIController.stage.show();
            });
        }
    }

    private void closeSocket(Socket s1, InputStream s1in, OutputStream s1out){
        if(s1 == null){
            return;
        }
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
