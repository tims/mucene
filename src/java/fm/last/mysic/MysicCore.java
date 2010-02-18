package fm.last.mysic;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.store.Directory;

import fm.last.mysic.schema.Schema;

public class MysicCore {
  private Directory index;
  private Analyzer analyzer;
  private Schema schema;

  /**
   * Create a mysic core with a directory, eg RAMDirectory.
   * 
   * @param index
   */
  public MysicCore(Directory index, Analyzer analyzer, Schema schema) {
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
