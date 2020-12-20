/*    */ package tagger;
/*    */ 
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.ActionListener;
/*    */ import javax.swing.JCheckBoxMenuItem;
/*    */ 
/*    */ class CloseOnSaveOption extends JCheckBoxMenuItem implements ActionListener {
/*    */   CloseOnSaveOption() {
/*  9 */     super(Tagger.getString("close_on_save.menu.label"));
/* 10 */     setMnemonic(Tagger.getMnemonic("close_on_save.menu.mnemonic"));
/* 11 */     setSelected(Tagger.OPTIONS.getBoolean("closeTabsOnSave", false));
/* 12 */     addActionListener(this);
/*    */   }
/*    */   
/*    */   public void actionPerformed(ActionEvent e) {
/* 16 */     Tagger.OPTIONS.putBoolean("closeTabsOnSave", isSelected());
/*    */   }
/*    */ }


/* Location:              D:\Users\na\Desktop\TaggerJava6.zip!\TaggerJava6\Tagger.jar!\tagger\CloseOnSaveOption.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */