package edu.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Meanings {

    private HashMap<String,String> meaningMap = null;

    @JsonCreator
    public Meanings(@JsonProperty("meaningMap")HashMap meaningMap) {
        this.meaningMap = meaningMap;
    }

    //merge new meanings into old meanings, will replace old meanings
    public void merge(Meanings newMeanings){
        //sanity check
        if(newMeanings == null || newMeanings.meaningMap == null){
            return;
        }
        if(this.meaningMap == null){
            return;
        }
        if(this.meaningMap.isEmpty()){
            this.meaningMap = newMeanings.getMeaningMap();
            return;
        }

        //merge new
        HashMap<String,String> newMap = newMeanings.getMeaningMap();
        for(String key: newMap.keySet()){
            this.getMeaningMap().put(key,newMap.get(key));
        }
    }

    public HashMap<String, String> getMeaningMap() {
        return meaningMap;
    }

    public void setMeaningMap(HashMap<String, String> meaningMap) {
        this.meaningMap = meaningMap;
    }

    @Override
    public String toString() {
        return "Meanings{" +
                "meaningMap=" + meaningMap +
                '}';
    }
}
