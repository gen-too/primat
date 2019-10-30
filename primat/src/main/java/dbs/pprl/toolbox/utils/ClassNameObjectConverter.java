package dbs.pprl.toolbox.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Class for dynamically creating objects with given class name and arguments.
 * 
 * @author mfranke
 *
 */
public class ClassNameObjectConverter {

	/**
	 * Creates an object of given class.
	 * 
	 * @param dir Package to search for class name.
	 * @param className Name of the class.
	 * @param params Arguments passed to the constructor.
	 * @return An instance of the specified class with the given arguments.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws ClassNotFoundException
	 */
	public static Object getObject(String dir, String className, String... params) 
		throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException{
		
		final Class<?> clazz = Class.forName(dir + "." + className);
		
		final int arguments = params.length;
		
		if (arguments == 0){
			return clazz.newInstance();
		}
		else{
			final Constructor<?>[] ctors = clazz.getConstructors();
			
			for (final Constructor<?> ctor : ctors){
				final Class<?>[] ctorTypes = ctor.getParameterTypes();
				final int ctorArgs = ctorTypes.length;

				if (ctorArgs != arguments){
					continue;
				}
				else{
					final Object[] initargs = new Object[ctorTypes.length];
					for (int i = 0; i < initargs.length; i++){
						final Class<?> paramType = ctorTypes[i];
						initargs[i] = StringToObjectConverter.convert(params[i], paramType);
					}
					try{
						return ctor.newInstance(initargs);
					}
					catch(Exception e){
						continue;
					}
				}
			}
		}
		throw new InstantiationException();
	}	
}