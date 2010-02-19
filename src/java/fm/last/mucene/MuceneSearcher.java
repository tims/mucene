package fm.last.mucene;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;

import com.google.common.collect.Lists;

public class MuceneSearcher {
  private static Logger log = Logger.getLogger(MuceneSearcher.class);

  private RAMDirectory index = null;
  private IndexWriter writer = null;

  public void init() throws IOException {
    if (index != null) {
      return;
    }
    index = new RAMDirectory();
    writer = new IndexWriter(index, new StandardAnalyzer(), MaxFieldLength.UNLIMITED);
    // writer.addDocument(doc);
  }

  @Override
  public void put(Map<String, String> fields) throws IOException {
    init();

    for (Block b : blocks) {
      Document doc = new Document();
      for (fm.last.bobbie.Field f : b.getFields()) {
        Field field = new Field(f.getName(), f.getValue(), Store.YES, Index.ANALYZED);
        doc.add(field);
      }
      writer.addDocument(doc);
    }
    writer.commit();
    log.info("num ram docs: " + writer.numRamDocs());
    log.info("num docs: " + writer.numDocs());

    for (Block b : blocks) {
      for (fm.last.bobbie.Field f : b.getFields()) {
        log.info("querying for: " + f.getName() + "=" + f.getValue());
        IndexSearcher searcher = new IndexSearcher(index);
        QueryParser queryParser = new QueryParser(f.getName(), new StandardAnalyzer());
        Query query;
        try {
          query = queryParser.parse(f.getValue());
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
    }

    log.info("size in bytes: " + index.sizeInBytes());
  }

  @Override
  public String getName() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setName(String name) {
    // TODO Auto-generated method stub

  }
}
