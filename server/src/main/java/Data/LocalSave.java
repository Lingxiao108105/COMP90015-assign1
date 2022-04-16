package Data;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.utils.Json;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LocalSave {

    private static String path = null;
    private static File file = null;
    private static LocalSave localSave = null;
    private final static Object key = new Object();

    public static void initialize(String path){
        if(LocalSave.path == null){
            LocalSave.path = path;
            LocalSave.file = new File(LocalSave.path);
            localSave = new LocalSave();
        }
    }

    public static LocalSave getInstance(){
        return LocalSave.localSave;
    }

    public Map<String,String> readFromFile(){
        ObjectMapper mapper = Json.getMapper();
        Map<String, String> dictionary = null;

        //read the dictionary
        try {
            synchronized (key) {
                dictionary = mapper.readValue(file, HashMap.class);
            }
        }
        catch (FileNotFoundException e){
            dictionary = new HashMap<>();
            System.out.println("Local save do not exist!");
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return dictionary;
    }

    public void saveToFile(Map<String, String> dictionary){
        //sanity check
        if(dictionary == null){
            return;
        }

        //delete the local save if dictionary is empty
        synchronized (key) {
            if(dictionary.isEmpty()){
                file.delete();
                return;
            }
        }

        //save the dictionary
        ObjectMapper mapper = Json.getMapper();
        try {
            synchronized (key) {
                 mapper.writeValue(file, dictionary);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }



}
