package dbs.pprl.toolbox.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SetUtils {

	public static <T> Set<T> getDuplicates(List<T> elementList){
		final Set<T> singletons = new HashSet<T>(elementList.size());
		final Set<T> duplicates = new HashSet<T>();
		
		for (final T element : elementList) {
			final boolean isDuplicate = !singletons.add(element);
			if (isDuplicate) {
				duplicates.add(element);
			}
		}
		return duplicates;
	}
	
	public static <T> Set<T> getSingletons(List<T> elementList){
		final Set<T> singletons = new HashSet<T>(elementList.size());
		final Set<T> duplicates = new HashSet<T>();
		
		for (final T element : elementList) {
			final boolean isDuplicate = !singletons.add(element);
			if (isDuplicate) {
				duplicates.add(element);
			}
		}
		return singletons;
	}
	
	public static <T> boolean hasDuplicates(List<T> elementList){
		final Set<T> singletons = new HashSet<T>(elementList.size());
		
		for (final T element : elementList) {
			final boolean isDuplicate = !singletons.add(element);
			if (isDuplicate) {
				return true;
			}
		}
		return false;
	}
}
