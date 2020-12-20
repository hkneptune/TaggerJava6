/*    */ package tagger;
/*    */ 
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.ActionListener;
/*    */ import javax.swing.JCheckBoxMenuItem;
/*    */ 
/*    */ class GleanInfoOption extends JCheckBoxMenuItem implements ActionListener {
/*    */   GleanInfoOption() {
/*  9 */     super(Tagger.getString("glean_info.menu.label"));
/* 10 */     setMnemonic(Tagger.getMnemonic("glean_info.menu.mnemonic"));
/* 11 */     setSelected(Tagger.OPTIONS.getBoolean("gleanName", true));
/* 12 */     addActionListener(this);
/*    */   }
/*    */   
/*    */   public void actionPerformed(ActionEvent e) {
/* 16 */     Tagger.OPTIONS.putBoolean("gleanName", isSelected());
/*    */   }
/*    */ }


/* Location:              D:\Users\na\Desktop\TaggerJava6.zip!\TaggerJava6\Tagger.jar!\tagger\GleanInfoOption.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */