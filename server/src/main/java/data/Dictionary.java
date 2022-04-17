package data;


import common.enums.Status;

import java.util.concurrent.ConcurrentHashMap;

public class Dictionary {

    private static ConcurrentHashMap<String,Meanings> map = null;
    private static final Dictionary dictionary = new Dictionary();
    private final static Object key = new Object();

    public static void initialize(ConcurrentHashMap<String,Meanings> map){
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
        word.setMeanings(null);
        word.setMeanings(map.get(word.getSpell()));
        if(word.getMeanings() == null){
            return Status.NOTFOUND;
        }
        return Status.SUCCESS;
    }

    public Status add(Word word){
        if(map.containsKey(word.getSpell())){
            return Status.DUPLICATE;
        }
        synchronized (key) {
            map.put(word.getSpell(),word.getMeanings());
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
                Meanings oldMeaning = map.get(word.getSpell());
                oldMeaning.merge(word.getMeanings());
                LocalSave.getInstance().saveToFile(map);
                return Status.SUCCESS;
            }
        }
        return Status.NOTFOUND;
    }


}
