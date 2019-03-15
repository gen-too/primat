package dbs.pprl.toolbox.client.lisard.lookup;

/**
 * An implementation of the alias method implemented using Vose's algorithm.
 * The alias method allows for efficient sampling of random values from a
 * discrete probability distribution (i.e. rolling a loaded die) in O(1) time
 * each after O(n) preprocessing time.
 */
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import java.util.Random;
import java.util.ArrayList;
import java.util.Deque;
import java.util.ArrayDeque;

public final class FrequencyLookup extends IndipendentBasicRandomLookup {

	private final List<String> values;
	
	/* The probability and alias tables. */
	private final int[] alias;
	private final double[] probability;

	/**
	 * Constructs a new AliasMethod to sample from a discrete distribution and
	 * hand back outcomes based on the probability distribution.
	 * <p>
	 * Given as input a list of probabilities corresponding to outcomes 0, 1,
	 * ..., n - 1, along with the random number generator that should be used
	 * as the underlying generator, this constructor creates the probability 
	 * and alias tables needed to efficiently sample from this distribution.
	 *
	 * @param values The list of values
	 * @param probabilities The list of probabilities corresponding to the values.
	 */
	private FrequencyLookup(List<String> values, List<Double> probabilities) {
		super();
		this.values = values;

		final List<Double> probs = new ArrayList<Double>(probabilities);

		/* Allocate space for the probability and alias tables. */
		this.probability = new double[probs.size()];
		this.alias = new int[probs.size()];

		/* Compute the average probability and cache it for later use. */
		final double average = 1.0 / probs.size();

		/* Create two stacks to act as worklists as we populate the tables. */
		final Deque<Integer> small = new ArrayDeque<Integer>();
		final Deque<Integer> large = new ArrayDeque<Integer>();

		/* Populate the stacks with the input probabilities. */
		for (int i = 0; i < probs.size(); ++i) {
			/* If the probability is below the average probability, then we add
			 * it to the small list; otherwise we add it to the large list.
			 */
			if (probs.get(i) >= average) {
				large.add(i);
			}
			else {
				small.add(i);
			}
		}

		/* As a note: in the mathematical specification of the algorithm, we
		 * will always exhaust the small list before the big list.  However,
		 * due to floating point inaccuracies, this is not necessarily true.
		 * Consequently, this inner loop (which tries to pair small and large
		 * elements) will have to check that both lists aren't empty.
		 */
		while (!small.isEmpty() && !large.isEmpty()) {
			/* Get the index of the small and the large probabilities. */
			final int less = small.removeLast();
			final int more = large.removeLast();

			/* These probabilities have not yet been scaled up to be such that
			 * 1/n is given weight 1.0.  We do this here instead.
			 */
			this.probability[less] = probs.get(less) * probs.size();
			this.alias[less] = more;

			/* Decrease the probability of the larger one by the appropriate amount. */
			probs.set(more, (probs.get(more) + probs.get(less)) - average);

			/* If the new probability is less than the average, add it into the
			 * small list; otherwise add it to the large list.
			 */
			if (probs.get(more) >= 1.0 / probs.size()) {
				large.add(more);
			}
			else {
				small.add(more);
			}
		}

		/* At this point, everything is in one list, which means that the
		 * remaining probabilities should all be 1/n.  Based on this, set them
		 * appropriately.  Due to numerical issues, we can't be sure which
		 * stack will hold the entries, so we empty both.
		 */
		while (!small.isEmpty()) {
			this.probability[small.removeLast()] = 1.0;
		}

		while (!large.isEmpty()) {
			this.probability[large.removeLast()] = 1.0;
		}
	}
	
	public static class FrequencyLookupBuilder{
		
		private List<String> values;
		private List<Double> probabilities;
		
		private static void checkInput(List<String> values, List<Double> probabilities) {
			if (probabilities == null || values == null) {
				throw new NullPointerException(); 
			}
			if (probabilities.size() == 0 || values.size() == 0) {
				throw new IllegalArgumentException("Input must be nonempty.");
			}
			if (probabilities.size() != values.size()) {
				throw new IllegalArgumentException("Wrong input.");
			}
			
			final double sum = probabilities.stream().mapToDouble(Double::doubleValue).sum();
			if (sum != 1.0d) {
				throw new IllegalArgumentException("Sum of frequencies is not 1!");
			}
		}
		
		private static List<Double> normalizeProbabilities(List<Double> probabilities) {
			final long sum = probabilities.stream().mapToLong(Number::longValue).sum();
			
		    final List<Double> normalizedProbabilities = new ArrayList<Double>(probabilities.size());
			
			for (final Number prob : probabilities) {
				normalizedProbabilities.add(prob.doubleValue() / sum);
			}
			
			return normalizedProbabilities;
		}
				
		public FrequencyLookupBuilder(List<String> values, List<Double> probabilities) {
			this(values, probabilities, false);
		}
		
		public FrequencyLookupBuilder(List<String> values, List<Double> probabilities, boolean isNormalized) {
			this.values = values;
			
			if (isNormalized) {
				this.probabilities = probabilities;
			}
			else {
				this.probabilities = normalizeProbabilities(probabilities);
			}
			
		}
			
		public FrequencyLookupBuilder(Map<String, Double> valueProbMap) {
			this(valueProbMap, false);
		}
		
		public FrequencyLookupBuilder(Map<String, Double> valueProbMap, boolean isNormalized) {
			final List<String> values = new ArrayList<String>(valueProbMap.size());
			final List<Double> probs = new ArrayList<Double>(valueProbMap.size());
		
			for (final Entry<String, ? extends Number> entry : valueProbMap.entrySet()) {
				values.add(entry.getKey());
				probs.add(entry.getValue().doubleValue());
			}
			this.values = values;
			
			if (isNormalized) {
				this.probabilities = probs;
			}
			else {
				this.probabilities = normalizeProbabilities(probabilities);
			}
			
		}
		
		public FrequencyLookup build() {
			checkInput(this.values, this.probabilities);
			return new FrequencyLookup(this.values, this.probabilities);
		}
	}
		
	
	public List<String> getLookupValues(){
		return this.values;
	}
	
	/**
	 * Samples a value from the underlying distribution.
	 *
	 * @return A random value sampled from the underlying distribution.
	 */
	@Override
	protected String getRandomValue(Random rnd) {
		/* Generate a fair die roll to determine which column to inspect. */
		final int column = rnd.nextInt(probability.length);

		/* Generate a biased coin toss to determine which option to pick. */
		final boolean coinToss = rnd.nextDouble() < probability[column];

		/* Based on the outcome, return either the column or its alias. */
		final int index = coinToss ? column : alias[column];

		return this.values.get(index);
	}
	
	
	public static void main(String args[]) {
		final List<String> val = new ArrayList<>();
		val.add("Bernd");
		val.add("Anna");
		val.add("Quark");
		
		final List<Double> probs = new ArrayList<>();
		probs.add(9.0/15);
		probs.add(4.0/15);
		probs.add(2.0/15);
		
		
		final FrequencyLookup dic = new FrequencyLookup(val, probs);
		
		int anna = 0;
		int bernd = 0;
		
		for (int i = 0; i < 100; i++) {
			dic.setSeed(i);
			final String v = dic.getValue();
			if (v.equals("Anna")){
				anna++;
			}
			else if (v.equals("Bernd")){
				bernd++;
			}
		}
		
		System.out.println("Anna:" + anna);
		System.out.println("Bernd:" + bernd);
	}
}