package Data;


import common.enums.Status;
import server.Request;

import java.util.Map;

public class Dictionary {

    private static Map<String,String> map = null;
    private static final Dictionary dictionary = new Dictionary();

    public static void initialize(Map<String,String> map){
        if(Dictionary.map == null){
            Dictionary.map = map;
        }
    }

    public static Dictionary getInstance(){
        if(dictionary == null){
            return null;
        }
        return dictionary;
    }


    public Status query(Word word){
        if(map.containsKey(word.getSpell())){
            word.setMeaning(map.get(word.getSpell()));
            return Status.SUCCESS;
        }
        return Status.NOTFOUND;
    }

    public Status add(Word word){
        if(map.containsKey(word.getSpell())){
            return Status.DUPLICATE;
        }
        return Status.NOTFOUND;

    }

    public Status remove(Word word){


    }

    public Status update(Word word){


    }


}
