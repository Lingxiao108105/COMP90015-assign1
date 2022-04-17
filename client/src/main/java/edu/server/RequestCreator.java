package edu.server;

import edu.data.Meanings;
import edu.data.Word;

public class RequestCreator {

    public static Request QueryRequest(String spell){
        return new Request(0,RequestType.QUERY, new Word(spell,null));
    }

    public static Request AddRequest(String spell){
        return new Request(0,RequestType.ADD, new Word(spell,null));
    }

    public static Request RemoveRequest(String spell){
        return new Request(0,RequestType.REMOVE, new Word(spell,null));
    }

    public static Request UpdateRequest(String spell, Meanings meaning){
        return new Request(0,RequestType.UPDATE, new Word(spell,meaning));
    }
}
