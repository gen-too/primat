package dbs.pprl.toolbox.data_owner.encoding;

import java.util.List;

import dbs.pprl.toolbox.data_owner.data.records.EncodedRecord;
import dbs.pprl.toolbox.data_owner.data.records.Record;

public interface Encoder {

	public List<EncodedRecord> encode(List<Record> records);
}
