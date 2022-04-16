import Data.Word;
import com.fasterxml.jackson.core.JsonParser;
import common.utils.Json;
import server.Request;
import server.RequestType;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Application {

    public static void main(String[] args) throws IOException {
        Socket s1 = new Socket(args[0], Integer.parseInt(args[1]));

        OutputStream s1out = s1.getOutputStream();
        Request request = new Request(1, RequestType.ADD,new Word("hello","greetings"));
        while (true){
            Json.getInstance().configure(JsonParser.Feature.AUTO_CLOSE_SOURCE,false);
            Json.getInstance().writeValue(s1out,request);
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
