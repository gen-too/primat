package dbs.pprl.toolbox.data_owner.lisard.lookup;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IntegerLookup extends SimpleRandomLookup{

	private static List<String> getValues(int start, int end){
		return IntStream
				.rangeClosed(start, end)
				.boxed()
				.map(String::valueOf)
				.collect(Collectors.toList());
	}
	
	public IntegerLookup(int start, int end) {
		super(getValues(start,end));
	}
}