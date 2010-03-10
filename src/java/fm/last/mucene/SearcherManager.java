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
import org.apache.lucene.store.RAMDirectory;

public class SearcherManager {
  private static Logger log = Logger.getLogger(SearcherManager.class);

  private IndexManager indexManager;

  public SearchManager(IndexManager indexManager){
    this.indexManager = indexManager;
  }
  
  public MuceneSearcher getSearcher(int userid){
    // TODO: Caching.
    Directory index = indexManager.getIndex(userid);
    return new MuceneSearcher(index);
  }
}

