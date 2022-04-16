package server;

import common.utils.Json;

import java.io.IOException;
import java.io.InputStream;
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
        try {
            inputStream = s1.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        while (true){
            Request request = null;
            try {
                request = Json.getInstance().readValue(inputStream, Request.class);
            } catch (IOException e) {
                e.printStackTrace();
                //response with msg invalid format
                continue;
            }
            if(request != null && request.getRequestType() != null){
                if(request.getRequestType() == RequestType.QUERY){
                    query(request);
                }
                else if(request.getRequestType() == RequestType.ADD){
                    add(request);
                }
                else if(request.getRequestType() == RequestType.REMOVE){
                    remove(request);
                }
                else if(request.getRequestType() == RequestType.UPDATE){
                    update(request);
                }
                else {
                    //response with msg invalid request type
                }
            }
        }

    }

    private void query(Request request){


    }

    private void add(Request request){


    }

    private void remove(Request request){


    }

    private void update(Request request){


    }
}
