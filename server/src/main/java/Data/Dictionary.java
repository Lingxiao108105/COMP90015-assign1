package Data;


import common.enums.Status;

import java.util.concurrent.ConcurrentHashMap;

public class Dictionary {

    private static ConcurrentHashMap<String,String> map = null;
    private static final Dictionary dictionary = new Dictionary();
    private final static Object key = new Object();

    public static void initialize(ConcurrentHashMap<String,String> map){
        if(Dictionary.map == null){
            Dictionary.map = map;
        }
    }

    public static Dictionary getInstance(){
        if(map == null){
            return null;
        }
        return dictionary;
    }


    public Status query(Word word){
        word.setMeaning(null);
        word.setMeaning(map.get(word.getSpell()));
        if(word.getMeaning() == null){
            return Status.NOTFOUND;
        }
        return Status.SUCCESS;
    }

    public Status add(Word word){
        if(map.containsKey(word.getSpell())){
            return Status.DUPLICATE;
        }
        synchronized (key) {
            map.put(word.getSpell(),word.getMeaning());
            LocalSave.getInstance().saveToFile(map);
        }
        return Status.SUCCESS;

    }

    public Status remove(Word word){
        synchronized (key) {
            if(map.containsKey(word.getSpell())){
                map.remove(word.getSpell());
                LocalSave.getInstance().saveToFile(map);
                return Status.SUCCESS;
            }
        }
        return Status.NOTFOUND;

    }

    public Status update(Word word){
        synchronized (key) {
            if(map.containsKey(word.getSpell())){
                map.put(word.getSpell(),word.getMeaning());
                LocalSave.getInstance().saveToFile(map);
                return Status.SUCCESS;
            }
        }
        return Status.NOTFOUND;
    }


}
