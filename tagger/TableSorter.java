/*     */ package tagger;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.event.TableModelEvent;
/*     */ import javax.swing.event.TableModelListener;
/*     */ import javax.swing.table.AbstractTableModel;
/*     */ import javax.swing.table.JTableHeader;
/*     */ import javax.swing.table.TableCellRenderer;
/*     */ import javax.swing.table.TableColumnModel;
/*     */ import javax.swing.table.TableModel;
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
/*     */ public class TableSorter
/*     */   extends AbstractTableModel
/*     */ {
/*     */   protected TableModel tableModel;
/*     */   public static final int DESCENDING = -1;
/*     */   public static final int NOT_SORTED = 0;
/*     */   public static final int ASCENDING = 1;
/*  86 */   private static Directive EMPTY_DIRECTIVE = new Directive(-1, 0);
/*     */   
/*  88 */   public static final Comparator COMPARABLE_COMAPRATOR = new Comparator() {
/*     */       public int compare(Object o1, Object o2) {
/*  90 */         return ((Comparable<Object>)o1).compareTo(o2);
/*     */       }
/*     */     };
/*  93 */   public static final Comparator LEXICAL_COMPARATOR = new Comparator() {
/*     */       public int compare(Object o1, Object o2) {
/*  95 */         return o1.toString().compareTo(o2.toString());
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   private Row[] viewToModel;
/*     */   private int[] modelToView;
/*     */   private JTableHeader tableHeader;
/*     */   private MouseListener mouseListener;
/*     */   private TableModelListener tableModelListener;
/* 105 */   private Map<Class, Comparator> columnComparators = (Map)new HashMap<Class<?>, Comparator>();
/* 106 */   private List<Directive> sortingColumns = new ArrayList<Directive>();
/*     */   
/*     */   public TableSorter() {
/* 109 */     this.mouseListener = new MouseHandler();
/* 110 */     this.tableModelListener = new TableModelHandler();
/*     */   }
/*     */   
/*     */   public TableSorter(TableModel tableModel) {
/* 114 */     this();
/* 115 */     setTableModel(tableModel);
/*     */   }
/*     */   
/*     */   public TableSorter(TableModel tableModel, JTableHeader tableHeader) {
/* 119 */     this();
/* 120 */     setTableHeader(tableHeader);
/* 121 */     setTableModel(tableModel);
/*     */   }
/*     */   
/*     */   private void clearSortingState() {
/* 125 */     this.viewToModel = null;
/* 126 */     this.modelToView = null;
/*     */   }
/*     */   
/*     */   public TableModel getTableModel() {
/* 130 */     return this.tableModel;
/*     */   }
/*     */   
/*     */   public void setTableModel(TableModel tableModel) {
/* 134 */     if (this.tableModel != null) {
/* 135 */       this.tableModel.removeTableModelListener(this.tableModelListener);
/*     */     }
/*     */     
/* 138 */     this.tableModel = tableModel;
/* 139 */     if (this.tableModel != null) {
/* 140 */       this.tableModel.addTableModelListener(this.tableModelListener);
/*     */     }
/*     */     
/* 143 */     clearSortingState();
/* 144 */     fireTableStructureChanged();
/*     */   }
/*     */   
/*     */   public JTableHeader getTableHeader() {
/* 148 */     return this.tableHeader;
/*     */   }
/*     */   
/*     */   public void setTableHeader(JTableHeader tableHeader) {
/* 152 */     if (this.tableHeader != null) {
/* 153 */       this.tableHeader.removeMouseListener(this.mouseListener);
/* 154 */       TableCellRenderer defaultRenderer = this.tableHeader.getDefaultRenderer();
/* 155 */       if (defaultRenderer instanceof SortableHeaderRenderer) {
/* 156 */         this.tableHeader.setDefaultRenderer(((SortableHeaderRenderer)defaultRenderer).tableCellRenderer);
/*     */       }
/*     */     } 
/* 159 */     this.tableHeader = tableHeader;
/* 160 */     if (this.tableHeader != null) {
/* 161 */       this.tableHeader.addMouseListener(this.mouseListener);
/* 162 */       this.tableHeader.setDefaultRenderer(new SortableHeaderRenderer(this.tableHeader.getDefaultRenderer()));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSorting() {
/* 168 */     return (this.sortingColumns.size() != 0);
/*     */   }
/*     */   
/*     */   private Directive getDirective(int column) {
/* 172 */     for (int i = 0; i < this.sortingColumns.size(); i++) {
/* 173 */       Directive directive = this.sortingColumns.get(i);
/* 174 */       if (directive.column == column) {
/* 175 */         return directive;
/*     */       }
/*     */     } 
/* 178 */     return EMPTY_DIRECTIVE;
/*     */   }
/*     */   
/*     */   public int getSortingStatus(int column) {
/* 182 */     return (getDirective(column)).direction;
/*     */   }
/*     */   
/*     */   private void sortingStatusChanged() {
/* 186 */     clearSortingState();
/* 187 */     fireTableDataChanged();
/* 188 */     if (this.tableHeader != null) {
/* 189 */       this.tableHeader.repaint();
/*     */     }
/*     */   }
/*     */   
/*     */   public void setSortingStatus(int column, int status) {
/* 194 */     Directive directive = getDirective(column);
/* 195 */     if (directive != EMPTY_DIRECTIVE) {
/* 196 */       this.sortingColumns.remove(directive);
/*     */     }
/* 198 */     if (status != 0) {
/* 199 */       this.sortingColumns.add(new Directive(column, status));
/*     */     }
/* 201 */     sortingStatusChanged();
/*     */   }
/*     */   
/*     */   protected Icon getHeaderRendererIcon(int column, int size) {
/* 205 */     Directive directive = getDirective(column);
/* 206 */     if (directive == EMPTY_DIRECTIVE) {
/* 207 */       return null;
/*     */     }
/* 209 */     return new Arrow((directive.direction == -1), size, this.sortingColumns.indexOf(directive));
/*     */   }
/*     */   
/*     */   private void cancelSorting() {
/* 213 */     this.sortingColumns.clear();
/* 214 */     sortingStatusChanged();
/*     */   }
/*     */   
/*     */   public void setColumnComparator(Class type, Comparator comparator) {
/* 218 */     if (comparator == null) {
/* 219 */       this.columnComparators.remove(type);
/*     */     } else {
/* 221 */       this.columnComparators.put(type, comparator);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected Comparator getComparator(int column) {
/* 226 */     Class<?> columnType = this.tableModel.getColumnClass(column);
/* 227 */     Comparator comparator = this.columnComparators.get(columnType);
/* 228 */     if (comparator != null) {
/* 229 */       return comparator;
/*     */     }
/* 231 */     if (Comparable.class.isAssignableFrom(columnType)) {
/* 232 */       return COMPARABLE_COMAPRATOR;
/*     */     }
/* 234 */     return LEXICAL_COMPARATOR;
/*     */   }
/*     */   
/*     */   private Row[] getViewToModel() {
/* 238 */     if (this.viewToModel == null) {
/* 239 */       int tableModelRowCount = this.tableModel.getRowCount();
/* 240 */       this.viewToModel = new Row[tableModelRowCount];
/* 241 */       for (int row = 0; row < tableModelRowCount; row++) {
/* 242 */         this.viewToModel[row] = new Row(row);
/*     */       }
/*     */       
/* 245 */       if (isSorting()) {
/* 246 */         Arrays.sort((Object[])this.viewToModel);
/*     */       }
/*     */     } 
/* 249 */     return this.viewToModel;
/*     */   }
/*     */   
/*     */   public int modelIndex(int viewIndex) {
/* 253 */     return (getViewToModel()[viewIndex]).modelIndex;
/*     */   }
/*     */   
/*     */   public int viewIndex(int modelIndex) {
/* 257 */     return getModelToView()[modelIndex];
/*     */   }
/*     */   
/*     */   private int[] getModelToView() {
/* 261 */     if (this.modelToView == null) {
/* 262 */       int n = (getViewToModel()).length;
/* 263 */       this.modelToView = new int[n];
/* 264 */       for (int i = 0; i < n; i++) {
/* 265 */         this.modelToView[modelIndex(i)] = i;
/*     */       }
/*     */     } 
/* 268 */     return this.modelToView;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRowCount() {
/* 274 */     return (this.tableModel == null) ? 0 : this.tableModel.getRowCount();
/*     */   }
/*     */   
/*     */   public int getColumnCount() {
/* 278 */     return (this.tableModel == null) ? 0 : this.tableModel.getColumnCount();
/*     */   }
/*     */   
/*     */   public String getColumnName(int column) {
/* 282 */     return this.tableModel.getColumnName(column);
/*     */   }
/*     */   
/*     */   public Class getColumnClass(int column) {
/* 286 */     return this.tableModel.getColumnClass(column);
/*     */   }
/*     */   
/*     */   public boolean isCellEditable(int row, int column) {
/* 290 */     return this.tableModel.isCellEditable(modelIndex(row), column);
/*     */   }
/*     */   
/*     */   public Object getValueAt(int row, int column) {
/* 294 */     return this.tableModel.getValueAt(modelIndex(row), column);
/*     */   }
/*     */   
/*     */   public void setValueAt(Object aValue, int row, int column) {
/* 298 */     this.tableModel.setValueAt(aValue, modelIndex(row), column);
/*     */   }
/*     */   
/*     */   private class Row
/*     */     implements Comparable
/*     */   {
/*     */     private int modelIndex;
/*     */     
/*     */     public Row(int index) {
/* 307 */       this.modelIndex = index;
/*     */     }
/*     */     
/*     */     public int compareTo(Object o) {
/* 311 */       int row1 = this.modelIndex;
/* 312 */       int row2 = ((Row)o).modelIndex;
/*     */       
/* 314 */       for (Iterator<TableSorter.Directive> it = TableSorter.this.sortingColumns.iterator(); it.hasNext(); ) {
/* 315 */         TableSorter.Directive directive = it.next();
/* 316 */         int column = directive.column;
/* 317 */         Object o1 = TableSorter.this.tableModel.getValueAt(row1, column);
/* 318 */         Object o2 = TableSorter.this.tableModel.getValueAt(row2, column);
/*     */         
/* 320 */         int comparison = 0;
/*     */         
/* 322 */         if (o1 == null && o2 == null) {
/* 323 */           comparison = 0;
/* 324 */         } else if (o1 == null) {
/* 325 */           comparison = -1;
/* 326 */         } else if (o2 == null) {
/* 327 */           comparison = 1;
/*     */         } else {
/* 329 */           comparison = TableSorter.this.getComparator(column).compare(o1, o2);
/*     */         } 
/* 331 */         if (comparison != 0) {
/* 332 */           return (directive.direction == -1) ? -comparison : comparison;
/*     */         }
/*     */       } 
/* 335 */       return 0;
/*     */     }
/*     */   }
/*     */   
/*     */   private class TableModelHandler implements TableModelListener { private TableModelHandler() {}
/*     */     
/*     */     public void tableChanged(TableModelEvent e) {
/* 342 */       if (!TableSorter.this.isSorting()) {
/* 343 */         TableSorter.this.clearSortingState();
/* 344 */         TableSorter.this.fireTableChanged(e);
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */ 
/*     */       
/* 351 */       if (e.getFirstRow() == -1) {
/* 352 */         TableSorter.this.cancelSorting();
/* 353 */         TableSorter.this.fireTableChanged(e);
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
/*     */         return;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 375 */       int column = e.getColumn();
/* 376 */       if (e.getFirstRow() == e.getLastRow() && column != -1 && TableSorter.this.getSortingStatus(column) == 0 && TableSorter.this.modelToView != null) {
/*     */ 
/*     */ 
/*     */         
/* 380 */         int viewIndex = TableSorter.this.getModelToView()[e.getFirstRow()];
/* 381 */         TableSorter.this.fireTableChanged(new TableModelEvent(TableSorter.this, viewIndex, viewIndex, column, e.getType()));
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */ 
/*     */       
/* 388 */       TableSorter.this.clearSortingState();
/* 389 */       TableSorter.this.fireTableDataChanged();
/*     */     } }
/*     */   
/*     */   private class MouseHandler extends MouseAdapter {
/*     */     private MouseHandler() {}
/*     */     
/*     */     public void mouseClicked(MouseEvent e) {
/* 396 */       JTableHeader h = (JTableHeader)e.getSource();
/* 397 */       TableColumnModel columnModel = h.getColumnModel();
/* 398 */       int viewColumn = columnModel.getColumnIndexAtX(e.getX());
/* 399 */       int column = columnModel.getColumn(viewColumn).getModelIndex();
/* 400 */       if (column != -1) {
/* 401 */         int status = TableSorter.this.getSortingStatus(column);
/* 402 */         if (!e.isControlDown()) {
/* 403 */           TableSorter.this.cancelSorting();
/*     */         }
/*     */ 
/*     */         
/* 407 */         status += e.isShiftDown() ? -1 : 1;
/* 408 */         status = (status + 4) % 3 - 1;
/* 409 */         TableSorter.this.setSortingStatus(column, status);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class Arrow implements Icon {
/*     */     private boolean descending;
/*     */     private int size;
/*     */     private int priority;
/*     */     
/*     */     public Arrow(boolean descending, int size, int priority) {
/* 420 */       this.descending = descending;
/* 421 */       this.size = size;
/* 422 */       this.priority = priority;
/*     */     }
/*     */     
/*     */     public void paintIcon(Component c, Graphics g, int x, int y) {
/* 426 */       Color color = (c == null) ? Color.GRAY : c.getBackground();
/*     */ 
/*     */       
/* 429 */       int dx = (int)((this.size / 2) * Math.pow(0.8D, this.priority));
/* 430 */       int dy = this.descending ? dx : -dx;
/*     */       
/* 432 */       y = y + 5 * this.size / 6 + (this.descending ? -dy : 0);
/* 433 */       int shift = this.descending ? 1 : -1;
/* 434 */       g.translate(x, y);
/*     */ 
/*     */       
/* 437 */       g.setColor(color.darker());
/* 438 */       g.drawLine(dx / 2, dy, 0, 0);
/* 439 */       g.drawLine(dx / 2, dy + shift, 0, shift);
/*     */ 
/*     */       
/* 442 */       g.setColor(color.brighter());
/* 443 */       g.drawLine(dx / 2, dy, dx, 0);
/* 444 */       g.drawLine(dx / 2, dy + shift, dx, shift);
/*     */ 
/*     */       
/* 447 */       if (this.descending) {
/* 448 */         g.setColor(color.darker().darker());
/*     */       } else {
/* 450 */         g.setColor(color.brighter().brighter());
/*     */       } 
/* 452 */       g.drawLine(dx, 0, 0, 0);
/*     */       
/* 454 */       g.setColor(color);
/* 455 */       g.translate(-x, -y);
/*     */     }
/*     */     
/*     */     public int getIconWidth() {
/* 459 */       return this.size;
/*     */     }
/*     */     
/*     */     public int getIconHeight() {
/* 463 */       return this.size;
/*     */     }
/*     */   }
/*     */   
/*     */   private class SortableHeaderRenderer implements TableCellRenderer {
/*     */     private TableCellRenderer tableCellRenderer;
/*     */     
/*     */     public SortableHeaderRenderer(TableCellRenderer tableCellRenderer) {
/* 471 */       this.tableCellRenderer = tableCellRenderer;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
/* 480 */       Component c = this.tableCellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
/*     */       
/* 482 */       if (c instanceof JLabel) {
/* 483 */         JLabel l = (JLabel)c;
/* 484 */         l.setHorizontalTextPosition(2);
/* 485 */         int modelColumn = table.convertColumnIndexToModel(column);
/* 486 */         l.setIcon(TableSorter.this.getHeaderRendererIcon(modelColumn, l.getFont().getSize()));
/*     */       } 
/* 488 */       return c;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class Directive {
/*     */     private int column;
/*     */     private int direction;
/*     */     
/*     */     public Directive(int column, int direction) {
/* 497 */       this.column = column;
/* 498 */       this.direction = direction;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Users\na\Desktop\TaggerJava6.zip!\TaggerJava6\Tagger.jar!\tagger\TableSorter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */