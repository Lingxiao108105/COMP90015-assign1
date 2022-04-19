import data.Dictionary;
import data.LocalSave;
import data.Meanings;
import server.Server;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lingxiao li 1031146
 */
public class Application {
    public static void main(String[] args) {

        //sanity check
        if(args.length < 2){
            System.out.println("Please enter <server-port> as first argument, <file path> as second argument!");
            return;
        }
        //initialize the dictionary
        LocalSave.initialize(args[1]);
        LocalSave save = LocalSave.getInstance();
        ConcurrentHashMap<String, Meanings> dictionary = save.readFromFile();
        Dictionary.initialize(dictionary);

        //start the server
        Server.start(args);

    }
}
