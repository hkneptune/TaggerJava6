/*    */ package tagger;
/*    */ 
/*    */ import java.awt.event.ActionEvent;
/*    */ import javax.swing.AbstractAction;
/*    */ import javax.swing.KeyStroke;
/*    */ import javax.swing.undo.UndoManager;
/*    */ 
/*    */ class RedoAction
/*    */   extends AbstractAction {
/*    */   private final Tagger myTagger;
/*    */   
/*    */   RedoAction(Tagger tagger) {
/* 13 */     super(Tagger.getString("redo.menu.label"));
/* 14 */     putValue("AcceleratorKey", KeyStroke.getKeyStroke(89, 2));
/* 15 */     putValue("MnemonicKey", Integer.valueOf(Tagger.getMnemonic("redo.menu.mnemonic")));
/*    */     
/* 17 */     this.myTagger = tagger;
/* 18 */     setEnabled(false);
/*    */   }
/*    */   
/*    */   public void actionPerformed(ActionEvent e) {
/* 22 */     this.myTagger.getSelectedTab().getUndoManager().redo();
/* 23 */     update();
/* 24 */     this.myTagger.getUndoAction().update();
/*    */   }
/*    */ 
/*    */   
/*    */   void update() {
/*    */     UndoManager undoManager;
/*    */     try {
/* 31 */       undoManager = this.myTagger.getSelectedTab().getUndoManager();
/* 32 */     } catch (NullPointerException e) {
/* 33 */       undoManager = null;
/*    */     } 
/*    */     
/* 36 */     if (undoManager != null && undoManager.canRedo()) {
/* 37 */       setEnabled(true);
/* 38 */       putValue("Name", undoManager.getRedoPresentationName());
/*    */     } else {
/* 40 */       setEnabled(false);
/* 41 */       putValue("Name", Tagger.getString("redo.menu.label"));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\Users\na\Desktop\TaggerJava6.zip!\TaggerJava6\Tagger.jar!\tagger\RedoAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */