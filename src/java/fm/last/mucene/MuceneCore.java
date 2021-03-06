package fm.last.mucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.store.Directory;

import fm.last.mucene.schema.Schema;

public class MuceneCore {
  private Directory index;
  private Analyzer analyzer;
  private Schema schema;

  /**
   * Create a mysic core with a directory, eg RAMDirectory.
   * 
   * @param index
   */
  public MuceneCore(Directory index, Analyzer analyzer, Schema schema) {
    super();
    this.index = index;
    this.analyzer = analyzer;
    this.schema = schema;
  }

  public Analyzer getAnalyzer() {
    return analyzer;
  }

  public Schema getSchema() {
    return schema;
  }
}
