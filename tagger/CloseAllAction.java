/*    */ package tagger;
/*    */ 
/*    */ import java.awt.event.ActionEvent;
/*    */ import javax.swing.AbstractAction;
/*    */ 
/*    */ class CloseAllAction extends AbstractAction {
/*    */   private final Tagger myTagger;
/*    */   
/*    */   CloseAllAction(Tagger tagger) {
/* 10 */     super(Tagger.getString("close_all.menu.label"));
/* 11 */     putValue("MnemonicKey", Integer.valueOf(Tagger.getMnemonic("close_all.menu.mnemonic")));
/*    */     
/* 13 */     this.myTagger = tagger;
/*    */   }
/*    */   
/*    */   public void actionPerformed(ActionEvent e) {
/* 17 */     this.myTagger.removeAllTabs();
/*    */   }
/*    */ }


/* Location:              D:\Users\na\Desktop\TaggerJava6.zip!\TaggerJava6\Tagger.jar!\tagger\CloseAllAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */