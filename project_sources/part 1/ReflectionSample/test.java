import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// normal flow
		MyClass myClass = new MyClass(0);
		myClass.increase(2);
		System.out.println("Normal -> " + myClass);

		// reflection flow
		try {
			Constructor<MyClass> constructor = MyClass.class.getConstructor(int.class); //obtain a specific constructor of the class
			MyClass myClassReflect = (MyClass) constructor.newInstance(10); // create a new instances of object using the specific constructor 
			Method method = MyClass.class.getMethod("increase", int.class); // obtain a method of the class
			method.invoke(myClassReflect, 5); //invoke the method to a object
			Field field = MyClass.class.getDeclaredField("count"); // obtain a field of the class
			field.setAccessible(true);
			System.out.println("myClassReflect -> " + field.get(myClassReflect)); //get the value of the object's field
			field.set(myClassReflect, 17);// modify the value of the object's field 
			System.out.println("After change field:myClassReflect -> " + field.get(myClassReflect)); 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//create a new instances By classname, parametertypes, and args
		String classname = MyClass.class.getName();
		Class<?> parametertypes = int.class;
		Object[] arg = new Object[]{10};
		
		Object myClassReflect1 = createByName(classname, parametertypes, arg);
		System.out.println("Create myClassReflect1->"+myClassReflect1);
		//obtain the class information by object
		Class<?> c = myClassReflect1.getClass();
		try {
			// obtain a method of the class
			Method method = c.getMethod("increase", parametertypes);
			//invoke the method to a object
			method.invoke(myClassReflect1, 5);
			System.out.println("Increase myClassReflect1->"+myClassReflect1);
			
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public static Object createByName(String classname, Class<?> parametertypes, Object[] args){
		try {
    		//obtain the class information by class name
			Class<?> c = Class.forName(classname);
			//obtain the class constructor by parametertypes
			Constructor<?> constructor = c.getConstructor(parametertypes);
			//create a new Instance by invoke the constructor with args
			Object o = constructor.newInstance(args);
			return o;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
}
