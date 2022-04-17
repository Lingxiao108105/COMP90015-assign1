package server;

import common.enums.Status;
import common.utils.Json;
import data.Dictionary;
import data.Word;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RequestHandler implements Runnable{

    private Socket s1 = null;

    public RequestHandler(Socket s1) {
        this.s1 = s1;
    }

    @Override
    public void run() {
        //sanity check
        if(s1 == null){
            return;
        }
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = s1.getInputStream();
            outputStream = s1.getOutputStream();
        } catch (IOException e) {
            System.out.println("Fail to open IO stream!");
            closeSocket(s1,inputStream,outputStream);
            return;
        }

        while (true){

            if(Thread.currentThread().isInterrupted()){
                closeSocket(s1,inputStream,outputStream);
                break;
            }

            Request request = null;
            try {
                request = Json.getInstance().readValue(inputStream, Request.class);
                System.out.println("Get request: " + request);
            } catch (IOException e) {
                //response with msg invalid request
                Response response = invalidRequest();
                try {
                    Json.getInstance().writeValue(outputStream,response);
                } catch (IOException ex) {
                    closeSocket(s1,inputStream,outputStream);
                    return;
                }
                continue;
            }
            //execute request and produce response
            Response response = invalidRequest();
            if(request != null && request.getRequestType() != null){
                if(request.getRequestType() == RequestType.QUERY){
                    response = query(request);
                }
                else if(request.getRequestType() == RequestType.ADD){
                    response = add(request);
                }
                else if(request.getRequestType() == RequestType.REMOVE){
                    response = remove(request);
                }
                else if(request.getRequestType() == RequestType.UPDATE){
                    response = update(request);
                }
                else {
                    //response with msg invalid request type
                    response = invalidRequestType(request);
                }
            }

            //response
            try {
                Json.getInstance().writeValue(outputStream,response);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Fail to response to " + s1.getRemoteSocketAddress());
                closeSocket(s1,inputStream,outputStream);
                System.out.flush();
                return;
            }
        }

    }

    private Response query(Request request){
        Word word = request.getWord();
        assert Dictionary.getInstance() != null;
        Status status = Dictionary.getInstance().query(word);
        return new Response(request.getLogicalTime(),status,word);
    }

    private Response add(Request request){
        Word word = request.getWord();
        assert Dictionary.getInstance() != null;
        Status status = Dictionary.getInstance().add(word);
        return new Response(request.getLogicalTime(),status,word);
    }

    private Response remove(Request request){
        Word word = request.getWord();
        assert Dictionary.getInstance() != null;
        Status status = Dictionary.getInstance().remove(word);
        return new Response(request.getLogicalTime(),status,word);
    }

    private Response update(Request request){
        Word word = request.getWord();
        assert Dictionary.getInstance() != null;
        Status status = Dictionary.getInstance().update(word);
        return new Response(request.getLogicalTime(),status,word);
    }

    private Response invalidRequest(){
        return new Response(0,Status.INVALID_FORMAT,null);
    }

    private Response invalidRequestType(Request request){
        return new Response(request.getLogicalTime(),Status.INVALID_REQUEST_TYPE,request.getWord());
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

    @Override
    public String toString() {
        return "RequestHandler{" +
                "connect to" + s1.getRemoteSocketAddress() +
                '}';
    }
}
