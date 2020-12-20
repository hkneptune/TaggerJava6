/*     */ package tagger;
/*     */ 
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.io.File;
/*     */ import java.util.List;
/*     */ import java.util.Scanner;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.SwingWorker;
/*     */ import net.iharder.dnd.FileDrop;
/*     */ 
/*     */ class OpenAction
/*     */   extends AbstractAction
/*     */   implements FileDrop.Listener {
/*  15 */   private static int FILE_COUNT = 0;
/*     */   
/*     */   private final Tagger myTagger;
/*     */   
/*     */   OpenAction(Tagger tagger) {
/*  20 */     super(Tagger.getString("open.menu.label"));
/*  21 */     putValue("AcceleratorKey", KeyStroke.getKeyStroke(79, 2));
/*  22 */     putValue("MnemonicKey", Integer.valueOf(Tagger.getMnemonic("open.menu.mnemonic")));
/*     */     
/*  24 */     this.myTagger = tagger;
/*     */   }
/*     */   
/*     */   public void actionPerformed(ActionEvent e) {
/*  28 */     File[] files = this.myTagger.showOpenDialog();
/*     */     
/*  30 */     if (files != null)
/*  31 */       open(files); 
/*     */   }
/*     */   
/*     */   private void extractArt(final Tab tab) {
/*  35 */     final File temp = new File("temp/" + FILE_COUNT);
/*  36 */     temp.mkdirs();
/*  37 */     temp.deleteOnExit();
/*     */     
/*  39 */     SwingWorker<Object, Object> worker = new SwingWorker<Object, Object>() {
/*     */         public Object doInBackground() {
/*  41 */           File file = tab.getFile();
/*     */           
/*     */           try {
/*  44 */             AtomicParsley.execute(new String[] { AtomicParsley.getExecutable(), file.getAbsolutePath(), "--extractPixToPath", this.val$temp.getAbsolutePath() + "/" + file.getName() }).waitFor();
/*     */             OpenAction.FILE_COUNT++;
/*  46 */           } catch (Exception e) {}
/*     */           
/*  48 */           return null;
/*     */         }
/*     */         
/*     */         protected void done() {
/*  52 */           for (File file : temp.listFiles()) {
/*     */             try {
/*  54 */               tab.addArtwork(file);
/*  55 */               file.deleteOnExit();
/*  56 */             } catch (Exception e) {}
/*     */           } 
/*     */         }
/*     */       };
/*     */     
/*  61 */     worker.execute();
/*     */   }
/*     */   
/*     */   private void open(File... files) {
/*  65 */     SwingWorker<Object, Tab> worker = new SwingWorker<Object, Tab>() {
/*     */         public Object doInBackground() {
/*  67 */           for (File file : files) {
/*  68 */             if (file != null) {
/*     */               Scanner in2;
/*     */               
/*  71 */               Tab tab = new Tab(OpenAction.this.myTagger, file);
/*  72 */               Scanner in1 = null;
/*     */             } 
/*     */           } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 186 */           return null;
/*     */         }
/*     */         
/*     */         protected void process(List<Tab> tabs) {
/* 190 */           for (Tab tab : tabs) {
/* 191 */             OpenAction.this.myTagger.addTab(tab);
/* 192 */             tab.setSaved(true);
/*     */           } 
/*     */           
/* 195 */           OpenAction.this.myTagger.setActionsEnabled(true);
/*     */         }
/*     */       };
/*     */     
/* 199 */     worker.execute();
/*     */   }
/*     */   
/*     */   public void filesDropped(File[] files) {
/* 203 */     for (int i = 0; i < files.length; i++) {
/* 204 */       if (!Tagger.MPEG4_FILTER.accept(files[i])) {
/* 205 */         files[i] = null;
/*     */       }
/*     */     } 
/* 208 */     open(files);
/*     */   }
/*     */ }


/* Location:              D:\Users\na\Desktop\TaggerJava6.zip!\TaggerJava6\Tagger.jar!\tagger\OpenAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */