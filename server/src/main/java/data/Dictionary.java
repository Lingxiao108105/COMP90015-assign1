package data;

import common.enums.Status;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Dictionary
 * contains all the words in a ConcurrentHashMap
 *
 * @author lingxiao li 1031146
 */
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


    /**
     * query a word in the dictionary
     * @param word word to search
     * @return status of query
     */
    public Status query(Word word){
        word.setMeanings(null);
        word.setMeanings(map.get(word.getSpell()));
        if(word.getMeanings() == null){
            return Status.NOTFOUND;
        }
        return Status.SUCCESS;
    }

    /**
     * add a word to the dictionary
     * will also store the dictionary to local file
     * @param word word to add
     * @return status of add
     */
    public Status add(Word word){
        if(map.containsKey(word.getSpell())){
            return Status.DUPLICATE;
        }
        if(word.getMeanings() == null || word.getMeanings().getMeaningMap().isEmpty()){
            return Status.EMPTY_MEANING;
        }
        synchronized (key) {
            removeEmptyMeaning(word.getMeanings());
            map.put(word.getSpell(),word.getMeanings());
            LocalSave.getInstance().saveToFile(map);
        }
        return Status.SUCCESS;

    }

    /**
     * remove a word from the dictionary
     * will also store the dictionary to local file
     * @param word word to remove
     * @return status of remove
     */
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

    /**
     * update a word in the dictionary
     * will also store the dictionary to local file
     * @param word word to update
     * @return status of update
     */
    public Status update(Word word){
        //sanity check
        if(word.getMeanings() == null || word.getMeanings().getMeaningMap().isEmpty()){
            return Status.EMPTY_MEANING;
        }
        synchronized (key) {
            if(map.containsKey(word.getSpell())){
                removeEmptyMeaning(word.getMeanings());
                map.replace(word.getSpell(),word.getMeanings());
                LocalSave.getInstance().saveToFile(map);
                return Status.SUCCESS;
            }
        }
        return Status.NOTFOUND;
    }

    /**
     * remove empty meaning
     * @param meanings meanings to iterate
     */
    public void removeEmptyMeaning(Meanings meanings){
        //sanity check
        if(meanings == null || meanings.getMeaningMap() == null || meanings.getMeaningMap().isEmpty()){
            return;
        }
        meanings.getMeaningMap().entrySet().removeIf(entry -> entry.getKey().isBlank() || entry.getValue().isBlank());
    }


}
