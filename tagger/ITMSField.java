/*    */ package tagger;
/*    */ 
/*    */ import javax.swing.JCheckBox;
/*    */ import javax.swing.JPanel;
/*    */ import javax.swing.JTextField;
/*    */ import javax.swing.SpringLayout;
/*    */ 
/*    */ class ITMSField extends JPanel {
/*    */   private final JTextField myField;
/*    */   private final JCheckBox myCheckBox;
/*    */   
/*    */   ITMSField(Tagger tagger) {
/* 13 */     super(new SpringLayout());
/*    */     
/* 15 */     this.myField = new JTextField();
/* 16 */     this.myField.setEditable(false);
/* 17 */     add(this.myField);
/*    */     
/* 19 */     this.myCheckBox = new JCheckBox();
/* 20 */     this.myCheckBox.setEnabled(false);
/* 21 */     this.myCheckBox.setOpaque(false);
/* 22 */     add(this.myCheckBox);
/*    */     
/* 24 */     tagger.addListeners(this.myField);
/* 25 */     setOpaque(false);
/* 26 */     SpringUtilities.makeCompactGrid(this, 1, 2, 0, 0, 3, 0);
/*    */   }
/*    */   
/*    */   JTextField getField() {
/* 30 */     return this.myField;
/*    */   }
/*    */   
/*    */   void setText(String text) {
/* 34 */     this.myField.setText(text);
/* 35 */     this.myCheckBox.setSelected(true);
/* 36 */     this.myCheckBox.setEnabled(true);
/*    */   }
/*    */   
/*    */   boolean isSelected() {
/* 40 */     return this.myCheckBox.isSelected();
/*    */   }
/*    */ }


/* Location:              D:\Users\na\Desktop\TaggerJava6.zip!\TaggerJava6\Tagger.jar!\tagger\ITMSField.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */