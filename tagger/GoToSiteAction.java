/*    */ package tagger;
/*    */ 
/*    */ import java.awt.Desktop;
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.net.URI;
/*    */ import javax.swing.AbstractAction;
/*    */ 
/*    */ class GoToSiteAction extends AbstractAction {
/*    */   private final Tagger myTagger;
/*    */   
/*    */   GoToSiteAction(Tagger tagger) {
/* 12 */     super(Tagger.getString("visit_site.menu.label"));
/* 13 */     putValue("MnemonicKey", Integer.valueOf(Tagger.getMnemonic("visit_site.menu.mnemonic")));
/*    */     
/* 15 */     this.myTagger = tagger;
/*    */   }
/*    */   
/*    */   public void actionPerformed(ActionEvent e) {
/*    */     try {
/* 20 */       Desktop.getDesktop().browse(new URI("http://tvtagger.wordpress.com/"));
/* 21 */     } catch (Exception ex) {
/* 22 */       this.myTagger.showError(Tagger.getString("error.visit_site"), ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\Users\na\Desktop\TaggerJava6.zip!\TaggerJava6\Tagger.jar!\tagger\GoToSiteAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */