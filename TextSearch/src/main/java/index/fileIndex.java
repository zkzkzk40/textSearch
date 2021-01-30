package index;

import filesList.fileTextGet.getText;
import filesList.indexGet.fileList;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import javax.swing.text.Highlighter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class fileIndex {
  String path,index="index",text="text";
  Map<String, String> searchPaths=new HashMap<>();
  private Document document;
  private Directory directory;
  private Analyzer analyzer;
  private IndexWriterConfig indexWriterConfig;
  private IndexWriter indexWriter;
  private IndexSearcher indexSearcher;
  private IndexReader reader;
  public fileIndex(String Path)  {
    path=Path;
    document=new Document();
    try {
      directory=FSDirectory.open(Paths.get(path));
      analyzer=new IKAnalyzer();
      indexWriterConfig=new IndexWriterConfig(analyzer);
      indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
      indexWriter=new IndexWriter(directory, indexWriterConfig);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  public void fileClose()throws IOException{
    indexWriter.close();
  }
  public void addData(String dataIndex, String dataText) throws IOException{
    document=new Document();
    document.add(new TextField(index,dataIndex, Field.Store.YES));
    document.add(new TextField(text,dataText, Field.Store.YES));
    indexWriter.addDocument(document);
  }
  void readFileIndex(String item) throws IOException{
    // 索引目录对象
    directory = FSDirectory.open(Paths.get(path));
    // 索引读取工具
    reader = DirectoryReader.open(directory);
    // 索引搜索工具
    indexSearcher = new IndexSearcher(reader);

    // 创建查询解析器,两个参数：默认要查询的字段的名称，分词器
    QueryParser parser = new QueryParser(index, new IKAnalyzer());
    // 创建查询对象
    Query query = null;
    try {
      query = parser.parse(item);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    // 搜索数据,两个参数：查询条件对象要查询的最大结果条数
    // 返回的结果是 按照匹配度排名得分前N名的文档信息（包含查询到的总条数信息、所有符合条件的文档的编号信息）。
    TopDocs topDocs = indexSearcher.search(query, 10);
    // 获取总条数
    System.out.println("本次搜索共找到" + topDocs.totalHits + "条数据");
    // 获取得分文档对象（ScoreDoc）数组.SocreDoc中包含：文档的编号、文档的得分
    ScoreDoc[] scoreDocs = topDocs.scoreDocs;
    for (ScoreDoc scoreDoc : scoreDocs) {
      // 取出文档编号
      int docID = scoreDoc.doc;
      // 根据编号去找文档
      Document doc = reader.document(docID);
      System.out.println(index+": " + doc.get(index));
      System.out.println(text+": " + doc.get(text));
      // 取出文档得分
      System.out.println("得分： " + scoreDoc.score);
    }
  }
  public void readFileText(String item)throws IOException{
    Map<String, String> result = new HashMap<>();
    // 索引目录对象
    directory = FSDirectory.open(Paths.get(path));
    // 索引读取工具
    reader = DirectoryReader.open(directory);
    // 索引搜索工具
    indexSearcher = new IndexSearcher(reader);
    // 创建查询解析器,两个参数：默认要查询的字段的名称，分词器
    QueryParser parser = new QueryParser(text, new IKAnalyzer());
    // 创建查询对象
    Query query = null;
    try {
      query = parser.parse(item);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    // 搜索数据,两个参数：查询条件对象要查询的最大结果条数
    // 返回的结果是 按照匹配度排名得分前N名的文档信息（包含查询到的总条数信息、所有符合条件的文档的编号信息）。
    TopDocs topDocs = indexSearcher.search(query, 100000);
    // 获取总条数
    //System.out.println("本次搜索共找到" + topDocs.totalHits + "条数据");
    // 获取得分文档对象（ScoreDoc）数组.SocreDoc中包含：文档的编号、文档的得分
    ScoreDoc[] scoreDocs = topDocs.scoreDocs;
    for (ScoreDoc scoreDoc : scoreDocs) {
      // 取出文档编号
      int docID = scoreDoc.doc;
      // 根据编号去找文档
      Document doc = reader.document(docID);
      //System.out.println(pa);
      searchPaths.put(doc.get(index),doc.get(text));
    }
  }
  public  Map<String, String> getSearchPaths(){
    return searchPaths;
  }
  public void search(Query query) throws Exception {
    searchPaths=new HashMap<>();
    // 索引目录对象
    directory = FSDirectory.open(Paths.get(path));
    // 索引读取工具
    reader = DirectoryReader.open(directory);
    // 索引搜索工具
    indexSearcher = new IndexSearcher(reader);

    // 搜索数据,两个参数：查询条件对象要查询的最大结果条数
    // 返回的结果是 按照匹配度排名得分前N名的文档信息（包含查询到的总条数信息、所有符合条件的文档的编号信息）。
    TopDocs topDocs = indexSearcher.search(query,1000000);
    // 获取总条数
    // 获取得分文档对象（ScoreDoc）数组.SocreDoc中包含：文档的编号、文档的得分
    ScoreDoc[] scoreDocs = topDocs.scoreDocs;

    for (ScoreDoc scoreDoc : scoreDocs) {
      // 取出文档编号
      int docID = scoreDoc.doc;
      // 根据编号去找文档
      Document doc = reader.document(docID);
      searchPaths.put(doc.get(index),doc.get(text));
    }
  }
  public void termQuery(String item) throws Exception {
    Query query=new TermQuery(new Term(text,item));
    search(query);
  }
  public void WildCardQuery(String item) throws Exception{
    Query query=new WildcardQuery(new Term(text,item));
    search(query);
  }
  public void FuzzyQuery(String item) throws Exception {
    Query query=new FuzzyQuery(new Term(text,item));
    search(query);
  }
  public static void main(String[] args) {
    try {
      fileIndex fileIndex=new fileIndex(".\\indexDir");
//      fileList fileList=new fileList("D:\\desktop\\test");
//      ArrayList<String> myfiles=fileList.getFiles();
//      for(String s:myfiles){
//        fileIndex.addData(s, getText.read(s) );
//      }
//      fileIndex.fileClose();
      fileIndex.readFileText("御宇多年求不得");
      Map<String, String> map=new HashMap<>();
      map=fileIndex.getSearchPaths();
      for(Map.Entry<String, String> entry :map.entrySet()){
        System.out.println(entry.getKey());
      }

    } catch (IOException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
