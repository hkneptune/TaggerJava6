/*     */ package tagger;
/*     */ 
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.File;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Scanner;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.SwingWorker;
/*     */ 
/*     */ class SaveAction
/*     */   extends AbstractAction
/*     */   implements PropertyChangeListener {
/*     */   private final Tagger myTagger;
/*     */   private Tab myCurrentTab;
/*     */   
/*     */   class Worker
/*     */     extends SwingWorker<Object, Tab> {
/*     */     private final Tab[] myTabs;
/*     */     private final File myDest;
/*     */     
/*     */     Worker(Tab... tabs) {
/*  27 */       this.myTabs = tabs;
/*  28 */       this.myDest = null;
/*  29 */       addPropertyChangeListener(SaveAction.this);
/*     */     }
/*     */     
/*     */     Worker(Tab tab, File dest) {
/*  33 */       this.myTabs = new Tab[] { tab };
/*  34 */       this.myDest = dest;
/*  35 */       addPropertyChangeListener(SaveAction.this);
/*     */     }
/*     */     
/*     */     public Object doInBackground() {
/*  39 */       for (Tab tab : this.myTabs) {
/*  40 */         firePropertyChange("tab", null, tab);
/*  41 */         Scanner in = null;
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
/*  65 */       return null;
/*     */     }
/*     */     
/*     */     protected void process(List<Tab> tabs) {
/*  69 */       if (Tagger.OPTIONS.getBoolean("closeTabsOnSave", false)) {
/*  70 */         for (Tab tab : tabs)
/*  71 */           SaveAction.this.myTagger.removeTab(tab); 
/*     */       } else {
/*  73 */         for (Tab tab : tabs)
/*  74 */           tab.setSaved(true); 
/*     */       } 
/*     */     }
/*     */     
/*     */     protected void done() {
/*  79 */       JOptionPane.showMessageDialog(SaveAction.this.myTagger, Tagger.getString("save.dialog.complete"), Tagger.getString("save.dialog.title"), 1);
/*     */       
/*  81 */       SaveAction.this.myTagger.setProgress(null, 0);
/*  82 */       SaveAction.this.myCurrentTab = null;
/*     */     }
/*     */   }
/*     */   
/*     */   SaveAction(Tagger tagger) {
/*  87 */     super(Tagger.getString("save.menu.label"));
/*  88 */     putValue("AcceleratorKey", KeyStroke.getKeyStroke(83, 2));
/*  89 */     putValue("MnemonicKey", Integer.valueOf(Tagger.getMnemonic("save.menu.mnemonic")));
/*     */     
/*  91 */     this.myTagger = tagger;
/*     */   }
/*     */   
/*     */   public void actionPerformed(ActionEvent e) {
/*  95 */     save(new Tab[] { this.myTagger.getSelectedTab() });
/*     */   }
/*     */   
/*     */   private static LinkedList<String> getArgs(Tab tab, File dest) {
/*  99 */     LinkedList<String> args = new LinkedList<String>();
/* 100 */     args.add(AtomicParsley.getExecutable());
/* 101 */     args.add(tab.getFile().getAbsolutePath());
/*     */     
/* 103 */     for (String atom : AtomicParsley.ATOMS.keySet()) {
/* 104 */       String input = Tagger.getInput(tab.getField(atom));
/*     */       
/* 106 */       if (atom.matches("akID|apID|atID|cmID|cnID|geID|plID|sfID")) {
/* 107 */         if (input.equals("false")) {
/* 108 */           args.add("--manualAtomRemove");
/* 109 */           args.add("moov.udta.meta.ilst." + atom);
/*     */         }  continue;
/* 111 */       }  if (atom.matches("iTunEXTC|iTunMOVI|iTunNORM|iTunSMPB|iTunes_CDDB_1|iTunes_CDDB_IDs|iTunes_CDDB_TrackNumber|tool")) {
/* 112 */         if (input == null || input.equals("")) {
/* 113 */           args.add("--manualAtomRemove");
/* 114 */           args.add("moov.udta.meta.ilst.----.name:[" + atom + "]"); continue;
/*     */         } 
/* 116 */         args.add("--rDNSatom");
/* 117 */         args.add(input.replace("\"", "\\\""));
/* 118 */         args.add("name=" + atom);
/* 119 */         args.add("domain=com.apple.iTunes"); continue;
/*     */       } 
/* 121 */       if (atom.matches("soaa|soal|soar|soco|sonm|sosn")) {
/* 122 */         if (input == null || input.equals("")) {
/* 123 */           args.add("--manualAtomRemove");
/* 124 */           args.add("moov.udta.meta.ilst." + atom); continue;
/*     */         } 
/* 126 */         args.add("--sortOrder");
/* 127 */         args.add(Tab.SORTS.get(atom));
/* 128 */         args.add(input.replace("\"", "\\\""));
/*     */         continue;
/*     */       } 
/* 131 */       args.add("--" + (String)AtomicParsley.ATOMS.get(atom));
/*     */       
/* 133 */       if (atom.equals("rtng")) {
/* 134 */         input = Tab.ADVISORIES.get(input);
/* 135 */       } else if (atom.equals("stik")) {
/* 136 */         input = Tab.KINDS.get(input);
/* 137 */       } else if (atom.matches("disk|trkn")) {
/* 138 */         input = Tagger.getInput(tab.getField(atom + ".number"));
/*     */         
/* 140 */         if (input != null && !input.equals("")) {
/* 141 */           String total = Tagger.getInput(tab.getField(atom + ".total"));
/*     */           
/* 143 */           if (total != null && !total.equals("")) {
/* 144 */             input = input + "/" + total;
/*     */           }
/*     */         } 
/*     */       } 
/* 148 */       if (input == null || input.equals("")) {
/* 149 */         args.add("\"\""); continue;
/*     */       } 
/* 151 */       args.add(input.replace("\"", "\\\""));
/*     */     } 
/*     */ 
/*     */     
/* 155 */     args.add("--artwork");
/* 156 */     args.add("REMOVE_ALL");
/*     */     
/* 158 */     for (File f : tab.getArt()) {
/* 159 */       args.add("--artwork");
/* 160 */       args.add(f.getAbsolutePath());
/*     */     } 
/*     */     
/* 163 */     if (dest == null) {
/* 164 */       args.add("--overWrite");
/*     */     } else {
/* 166 */       args.add("--output");
/* 167 */       args.add(dest.getAbsolutePath());
/*     */     } 
/*     */     
/* 170 */     return args;
/*     */   }
/*     */   
/*     */   void save(Tab tab, File dest) {
/* 174 */     (new Worker(tab, dest)).execute();
/*     */   }
/*     */   
/*     */   void save(Tab... tabs) {
/* 178 */     (new Worker(tabs)).execute();
/*     */   }
/*     */   
/*     */   public void propertyChange(PropertyChangeEvent e) {
/* 182 */     if (e.getPropertyName().equals("tab")) {
/* 183 */       this.myCurrentTab = (Tab)e.getNewValue();
/* 184 */       this.myTagger.setSelectedTab(this.myCurrentTab);
/* 185 */     } else if (e.getPropertyName().equals("progress")) {
/* 186 */       this.myTagger.setProgress(this.myCurrentTab.getFile(), ((Integer)e.getNewValue()).intValue());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\Users\na\Desktop\TaggerJava6.zip!\TaggerJava6\Tagger.jar!\tagger\SaveAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */