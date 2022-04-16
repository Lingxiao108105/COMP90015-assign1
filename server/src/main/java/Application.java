import Data.LocalSave;

import java.util.concurrent.ConcurrentHashMap;

public class Application {
    public static void main(String[] args) {

        LocalSave.initialize(args[1]);
        LocalSave save = LocalSave.getInstance();
        ConcurrentHashMap<String, String> dictionary = save.readFromFile();


    }
}
