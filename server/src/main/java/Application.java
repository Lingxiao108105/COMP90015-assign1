import Data.LocalSave;

import java.util.Map;

public class Application {
    public static void main(String[] args) {

        LocalSave.initialize(args[1]);
        LocalSave save = LocalSave.getInstance();
        Map<String, String> dictionary = save.readFromFile();


    }
}
