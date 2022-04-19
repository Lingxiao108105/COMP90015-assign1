package edu.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * single meaning of a word
 * use to represent data in table for javafx
 *
 * @author lingxiao li 1031146
 */
public class Meaning {

    private String POS;
    private String meaning;

    public Meaning(String POS, String meaning) {
        this.POS = POS;
        this.meaning = meaning;
    }

    public String getPOS() {
        return POS;
    }

    public void setPOS(String POS) {
        this.POS = POS;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    /**
     * convert from map to list of meanings for table in javafx to display
     * @param map map to convert
     * @return list of meanings
     */
    public static List<Meaning> fromMap(Map<String,String> map){
        List<Meaning> meaningList = new ArrayList<>();
        for (Map.Entry<String,String> entry: map.entrySet()){
            meaningList.add(new Meaning(entry.getKey(),entry.getValue()));
        }
        return meaningList;
    }

    /**
     * convert from list of meanings to Meanings of word
     * @param meaningList list of single meaning
     * @return Meanings of word for transmission
     */
    public static Meanings listToMeanings(List<Meaning> meaningList){
        HashMap<String, String> map = new HashMap<>();
        for (Meaning m: meaningList){
            map.put(m.getPOS(),m.getMeaning());
        }
        return new Meanings(map);
    }

    @Override
    public String toString() {
        return "Meaning{" +
                "POS='" + POS + '\'' +
                ", meaning='" + meaning + '\'' +
                '}';
    }
}
