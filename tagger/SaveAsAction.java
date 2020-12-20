/*    */ package tagger;
/*    */ 
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.io.File;
/*    */ import javax.swing.AbstractAction;
/*    */ 
/*    */ class SaveAsAction extends AbstractAction {
/*    */   private final Tagger myTagger;
/*    */   
/*    */   SaveAsAction(Tagger tagger) {
/* 11 */     super(Tagger.getString("save_as.menu.label"));
/* 12 */     putValue("MnemonicKey", Integer.valueOf(Tagger.getMnemonic("save_as.menu.mnemonic")));
/*    */     
/* 14 */     this.myTagger = tagger;
/*    */   }
/*    */   
/*    */   public void actionPerformed(ActionEvent e) {
/* 18 */     File file = this.myTagger.showSaveAsDialog();
/*    */     
/* 20 */     if (file != null) {
/* 21 */       Tab tab = this.myTagger.getSelectedTab();
/* 22 */       file = Tagger.resolveFile(file, Tagger.getExtension(tab.getFile()));
/* 23 */       this.myTagger.getSaveAction().save(tab, file);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\Users\na\Desktop\TaggerJava6.zip!\TaggerJava6\Tagger.jar!\tagger\SaveAsAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */