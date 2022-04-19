package data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.utils.Json;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * save dictionary to file and read dictionary from file
 *
 * @author lingxiao li 1031146
 */
public class LocalSave {

    private static String path = null;
    private static File file = null;
    private final static String tempPath = "temp.json";
    private static File temp = null;
    private static LocalSave localSave = null;
    private final static Object key = new Object();

    /**
     * initialize the local save
     * @param path path to read and save the dictionary
     */
    public static void initialize(String path){
        if(LocalSave.path == null){
            LocalSave.path = path;
            LocalSave.file = new File(LocalSave.path);
            LocalSave.temp = new File(LocalSave.tempPath);
            localSave = new LocalSave();
        }
    }

    public static LocalSave getInstance(){
        return LocalSave.localSave;
    }

    /**
     * read dictionary from map
     * @return dictionary
     */
    public ConcurrentHashMap<String,Meanings> readFromFile() {
        ObjectMapper mapper = Json.getInstance();
        ConcurrentHashMap<String, Meanings> dictionary = null;

        //read the dictionary
        try {
            synchronized (key) {
                dictionary = mapper.readValue(file, new TypeReference<ConcurrentHashMap<String,Meanings>>(){});
            }
        }
        catch (FileNotFoundException e){
            System.out.println("Local save do not exist!");
        }
        catch (IOException e) {
            System.out.println("Fail to read from file! Will start with an empty dictionary");
        }

        //try to read from temp file
        if(dictionary == null){
            //read the dictionary
            try {
                synchronized (key) {
                    dictionary = mapper.readValue(temp, new TypeReference<ConcurrentHashMap<String,Meanings>>(){});
                }
            } catch (Exception e){
                dictionary = new ConcurrentHashMap<String,Meanings>();
            }
        }

        saveToFile(dictionary);
        return dictionary;
    }

    /**
     * save dictionary to local file
     * will save to temp file first
     * @param dictionary dictionary to save
     */
    public void saveToFile(ConcurrentHashMap<String, Meanings> dictionary){
        //sanity check
        if(dictionary == null){
            return;
        }
        //save the dictionary
        ObjectMapper mapper = Json.getInstance();
        try {
            synchronized (key) {
                mapper.writeValue(temp, dictionary);
                mapper.writeValue(file, dictionary);
            }
        }
        catch (IOException e) {
            System.out.println("Fail to save to path: " + path);
            System.out.println("Please enter valid path!");
            System.exit(-1);
        }
    }



}
