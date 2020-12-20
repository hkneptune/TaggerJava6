/*     */ package tagger;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import javax.swing.Spring;
/*     */ import javax.swing.SpringLayout;
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
/*     */ public class SpringUtilities
/*     */ {
/*     */   public static void printSizes(Component c) {
/*  20 */     System.out.println("minimumSize = " + c.getMinimumSize());
/*  21 */     System.out.println("preferredSize = " + c.getPreferredSize());
/*  22 */     System.out.println("maximumSize = " + c.getMaximumSize());
/*     */   }
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
/*     */   public static void makeGrid(Container parent, int rows, int cols, int initialX, int initialY, int xPad, int yPad) {
/*     */     SpringLayout layout;
/*     */     try {
/*  45 */       layout = (SpringLayout)parent.getLayout();
/*  46 */     } catch (ClassCastException exc) {
/*  47 */       System.err.println("The first argument to makeGrid must use SpringLayout.");
/*     */       
/*     */       return;
/*     */     } 
/*  51 */     Spring xPadSpring = Spring.constant(xPad);
/*  52 */     Spring yPadSpring = Spring.constant(yPad);
/*  53 */     Spring initialXSpring = Spring.constant(initialX);
/*  54 */     Spring initialYSpring = Spring.constant(initialY);
/*  55 */     int max = rows * cols;
/*     */ 
/*     */ 
/*     */     
/*  59 */     Spring maxWidthSpring = layout.getConstraints(parent.getComponent(0)).getWidth();
/*     */     
/*  61 */     Spring maxHeightSpring = layout.getConstraints(parent.getComponent(0)).getWidth();
/*     */     int i;
/*  63 */     for (i = 1; i < max; i++) {
/*  64 */       SpringLayout.Constraints cons = layout.getConstraints(parent.getComponent(i));
/*     */ 
/*     */       
/*  67 */       maxWidthSpring = Spring.max(maxWidthSpring, cons.getWidth());
/*  68 */       maxHeightSpring = Spring.max(maxHeightSpring, cons.getHeight());
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  73 */     for (i = 0; i < max; i++) {
/*  74 */       SpringLayout.Constraints cons = layout.getConstraints(parent.getComponent(i));
/*     */ 
/*     */       
/*  77 */       cons.setWidth(maxWidthSpring);
/*  78 */       cons.setHeight(maxHeightSpring);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  83 */     SpringLayout.Constraints lastCons = null;
/*  84 */     SpringLayout.Constraints lastRowCons = null;
/*  85 */     for (int j = 0; j < max; j++) {
/*  86 */       SpringLayout.Constraints cons = layout.getConstraints(parent.getComponent(j));
/*     */       
/*  88 */       if (j % cols == 0) {
/*  89 */         lastRowCons = lastCons;
/*  90 */         cons.setX(initialXSpring);
/*     */       } else {
/*  92 */         cons.setX(Spring.sum(lastCons.getConstraint("East"), xPadSpring));
/*     */       } 
/*     */ 
/*     */       
/*  96 */       if (j / cols == 0) {
/*  97 */         cons.setY(initialYSpring);
/*     */       } else {
/*  99 */         cons.setY(Spring.sum(lastRowCons.getConstraint("South"), yPadSpring));
/*     */       } 
/*     */       
/* 102 */       lastCons = cons;
/*     */     } 
/*     */ 
/*     */     
/* 106 */     SpringLayout.Constraints pCons = layout.getConstraints(parent);
/* 107 */     pCons.setConstraint("South", Spring.sum(Spring.constant(yPad), lastCons.getConstraint("South")));
/*     */ 
/*     */ 
/*     */     
/* 111 */     pCons.setConstraint("East", Spring.sum(Spring.constant(xPad), lastCons.getConstraint("East")));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static SpringLayout.Constraints getConstraintsForCell(int row, int col, Container parent, int cols) {
/* 122 */     SpringLayout layout = (SpringLayout)parent.getLayout();
/* 123 */     Component c = parent.getComponent(row * cols + col);
/* 124 */     return layout.getConstraints(c);
/*     */   }
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
/*     */   public static void makeCompactGrid(Container parent, int rows, int cols, int initialX, int initialY, int xPad, int yPad) {
/*     */     SpringLayout layout;
/*     */     try {
/* 148 */       layout = (SpringLayout)parent.getLayout();
/* 149 */     } catch (ClassCastException exc) {
/* 150 */       System.err.println("The first argument to makeCompactGrid must use SpringLayout.");
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 155 */     Spring x = Spring.constant(initialX);
/* 156 */     for (int c = 0; c < cols; c++) {
/* 157 */       Spring width = Spring.constant(0); int i;
/* 158 */       for (i = 0; i < rows; i++) {
/* 159 */         width = Spring.max(width, getConstraintsForCell(i, c, parent, cols).getWidth());
/*     */       }
/*     */ 
/*     */       
/* 163 */       for (i = 0; i < rows; i++) {
/* 164 */         SpringLayout.Constraints constraints = getConstraintsForCell(i, c, parent, cols);
/*     */         
/* 166 */         constraints.setX(x);
/* 167 */         constraints.setWidth(width);
/*     */       } 
/*     */       
/* 170 */       if (c == cols - 1) {
/* 171 */         x = Spring.sum(x, width);
/*     */       } else {
/* 173 */         x = Spring.sum(x, Spring.sum(width, Spring.constant(xPad)));
/*     */       } 
/*     */     } 
/*     */     
/* 177 */     Spring y = Spring.constant(initialY);
/* 178 */     for (int r = 0; r < rows; r++) {
/* 179 */       Spring height = Spring.constant(0); int i;
/* 180 */       for (i = 0; i < cols; i++) {
/* 181 */         height = Spring.max(height, getConstraintsForCell(r, i, parent, cols).getHeight());
/*     */       }
/*     */ 
/*     */       
/* 185 */       for (i = 0; i < cols; i++) {
/* 186 */         SpringLayout.Constraints constraints = getConstraintsForCell(r, i, parent, cols);
/*     */         
/* 188 */         constraints.setY(y);
/* 189 */         constraints.setHeight(height);
/*     */       } 
/* 191 */       y = Spring.sum(y, Spring.sum(height, Spring.constant(yPad)));
/*     */     } 
/*     */ 
/*     */     
/* 195 */     SpringLayout.Constraints pCons = layout.getConstraints(parent);
/* 196 */     pCons.setConstraint("South", y);
/* 197 */     pCons.setConstraint("East", x);
/*     */   }
/*     */ }


/* Location:              D:\Users\na\Desktop\TaggerJava6.zip!\TaggerJava6\Tagger.jar!\tagger\SpringUtilities.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */