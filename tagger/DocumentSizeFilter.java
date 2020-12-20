/*    */ package tagger;
/*    */ 
/*    */ import java.awt.Toolkit;
/*    */ import javax.swing.text.AttributeSet;
/*    */ import javax.swing.text.BadLocationException;
/*    */ import javax.swing.text.DocumentFilter;
/*    */ 
/*    */ class DocumentSizeFilter extends DocumentFilter {
/*    */   private final int myMaxCharacters;
/*    */   
/*    */   DocumentSizeFilter(int maxChars) {
/* 12 */     this.myMaxCharacters = maxChars;
/*    */   }
/*    */   
/*    */   public void insertString(DocumentFilter.FilterBypass fb, int offs, String str, AttributeSet a) throws BadLocationException {
/* 16 */     if (fb.getDocument().getLength() + str.length() <= this.myMaxCharacters) {
/* 17 */       super.insertString(fb, offs, str, a);
/*    */     } else {
/* 19 */       Toolkit.getDefaultToolkit().beep();
/*    */     } 
/*    */   }
/*    */   public void replace(DocumentFilter.FilterBypass fb, int offs, int length, String str, AttributeSet a) throws BadLocationException {
/* 23 */     if (fb.getDocument().getLength() + str.length() - length <= this.myMaxCharacters) {
/* 24 */       super.replace(fb, offs, length, str, a);
/*    */     } else {
/* 26 */       Toolkit.getDefaultToolkit().beep();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\Users\na\Desktop\TaggerJava6.zip!\TaggerJava6\Tagger.jar!\tagger\DocumentSizeFilter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */