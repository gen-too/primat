package dbs.pprl.toolbox.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import dbs.pprl.toolbox.client.common.StringConverter;

public class ClassNameObjectConverter {

	public static Object getObject(String dir, String className, String... params) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException{
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
						initargs[i] = StringConverter.convert(params[i], paramType);
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