/*    */ package tagger;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.event.MouseAdapter;
/*    */ import java.awt.event.MouseEvent;
/*    */ import java.io.File;
/*    */ import java.util.ArrayList;
/*    */ import javax.swing.JPanel;
/*    */ import javax.swing.JPopupMenu;
/*    */ import javax.swing.JScrollPane;
/*    */ import net.iharder.dnd.FileDrop;
/*    */ 
/*    */ class JCoverFlow
/*    */   extends JScrollPane implements FileDrop.Listener {
/*    */   private final Tagger myTagger;
/*    */   private final JPanel myPanel;
/*    */   
/* 18 */   private final MouseAdapter myPopupListener = new MouseAdapter() {
/*    */       public void mousePressed(MouseEvent e) {
/* 20 */         for (CoverFlowPicture cfp : JCoverFlow.this.myPictures) {
/* 21 */           cfp.setSelected(false);
/*    */         }
/* 23 */         JCoverFlow.this.mySelectedPicture = (CoverFlowPicture)e.getComponent();
/* 24 */         JCoverFlow.this.mySelectedPicture.setSelected(true);
/* 25 */         JCoverFlow.this.repaint();
/* 26 */         JCoverFlow.this.myTagger.getExportArtworkAction().update();
/* 27 */         JCoverFlow.this.myTagger.getRemoveArtworkAction().update();
/*    */       }
/*    */       
/*    */       public void mouseClicked(MouseEvent e) {
/* 31 */         if (e.isPopupTrigger())
/* 32 */           JCoverFlow.this.myPopup.show(e.getComponent(), e.getX() + 5, e.getY() + 5); 
/*    */       }
/*    */       
/*    */       public void mouseReleased(MouseEvent e) {
/* 36 */         if (e.isPopupTrigger())
/* 37 */           JCoverFlow.this.myPopup.show(e.getComponent(), e.getX() + 5, e.getY() + 5); 
/*    */       }
/*    */     };
/*    */   private final JPopupMenu myPopup; private final ArrayList<CoverFlowPicture> myPictures;
/*    */   private CoverFlowPicture mySelectedPicture;
/*    */   
/*    */   JCoverFlow(Tagger tagger) {
/* 44 */     super(21, 30);
/* 45 */     this.myTagger = tagger;
/*    */     
/* 47 */     this.myPictures = new ArrayList<CoverFlowPicture>();
/*    */     
/* 49 */     this.myPanel = new JPanel();
/* 50 */     this.myPanel.setBackground(Color.BLACK);
/* 51 */     setViewportView(this.myPanel);
/*    */     
/* 53 */     this.myPopup = new JPopupMenu();
/* 54 */     this.myPopup.add(this.myTagger.getExportArtworkAction());
/* 55 */     this.myPopup.add(this.myTagger.getRemoveArtworkAction());
/*    */     
/* 57 */     new FileDrop(this, null, this);
/*    */   }
/*    */   
/*    */   CoverFlowPicture getSelectedPicture() {
/* 61 */     return this.mySelectedPicture;
/*    */   }
/*    */   
/*    */   void add(File image) throws Exception {
/* 65 */     CoverFlowPicture cfp = new CoverFlowPicture(image, 350);
/* 66 */     this.myPanel.add(cfp);
/* 67 */     this.myPictures.add(cfp);
/* 68 */     cfp.addMouseListener(this.myPopupListener);
/* 69 */     invalidate();
/* 70 */     validate();
/*    */   }
/*    */   
/*    */   void remove(CoverFlowPicture cfp) {
/* 74 */     this.myPictures.remove(cfp);
/* 75 */     this.myPanel.remove(cfp);
/* 76 */     repaint();
/*    */     
/* 78 */     this.mySelectedPicture = null;
/* 79 */     this.myTagger.getExportArtworkAction().update();
/* 80 */     this.myTagger.getRemoveArtworkAction().update();
/*    */   }
/*    */   
/*    */   public void filesDropped(File[] files) {
/* 84 */     for (File file : files) {
/* 85 */       if (Tagger.IMAGE_FILTER.accept(file))
/*    */         try {
/* 87 */           add(file);
/* 88 */         } catch (Exception e) {} 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\Users\na\Desktop\TaggerJava6.zip!\TaggerJava6\Tagger.jar!\tagger\JCoverFlow.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */