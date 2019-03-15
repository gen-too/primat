package dbs.pprl.toolbox.client.encoding;

import java.util.List;

import dbs.pprl.toolbox.client.data.records.Record;
import dbs.pprl.toolbox.client.data.records.EncodedRecord;

public interface Encoder {

	public List<EncodedRecord> encode(List<Record> records);
}
