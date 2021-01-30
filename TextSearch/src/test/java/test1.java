import filesList.fileTextGet.readExcel;
public class test1 {
  public static void main(String[] args) {
    String path="src/main/resources/大二排考表.xlsx";
    System.out.println(readExcel.getText(path));
  }

}
