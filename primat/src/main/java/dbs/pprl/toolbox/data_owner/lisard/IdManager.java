package dbs.pprl.toolbox.data_owner.lisard;


public final class IdManager {
	
	public static final String ORIGINAL = "org";
	public static final String DUPLICATE = "dup";
	
	private long recordNumber;
	private long clusterNumber;
	private long familyNumber;
	
	public IdManager(){
		this.recordNumber = 0;
		this.clusterNumber = 0;
		this.familyNumber = 0;
	}
	
	public long getCurrentRecordNumber(){
		return this.recordNumber;
	}
	
	public long getCurrentFamilyNumber(){
		return this.familyNumber;
	}
	
	public long getCurrentClusterNumber(){
		return this.clusterNumber;
	}
	
	public static String generateId(String orgDup, long recordId, long clusterId, long familyId, char origin, char dataOwner){
		return RecordId.from(orgDup, recordId, clusterId, familyId, origin, dataOwner).toString();
	}
	
	public static RecordId generateRecordId(String orgDup, long recordId, long clusterId, long familyId, char origin, char dataOwner){
		return RecordId.from(orgDup, recordId, clusterId, familyId, origin, dataOwner);
	}
	
	public String generateDuplicateRecordId(long clusterNumber, long familyNumber, char origin, char dataOwner){
		this.recordNumber++;
//		System.out.println(this.recordNumber);
		return generateId(DUPLICATE, this.recordNumber, clusterNumber, familyNumber, origin, dataOwner);
	}
	
	public String generateDuplicateRecordId(RecordId id, char dataOwner) {
		return this.generateDuplicateRecordId(Long.valueOf(id.getClusterId()), Long.valueOf(id.getFamilyId()), id.getOrigin().charAt(0), dataOwner);
	}
	
	public String getFamilyRecordId(char origin){
		this.recordNumber++;
		this.clusterNumber++;
		
		return generateId(ORIGINAL, this.recordNumber, this.clusterNumber, this.familyNumber, origin, origin);
	}
	
	public String getIndipendentRecordId(char origin){
		this.recordNumber++;
		this.clusterNumber++;
		this.familyNumber++;
		
		return generateId(ORIGINAL, this.recordNumber, this.clusterNumber, this.familyNumber, origin, origin);
	}
}