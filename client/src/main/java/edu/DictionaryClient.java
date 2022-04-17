package edu;

import edu.data.Word;
import com.fasterxml.jackson.core.JsonParser;
import edu.common.utils.Json;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import edu.server.Request;
import edu.server.RequestType;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class DictionaryClient extends Application {

    public static void main(String[] args) throws IOException {

        launch();

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

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(DictionaryClient.class.getResource("view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}
