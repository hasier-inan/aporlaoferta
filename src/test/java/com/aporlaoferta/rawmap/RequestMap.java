package com.aporlaoferta.rawmap;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hasiermetal on 29/01/15.
 */
public class RequestMap {

    public static Map<String, String> getMapFromJsonString(String response) throws java.io.IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response, new TypeReference<HashMap<String, String>>() {
        });
    }

    public static String getJsonFromMap(Object map) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, true);
        return mapper.writeValueAsString(map);
    }
}
