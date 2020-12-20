/*    */ package tagger;
/*    */ 
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.PrintWriter;
/*    */ import java.io.UnsupportedEncodingException;
/*    */ 
/*    */ class EasyWriter {
/*    */   private String myFileName;
/*    */   private PrintWriter myOutFile;
/*    */   
/*    */   EasyWriter(String fileName) throws FileNotFoundException, UnsupportedEncodingException {
/* 12 */     this.myFileName = fileName;
/* 13 */     this.myOutFile = new PrintWriter(fileName, "utf-8");
/*    */   }
/*    */   
/*    */   void close() {
/* 17 */     if (this.myFileName != null)
/* 18 */       this.myOutFile.close(); 
/*    */   }
/*    */   
/*    */   void print(char ch) {
/* 22 */     this.myOutFile.print(ch);
/*    */   }
/*    */   
/*    */   void print(int k) {
/* 26 */     this.myOutFile.print(k);
/*    */   }
/*    */   
/*    */   void print(double x) {
/* 30 */     this.myOutFile.print(x);
/*    */   }
/*    */   
/*    */   void print(String s) {
/* 34 */     this.myOutFile.print(s);
/*    */   }
/*    */   
/*    */   void print(Object obj) {
/* 38 */     this.myOutFile.print(obj);
/*    */   }
/*    */   
/*    */   void println() {
/* 42 */     this.myOutFile.println();
/*    */   }
/*    */   
/*    */   void println(char ch) {
/* 46 */     this.myOutFile.println(ch);
/*    */   }
/*    */   
/*    */   void println(int k) {
/* 50 */     this.myOutFile.println(k);
/*    */   }
/*    */   
/*    */   void println(double x) {
/* 54 */     this.myOutFile.println(x);
/*    */   }
/*    */   
/*    */   void println(String s) {
/* 58 */     this.myOutFile.println(s);
/*    */   }
/*    */   
/*    */   void println(Object obj) {
/* 62 */     this.myOutFile.println(obj);
/*    */   }
/*    */ }


/* Location:              D:\Users\na\Desktop\TaggerJava6.zip!\TaggerJava6\Tagger.jar!\tagger\EasyWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */