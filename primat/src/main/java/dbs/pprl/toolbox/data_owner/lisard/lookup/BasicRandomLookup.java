package dbs.pprl.toolbox.data_owner.lisard.lookup;

import java.util.Random;

public abstract class BasicRandomLookup implements RandomLookup{

	protected Long seed;

	protected BasicRandomLookup() {
		this(42L);
	}
	
	protected BasicRandomLookup(Long seed) {
		this.seed = seed;
	}
	
	@Override
	public String getValue() {
		if (this.seed == null) {
			throw new RuntimeException("No seed was set!");
		}
		else {
			final Random rnd = new Random(this.seed);
			final String res = this.getRandomValue(rnd);
			this.seed = rnd.nextLong();
			return res;
		}
	}
	
	protected abstract String getRandomValue(Random rnd);
	
	@Override
	public Long setSeed(long seed) {
		final Long oldSeed = this.seed;
		this.seed = seed;
		return oldSeed;
	}
}