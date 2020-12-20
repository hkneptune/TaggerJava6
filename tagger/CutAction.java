/*    */ package tagger;
/*    */ 
/*    */ import java.awt.event.FocusEvent;
/*    */ import java.awt.event.FocusListener;
/*    */ import javax.swing.KeyStroke;
/*    */ import javax.swing.event.CaretEvent;
/*    */ import javax.swing.event.CaretListener;
/*    */ import javax.swing.text.DefaultEditorKit;
/*    */ import javax.swing.text.JTextComponent;
/*    */ 
/*    */ class CutAction
/*    */   extends DefaultEditorKit.CutAction
/*    */   implements CaretListener, FocusListener
/*    */ {
/*    */   CutAction() {
/* 16 */     putValue("Name", Tagger.getString("cut.menu.label"));
/* 17 */     putValue("AcceleratorKey", KeyStroke.getKeyStroke(88, 2));
/* 18 */     putValue("MnemonicKey", Integer.valueOf(Tagger.getMnemonic("cut.menu.mnemonic")));
/*    */     
/* 20 */     setEnabled(false);
/*    */   }
/*    */   
/*    */   public void focusGained(FocusEvent e) {
/* 24 */     final JTextComponent c = (JTextComponent)e.getSource();
/*    */     
/* 26 */     caretUpdate(new CaretEvent(c) {
/*    */           public int getDot() {
/* 28 */             return c.getCaret().getDot();
/*    */           }
/*    */           
/*    */           public int getMark() {
/* 32 */             return c.getCaret().getMark();
/*    */           }
/*    */         });
/*    */   }
/*    */   
/*    */   public void focusLost(FocusEvent e) {
/* 38 */     if (!e.isTemporary() && !(e.getOppositeComponent() instanceof JTextComponent))
/* 39 */       setEnabled(false); 
/*    */   }
/*    */   
/*    */   public void caretUpdate(CaretEvent e) {
/* 43 */     JTextComponent c = (JTextComponent)e.getSource();
/*    */     
/* 45 */     setEnabled((c.isEditable() && c.isEnabled() && e.getMark() != e.getDot()));
/*    */   }
/*    */ }


/* Location:              D:\Users\na\Desktop\TaggerJava6.zip!\TaggerJava6\Tagger.jar!\tagger\CutAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */