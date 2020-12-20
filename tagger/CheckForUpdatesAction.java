/*    */ package tagger;
/*    */ 
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.InputStreamReader;
/*    */ import java.net.URL;
/*    */ import javax.swing.AbstractAction;
/*    */ import javax.swing.JOptionPane;
/*    */ 
/*    */ class CheckForUpdatesAction
/*    */   extends AbstractAction {
/*    */   private static final String UPDATE_URL = "http://tvtagger.googlepages.com/version.txt";
/*    */   private final Tagger myTagger;
/*    */   
/*    */   CheckForUpdatesAction(Tagger tagger) {
/* 16 */     super(Tagger.getString("check_for_updates.menu.label"));
/* 17 */     putValue("MnemonicKey", Integer.valueOf(Tagger.getMnemonic("check_for_updates.menu.mnemonic")));
/*    */     
/* 19 */     this.myTagger = tagger;
/*    */   }
/*    */   public void actionPerformed(ActionEvent e) {
/*    */     try {
/*    */       String message;
/* 24 */       BufferedReader br = new BufferedReader(new InputStreamReader((new URL("http://tvtagger.googlepages.com/version.txt")).openStream()));
/* 25 */       int latest = Integer.parseInt(br.readLine());
/* 26 */       br.close();
/*    */ 
/*    */       
/* 29 */       if (latest > Tagger.getVersion()) {
/* 30 */         message = Tagger.getString("check_for_updates.dialog.updatesAvailable");
/*    */       } else {
/* 32 */         message = Tagger.getString("check_for_updates.dialog.noUpdatesAvailable");
/*    */       } 
/* 34 */       JOptionPane.showMessageDialog(this.myTagger, message, Tagger.getString("check_for_updates.dialog.title"), 1);
/* 35 */     } catch (Exception ex) {
/* 36 */       this.myTagger.showError(Tagger.getString("error.check_for_updates"), ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\Users\na\Desktop\TaggerJava6.zip!\TaggerJava6\Tagger.jar!\tagger\CheckForUpdatesAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */