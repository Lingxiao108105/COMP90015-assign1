import common.utils.CustomThreadPool;
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

        //set max thread number in thread pool
        if(args.length >= 3){
            try{
                int maxWorkerCount = Integer.parseInt(args[2]);
                if(maxWorkerCount > 0){
                    CustomThreadPool.setMaxWorkerCount(maxWorkerCount);
                }
                else {
                    System.out.println("Please enter positive number for <maxWorkerCount> as third argument!");
                }
            }
            catch (Exception e){
                System.out.println("Please enter valid <maxWorkerCount> as third argument!");
            }
        }

        //start the server
        Server.start(args);

    }
}
