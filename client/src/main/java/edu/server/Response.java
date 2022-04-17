package edu.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.common.enums.Status;
import edu.data.Word;

public class Response {

    private int logicalTime;
    private Status status;
    private Word word;

    @JsonCreator
    public Response(@JsonProperty("logicalTime")int logicalTime,
                    @JsonProperty("status")Status status,
                    @JsonProperty("word")Word word) {
        this.logicalTime = logicalTime;
        this.status = status;
        this.word = word;
    }

    public int getLogicalTime() {
        return logicalTime;
    }

    public void setLogicalTime(int logicalTime) {
        this.logicalTime = logicalTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }

    @Override
    public String toString() {
        return "Response{" +
                "logicalTime=" + logicalTime +
                ", status=" + status +
                ", word=" + word +
                '}';
    }
}
