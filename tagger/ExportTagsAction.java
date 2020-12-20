/*    */ package tagger;
/*    */ 
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.io.File;
/*    */ import java.io.FileOutputStream;
/*    */ import java.text.MessageFormat;
/*    */ import javax.swing.AbstractAction;
/*    */ import javax.swing.filechooser.FileFilter;
/*    */ import org.apache.poi.hssf.usermodel.HSSFRichTextString;
/*    */ import org.apache.poi.hssf.usermodel.HSSFRow;
/*    */ import org.apache.poi.hssf.usermodel.HSSFSheet;
/*    */ import org.apache.poi.hssf.usermodel.HSSFWorkbook;
/*    */ 
/*    */ class ExportTagsAction extends AbstractAction {
/*    */   private final Tagger myTagger;
/*    */   
/*    */   ExportTagsAction(Tagger tagger) {
/* 18 */     super(Tagger.getString("export_tags.menu.label"));
/* 19 */     putValue("MnemonicKey", Integer.valueOf(Tagger.getMnemonic("export_tags.menu.mnemonic")));
/*    */     
/* 21 */     this.myTagger = tagger;
/*    */   }
/*    */   
/*    */   public void actionPerformed(ActionEvent e) {
/* 25 */     File file = this.myTagger.showExportTagsDialog();
/*    */     
/* 27 */     if (file != null) {
/* 28 */       FileFilter filter = this.myTagger.getImportExportChooser().getFileFilter();
/*    */       
/* 30 */       if (filter == Tagger.TAG_ARCHIVE_FILTER) {
/* 31 */         String ext = Tagger.getExtension(file);
/*    */         
/* 33 */         if (ext == null)
/* 34 */         { exportXML(Tagger.resolveFile(file, "xml")); }
/* 35 */         else if (ext.equals("xls"))
/* 36 */         { exportXLS(file); }
/*    */         else
/* 38 */         { exportXML(file); } 
/* 39 */       } else if (filter == Tagger.XML_FILTER) {
/* 40 */         exportXML(Tagger.resolveFile(file, "xml"));
/* 41 */       } else if (filter == Tagger.XLS_FILTER) {
/* 42 */         exportXLS(Tagger.resolveFile(file, "xls"));
/*    */       } 
/*    */     } 
/*    */   }
/*    */   private void exportXLS(File file) {
/* 47 */     Tab tab = this.myTagger.getSelectedTab();
/*    */     
/*    */     try {
/* 50 */       FileOutputStream writer = new FileOutputStream(file);
/* 51 */       HSSFWorkbook wb = new HSSFWorkbook();
/* 52 */       HSSFSheet sheet = wb.createSheet();
/*    */       
/* 54 */       int i = 0;
/* 55 */       for (String atom : AtomicParsley.I18N.keySet()) {
/* 56 */         HSSFRow row = sheet.createRow(i++);
/*    */         
/* 58 */         row.createCell((short)0).setCellValue(new HSSFRichTextString(AtomicParsley.I18N.get(atom)));
/* 59 */         row.createCell((short)1).setCellValue(new HSSFRichTextString(Tagger.getInput(tab.getField(atom))));
/*    */       } 
/*    */       
/* 62 */       wb.write(writer);
/* 63 */       writer.close();
/* 64 */     } catch (Exception e) {
/* 65 */       this.myTagger.showError(MessageFormat.format(Tagger.getString("error.export_tags"), new Object[] { tab.getFile().getName(), file.getName() }), e);
/*    */     } 
/*    */   }
/*    */   
/*    */   private void exportXML(File file) {
/* 70 */     Tab tab = this.myTagger.getSelectedTab();
/* 71 */     EasyWriter writer = null;
/*    */     
/*    */     try {
/* 74 */       writer = new EasyWriter(file.getAbsolutePath());
/*    */       
/* 76 */       writer.println("<?xml version=\"1.0\"?>");
/* 77 */       writer.println("<tags>");
/*    */       
/* 79 */       for (String atom : AtomicParsley.I18N.keySet()) {
/* 80 */         writer.println("\t<" + (String)AtomicParsley.I18N.get(atom) + ">" + Tagger.getInput(tab.getField(atom)).replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;") + "</" + (String)AtomicParsley.I18N.get(atom) + ">");
/*    */       }
/* 82 */       writer.print("</tags>");
/* 83 */     } catch (Exception e) {
/* 84 */       this.myTagger.showError(MessageFormat.format(Tagger.getString("error.export_tags"), new Object[] { tab.getFile().getName(), file.getName() }), e);
/*    */     } finally {
/* 86 */       if (writer != null)
/* 87 */         writer.close(); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\Users\na\Desktop\TaggerJava6.zip!\TaggerJava6\Tagger.jar!\tagger\ExportTagsAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */