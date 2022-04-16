package Data;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.utils.Json;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

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

    public ConcurrentHashMap<String,String> readFromFile(){
        ObjectMapper mapper = Json.getInstance();
        ConcurrentHashMap<String, String> dictionary = null;

        //read the dictionary
        try {
            synchronized (key) {
                dictionary = mapper.readValue(file, ConcurrentHashMap.class);
            }
        }
        catch (FileNotFoundException e){
            dictionary = new ConcurrentHashMap<>();
            System.out.println("Local save do not exist!");
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return dictionary;
    }

    public void saveToFile(ConcurrentHashMap<String, String> dictionary){
        //sanity check
        if(dictionary == null){
            return;
        }

        //delete the local save if dictionary is empty
        synchronized (key) {
            if(dictionary.isEmpty() && file.exists()){
                if(!file.delete()){
                    System.out.println("Fail to delete the file");
                }
                return;
            }
        }

        //save the dictionary
        ObjectMapper mapper = Json.getInstance();
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
