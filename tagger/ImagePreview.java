/*    */ package tagger;
/*    */ 
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Graphics;
/*    */ import java.beans.PropertyChangeEvent;
/*    */ import java.beans.PropertyChangeListener;
/*    */ import java.io.File;
/*    */ import javax.swing.ImageIcon;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.JFileChooser;
/*    */ 
/*    */ class ImagePreview
/*    */   extends JComponent implements PropertyChangeListener {
/* 14 */   ImageIcon thumbnail = null;
/* 15 */   File file = null;
/*    */   
/*    */   ImagePreview(JFileChooser fc) {
/* 18 */     setPreferredSize(new Dimension(100, 50));
/* 19 */     fc.addPropertyChangeListener(this);
/*    */   }
/*    */   
/*    */   public void loadImage() {
/* 23 */     if (this.file == null) {
/* 24 */       this.thumbnail = null;
/*    */ 
/*    */       
/*    */       return;
/*    */     } 
/*    */ 
/*    */     
/* 31 */     ImageIcon tmpIcon = new ImageIcon(this.file.getPath());
/* 32 */     if (tmpIcon != null) {
/* 33 */       if (tmpIcon.getIconWidth() > 90) {
/* 34 */         this.thumbnail = new ImageIcon(tmpIcon.getImage().getScaledInstance(90, -1, 1));
/*    */       }
/*    */       else {
/*    */         
/* 38 */         this.thumbnail = tmpIcon;
/*    */       } 
/*    */     }
/*    */   }
/*    */   
/*    */   public void propertyChange(PropertyChangeEvent e) {
/* 44 */     boolean update = false;
/* 45 */     String prop = e.getPropertyName();
/*    */ 
/*    */     
/* 48 */     if ("directoryChanged".equals(prop)) {
/* 49 */       this.file = null;
/* 50 */       update = true;
/*    */     
/*    */     }
/* 53 */     else if ("SelectedFileChangedProperty".equals(prop)) {
/* 54 */       this.file = (File)e.getNewValue();
/* 55 */       update = true;
/*    */     } 
/*    */ 
/*    */     
/* 59 */     if (update) {
/* 60 */       this.thumbnail = null;
/* 61 */       if (isShowing()) {
/* 62 */         loadImage();
/* 63 */         repaint();
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   protected void paintComponent(Graphics g) {
/* 69 */     if (this.thumbnail == null) {
/* 70 */       loadImage();
/*    */     }
/* 72 */     if (this.thumbnail != null) {
/* 73 */       int x = getWidth() / 2 - this.thumbnail.getIconWidth() / 2;
/* 74 */       int y = getHeight() / 2 - this.thumbnail.getIconHeight() / 2;
/*    */       
/* 76 */       if (y < 0) {
/* 77 */         y = 0;
/*    */       }
/*    */       
/* 80 */       if (x < 5) {
/* 81 */         x = 5;
/*    */       }
/* 83 */       this.thumbnail.paintIcon(this, g, x, y);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\Users\na\Desktop\TaggerJava6.zip!\TaggerJava6\Tagger.jar!\tagger\ImagePreview.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */