package fm.last.mucene;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;

import fm.last.mucene.schema.FieldDescriptor;

public class MuceneIndexer {
  private IndexWriter writer;
  private MuceneCore core;

  // = new IndexWriter(index, new StandardAnalyzer(), MaxFieldLength.UNLIMITED);

  // TODO: this should be in a wrapper class called MysicDocument?
  // enforce type checks and get stored and analysed settings from the schema.
  public void addDocument(Map<String, String> fields) throws IOException, UnknownFieldException {

    Document doc = new Document();
    for (Entry<String, String> entry : fields.entrySet()) {
      FieldDescriptor fieldDesc = core.getSchema().get(entry.getKey());
      if (fieldDesc == null) {
        throw new UnknownFieldException();
      }
      Store stored = fieldDesc.isStored() ? Store.YES : Store.NO;
      Index indexed = fieldDesc.isIndexed() ? Index.ANALYZED : Index.NO;
      Field field = new Field(fieldDesc.getName(), entry.getValue(), stored, indexed);
      doc.add(field);
    }
    writer.addDocument(doc);
    writer.commit();
  }
}
