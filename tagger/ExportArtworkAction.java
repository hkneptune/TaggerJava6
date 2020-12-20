/*    */ package tagger;
/*    */ 
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.io.File;
/*    */ import java.text.MessageFormat;
/*    */ import javax.imageio.ImageIO;
/*    */ import javax.swing.AbstractAction;
/*    */ import javax.swing.filechooser.FileFilter;
/*    */ 
/*    */ class ExportArtworkAction extends AbstractAction {
/*    */   private final Tagger myTagger;
/*    */   
/*    */   ExportArtworkAction(Tagger tagger) {
/* 14 */     super(Tagger.getString("export_artwork.menu.label"));
/* 15 */     putValue("MnemonicKey", Integer.valueOf(Tagger.getMnemonic("export_artwork.menu.mnemonic")));
/* 16 */     setEnabled(false);
/*    */     
/* 18 */     this.myTagger = tagger;
/*    */   }
/*    */   
/*    */   public void actionPerformed(ActionEvent e) {
/* 22 */     File file = this.myTagger.showExportArtworkDialog();
/*    */     
/* 24 */     if (file != null) {
/* 25 */       FileFilter filter = this.myTagger.getArtworkChooser().getFileFilter();
/*    */       
/* 27 */       if (filter == Tagger.IMAGE_FILTER) {
/* 28 */         String ext = Tagger.getExtension(file);
/*    */         
/* 30 */         if (ext == null)
/* 31 */         { export(Tagger.resolveFile(file, "jpg"), "jpeg"); }
/* 32 */         else if (ext.equals("png"))
/* 33 */         { export(file, "png"); }
/*    */         else
/* 35 */         { export(file, "jpeg"); } 
/* 36 */       } else if (filter == Tagger.JPEG_FILTER) {
/* 37 */         export(Tagger.resolveFile(file, "jpg"), "jpeg");
/* 38 */       } else if (filter == Tagger.PNG_FILTER) {
/* 39 */         export(Tagger.resolveFile(file, "png"), "png");
/*    */       } 
/*    */     } 
/*    */   }
/*    */   void update() {
/* 44 */     setEnabled((this.myTagger.getSelectedTab() != null && this.myTagger.getSelectedTab().getSelectedArtwork() != null));
/*    */   }
/*    */   
/*    */   private void export(File file, String type) {
/*    */     try {
/* 49 */       ImageIO.write(this.myTagger.getSelectedTab().getSelectedArtwork().getImage(), type, file);
/* 50 */     } catch (Exception e) {
/* 51 */       this.myTagger.showError(MessageFormat.format(Tagger.getString("error.export_artwork"), new Object[] { this.myTagger.getSelectedTab().getFile(), file }), e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\Users\na\Desktop\TaggerJava6.zip!\TaggerJava6\Tagger.jar!\tagger\ExportArtworkAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */