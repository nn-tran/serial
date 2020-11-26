

import java.util.*;
import javax.json.*;
import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.*;
import java.nio.charset.StandardCharsets;

/**
 * CPSC 501 
 * JSON serializer
 *
 * @author Nguyen Tran, based on code by Jonathan Hudson
 */
public class Serializer {

	/**
	 * serialize an object into a JSON object
	 * @param object the original Java object
	 * @return serialized object
	 * @throws Exception with no error handling
	 */
	public static JsonObject serializeObject(Object object) throws Exception {
		JsonArrayBuilder object_list = Json.createArrayBuilder();
		serializeHelper(object, object_list, new IdentityHashMap<Object, String>());
		JsonObjectBuilder json_base_object = Json.createObjectBuilder();
		json_base_object.add("objects", object_list);
		return json_base_object.build();
	}

	/**
	 * builds JSON object from source object
	 * @param source original Java object
	 * @param object_list list of JSON-ified objects
	 * @param object_tracking_map identifier map
	 * @throws Exception with no error handling
	 */
	private static void serializeHelper(Object source, JsonArrayBuilder object_list, Map<Object, String> object_tracking_map) throws Exception {
		if (object_tracking_map.get(source) != null) {
			return;
		}
		String object_id = Integer.toString(object_tracking_map.size());
		object_tracking_map.put(source, object_id);
		JsonObjectBuilder object_info = Json.createObjectBuilder();
		object_info.add("id", object_id);
		
		Class object_class = source.getClass();
		object_info.add("class", object_class.getName());
		if (object_class.isArray()) {
			arrayHandler(object_info, source, object_class, object_list, object_tracking_map);
		} else {
			objectHandler(object_info, source, object_class, object_list, object_tracking_map);
		}
		
		
		object_list.add(object_info);
	}
	
	/**
	 * handles array object
	 * @param object_info the current JSON builder
	 * @param source original object
	 * @param object_class original object class
	 * @param object_list list of all objects referenced
	 * @param object_tracking_map identifier map
	 * @throws Exception with no error handling
	 */
	private static void arrayHandler(JsonObjectBuilder object_info, Object source, Class object_class, JsonArrayBuilder object_list, Map<Object, String> object_tracking_map) throws Exception{
		object_info.add("type", "array");
		int len = Array.getLength(source);
		object_info.add("length",  len + "");
		JsonArrayBuilder array = Json.createArrayBuilder();
		for (int i = 0; i < len; ++i) {
			Object value = Array.get(source, i);
			JsonObjectBuilder element = Json.createObjectBuilder();
			if (object_class.getComponentType().isPrimitive()) {
				element.add("value", value.toString());
			} else if (value == null) {
				element.add("reference", "null");
			} else if (value != null) {
				serializeHelper(value, object_list, object_tracking_map);
				element.add("reference", object_tracking_map.get(value));
			}
			array.add(element);
		}
		object_info.add("entries", array);
	}
	
	/**
	 * handles non-array object
	 * @param object_info the current JSON builder
	 * @param source original object
	 * @param object_class original object class
	 * @param object_list list of all objects referenced
	 * @param object_tracking_map identifier map
	 * @throws Exception
	 */
	private static void objectHandler(JsonObjectBuilder object_info, Object source, Class object_class, JsonArrayBuilder object_list, Map<Object, String> object_tracking_map) throws Exception{
		object_info.add("type", "object");
		Field[] fields = object_class.getDeclaredFields();
		JsonArrayBuilder fieldsJson = Json.createArrayBuilder();
		Field.setAccessible(fields, true);
		for (Field f : fields) {
			if (Modifier.isStatic(f.getModifiers())) continue;
			JsonObjectBuilder field = Json.createObjectBuilder();
			field.add("name", f.getName());
			field.add("declaringclass", f.getDeclaringClass().getName());
			
			Object value = f.get(source);
			
			if (f.getType().isPrimitive()) {
				field.add("value", value.toString());
			} else if (value == null) {
				field.add("reference", "null");
			} else {
				serializeHelper(value, object_list, object_tracking_map);
				field.add("reference", object_tracking_map.get(value));
			}
			fieldsJson.add(field);
			
		}
		object_info.add("fields", fieldsJson);
	}
	
	/**
	 * create objects for testing
	 * @param input the input stream to be used for parameters, is usually System.in
	 * @return an array of all objects created
	 */
	static Object[] createObj(InputStream input) {
		System.out.println("Choose option 1-5 (refer to assignment description):");
		Scanner sc = new Scanner(input);
		int mode = sc.nextInt();
		Object[] output = null;
		switch (mode) {
			case 1://creating a simple object
				System.out.println("Simple object, enter bool, int, double:");
				boolean b = sc.nextBoolean();
				int i = sc.nextInt();
				double d = sc.nextDouble();
				Object simObj = new SimpleObject(b,i,d);
				output = new Object[1];
				output[0] = simObj;
				break;
			case 2://creating 2 objects referencing each other
				System.out.println("Object with reference, enter 2 integers for 2 objects:");
				Object ref1 = new RefObject(sc.nextInt());
				Object ref2 = new RefObject(sc.nextInt());
				((RefObject) ref1).setParent((RefObject) ref2);
				((RefObject) ref2).setParent((RefObject) ref1);
				output = new Object[2];
				output[0] = ref1;
				output[1] = ref2;
				break;
			case 3://create an array of primitives
				System.out.println("Object with array, enter array length:");
				Object arrayObj = new ArrayObject(sc.nextInt());
				output = new Object[1];
				output[0] = arrayObj;
				break;
			case 4://create an array of references, initialize 2 at index 5 and 7
				System.out.println("Object with array of references, enter array length:");
				Object refArrayObj = new RefArrayObject(sc.nextInt());
				if (((RefArrayObject) refArrayObj).getSimArray().length > 7) {
					((RefArrayObject) refArrayObj).setArrayElement(new SimpleObject(), 5);
					((RefArrayObject) refArrayObj).setArrayElement(new SimpleObject(true, 1, 70.0), 7);
				}
				output = new Object[1];
				output[0] = refArrayObj;
				break;
			case 5://create a collection (arrayList)
				System.out.println("Object with collection, enter collection size:");
				Object collectionObj = new CollectionObject(sc.nextInt());
				output = new Object[1];
				output[0] = collectionObj;
				break;
		}
		return output;
	} 
	
	
	public static void main(String[] args) throws Exception {
		/* testing code, reading from string and write to file test.json
		String tester = "5\n10";
		InputStream stream = new ByteArrayInputStream(tester.getBytes(StandardCharsets.UTF_8));
		DataOutputStream dout = new DataOutputStream(new FileOutputStream("test.json"));
		JsonObject object = Serializer.serializeObject(createObj(stream)[0]);
		JsonWriter writer = Json.createWriter(dout);
		writer.writeObject(object);
		*/
		InputStream stream = System.in;
		try{
			//System.out.println("broadcasting JSON...");
			ServerSocket ss = new ServerSocket(8192);
			Socket s = ss.accept();//establishes connection
			DataOutputStream dout = new DataOutputStream(s.getOutputStream());
			JsonObject object = Serializer.serializeObject(createObj(stream)[0]);
			JsonWriter writer = Json.createWriter(dout);
			writer.writeObject(object);
			writer.close();
			dout.flush();
			dout.close();
			s.close();
			ss.close();
		} catch (Exception e){
			e.printStackTrace();
		}
		
	}  
}