package UI;
import javax.swing.*;
import javax.swing.colorchooser.DefaultColorSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import filesList.fileTextGet.*;
import index.fileIndex;
import filesList.indexGet.fileList;
import fileOpen.fileOpen;


public class mainUI{
  JFrame f = new JFrame("检索本地内容");
  //组件添加-------------------------------------------------------------------------------------------------------
  //顶部 搜索框、搜索按钮、搜索类型
  // 新建索引
  JButton index = new JButton("新建索引");
  //搜索框
  JTextField SearchText = new JTextField(70);
  //搜索按钮
  JButton SearchButton = new JButton("查找");

  //搜索类型
  JComboBox<String> SearchType = new JComboBox<String>();
  //JTable的内容
  //定义行信息数据
  private Vector title = new Vector(2);
  //存储数据
  private Vector<Vector> data = new Vector<>();
  //查找模式 模糊 精确
  String selectModel;
  String keyWord;
  //文本域的内容
  //文本内容
  JTextArea TextContent= new JTextArea(40, 40);
  //给文本内容添加滚动条
  JScrollPane ScrollT = new JScrollPane(TextContent);
  //高亮
  Highlighter TextLighter = TextContent.getHighlighter();
  DefaultHighlighter.DefaultHighlightPainter p = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);
  boolean isHighlighted=true;
  Box topBox = Box.createHorizontalBox();
  Box t = Box.createVerticalBox();
  Box s = Box.createVerticalBox();
  fileIndex fI=new fileIndex(".\\indexDir");
  //highLightLenth 是高亮的文本的下标和长度
  Map<Integer, Integer> highLightLenth=new HashMap<>();
  //记录下标
  Vector<Integer> highLightIndex=new Vector<>();
  int nowIndex=1;
  Map<String, String> searchPaths;
  JCheckBox txt = new JCheckBox("TXT");
  JCheckBox word = new JCheckBox("WORD");
  JCheckBox pdf = new JCheckBox("PDF");
  JCheckBox ppt = new JCheckBox("PPT");
  JCheckBox excel = new JCheckBox("EXCEL");
  JCheckBox all = new JCheckBox("All");
  JTextField page = new JTextField(5);//页码
  JButton up = new JButton("↑");//上一页 ↑和↓
  JButton down = new JButton("↓");//下一页
  JButton highLight = new JButton("高亮");//高亮
  JButton colorChoose = new JButton("颜色");//颜色选择
  //fileFormatSelectionBox是五个文件格式选择的框所在的box
  Box fileFormatSelectionBox =Box.createHorizontalBox();
  //textContentSelectionBox是单击表格后出现的Box,用于快捷选择查找的文本
  Box textContentSelectionBox =Box.createHorizontalBox();
  public void init(){
    title.add("文件名");
    title.add("路径");
    SearchButton.setMnemonic(KeyEvent.VK_ENTER);
    DefaultTableModel model = new DefaultTableModel(data, title){
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };//设置不可编辑
    JTable SearchContent = new JTable(model);
    TableColumn column = SearchContent.getColumnModel().getColumn(0);
    column.setMinWidth(150);
    column.setMaxWidth(800);
    //给搜索内容添加滚动条
    JScrollPane ScrollS = new JScrollPane(SearchContent);
    //设置表格高度
    SearchContent.setRowHeight(25);
    //设置表格字体
    SearchContent.setFont(new Font("新罗马",Font.PLAIN,20));
    //设置表头字体
    SearchContent.getTableHeader().setFont(new Font("新罗马",Font.BOLD,22));
    //设置选中的背景颜色
    SearchContent.setSelectionBackground(Color.GRAY);
    s.add(fileFormatSelectionBox);
    s.add(ScrollS);

    SearchContent.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {//SearchContent添加鼠标双击事件，分别打开文件和文件目录
        int row = SearchContent.getSelectedRow();
        int col = SearchContent.getSelectedColumn();
        Object path=model.getValueAt(row,1);
        if(e.getClickCount()==2){
          if(col==0){
            //打开文件
            fileOpen.openFile((String) path);
          }
          else if(col==1){
            fileOpen.openDir((String) path);
          }
        }
        else if(e.getClickCount()==1){
          highLightIndex.clear();
          highLightLenth.clear();
          textContentSelectionBox.setVisible(true);
          TextContent.setText(searchPaths.get(path));
          //移除高亮
          TextLighter.removeAllHighlights();
          setHighlight(keyWord);
          if(!isHighlighted) {
            TextLighter.removeAllHighlights();
          }
          TextContent.setCaretPosition(highLightIndex.get(0));
          nowIndex = 1;
          page.setText(nowIndex + "/" + highLightIndex.size());
        }
      }
    });
    //组装顶部
    addTop();
    addBox2();
    //设置选择框响应
    setCheckBox();
    //搜索按钮事件响应
    SearchButton.addActionListener((e)->{
      //设置textContentSelectionBox不可见
      textContentSelectionBox.setVisible(false);
      //清空文本框内容
      TextContent.setText("");
      keyWord= SearchText.getText();
      if (keyWord.isEmpty()) {
        JOptionPane.showMessageDialog(f, "您未键入搜索内容!", "错误",
                JOptionPane.ERROR_MESSAGE);
        return;
      }
      else {
        try {
          fI.readFileText(keyWord);
          model.setRowCount(0);
          searchPaths=fI.getSearchPaths();
          if(searchPaths.size()==0){
            JOptionPane.showMessageDialog(f, "未查找到信息", "提示",
                    JOptionPane.ERROR_MESSAGE);
            return;
          }
          selectModel= (String) SearchType.getSelectedItem();
          if(selectModel.equals("精准搜索")) {
            for(Map.Entry<String, String> entry :searchPaths.entrySet()){
              if(isSelected(entry.getKey())&&entry.getValue().contains(keyWord)){
                Vector t = new Vector();
                t.add(getFileName(entry.getKey()));
                t.add(entry.getKey());
                data.add(t);
              }
            }
          }
          else if(selectModel.equals("模糊搜索")){
            for(Map.Entry<String, String> entry :searchPaths.entrySet()){
              if(isSelected(entry.getKey())){
                Vector t = new Vector();
                t.add(getFileName(entry.getKey()));
                t.add(entry.getKey());
                data.add(t);
              }
            }
          }
          } catch (Exception ioException) {
            ioException.printStackTrace();
          }
      }
      if(data.isEmpty()){
        JOptionPane.showMessageDialog(f, "未能查找到内容", "提示",
                JOptionPane.ERROR_MESSAGE);
      }
      SearchContent.validate();
      SearchContent.updateUI();
    });
    // 点击Index按钮，新建索引
    index.addActionListener((e)->{
      JFileChooser fd=new JFileChooser();
      fd.setCurrentDirectory(new File(".\\src/main/resources"));
      fd.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      fd.showOpenDialog(null);
      File f=fd.getSelectedFile();
      //获取此文件夹下的所有文件
      fileList fileList= null;
      try {
        fileList = new fileList(f.toString());
      } catch (IOException ioException) {
        ioException.printStackTrace();
      }
      ArrayList<String> arrayList=fileList.getFiles();
      JProgressBarDemo frame=new JProgressBarDemo(arrayList);
      //frame.setBounds(300,200,300,150);    //设置容器的大小
      frame.setVisible(true);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.pack();
    });
    //组装搜索内容和文本内容
    addSearchAndTextContent();
    setTextContentSelectionBoxEvent();
  }
  private void addBox2(){
    //组装
    //添加进fileFormatSelectionBox
    fileFormatSelectionBox.add(all);
    fileFormatSelectionBox.add(txt);
    fileFormatSelectionBox.add(word);
    fileFormatSelectionBox.add(ppt);
    fileFormatSelectionBox.add(pdf);
    fileFormatSelectionBox.add(excel);
    //添加进textContentSelectionBox
    textContentSelectionBox.setVisible(false);
    page.setPreferredSize(new Dimension(5,5));
    textContentSelectionBox.add(page);
    textContentSelectionBox.add(Box.createHorizontalStrut(30));
    textContentSelectionBox.add(up);
    textContentSelectionBox.add(down);
    textContentSelectionBox.add(Box.createHorizontalStrut(15));
    textContentSelectionBox.add(highLight);
    textContentSelectionBox.add(Box.createHorizontalStrut(10));
    textContentSelectionBox.add(colorChoose);
    textContentSelectionBox.add(Box.createHorizontalStrut(29));
    all.setSelected(true);
    txt.setSelected(true);
    word.setSelected(true);
    pdf.setSelected(true);
    ppt.setSelected(true);
    excel.setSelected(true);
    textContentSelectionBox.add(Box.createVerticalStrut(10));
    //设置字体
    all.setFont(new Font("新罗马",Font.BOLD,14));
    txt.setFont(new Font("新罗马",Font.BOLD,14));
    ppt.setFont(new Font("新罗马",Font.BOLD,14));
    pdf.setFont(new Font("新罗马",Font.BOLD,14));
    word.setFont(new Font("新罗马",Font.BOLD,14));
    excel.setFont(new Font("新罗马",Font.BOLD,14));
  }
  //寻找文本内容中的关键词，并给高亮
  private void setHighlight(String keys){
    highLightIndex.clear();
    highLightLenth.clear();
    try {
      String text = TextContent.getText().toLowerCase();
      int lenth=text.length();
      String key;
      if(selectModel.equals("模糊搜索")){
        for(int i=0;i<lenth;i++){
          key= String.valueOf(text.charAt(i));
          if(keys.contains(key)){
            //记录被高亮文本的位置和长度
            highLightIndex.add(i);
            highLightLenth.put(i,1);
            TextLighter.addHighlight(i,i+1,p);
          }
        }
      }
      else if(selectModel.equals("精准搜索")) {
        int keysLenth=keys.length();
        for(int i=0;i<lenth-keysLenth-1;i++){
          key= text.substring(i,i+keysLenth);
          if(keys.equals(key)){
            highLightIndex.add(i);
            highLightLenth.put(i,key.length());
            TextLighter.addHighlight(i,i+keysLenth,p);
          }
        }
      }
    }catch (BadLocationException e) {
      e.printStackTrace();
    }
  }
  private void addTop(){
    SearchType.addItem("模糊搜索");
    SearchType.addItem("精准搜索");
    SearchText.setFont(new Font("楷体",Font.BOLD,23));
    topBox.add(Box.createHorizontalStrut(10));
    topBox.add(index);
    topBox.add(Box.createHorizontalStrut(14));
    topBox.add(SearchText);
    topBox.add(Box.createHorizontalStrut(14));
    topBox.add(SearchButton);
    topBox.add(Box.createHorizontalStrut(10));
    topBox.add(SearchType);
    topBox.add(Box.createHorizontalStrut(10));
    f.add(topBox, BorderLayout.NORTH);
  }
  private void addSearchAndTextContent(){
    // 设置文本域的自动换行
    TextContent.setLineWrap(true);
    //激活换行不换字
    TextContent.setWrapStyleWord(true);
    TextContent.setFont(new Font("楷体",Font.BOLD,18));
    t.add(textContentSelectionBox);
    t.add(ScrollT);
    //添加进Frame
    f.add(s, BorderLayout.CENTER);
    f.add(t, BorderLayout.EAST);
    //default Setting
    f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    f.pack();
    f.setBounds(0,0,1000, 800);
    f.setVisible(true);
    f.setLocationRelativeTo(null);
  }
  private void setCheckBox(){
    //给all添加事件
    all.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if(!all.isSelected()){
          txt.setSelected(false);
          pdf.setSelected(false);
          ppt.setSelected(false);
          word.setSelected(false);
          excel.setSelected(false);
        }
        else{
          txt.setSelected(true);
          pdf.setSelected(true);
          ppt.setSelected(true);
          word.setSelected(true);
          excel.setSelected(true);
        }
      }
    });
    //给 txt word PDF PPT Excel 添加事件
    txt.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if(all.isSelected()){
          txt.setSelected(true);
          all.setSelected(false);
          pdf.setSelected(false);
          ppt.setSelected(false);
          word.setSelected(false);
          excel.setSelected(false);
        }
      }
    });
    word.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if(all.isSelected()){
          word.setSelected(true);
          all.setSelected(false);
          pdf.setSelected(false);
          ppt.setSelected(false);
          txt.setSelected(false);
          excel.setSelected(false);
        }
      }
    });
    ppt.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if(all.isSelected()){
          ppt.setSelected(true);
          all.setSelected(false);
          pdf.setSelected(false);
          word.setSelected(false);
          txt.setSelected(false);
          excel.setSelected(false);
        }
      }
    });
    excel.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if(all.isSelected()){
          excel.setSelected(true);
          all.setSelected(false);
          pdf.setSelected(false);
          ppt.setSelected(false);
          txt.setSelected(false);
          word.setSelected(false);
        }
      }
    });
    pdf.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if(all.isSelected()){
          pdf.setSelected(true);
          all.setSelected(false);
          word.setSelected(false);
          ppt.setSelected(false);
          txt.setSelected(false);
          excel.setSelected(false);
        }
      }
    });
  }
  private void setTextContentSelectionBoxEvent(){
    //设置一点击就选中全部文本,方便输入
    page.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e) {
        page.setSelectionStart(0);
        page.setSelectionEnd(page.getText().length());
      }
      @Override
      public void focusLost(FocusEvent e) {
      }
    });
    TextContent.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e) {
        int selectIndex=highLightIndex.get(nowIndex-1);
        TextContent.setCaretPosition(selectIndex);
        TextContent.setSelectionStart(selectIndex);
        TextContent.setSelectionEnd(selectIndex+highLightLenth.get(selectIndex));
      }

      @Override
      public void focusLost(FocusEvent e) {

      }
    });
    //设置按钮监听器
    page.addActionListener(e->{
      String pageText=page.getText();
      if(pageText.contains("/")){
        pageText=pageText.substring(0,pageText.indexOf("/"));
      }
      int pageIndex = nowIndex;
      try {
        pageIndex=Integer.valueOf(pageText).intValue();
        if(pageIndex==0){
          pageIndex=1;
        }
      }catch (NumberFormatException numberFormatException){
        numberFormatException.printStackTrace();
        JOptionPane.showMessageDialog(f, "请输入正确的格式", "错误",
                JOptionPane.ERROR_MESSAGE);
        return;
      }finally {
        if(pageIndex>highLightIndex.size()){
          pageIndex=highLightIndex.size();
        }
        nowIndex=pageIndex;
        page.setText(nowIndex+"/"+highLightIndex.size());
      }
      page.setSelectionStart(0);
      page.setSelectionEnd(page.getText().length());
      int selectIndex=highLightIndex.get(pageIndex-1);
      TextContent.setCaretPosition(selectIndex);
      TextContent.setSelectionStart(selectIndex);
      TextContent.setSelectionEnd(selectIndex+highLightLenth.get(selectIndex));
    });
    //向上查找
    up.addActionListener((e)->{
      if(nowIndex<=1){
        return;
      }
      nowIndex--;
      page.setText(nowIndex+"/"+highLightIndex.size());
      int selectIndex=highLightIndex.get(nowIndex-1);
      TextContent.setCaretPosition(selectIndex);
      TextContent.setSelectionStart(selectIndex);
      TextContent.setSelectionEnd(selectIndex+highLightLenth.get(selectIndex));
    });
    //向下查找
    down.addActionListener((e)->{
      if(nowIndex>=highLightIndex.size()){
        return;
      }
      nowIndex++;
      page.setText(nowIndex+"/"+highLightIndex.size());
      int selectIndex=highLightIndex.get(nowIndex-1);
      TextContent.setCaretPosition(selectIndex);
      TextContent.setSelectionStart(selectIndex);
      TextContent.setSelectionEnd(selectIndex+highLightLenth.get(selectIndex));
    });
    highLight.addActionListener(e -> {
      if(isHighlighted){
        //移除高亮
        TextLighter.removeAllHighlights();
        isHighlighted=false;
      }
      else {
        //添加高亮
        setHighlight(keyWord);
        isHighlighted=true;
      }
      int selectIndex=highLightIndex.get(nowIndex-1);
      TextContent.setSelectionStart(selectIndex);
      TextContent.setSelectionEnd(selectIndex+highLightLenth.get(selectIndex));
    });
    colorChoose.addActionListener(e->{
      DefaultColorSelectionModel Model=new DefaultColorSelectionModel();
      JColorChooser jColorChooser=new JColorChooser(Model);
      Color color=jColorChooser.getColor();
      Color color1=JColorChooser.showDialog(null,"颜色选择",color);
      p = new DefaultHighlighter.DefaultHighlightPainter(color1);
      TextLighter.removeAllHighlights();
      setHighlight(keyWord);
      isHighlighted=true;
    });
  }
  private boolean isSelected(String path){
    if(path.endsWith(".txt")) {
      return txt.isSelected();
    }
    else if(path.endsWith(".doc")||path.endsWith(".docx")){
      return word.isSelected();
    }
    else if(path.endsWith(".pdf")){
      return pdf.isSelected();
    }
    else if(path.endsWith(".pptx")||path.endsWith(".ppt")){
      return ppt.isSelected();
    }
    else if(path.endsWith(".xlsx")||path.endsWith(".xls")){
      return excel.isSelected();
    }
    else {
      return true;
    }
  }
  static String getFileName(String path){
    if(path.contains("/")) {
      return path.substring(path.lastIndexOf("/")+1);
    }
    else if(path.contains("\\")) {
      return path.substring(path.lastIndexOf("\\")+1);
    }
    return path;
  }
  public static void main(String[] args) {
    mainUI mainUI=new mainUI();
    mainUI.init();
  }

  public class JProgressBarDemo extends JFrame {
    private void centerWindow(){
      //获得显示屏桌面窗口的大小
      Toolkit tk=getToolkit();
      Dimension dm=tk.getScreenSize();
      //让窗口居中显示
      setLocation((int)(dm.getWidth()-getWidth())/2,(int)(dm.getHeight()-getHeight())/2);
    }
    public JProgressBarDemo(ArrayList<String> arrayList) {
      setTitle("新建索引");
      JLabel label=new JLabel("新建索引");
      //创建一个进度条
      JProgressBar progressBar=new JProgressBar();
      JButton button=new JButton("完成");
      button.setEnabled(false);
      Container container=getContentPane();
      container.setLayout(new GridLayout(3,1));
      JPanel panel1=new JPanel(new FlowLayout(FlowLayout.LEFT));
      JPanel panel2=new JPanel(new FlowLayout(FlowLayout.CENTER));
      JPanel panel3=new JPanel(new FlowLayout(FlowLayout.RIGHT));
      panel1.add(label);    //添加标签
      panel2.add(progressBar);    //添加进度条
      panel3.add(button);    //添加按钮
      container.add(panel1);
      container.add(panel2);
      container.add(panel3);
      this.setSize(100,100);
      centerWindow();
      progressBar.setStringPainted(true);
      //开启一个线程处理进度
      new Progress(progressBar, button,arrayList).start();
      button.addActionListener(new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          dispose();
        }
      });
    }
    private class Progress extends Thread {
      JProgressBar progressBar;
      JButton button;
      ArrayList<String> arrayList;
      Progress(JProgressBar progressBar,JButton button,ArrayList<String> _arrayList)
      {
        this.progressBar=progressBar;
        this.button=button;
        this.arrayList=_arrayList;
      }

      @Override
      public void run() {
        try {
          //弹出选择文件的窗口
          int fileSize=arrayList.size();
          String text;
          int index=1;
          for(String string:arrayList){
            text=getText.read(string);
            fI.addData(string,text);
            System.out.println(string);
            progressBar.setValue((index++)*100/fileSize);
            //progressBar.setString((index++)+"/"+fileSize);
          }
          fI.fileClose();
        } catch (IOException ioException) {
          ioException.printStackTrace();
        }
        progressBar.setIndeterminate(false);
        progressBar.setString("索引完成！");
        button.setEnabled(true);
      }
    }
  }
}


