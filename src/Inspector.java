import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * CPSC 501
 * Inspector class
 *
 * @author Nguyen Tran
 */

public class Inspector {

	/**
	 * inspect object
	 * @param obj		object inspected
	 * @param recursive	recursion status
	 */
	public void inspect(Object obj, boolean recursive) {
		Class c = obj.getClass();
		inspectClass(c, obj, recursive, 0);
	}

	/**
	 * inspect class of an object
	 * @param c			class of object
	 * @param obj		object inspected
	 * @param recursive	recursion status
	 * @param depth		current depth, used for recursion
	 */
	private void inspectClass(Class c, Object obj, boolean recursive, int depth) {
		String tab = indent(depth);
		System.out.println(tab + "CLASS");
		System.out.println(tab + "class: " + c.getName());
		fieldHandler(c, obj, recursive, depth, tab);
		
	}

	/**
	 * finds and prints array details
	 * @param c			class of object
	 * @param obj		object inspected
	 * @param tab		the indentation for printing
	 */
	private void arrayHandler(Class c, Object obj, String tab) {
		if (c.isArray()) {
			int len = Array.getLength(obj);
			System.out.println(tab + "Component type: " + c.getComponentType());
			System.out.println(tab + "Length: " + len);
			System.out.println(tab + "Values->");
			for (int i = 0; i < len; ++i) {
				Object value = Array.get(obj, i);
				System.out.println(tab + " " + value);
				
			}
		}
	}
	

	/**
	 * finds and prints field details
	 * will recursively inspect fields if recursive == true
	 * @param c			class of object
	 * @param obj		object inspected
	 * @param recursive	recursion status
	 * @param depth		current depth, used for recursion
	 * @param tab		the indentation for printing
	 */
	private void fieldHandler(Class c, Object obj, boolean recursive, int depth, String tab) {
		Field[] fields = c.getDeclaredFields();
		Field.setAccessible(fields, true);
		System.out.print(tab + " Fields->");
		if (fields.length == 0) {
			System.out.println(" NONE");
		} else {
			System.out.println();
			for (Field f : fields) {
				System.out.print(printField(f, tab));
				try {
					Object value = f.get(obj);
					if (value.getClass().isArray()) {
						arrayHandler(value.getClass(), value, tab + "   ");
					
					} else {
						System.out.println(tab + "   Value: " + value);
						if (recursive && value != null && !f.getType().isPrimitive()) {
							System.out.println(tab + "    -> Recursively Inspect");
							inspectClass(value.getClass(), value, recursive, depth+1);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * make indentation based on depth
	 * @param depth	current recursion depth
	 * @return		the indent string
	 */
	String indent(int depth) {
		String res = "";
		for (int i = 0; i < depth; ++i) {
			res += "\t";
		}
		return res;
	}
	
	/**
	 * make a printable string for a field
	 * does not handle recursion
	 * @param f		field to print
	 * @param tab	indentation
	 * @return		the printable string
	 */
	String printField(Field f, String tab) {
		String output = "";
		output += tab + "  FIELD\n";
		output += tab + "   Name: "+ f.getName() + "\n";
		output += tab + "   Type: "+ f.getType() + "\n";
		return output;
	}

}