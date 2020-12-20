/*    */ package tagger;
/*    */ 
/*    */ import java.util.LinkedHashMap;
/*    */ 
/*    */ class BidiLinkedHashMap<K, V> extends LinkedHashMap<K, V> {
/*    */   private final LinkedHashMap<V, K> myMirror;
/*    */   
/*    */   BidiLinkedHashMap(int size) {
/*  9 */     super(size);
/* 10 */     this.myMirror = new LinkedHashMap<V, K>(size);
/*    */   }
/*    */   
/*    */   K getKeyForValue(V value) {
/* 14 */     return this.myMirror.get(value);
/*    */   }
/*    */   
/*    */   public V put(K key, V value) {
/* 18 */     super.put(key, value);
/* 19 */     this.myMirror.put(value, key);
/* 20 */     return value;
/*    */   }
/*    */ }


/* Location:              D:\Users\na\Desktop\TaggerJava6.zip!\TaggerJava6\Tagger.jar!\tagger\BidiLinkedHashMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */