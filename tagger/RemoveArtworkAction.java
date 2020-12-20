/*    */ package tagger;
/*    */ 
/*    */ import java.awt.event.ActionEvent;
/*    */ import javax.swing.AbstractAction;
/*    */ 
/*    */ class RemoveArtworkAction extends AbstractAction {
/*    */   private final Tagger myTagger;
/*    */   
/*    */   RemoveArtworkAction(Tagger tagger) {
/* 10 */     super(Tagger.getString("remove_artwork.menu.label"));
/* 11 */     putValue("MnemonicKey", Integer.valueOf(Tagger.getMnemonic("remove_artwork.menu.mnemonic")));
/* 12 */     setEnabled(false);
/*    */     
/* 14 */     this.myTagger = tagger;
/*    */   }
/*    */   
/*    */   void update() {
/* 18 */     setEnabled((this.myTagger.getSelectedTab() != null && this.myTagger.getSelectedTab().getSelectedArtwork() != null));
/*    */   }
/*    */   
/*    */   public void actionPerformed(ActionEvent e) {
/* 22 */     this.myTagger.getSelectedTab().removeArtwork(this.myTagger.getSelectedTab().getSelectedArtwork());
/*    */   }
/*    */ }


/* Location:              D:\Users\na\Desktop\TaggerJava6.zip!\TaggerJava6\Tagger.jar!\tagger\RemoveArtworkAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */