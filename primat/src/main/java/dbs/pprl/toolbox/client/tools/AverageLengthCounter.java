package dbs.pprl.toolbox.client.tools;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import dbs.pprl.toolbox.client.data.attributes.Attribute;
import dbs.pprl.toolbox.client.data.records.Record;
import dbs.pprl.toolbox.client.preprocessing.preprocessors.Preprocessor;

public class AverageLengthCounter implements Preprocessor{

	@Override
	public void apply(List<Record> records, LinkedList<String> header) {
		final Map<Integer, DescriptiveStatistics> statMap = new LinkedHashMap<>();
		
		if (header != null && header.size() > 0){
			for (int i = 0; i < header.size(); i++){
				statMap.put(i, new DescriptiveStatistics());
			}
		}
		
		for (final Record rec : records){
			final List<Attribute<?>> attributes = rec.getAttributes();
			final ListIterator<Attribute<?>> it = attributes.listIterator();
			
			while(it.hasNext()){
				final int index = it.nextIndex();
				final Attribute<?> attr = it.next();
				final int valueLength = attr.getStringValue().length();
				statMap.get(index).addValue(valueLength);
			}
		}
		
		final List<Double> avgLengths = new ArrayList<>();
		for (final DescriptiveStatistics stat : statMap.values()){
			avgLengths.add(stat.getMean());
		}
		
		System.out.println(avgLengths);
	}
	
}