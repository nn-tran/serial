

import java.lang.reflect.*;
import java.util.*;
import javax.json.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

/**
 * CPSC 501 
 * JSON deserializer
 *
 * @author Nguyen Tran, based on code by Jonathan Hudson
 */
public class Deserializer {

	/**
	 * deserialize a JSON object
	 * @param json_object the object to deserialize
	 * @return the Java object created, with any other referenced objects
	 * @throws Exception with no error handling
	 */
	public static Object deserializeObject(JsonObject json_object) throws Exception {
		JsonArray object_list = json_object.getJsonArray("objects");
		Map object_tracking_map = new HashMap();
		createInstances(object_tracking_map, object_list);
		fillInstances(object_tracking_map, object_list);
		return object_tracking_map.get("0");
	}

	/**
	 * create object instances required
	 * @param object_tracking_map identifier map
	 * @param object_list list of JSON objects to create instances of
	 * @throws Exception with no error handling
	 */
	private static void createInstances(Map object_tracking_map, JsonArray object_list) throws Exception {
		for (int i = 0; i < object_list.size(); ++i) {
			JsonObject object_info = object_list.getJsonObject(i);
			Class object_class = Class.forName(object_info.getString("class"));
			Object object_instance;
			
			if (object_class.isArray()) {
				object_instance = Array.newInstance(object_class.getComponentType(), Integer.parseInt(object_info.getString("length")));
			} else {
				Constructor constructor = object_class.getDeclaredConstructor();
				constructor.setAccessible(true);
				object_instance = constructor.newInstance();
			}
			
			
			//Make object
			object_tracking_map.put(object_info.getString("id"), object_instance);
		}
	}

	/**
	 * change fields and link references of object instances
	 * @param object_tracking_map identifier map
	 * @param object_list list of JSON objects to complete
	 * @throws Exception with no error handling
	 */
	private static void fillInstances(Map object_tracking_map, JsonArray object_list) throws Exception {
		for (int i = 0; i < object_list.size(); ++i) {
			JsonObject object_info = object_list.getJsonObject(i);
			Class object_class = Class.forName(object_info.getString("class"));
			Object object_instance = object_tracking_map.get(object_info.getString("id"));
			if (object_class.isArray()){
				JsonArray object_entries = object_info.getJsonArray("entries");
				for (int j = 0; j < object_entries.size(); ++j) {
					JsonObject element = object_entries.getJsonObject(j);
					if (element.getString("value", "") != "") {
						Array.set(object_instance, j, convert(object_class.getComponentType(), element.getString("value")));
					} else if (element.getString("reference", "") != ""){
						Array.set(object_instance, j, object_tracking_map.get(element.getString("reference")));
					}
					System.out.println(Array.get(object_instance, j) + "");
				}
			} else {
				JsonArray object_fields = object_info.getJsonArray("fields");
				for (int j = 0; j < object_fields.size(); ++j) {
					JsonObject jField = object_fields.getJsonObject(j);
					Field f = object_class.getDeclaredField(jField.getString("name"));
					f.setAccessible(true);
					if (jField.getString("value", "") != "") {
						f.set(object_instance, convert(f.getType(), jField.getString("value")));
					} else if (jField.getString("reference", "") != "" 
							|| jField.getString("reference", "") != "null"){
						f.set(object_instance, object_tracking_map.get(jField.getString("reference")));
					}
					
				}
			}
			
		}
	}
	
	/**
	 * convert string to appropriate primitive type
	 * @param type resulting class
	 * @param value string representation of the value
	 * @return object of the correct type
	 * @throws Exception with no error handling
	 */
	private static Object convert(Class type, String value) throws Exception {
		Object ret = null;
		if (type == boolean.class) {
			ret = Boolean.parseBoolean(value);
		} else if (type == byte.class) {
			ret = Byte.parseByte(value);
		} else if (type == short.class) {
			ret = Short.parseShort(value);
		} else if (type == char.class) {
			ret = value.charAt(0);
		} else if (type == int.class) {
			ret = Integer.parseInt(value);
		} else if (type == long.class) {
			ret = Long.parseLong(value);
		} else if (type == float.class) {
			ret = Float.parseFloat(value);
		} else {
			ret = Double.parseDouble(value);
		}
		return ret;
	}

	public static void main(String[] args){  
		try {  
			System.out.println("listening for JSON...");
			Socket s = new Socket("localhost",8192);
			//BufferedReader d = new BufferedReader(new FileReader("test.json"));//testing code, read from test.json instead of through the network
			BufferedReader d = new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
			JsonReader jReader = Json.createReader(s.getInputStream());
			Object obj = Deserializer.deserializeObject(jReader.readObject());
			Inspector in = new Inspector();
			if (obj != null) {
				in.inspect(obj, false);
			} else {
				System.out.println("nullobject");
			}
		} catch (Exception e){
			e.printStackTrace(); 
		}
	}
		
}