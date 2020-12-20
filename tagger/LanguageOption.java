/*    */ package tagger;
/*    */ 
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.ActionListener;
/*    */ import java.util.HashMap;
/*    */ import java.util.Locale;
/*    */ import javax.swing.ButtonGroup;
/*    */ import javax.swing.ButtonModel;
/*    */ import javax.swing.JMenu;
/*    */ import javax.swing.JRadioButtonMenuItem;
/*    */ 
/*    */ class LanguageOption extends JMenu implements ActionListener {
/*    */   private final ButtonGroup myGroup;
/*    */   private final HashMap<ButtonModel, String> myModels;
/*    */   
/*    */   LanguageOption() {
/* 17 */     super(Tagger.getString("language.menu.label"));
/* 18 */     setMnemonic(Tagger.getMnemonic("language.menu.mnemonic"));
/*    */     
/* 20 */     this.myGroup = new ButtonGroup();
/* 21 */     this.myModels = new HashMap<ButtonModel, String>(3);
/*    */     
/* 23 */     JRadioButtonMenuItem myEnglishItem = new JRadioButtonMenuItem(Locale.ENGLISH.getDisplayName(Tagger.LOCALE));
/* 24 */     myEnglishItem.addActionListener(this);
/* 25 */     this.myGroup.add(myEnglishItem);
/* 26 */     this.myModels.put(myEnglishItem.getModel(), "en");
/* 27 */     add(myEnglishItem);
/*    */     
/* 29 */     JRadioButtonMenuItem myGermanItem = new JRadioButtonMenuItem(Locale.GERMAN.getDisplayName(Tagger.LOCALE));
/* 30 */     myGermanItem.addActionListener(this);
/* 31 */     this.myGroup.add(myGermanItem);
/* 32 */     this.myModels.put(myGermanItem.getModel(), "de");
/* 33 */     add(myGermanItem);
/*    */     
/* 35 */     this.myGroup.setSelected(myEnglishItem.getModel(), Tagger.LOCALE.getLanguage().equals("en"));
/* 36 */     this.myGroup.setSelected(myGermanItem.getModel(), Tagger.LOCALE.getLanguage().equals("de"));
/*    */   }
/*    */   
/*    */   public void actionPerformed(ActionEvent e) {
/* 40 */     Tagger.OPTIONS.put("language", this.myModels.get(this.myGroup.getSelection()));
/*    */   }
/*    */ }


/* Location:              D:\Users\na\Desktop\TaggerJava6.zip!\TaggerJava6\Tagger.jar!\tagger\LanguageOption.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */