import common.utils.Json;
import data.Dictionary;
import data.LocalSave;
import data.Meanings;
import data.Word;
import server.Server;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class Application {
    public static void main(String[] args) throws IOException {

        if(args.length < 2){
            System.out.println("Please enter <server-address> as first argument, <server-port> as second argument!");
            return;
        }
        //initialize the dictionary
        LocalSave.initialize(args[1]);
        LocalSave save = LocalSave.getInstance();
        ConcurrentHashMap<String, Meanings> dictionary = save.readFromFile();
        Dictionary.initialize(dictionary);

        Server.start(args);

    }
}
