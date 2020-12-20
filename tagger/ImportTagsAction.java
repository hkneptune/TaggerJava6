/*     */ package tagger;
/*     */ 
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.text.MessageFormat;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.filechooser.FileFilter;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import net.iharder.dnd.FileDrop;
/*     */ import org.apache.poi.hssf.usermodel.HSSFRow;
/*     */ import org.apache.poi.hssf.usermodel.HSSFSheet;
/*     */ import org.apache.poi.hssf.usermodel.HSSFWorkbook;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ class ImportTagsAction
/*     */   extends AbstractAction implements FileDrop.Listener {
/*     */   private final Tagger myTagger;
/*     */   
/*     */   ImportTagsAction(Tagger tagger) {
/*  23 */     super(Tagger.getString("import_tags.menu.label"));
/*  24 */     putValue("MnemonicKey", Integer.valueOf(Tagger.getMnemonic("import_tags.menu.mnemonic")));
/*     */     
/*  26 */     this.myTagger = tagger;
/*     */   }
/*     */   
/*     */   public void actionPerformed(ActionEvent e) {
/*  30 */     File file = this.myTagger.showImportTagsDialog();
/*     */     
/*  32 */     if (file != null) {
/*  33 */       FileFilter filter = this.myTagger.getImportExportChooser().getFileFilter();
/*     */       
/*  35 */       if (filter == Tagger.TAG_ARCHIVE_FILTER) {
/*  36 */         String ext = Tagger.getExtension(file);
/*     */         
/*  38 */         if (ext == null)
/*  39 */         { importXML(Tagger.resolveFile(file, "xml")); }
/*  40 */         else if (ext.equals("xls"))
/*  41 */         { importXLS(file); }
/*     */         else
/*  43 */         { importXML(file); } 
/*  44 */       } else if (filter == Tagger.XML_FILTER) {
/*  45 */         importXML(Tagger.resolveFile(file, "xml"));
/*  46 */       } else if (filter == Tagger.XLS_FILTER) {
/*  47 */         importXLS(Tagger.resolveFile(file, "xls"));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public void filesDropped(File[] files) {
/*  52 */     boolean xml = Tagger.XML_FILTER.accept(files[0]);
/*  53 */     boolean xls = Tagger.XLS_FILTER.accept(files[0]);
/*     */     
/*  55 */     if (!xml && !xls) {
/*     */       return;
/*     */     }
/*  58 */     String message = MessageFormat.format(Tagger.getString("warning.import_tags"), new Object[] { files[0].getName(), this.myTagger.getSelectedTab().getFile().getName() });
/*     */     
/*  60 */     if (JOptionPane.showConfirmDialog(this.myTagger, message, Tagger.getString("warning.dialog.title"), 0, 2) == 0)
/*  61 */       if (xml) {
/*  62 */         importXML(files[0]);
/*  63 */       } else if (xls) {
/*  64 */         importXLS(files[0]);
/*     */       }  
/*     */   }
/*     */   
/*     */   private void importXLS(File file) {
/*  69 */     Tab tab = this.myTagger.getSelectedTab();
/*     */     
/*     */     try {
/*  72 */       FileInputStream fis = new FileInputStream(file);
/*  73 */       HSSFWorkbook wb = new HSSFWorkbook(fis);
/*  74 */       HSSFSheet sheet = wb.getSheetAt(0);
/*     */       
/*  76 */       for (int i = 0; i <= sheet.getLastRowNum(); i++) {
/*  77 */         HSSFRow row = sheet.getRow(i);
/*     */         
/*  79 */         if (row != null) {
/*  80 */           Tagger.setInput(tab.getField(AtomicParsley.I18N.getKeyForValue(row.getCell((short)0).getRichStringCellValue().getString())), row.getCell((short)1).getRichStringCellValue().getString());
/*     */         }
/*     */       } 
/*  83 */       fis.close();
/*  84 */     } catch (Exception e) {
/*  85 */       this.myTagger.showError(MessageFormat.format(Tagger.getString("error.import_tags"), new Object[] { file.getName(), tab.getFile().getName() }), e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void importXML(File file) {
/*  90 */     Tab tab = this.myTagger.getSelectedTab();
/*     */     
/*     */     try {
/*  93 */       NodeList tags = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file).getDocumentElement().getChildNodes();
/*     */       
/*  95 */       for (int i = 0; i < tags.getLength(); i++) {
/*  96 */         if (tags.item(i).getNodeType() == 1) {
/*  97 */           Element e = (Element)tags.item(i);
/*  98 */           Tagger.setInput(tab.getField(AtomicParsley.I18N.getKeyForValue(e.getTagName())), e.getTextContent().replace("&lt;", "<").replace("&gt;", ">").replace("&amp;", "&"));
/*     */         } 
/*     */       } 
/* 101 */     } catch (Exception e) {
/* 102 */       this.myTagger.showError(MessageFormat.format(Tagger.getString("error.import_tags"), new Object[] { file.getName(), tab.getFile().getName() }), e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\Users\na\Desktop\TaggerJava6.zip!\TaggerJava6\Tagger.jar!\tagger\ImportTagsAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */