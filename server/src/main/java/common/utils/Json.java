package common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Json {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static ObjectMapper getMapper(){
        return mapper;
    }



}
