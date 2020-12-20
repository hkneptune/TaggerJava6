/*    */ package tagger;
/*    */ 
/*    */ import java.awt.event.ActionEvent;
/*    */ import javax.swing.AbstractAction;
/*    */ 
/*    */ class ExitAction extends AbstractAction {
/*    */   private final Tagger myTagger;
/*    */   
/*    */   ExitAction(Tagger tagger) {
/* 10 */     super(Tagger.getString("exit.menu.label"));
/* 11 */     putValue("MnemonicKey", Integer.valueOf(Tagger.getMnemonic("exit.menu.mnemonic")));
/*    */     
/* 13 */     this.myTagger = tagger;
/*    */   }
/*    */   
/*    */   public void actionPerformed(ActionEvent e) {
/* 17 */     this.myTagger.windowClosing(null);
/* 18 */     System.exit(0);
/*    */   }
/*    */ }


/* Location:              D:\Users\na\Desktop\TaggerJava6.zip!\TaggerJava6\Tagger.jar!\tagger\ExitAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */