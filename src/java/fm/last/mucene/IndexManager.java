package fm.last.mucene;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

// TODO: this should be an interface. We could have a FileIndexManager, CouchdbIndexManager, CassandraIndexManager.
public class IndexManager {
  private static Logger log = Logger.getLogger(SearcherManager.class);

  public IndexManager() {
    
    this.indexManager = indexManager;
  }
  
  public Directory getIndex(int userid){
    Directory index = new FileDirectory("somepath/"+userid)
    return index;
    // resource should be managed? 
    // can a filedirectory be opened by two searchers at a time? 
    // I think it can, since they are always read only.
    // Couch db might be nice for the versioning actually...
  }
}

