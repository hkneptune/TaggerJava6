/*    */ package tagger;
/*    */ 
/*    */ import java.awt.event.ActionEvent;
/*    */ import javax.swing.AbstractAction;
/*    */ 
/*    */ class SaveAllAction extends AbstractAction {
/*    */   private final Tagger myTagger;
/*    */   
/*    */   SaveAllAction(Tagger tagger) {
/* 10 */     super(Tagger.getString("save_all.menu.label"));
/* 11 */     putValue("MnemonicKey", Integer.valueOf(Tagger.getMnemonic("save_all.menu.mnemonic")));
/*    */     
/* 13 */     this.myTagger = tagger;
/*    */   }
/*    */   
/*    */   public void actionPerformed(ActionEvent e) {
/* 17 */     this.myTagger.getSaveAction().save(this.myTagger.getTabs());
/*    */   }
/*    */ }


/* Location:              D:\Users\na\Desktop\TaggerJava6.zip!\TaggerJava6\Tagger.jar!\tagger\SaveAllAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */