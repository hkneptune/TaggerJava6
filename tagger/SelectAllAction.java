/*    */ package tagger;
/*    */ 
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.FocusEvent;
/*    */ import java.awt.event.FocusListener;
/*    */ import javax.swing.KeyStroke;
/*    */ import javax.swing.text.JTextComponent;
/*    */ import javax.swing.text.TextAction;
/*    */ 
/*    */ class SelectAllAction
/*    */   extends TextAction
/*    */   implements FocusListener {
/*    */   SelectAllAction() {
/* 14 */     super(Tagger.getString("select_all.menu.label"));
/* 15 */     putValue("AcceleratorKey", KeyStroke.getKeyStroke(65, 2));
/* 16 */     putValue("MnemonicKey", Integer.valueOf(Tagger.getMnemonic("select_all.menu.mnemonic")));
/*    */     
/* 18 */     setEnabled(false);
/*    */   }
/*    */   
/*    */   public void actionPerformed(ActionEvent e) {
/* 22 */     JTextComponent c = getTextComponent(e);
/*    */     
/* 24 */     if (c != null)
/* 25 */       getTextComponent(e).selectAll(); 
/*    */   }
/*    */   
/*    */   public void focusGained(FocusEvent e) {
/* 29 */     setEnabled(true);
/*    */   }
/*    */   
/*    */   public void focusLost(FocusEvent e) {
/* 33 */     setEnabled((e.isTemporary() || e.getOppositeComponent() instanceof JTextComponent));
/*    */   }
/*    */ }


/* Location:              D:\Users\na\Desktop\TaggerJava6.zip!\TaggerJava6\Tagger.jar!\tagger\SelectAllAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */