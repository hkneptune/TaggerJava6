/*     */ package tagger;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.KeyListener;
/*     */ import java.io.StringReader;
/*     */ import java.io.StringWriter;
/*     */ import javax.swing.BoxLayout;
/*     */ import javax.swing.DefaultCellEditor;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.table.DefaultTableModel;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.InputSource;
/*     */ 
/*     */ class CastField
/*     */   extends JPanel implements ActionListener, KeyListener {
/*  30 */   private static final String[] ROLES = new String[] { Tagger.getString("role.actor"), Tagger.getString("role.director"), Tagger.getString("role.producer") };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  36 */   private static final String[] COLUMNS = new String[] { Tagger.getString("tag.cast.name"), Tagger.getString("tag.cast.role") };
/*     */   
/*     */   private final DefaultTableModel myModel;
/*     */   
/*     */   private final TableSorter mySorter;
/*     */   
/*     */   private final JButton myButton;
/*     */   
/*     */   private final JTable myTable;
/*     */   
/*     */   private final Tab myTab;
/*     */   
/*     */   CastField(Tab tab) {
/*  49 */     setLayout(new BoxLayout(this, 1));
/*     */     
/*  51 */     this.myTab = tab;
/*  52 */     this.myModel = new DefaultTableModel((Object[])COLUMNS, 0);
/*  53 */     this.mySorter = new TableSorter(this.myModel);
/*     */     
/*  55 */     this.myTable = new JTable(this.mySorter);
/*  56 */     this.myTable.addKeyListener(this);
/*  57 */     this.myTable.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(new JComboBox<String>(ROLES)));
/*  58 */     this.myTable.setCellSelectionEnabled(true);
/*  59 */     this.myTable.setPreferredScrollableViewportSize(new Dimension((this.myTable.getPreferredScrollableViewportSize()).width, this.myTable.getRowHeight() * 10));
/*  60 */     add(new JScrollPane(this.myTable));
/*     */     
/*  62 */     this.mySorter.setTableHeader(this.myTable.getTableHeader());
/*     */     
/*  64 */     this.myButton = new JButton(Tagger.getString("tag.cast.add_member"));
/*  65 */     this.myButton.addActionListener(this);
/*  66 */     this.myButton.setAlignmentX(0.5F);
/*  67 */     add(this.myButton);
/*     */     
/*  69 */     setOpaque(false);
/*     */   }
/*     */   
/*     */   String getText() {
/*  73 */     if (this.myTable.getRowCount() == 0) {
/*  74 */       return "";
/*     */     }
/*     */     try {
/*  77 */       Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
/*  78 */       doc.setXmlStandalone(true);
/*     */       
/*  80 */       Element plist = doc.createElement("plist");
/*  81 */       plist.setAttribute("version", "1.0");
/*  82 */       doc.appendChild(plist);
/*     */       
/*  84 */       Element dict = doc.createElement("dict");
/*  85 */       plist.appendChild(dict);
/*     */       
/*  87 */       Element castKey = doc.createElement("key");
/*  88 */       castKey.setTextContent("cast");
/*  89 */       Element castArray = doc.createElement("array");
/*     */       
/*  91 */       Element directorsKey = doc.createElement("key");
/*  92 */       directorsKey.setTextContent("directors");
/*  93 */       Element directorsArray = doc.createElement("array");
/*     */       
/*  95 */       Element producersKey = doc.createElement("key");
/*  96 */       producersKey.setTextContent("producers");
/*  97 */       Element producersArray = doc.createElement("array");
/*     */       
/*  99 */       int cast = 0;
/* 100 */       int directors = 0;
/* 101 */       int producers = 0;
/*     */       
/* 103 */       for (int i = 0; i < this.myModel.getRowCount(); i++) {
/* 104 */         Element iDict = doc.createElement("dict");
/*     */         
/* 106 */         Element iKey = doc.createElement("key");
/* 107 */         iKey.setTextContent("name");
/* 108 */         iDict.appendChild(iKey);
/*     */         
/* 110 */         Element iString = doc.createElement("string");
/* 111 */         iString.setTextContent((String)this.myModel.getValueAt(i, 0));
/* 112 */         iDict.appendChild(iString);
/*     */         
/* 114 */         String role = (String)this.myModel.getValueAt(i, 1);
/*     */         
/* 116 */         if (role.equals(ROLES[0])) {
/* 117 */           castArray.appendChild(iDict);
/* 118 */           cast++;
/* 119 */         } else if (role.equals(ROLES[1])) {
/* 120 */           directorsArray.appendChild(iDict);
/* 121 */           directors++;
/* 122 */         } else if (role.equals(ROLES[2])) {
/* 123 */           producersArray.appendChild(iDict);
/* 124 */           producers++;
/*     */         } 
/*     */       } 
/*     */       
/* 128 */       if (cast > 0) {
/* 129 */         dict.appendChild(castKey);
/* 130 */         dict.appendChild(castArray);
/*     */       } 
/*     */       
/* 133 */       if (directors > 0) {
/* 134 */         dict.appendChild(directorsKey);
/* 135 */         dict.appendChild(directorsArray);
/*     */       } 
/*     */       
/* 138 */       if (producers > 0) {
/* 139 */         dict.appendChild(producersKey);
/* 140 */         dict.appendChild(producersArray);
/*     */       } 
/*     */       
/* 143 */       Transformer transformer = TransformerFactory.newInstance().newTransformer();
/* 144 */       transformer.setOutputProperty("indent", "yes");
/* 145 */       transformer.setOutputProperty("doctype-public", "-//Apple Computer//DTD PLIST 1.0//EN");
/* 146 */       transformer.setOutputProperty("doctype-system", "http://www.apple.com/DTDs/PropertyList-1.0.dtd");
/*     */       
/* 148 */       StreamResult result = new StreamResult(new StringWriter());
/* 149 */       transformer.transform(new DOMSource(doc), result);
/*     */       
/* 151 */       return result.getWriter().toString();
/* 152 */     } catch (Exception e) {
/* 153 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   void setText(String data) {
/* 158 */     this.myModel.setRowCount(0);
/*     */     
/* 160 */     if (data == null || data.equals("")) {
/*     */       return;
/*     */     }
/*     */     try {
/* 164 */       Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(data)));
/*     */       
/* 166 */       if (doc.getDoctype().getPublicId().equals("-//Apple Computer//DTD PLIST 1.0//EN")) {
/* 167 */         Element dict = (Element)doc.getDocumentElement().getElementsByTagName("dict").item(0);
/*     */         
/* 169 */         NodeList keys = dict.getElementsByTagName("key");
/* 170 */         NodeList arrays = dict.getElementsByTagName("array");
/*     */         
/* 172 */         int count = 0;
/* 173 */         for (int i = 0; i < keys.getLength(); i++) {
/* 174 */           int role; if (!keys.item(i).getParentNode().equals(dict)) {
/*     */             continue;
/*     */           }
/* 177 */           String roleText = ((Element)keys.item(i)).getTextContent();
/*     */ 
/*     */           
/* 180 */           if (roleText.equals("cast")) {
/* 181 */             role = 0;
/* 182 */           } else if (roleText.equals("directors")) {
/* 183 */             role = 1;
/* 184 */           } else if (roleText.equals("producers")) {
/* 185 */             role = 2;
/*     */           } else {
/*     */             continue;
/*     */           } 
/* 189 */           NodeList dicts = ((Element)arrays.item(count++)).getElementsByTagName("dict");
/*     */           
/* 191 */           for (int j = 0; j < dicts.getLength(); j++) {
/* 192 */             String name = ((Element)((Element)dicts.item(j)).getElementsByTagName("string").item(0)).getTextContent();
/* 193 */             this.myModel.addRow((Object[])new String[] { name, ROLES[role] });
/*     */           }  continue;
/*     */         } 
/*     */       } 
/* 197 */     } catch (Exception e) {}
/*     */   }
/*     */   
/*     */   public void actionPerformed(ActionEvent e) {
/* 201 */     this.myModel.addRow((Object[])new String[] { "", ROLES[0] });
/* 202 */     this.myTable.scrollRectToVisible(this.myTable.getCellRect(this.mySorter.viewIndex(this.myModel.getRowCount() - 1), 0, false));
/* 203 */     this.myTable.editCellAt(this.mySorter.viewIndex(this.myModel.getRowCount() - 1), 0);
/* 204 */     this.myTable.getEditorComponent().requestFocusInWindow();
/* 205 */     this.myTab.setSaved(false);
/*     */   }
/*     */   
/*     */   public void keyPressed(KeyEvent e) {
/* 209 */     if ((e.getKeyCode() == 127 || e.getKeyCode() == 8) && this.myTable.getSelectedRowCount() > 0) {
/* 210 */       int[] rows = this.myTable.getSelectedRows();
/*     */       
/* 212 */       for (int i = 0; i < rows.length; i++) {
/* 213 */         this.myModel.removeRow(this.mySorter.modelIndex(rows[i] - i));
/*     */       }
/* 215 */       this.myTab.setSaved(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void keyTyped(KeyEvent e) {}
/*     */   
/*     */   public void keyReleased(KeyEvent e) {}
/*     */ }


/* Location:              D:\Users\na\Desktop\TaggerJava6.zip!\TaggerJava6\Tagger.jar!\tagger\CastField.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */