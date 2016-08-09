package  com.ap.misc.serialization;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonTypeConverter {
	
	protected ObjectMapper mapper;
	
	@Autowired
	public JsonTypeConverter(ObjectMapper objectMapper) {
		this.mapper = objectMapper;
	}
	
	public String convert(JsonNode value) {
		String val = null;
		try {
			val =  mapper.writeValueAsString(value);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return val;
	}

	public <T> T convert(String value, Class<T> type) {
		Object data = null;
		try {
			data = this.mapper.readValue(value, type);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (T) data;
    }
}
