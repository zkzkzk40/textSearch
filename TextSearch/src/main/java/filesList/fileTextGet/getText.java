package filesList.fileTextGet;

public class getText {
  public static String read(String path){
    if(path.endsWith(".txt")) {
      return ReadTXT.getTXT(path);
    }
    else if(path.endsWith(".doc")||path.endsWith(".docx")){
      return readWord.getText(path);
    }
    else if(path.endsWith(".pdf")){
      return readPDF.getText(path);
    }
    else if(path.endsWith(".pptx")||path.endsWith(".ppt")){
      return readPPT.getText(path);
    }
    else if(path.endsWith(".xlsx")||path.endsWith(".xls")){
      return readExcel.getText(path);
    }
    else {
      return "";
    }
  }

  public static void main(String[] args) {
    System.out.println(read("src/main/resources/test/论语.doc"));

  }
}
