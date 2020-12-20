/*    */ package tagger;
/*    */ 
/*    */ import java.awt.event.ActionEvent;
/*    */ import javax.swing.AbstractAction;
/*    */ import javax.swing.JOptionPane;
/*    */ 
/*    */ class AboutAction extends AbstractAction {
/*    */   private final Tagger myTagger;
/*    */   
/*    */   AboutAction(Tagger tagger) {
/* 11 */     super(Tagger.getString("about.menu.label"));
/* 12 */     putValue("MnemonicKey", Integer.valueOf(Tagger.getMnemonic("about.menu.mnemonic")));
/*    */     
/* 14 */     this.myTagger = tagger;
/*    */   }
/*    */   
/*    */   public void actionPerformed(ActionEvent e) {
/* 18 */     JOptionPane.showMessageDialog(this.myTagger, "Tagger 3.2b" + Tagger.getVersion() + " (Java 6)", Tagger.getString("about.dialog.title"), 1);
/*    */   }
/*    */ }


/* Location:              D:\Users\na\Desktop\TaggerJava6.zip!\TaggerJava6\Tagger.jar!\tagger\AboutAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */