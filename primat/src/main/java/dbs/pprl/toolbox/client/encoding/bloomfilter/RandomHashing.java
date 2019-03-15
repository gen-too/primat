package dbs.pprl.toolbox.client.encoding.bloomfilter;


import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public class RandomHashing extends HashingMethod {

	public static final String DEFAULT_KEY = "A_KEY";
	
	private final String key;
		
	public RandomHashing(int bfSize){
		this(bfSize, DEFAULT_KEY);
	}
	
	public RandomHashing(int bfSize, String key){
		super(bfSize);
		this.key = key;
	}
	
	@Override
	public Set<Integer> hash(String element, int hashFunctions) {
		final String saltedInput = element + this.salt;
		final Random rnd = getRandomNumberGenerator(saltedInput, this.key);
		
		final Set<Integer> positions = new HashSet<>();
		for (int i = 0; i < hashFunctions; i++){
			final int pos = rnd.nextInt(this.bfSize);
			positions.add(pos);
		}
		
		return positions;
	}	
	
	private static Random getRandomNumberGenerator(String token, String key){		
		try {
			long hashedToken = HashUtils.getHmacSHA256(token, key);
			return new Random(hashedToken);
		} 
		catch (Exception e) {
			return new Random(42);
		}		
		
	}
}