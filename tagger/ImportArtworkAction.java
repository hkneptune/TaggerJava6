/*    */ package tagger;
/*    */ 
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.io.File;
/*    */ import java.text.MessageFormat;
/*    */ import javax.swing.AbstractAction;
/*    */ 
/*    */ class ImportArtworkAction extends AbstractAction {
/*    */   private final Tagger myTagger;
/*    */   
/*    */   ImportArtworkAction(Tagger tagger) {
/* 12 */     super(Tagger.getString("import_artwork.menu.label"));
/* 13 */     putValue("MnemonicKey", Integer.valueOf(Tagger.getMnemonic("import_artwork.menu.mnemonic")));
/*    */     
/* 15 */     this.myTagger = tagger;
/*    */   }
/*    */   
/*    */   public void actionPerformed(ActionEvent e) {
/* 19 */     File[] files = this.myTagger.showImportArtworkDialog();
/*    */     
/* 21 */     if (files != null)
/* 22 */       for (File file : files) {
/*    */         try {
/* 24 */           this.myTagger.getSelectedTab().addArtwork(file);
/* 25 */         } catch (Exception ex) {
/* 26 */           this.myTagger.showError(MessageFormat.format(Tagger.getString("error.import_artwork"), new Object[] { file, this.myTagger.getSelectedTab().getFile() }), ex);
/*    */         } 
/*    */       }  
/*    */   }
/*    */ }


/* Location:              D:\Users\na\Desktop\TaggerJava6.zip!\TaggerJava6\Tagger.jar!\tagger\ImportArtworkAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */