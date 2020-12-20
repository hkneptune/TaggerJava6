/*    */ package tagger;
/*    */ 
/*    */ import java.awt.event.ActionEvent;
/*    */ import javax.swing.KeyStroke;
/*    */ import javax.swing.event.CaretEvent;
/*    */ import javax.swing.event.CaretListener;
/*    */ import javax.swing.text.JTextComponent;
/*    */ import javax.swing.text.TextAction;
/*    */ 
/*    */ class DeleteAction
/*    */   extends TextAction
/*    */   implements CaretListener {
/*    */   DeleteAction() {
/* 14 */     super(Tagger.getString("delete.menu.label"));
/* 15 */     putValue("AcceleratorKey", KeyStroke.getKeyStroke(127, 0));
/* 16 */     putValue("MnemonicKey", Integer.valueOf(Tagger.getMnemonic("delete.menu.mnemonic")));
/*    */     
/* 18 */     setEnabled(false);
/*    */   }
/*    */   
/*    */   public void actionPerformed(ActionEvent e) {
/* 22 */     JTextComponent c = getTextComponent(e);
/*    */     
/* 24 */     if (c != null)
/* 25 */       c.getActionForKeyStroke(KeyStroke.getKeyStroke(127, 0)).actionPerformed(null); 
/*    */   }
/*    */   
/*    */   public void caretUpdate(CaretEvent e) {
/* 29 */     JTextComponent c = (JTextComponent)e.getSource();
/*    */     
/* 31 */     setEnabled((c.isEditable() && c.isEnabled() && (e.getDot() != e.getMark() || e.getDot() < c.getText().length() - 1)));
/*    */   }
/*    */ }


/* Location:              D:\Users\na\Desktop\TaggerJava6.zip!\TaggerJava6\Tagger.jar!\tagger\DeleteAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */