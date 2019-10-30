package dbs.pprl.toolbox.data_owner.lisard;

public class DatasetConfiguration {
	
	int[][] records =
		{   
				// A, B, C
		// A	
				{8, 1, 1},
		// B	
				{10, 0, 0},
		// C
				{3, 1, 1}
		};
	
	private int sourceIndipendentRecords;
	private double familyRate;
	
	public DatasetConfiguration(int sourceIndipendentRecords, double familyRate) {
		this.sourceIndipendentRecords = sourceIndipendentRecords;
		this.familyRate = familyRate;
	}

	public int getSourceIndipendentRecords() {
		return sourceIndipendentRecords;
	}

	public void setSourceIndipendentRecords(int sourceIndipendentRecords) {
		this.sourceIndipendentRecords = sourceIndipendentRecords;
	}

	public double getFamilyRate() {
		return familyRate;
	}

	public void setFamilyRate(double familyRate) {
		this.familyRate = familyRate;
	}

	
}
