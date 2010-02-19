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

public class MuceneSearcher {
  private static Logger log = Logger.getLogger(MuceneSearcher.class);

  private RAMDirectory index = null;

  public void init() throws IOException {
    if (index != null) {
      return;
    }
    index = new RAMDirectory();
  }

  public void search(List<String> queries) throws IOException {
    for (String queryString : queries) {
      log.info("querying for: " + queryString);
      IndexSearcher searcher = new IndexSearcher(index);
      // TODO: queryParser should be created with fields to query over.
      QueryParser queryParser = new QueryParser(null, new StandardAnalyzer());
      Query query;
      try {
        query = queryParser.parse(queryString);
      } catch (ParseException e) {
        throw new IOException(e);
      }
      TopDocs topDocs = searcher.search(query, 10);
      log.info("hits: " + topDocs.totalHits);
      for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
        Document doc = searcher.doc(scoreDoc.doc);
        log.info("doc found:" + scoreDoc.doc + " score:" + scoreDoc.score);
        for (Object field : doc.getFields()) {
          log.info("Field: " + field.toString());
        }
      }
    }
    log.info("size in bytes: " + index.sizeInBytes());
  }
}
