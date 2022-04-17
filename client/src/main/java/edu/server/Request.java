package edu.server;

import edu.data.Word;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Request {

    private int logicalTime;
    private RequestType requestType;
    private Word word;

    @JsonCreator
    public Request(@JsonProperty("logicalTime")int logicalTime,
                   @JsonProperty("requestType")RequestType requestType,
                   @JsonProperty("word")Word word) {
        this.logicalTime = logicalTime;
        this.requestType = requestType;
        this.word = word;
    }

    public int getLogicalTime() {
        return logicalTime;
    }

    public void setLogicalTime(int logicalTime) {
        this.logicalTime = logicalTime;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }

    @Override
    public String toString() {
        return "Request{" +
                "logicalTime=" + logicalTime +
                ", requestType=" + requestType +
                ", word=" + word +
                '}';
    }
}
