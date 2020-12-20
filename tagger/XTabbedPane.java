/*     */ package tagger;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.SystemColor;
/*     */ import java.awt.datatransfer.DataFlavor;
/*     */ import java.awt.datatransfer.Transferable;
/*     */ import java.awt.dnd.DragGestureEvent;
/*     */ import java.awt.dnd.DragGestureListener;
/*     */ import java.awt.dnd.DragSource;
/*     */ import java.awt.dnd.DragSourceDragEvent;
/*     */ import java.awt.dnd.DragSourceDropEvent;
/*     */ import java.awt.dnd.DragSourceEvent;
/*     */ import java.awt.dnd.DragSourceListener;
/*     */ import java.awt.dnd.DropTarget;
/*     */ import java.awt.dnd.DropTargetDragEvent;
/*     */ import java.awt.dnd.DropTargetDropEvent;
/*     */ import java.awt.dnd.DropTargetEvent;
/*     */ import java.awt.dnd.DropTargetListener;
/*     */ import java.awt.dnd.InvalidDnDOperationException;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.image.BufferedImage;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JTabbedPane;
/*     */ import javax.swing.SwingUtilities;
/*     */ 
/*     */ class XTabbedPane
/*     */   extends JTabbedPane
/*     */ {
/*     */   private static final int LINEWIDTH = 3;
/*     */   private static final String NAME = "test";
/*  36 */   private final GhostGlassPane glassPane = new GhostGlassPane();
/*  37 */   private final Rectangle2D lineRect = new Rectangle2D.Double();
/*  38 */   private final DragSource dragSource = new DragSource();
/*     */   private final DropTarget dropTarget;
/*  40 */   private int dragTabIndex = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hasghost;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   class CDropTargetListener
/*     */     implements DropTargetListener
/*     */   {
/*     */     public void dragEnter(DropTargetDragEvent e) {
/* 103 */       if (isDragAcceptable(e)) { e.acceptDrag(e.getDropAction()); }
/* 104 */       else { e.rejectDrag(); }
/*     */     
/*     */     } public void dragExit(DropTargetEvent e) {}
/*     */     public void dropActionChanged(DropTargetDragEvent e) {}
/*     */     public void dragOver(DropTargetDragEvent e) {
/* 109 */       XTabbedPane.this.initTargetLine(XTabbedPane.this.getTargetTabIndex(e.getLocation()));
/* 110 */       if (XTabbedPane.this.hasGhost()) {
/* 111 */         XTabbedPane.this.glassPane.setPoint(e.getLocation());
/* 112 */         XTabbedPane.this.glassPane.repaint();
/*     */       } 
/*     */     }
/*     */     
/*     */     public void drop(DropTargetDropEvent e) {
/* 117 */       Transferable t = e.getTransferable();
/* 118 */       DataFlavor[] f = t.getTransferDataFlavors();
/* 119 */       if (isDropAcceptable(e)) {
/* 120 */         XTabbedPane.this.convertTab(XTabbedPane.this.dragTabIndex, XTabbedPane.this.getTargetTabIndex(e.getLocation()));
/* 121 */         e.dropComplete(true);
/*     */       } else {
/* 123 */         e.dropComplete(false);
/*     */       } 
/* 125 */       XTabbedPane.this.repaint();
/*     */     }
/*     */     public boolean isDragAcceptable(DropTargetDragEvent e) {
/* 128 */       Transferable t = e.getTransferable();
/* 129 */       if (t == null) return false; 
/* 130 */       DataFlavor[] f = e.getCurrentDataFlavors();
/* 131 */       if (t.isDataFlavorSupported(f[0]) && XTabbedPane.this.dragTabIndex >= 0) {
/* 132 */         return true;
/*     */       }
/* 134 */       return false;
/*     */     }
/*     */     public boolean isDropAcceptable(DropTargetDropEvent e) {
/* 137 */       Transferable t = e.getTransferable();
/* 138 */       if (t == null) return false; 
/* 139 */       DataFlavor[] f = t.getTransferDataFlavors();
/* 140 */       Point p = (Point)e.getLocation().clone();
/* 141 */       if (t.isDataFlavorSupported(f[0]) && XTabbedPane.this.dragTabIndex >= 0 && XTabbedPane.this.dragTabIndex != XTabbedPane.this.indexAtLocation(p.x, p.y)) {
/* 142 */         return true;
/*     */       }
/* 144 */       return false;
/*     */     }
/*     */   }
/*     */   
/* 148 */   XTabbedPane(JFrame frame) { this.hasghost = true; final DragSourceListener dsl = new DragSourceListener() { public void dragEnter(DragSourceDragEvent e) { e.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop); } public void dragExit(DragSourceEvent e) { e.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop); XTabbedPane.this.lineRect.setRect(0.0D, 0.0D, 0.0D, 0.0D); XTabbedPane.this.glassPane.setPoint(new Point(-1000, -1000)); XTabbedPane.this.glassPane.repaint(); } public void dragOver(DragSourceDragEvent e) { Point p = (Point)e.getLocation().clone(); SwingUtilities.convertPointFromScreen(p, XTabbedPane.this); if (XTabbedPane.this.getTabAreaBound().contains(p)) { e.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop); } else { e.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop); }  } public void dragDropEnd(DragSourceDropEvent e) { XTabbedPane.this.lineRect.setRect(0.0D, 0.0D, 0.0D, 0.0D); XTabbedPane.this.dragTabIndex = -1; if (XTabbedPane.this.hasGhost()) { XTabbedPane.this.glassPane.setVisible(false); XTabbedPane.this.glassPane.setImage((BufferedImage)null); }  } public void dropActionChanged(DragSourceDragEvent e) {} }
/*     */       ; final Transferable t = new Transferable() { private final DataFlavor FLAVOR = new DataFlavor("application/x-java-jvm-local-objectref", "test"); public Object getTransferData(DataFlavor flavor) { return XTabbedPane.this; } public DataFlavor[] getTransferDataFlavors() { DataFlavor[] f = new DataFlavor[1]; f[0] = this.FLAVOR; return f; } public boolean isDataFlavorSupported(DataFlavor flavor) { return flavor.getHumanPresentableName().equals("test"); } }
/* 150 */       ; DragGestureListener dgl = new DragGestureListener() { public void dragGestureRecognized(DragGestureEvent e) { XTabbedPane.this.initGlassPane(e.getComponent(), e.getDragOrigin()); try { e.startDrag(DragSource.DefaultMoveDrop, t, dsl); } catch (InvalidDnDOperationException idoe) {} } }; this.dropTarget = new DropTarget(this.glassPane, 3, new CDropTargetListener(), true); this.dragSource.createDefaultDragGestureRecognizer(this, 3, dgl); frame.setGlassPane(this.glassPane); } public void setPaintGhost(boolean flag) { this.hasghost = flag; }
/*     */   
/*     */   public boolean hasGhost() {
/* 153 */     return this.hasghost;
/*     */   }
/*     */   private int getTargetTabIndex(Point pt) {
/* 156 */     Point p = (Point)pt.clone();
/* 157 */     SwingUtilities.convertPointToScreen(p, this.glassPane);
/* 158 */     SwingUtilities.convertPointFromScreen(p, this);
/* 159 */     for (int i = 0; i < getTabCount(); i++) {
/* 160 */       Rectangle rectangle = getBoundsAt(i);
/* 161 */       rectangle.setRect((rectangle.x - rectangle.width / 2), rectangle.y, rectangle.width, rectangle.height);
/* 162 */       if (rectangle.contains(p)) {
/* 163 */         return i;
/*     */       }
/*     */     } 
/* 166 */     Rectangle rect = getBoundsAt(getTabCount() - 1);
/* 167 */     rect.setRect((rect.x + rect.width / 2), rect.y, (rect.width + 100), rect.height);
/* 168 */     if (rect.contains(p)) {
/* 169 */       return getTabCount();
/*     */     }
/* 171 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   private void convertTab(int prev, int next) {
/* 176 */     if (next < 0 || prev == next) {
/*     */       return;
/*     */     }
/*     */     
/* 180 */     Component tabComponent = getTabComponentAt(prev);
/* 181 */     Component cmp = getComponentAt(prev);
/* 182 */     String str = getTitleAt(prev);
/* 183 */     if (next == getTabCount()) {
/*     */       
/* 185 */       remove(prev);
/* 186 */       addTab(str, cmp);
/* 187 */       setTabComponentAt(getTabCount() - 1, tabComponent);
/* 188 */       setSelectedIndex(getTabCount() - 1);
/* 189 */     } else if (prev > next) {
/*     */       
/* 191 */       remove(prev);
/* 192 */       insertTab(str, (Icon)null, cmp, (String)null, next);
/* 193 */       setTabComponentAt(next, tabComponent);
/* 194 */       setSelectedIndex(next);
/*     */     } else {
/*     */       
/* 197 */       remove(prev);
/* 198 */       insertTab(str, (Icon)null, cmp, (String)null, next - 1);
/* 199 */       setTabComponentAt(next - 1, tabComponent);
/* 200 */       setSelectedIndex(next - 1);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void initTargetLine(int next) {
/* 205 */     if (next < 0 || this.dragTabIndex == next || next - this.dragTabIndex == 1) {
/* 206 */       this.lineRect.setRect(0.0D, 0.0D, 0.0D, 0.0D);
/* 207 */     } else if (next == getTabCount()) {
/* 208 */       Rectangle rect = getBoundsAt(getTabCount() - 1);
/* 209 */       this.lineRect.setRect((rect.x + rect.width - 1), rect.y, 3.0D, rect.height);
/* 210 */     } else if (next == 0) {
/* 211 */       Rectangle rect = getBoundsAt(0);
/* 212 */       this.lineRect.setRect(-1.0D, rect.y, 3.0D, rect.height);
/*     */     } else {
/* 214 */       Rectangle rect = getBoundsAt(next - 1);
/* 215 */       this.lineRect.setRect((rect.x + rect.width - 1), rect.y, 3.0D, rect.height);
/*     */     } 
/* 217 */     repaint();
/*     */   }
/*     */ 
/*     */   
/*     */   private void initGlassPane(Component c, Point pt) {
/* 222 */     Point p = (Point)pt.clone();
/* 223 */     this.dragTabIndex = indexAtLocation(p.x, p.y);
/*     */     
/* 225 */     if (hasGhost()) {
/* 226 */       Rectangle rect = getBoundsAt(this.dragTabIndex);
/* 227 */       BufferedImage image = new BufferedImage(c.getWidth(), c.getHeight(), 2);
/* 228 */       Graphics g = image.getGraphics();
/* 229 */       c.paint(g);
/* 230 */       image = image.getSubimage(rect.x, rect.y, rect.width, rect.height);
/* 231 */       this.glassPane.setImage(image);
/*     */     } 
/* 233 */     this.glassPane.setVisible(true);
/* 234 */     SwingUtilities.convertPointToScreen(p, c);
/* 235 */     SwingUtilities.convertPointFromScreen(p, this.glassPane);
/* 236 */     this.glassPane.setPoint(p);
/*     */   }
/*     */   
/*     */   private Rectangle getTabAreaBound() {
/* 240 */     Rectangle lastTab = getUI().getTabBounds(this, getTabCount() - 1);
/* 241 */     return new Rectangle(0, 0, getWidth(), lastTab.y + lastTab.height);
/*     */   }
/*     */   
/*     */   public void paintComponent(Graphics g) {
/* 245 */     super.paintComponent(g);
/* 246 */     if (this.dragTabIndex >= 0) {
/* 247 */       Graphics2D g2 = (Graphics2D)g;
/* 248 */       g2.setColor(SystemColor.activeCaption);
/* 249 */       g2.fill(this.lineRect);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\Users\na\Desktop\TaggerJava6.zip!\TaggerJava6\Tagger.jar!\tagger\XTabbedPane.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */