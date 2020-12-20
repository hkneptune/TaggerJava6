/*    */ package tagger;
/*    */ 
/*    */ import java.awt.event.ActionEvent;
/*    */ import javax.swing.AbstractAction;
/*    */ import javax.swing.KeyStroke;
/*    */ 
/*    */ class CloseAction
/*    */   extends AbstractAction {
/*    */   private final Tagger myTagger;
/*    */   
/*    */   CloseAction(Tagger tagger) {
/* 12 */     super(Tagger.getString("close.menu.label"));
/* 13 */     putValue("AcceleratorKey", KeyStroke.getKeyStroke(87, 2));
/* 14 */     putValue("MnemonicKey", Integer.valueOf(Tagger.getMnemonic("close.menu.mnemonic")));
/*    */     
/* 16 */     this.myTagger = tagger;
/*    */   }
/*    */   
/*    */   public void actionPerformed(ActionEvent e) {
/* 20 */     this.myTagger.removeTab(this.myTagger.getSelectedTab());
/*    */   }
/*    */ }


/* Location:              D:\Users\na\Desktop\TaggerJava6.zip!\TaggerJava6\Tagger.jar!\tagger\CloseAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */