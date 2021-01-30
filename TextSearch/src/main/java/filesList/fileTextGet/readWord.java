package filesList.fileTextGet;

import com.spire.doc.Document;

public class readWord {
  public static String getText(String path){
    Document doc = new Document();
    doc.loadFromFile(path);
    //获取文本保存为String
    String result = doc.getText();
    return result;
  }

  public static void main(String[] args) {
    //System.out.println(getText("src/main/resources/20162017学年（二）物理实验笔试试卷（参考答案） new.docx"));
    System.out.println(getText("src/main/resources/20162017学年（一）物理实验笔试试卷（参考答案）.doc"));
  }
}
