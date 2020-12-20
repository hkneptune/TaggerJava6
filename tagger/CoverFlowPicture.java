/*     */ package tagger;
/*     */ 
/*     */ import java.awt.AlphaComposite;
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.Color;
/*     */ import java.awt.Composite;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.GradientPaint;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.Image;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.ImageObserver;
/*     */ import java.io.File;
/*     */ import javax.imageio.ImageIO;
/*     */ import javax.swing.JLabel;
/*     */ 
/*     */ public class CoverFlowPicture
/*     */   extends JLabel {
/*     */   private final File myFile;
/*     */   private boolean myIsSelected;
/*     */   private final BufferedImage myOriginalImage;
/*  28 */   private BufferedImage myBufferedImage = null;
/*  29 */   private AlphaComposite myAlphaComposite = AlphaComposite.getInstance(3, 1.0F);
/*     */   
/*     */   CoverFlowPicture(File file, int width) throws Exception {
/*  32 */     this.myFile = file;
/*  33 */     this.myOriginalImage = setupImage();
/*  34 */     setPreferredSize(new Dimension(width, this.myOriginalImage.getHeight(null) * width / this.myOriginalImage.getWidth(null)));
/*     */   }
/*     */   
/*     */   File getFile() {
/*  38 */     return this.myFile;
/*     */   }
/*     */   
/*     */   BufferedImage getImage() {
/*  42 */     return this.myOriginalImage;
/*     */   }
/*     */   
/*     */   private BufferedImage setupImage() throws Exception {
/*  46 */     BufferedImage image = ImageIO.read(this.myFile);
/*     */     
/*  48 */     GraphicsConfiguration configuration = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
/*     */ 
/*     */ 
/*     */     
/*  52 */     BufferedImage originalImage = configuration.createCompatibleImage(image.getWidth(null), (int)(image.getHeight(null) * 1.5D), 3);
/*     */ 
/*     */     
/*  55 */     Graphics2D g = originalImage.createGraphics();
/*     */     
/*  57 */     g.drawImage(image, 0, 0, (ImageObserver)null);
/*  58 */     g.dispose();
/*  59 */     BufferedImage reflection = configuration.createCompatibleImage(image.getWidth(null), image.getHeight(null), 3);
/*  60 */     g = reflection.createGraphics();
/*  61 */     int drawHeight = image.getHeight(null);
/*  62 */     AffineTransform tranform = AffineTransform.getScaleInstance(1.0D, -1.0D);
/*  63 */     tranform.translate(0.0D, -drawHeight);
/*     */ 
/*     */     
/*  66 */     AffineTransform oldTransform = g.getTransform();
/*  67 */     g.setTransform(tranform);
/*  68 */     g.drawImage(image, 0, 0, image.getWidth(null), drawHeight, 0, 0, image.getWidth(null), image.getHeight(null), null);
/*  69 */     g.setTransform(oldTransform);
/*     */     
/*  71 */     GradientPaint painter = new GradientPaint(0.0F, 0.0F, new Color(0.0F, 0.0F, 0.0F, 0.5F), 0.0F, drawHeight / 2.0F, new Color(0.0F, 0.0F, 0.0F, 1.0F));
/*     */ 
/*     */     
/*  74 */     g.setComposite(AlphaComposite.DstOut);
/*  75 */     g.setPaint(painter);
/*     */     
/*  77 */     g.fill(new Rectangle2D.Double(0.0D, 0.0D, image.getWidth(null), drawHeight));
/*  78 */     g.dispose();
/*     */     
/*  80 */     g = originalImage.createGraphics();
/*  81 */     g.drawImage(reflection, 0, drawHeight, (ImageObserver)null);
/*  82 */     g.dispose();
/*     */ 
/*     */     
/*  85 */     this.myBufferedImage = originalImage;
/*  86 */     setPreferredSize(new Dimension(image.getWidth(null), image.getHeight(null)));
/*  87 */     return image;
/*     */   }
/*     */   
/*     */   public Dimension getPreferredSize() {
/*  91 */     Dimension d = super.getPreferredSize();
/*  92 */     d.height = (int)(d.height * 1.5D);
/*     */     
/*  94 */     return d;
/*     */   }
/*     */   
/*     */   void setSelected(boolean selected) {
/*  98 */     this.myIsSelected = selected;
/*     */   }
/*     */ 
/*     */   
/*     */   public void paint(Graphics graphics) {
/* 103 */     if (getX() + getWidth() < 0 && getY() + getHeight() < 0) {
/*     */       return;
/*     */     }
/* 106 */     Graphics2D g = (Graphics2D)graphics;
/* 107 */     Image image = this.myBufferedImage;
/*     */     
/* 109 */     int drawHeight = (int)(getHeight() / 1.5D);
/*     */     
/* 111 */     g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/* 112 */     g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
/*     */ 
/*     */     
/* 115 */     Composite oldAc = g.getComposite();
/* 116 */     g.setComposite(this.myAlphaComposite);
/* 117 */     g.drawImage(image, 0, 0, getWidth(), getHeight(), 0, 0, image.getWidth(null), image.getHeight(null), null);
/* 118 */     g.setComposite(oldAc);
/*     */     
/* 120 */     if (this.myIsSelected) {
/* 121 */       g.setColor(Color.BLUE);
/* 122 */       g.setStroke(new BasicStroke(4.0F));
/* 123 */       g.drawRect(0, 0, getWidth(), (int)(getHeight() / 1.5D));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\Users\na\Desktop\TaggerJava6.zip!\TaggerJava6\Tagger.jar!\tagger\CoverFlowPicture.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */