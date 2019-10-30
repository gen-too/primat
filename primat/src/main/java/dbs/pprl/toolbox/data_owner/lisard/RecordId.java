package dbs.pprl.toolbox.data_owner.lisard;

import java.text.DecimalFormat;
import java.util.StringJoiner;


public class RecordId {

	public static final String ID_COMPONENT_SEPARATOR = "-";	
	public static final DecimalFormat ID_FORMAT = new DecimalFormat("000000000");

	private String orgDup;
	private String recordId;
	private String clusterId;
	private String familyId;
	private String origin;
	private String dataOwner;
	
	public RecordId(String orgDup, String recordId, String clusterId, String familyId, String origin, String dataOwner){
		this.orgDup = orgDup;
		this.recordId = recordId;
		this.clusterId = clusterId;
		this.familyId = familyId;
		this.origin = origin;
		this.dataOwner = dataOwner;
	}
	
	public static RecordId from(String orgDup, long recordId, long clusterId, long familyId, char origin, char dataOwner){
		return 
			new RecordId(
				orgDup,
				ID_FORMAT.format(recordId),
				ID_FORMAT.format(clusterId),
				ID_FORMAT.format(familyId),
				String.valueOf(origin),
				String.valueOf(dataOwner)
			);
	}

	public static RecordId from(String string){
		final String[] parts = string.split(ID_COMPONENT_SEPARATOR);
		
		if (parts.length != 6){
			throw new RuntimeException("Wrong Id Format!");
		}
		else {
			return new RecordId(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
		}
	}
		
	public String getOrgDup() {
		return orgDup;
	}

	public String getRecordId() {
		return recordId;
	}

	public String getClusterId() {
		return clusterId;
	}

	public String getFamilyId() {
		return familyId;
	}

	public String getOrigin() {
		return origin;
	}

	public String getDataOwner() {
		return dataOwner;
	}

	public void setOrgDup(String orgDup) {
		this.orgDup = orgDup;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}

	public void setFamilyId(String familyId) {
		this.familyId = familyId;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public void setDataOwner(String dataOwner) {
		this.dataOwner = dataOwner;
	}

	@Override
	public String toString(){
		final StringJoiner id = new StringJoiner(ID_COMPONENT_SEPARATOR);
		id.add(this.orgDup);
		id.add(this.recordId);
		id.add(this.clusterId);
		id.add(this.familyId);
		id.add(this.origin);
		id.add(this.dataOwner);
		return id.toString();	
	}
}
