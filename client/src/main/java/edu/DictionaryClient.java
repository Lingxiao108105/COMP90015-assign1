package edu;

import edu.data.Meanings;
import edu.data.Word;
import edu.common.utils.Json;
import edu.server.Response;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import edu.server.Request;
import edu.server.RequestType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class DictionaryClient extends Application {

    public static void main(String[] args) throws IOException {
        launch();

        Socket s1 = new Socket("127.0.0.1" , 10000);

        OutputStream s1out = s1.getOutputStream();
        InputStream s1in = s1.getInputStream();
        Meanings meanings = new Meanings(null, "greetings", null, null, null, null, null, null);
        Request request = new Request(1, RequestType.ADD,new Word("hello",meanings));
        Json.getInstance().writeValue(s1out,request);
        Response response = Json.getInstance().readValue(s1in, Response.class);
        System.out.println(response);

        Json.getInstance().writeValue(s1out,request);
        response = Json.getInstance().readValue(s1in, Response.class);
        System.out.println(response);

        s1.close();
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
