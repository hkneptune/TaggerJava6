/*    */ package tagger;
/*    */ 
/*    */ import java.awt.Toolkit;
/*    */ import java.awt.datatransfer.Clipboard;
/*    */ import java.awt.datatransfer.DataFlavor;
/*    */ import java.awt.datatransfer.FlavorEvent;
/*    */ import java.awt.datatransfer.FlavorListener;
/*    */ import java.awt.event.FocusEvent;
/*    */ import java.awt.event.FocusListener;
/*    */ import javax.swing.KeyStroke;
/*    */ import javax.swing.text.DefaultEditorKit;
/*    */ import javax.swing.text.JTextComponent;
/*    */ 
/*    */ class PasteAction
/*    */   extends DefaultEditorKit.PasteAction
/*    */   implements FocusListener, FlavorListener
/*    */ {
/*    */   PasteAction() {
/* 19 */     putValue("Name", Tagger.getString("paste.menu.label"));
/* 20 */     putValue("AcceleratorKey", KeyStroke.getKeyStroke(86, 2));
/* 21 */     putValue("MnemonicKey", Integer.valueOf(Tagger.getMnemonic("paste.menu.mnemonic")));
/*    */     
/* 23 */     setEnabled(false);
/* 24 */     Toolkit.getDefaultToolkit().getSystemClipboard().addFlavorListener(this);
/*    */   }
/*    */   
/*    */   public void focusGained(FocusEvent e) {
/* 28 */     JTextComponent c = (JTextComponent)e.getComponent();
/*    */     
/* 30 */     if (c.isEditable() && c.isEnabled())
/* 31 */       flavorsChanged((FlavorEvent)null); 
/*    */   }
/*    */   
/*    */   public void flavorsChanged(FlavorEvent e) {
/* 35 */     Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
/*    */     
/*    */     try {
/* 38 */       setEnabled((c.getData(DataFlavor.selectBestTextFlavor(c.getAvailableDataFlavors())) != null));
/*    */     }
/* 40 */     catch (Exception ex) {
/* 41 */       setEnabled(false);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void focusLost(FocusEvent e) {
/* 46 */     if (!e.isTemporary() && !(e.getOppositeComponent() instanceof JTextComponent))
/* 47 */       setEnabled(false); 
/*    */   }
/*    */ }


/* Location:              D:\Users\na\Desktop\TaggerJava6.zip!\TaggerJava6\Tagger.jar!\tagger\PasteAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */