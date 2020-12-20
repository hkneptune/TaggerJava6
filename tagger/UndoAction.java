/*    */ package tagger;
/*    */ 
/*    */ import java.awt.event.ActionEvent;
/*    */ import javax.swing.AbstractAction;
/*    */ import javax.swing.KeyStroke;
/*    */ import javax.swing.undo.UndoManager;
/*    */ 
/*    */ class UndoAction
/*    */   extends AbstractAction {
/*    */   private final Tagger myTagger;
/*    */   
/*    */   UndoAction(Tagger tagger) {
/* 13 */     super(Tagger.getString("undo.menu.label"));
/* 14 */     putValue("AcceleratorKey", KeyStroke.getKeyStroke(90, 2));
/* 15 */     putValue("MnemonicKey", Integer.valueOf(Tagger.getMnemonic("undo.menu.mnemonic")));
/*    */     
/* 17 */     this.myTagger = tagger;
/* 18 */     setEnabled(false);
/*    */   }
/*    */   
/*    */   public void actionPerformed(ActionEvent e) {
/* 22 */     this.myTagger.getSelectedTab().getUndoManager().undo();
/* 23 */     update();
/* 24 */     this.myTagger.getRedoAction().update();
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
/* 36 */     if (undoManager != null && undoManager.canUndo()) {
/* 37 */       setEnabled(true);
/* 38 */       putValue("Name", undoManager.getUndoPresentationName());
/* 39 */       this.myTagger.getSelectedTab().setSaved(false);
/*    */     } else {
/* 41 */       setEnabled(false);
/* 42 */       putValue("Name", Tagger.getString("undo.menu.label"));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\Users\na\Desktop\TaggerJava6.zip!\TaggerJava6\Tagger.jar!\tagger\UndoAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */