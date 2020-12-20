/*     */ package tagger;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.Desktop;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.awt.event.WindowListener;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.File;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.prefs.Preferences;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JFileChooser;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JMenu;
/*     */ import javax.swing.JMenuBar;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JPopupMenu;
/*     */ import javax.swing.JProgressBar;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import javax.swing.event.ChangeListener;
/*     */ import javax.swing.filechooser.FileNameExtensionFilter;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import net.iharder.dnd.FileDrop;
/*     */ 
/*     */ class Tagger
/*     */   extends JFrame implements ChangeListener, WindowListener {
/*     */   private static final int VERSION = 20070429;
/*  44 */   static final Preferences OPTIONS = Preferences.userNodeForPackage(Tagger.class);
/*  45 */   static final Locale LOCALE = new Locale(OPTIONS.get("language", "en"));
/*     */   static {
/*  47 */     Locale.setDefault(LOCALE);
/*     */   }
/*  49 */   static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("tagger/i18n", LOCALE);
/*     */   
/*  51 */   static final FileNameExtensionFilter JPEG_FILTER = new FileNameExtensionFilter(getString("filter.jpeg") + " (*.jpg; *.jpeg)", new String[] { "jpg", "jpeg" });
/*  52 */   static final FileNameExtensionFilter JS_FILTER = new FileNameExtensionFilter(null, new String[] { "js" });
/*  53 */   static final FileNameExtensionFilter MPEG4_FILTER = new FileNameExtensionFilter(getString("filter.mpeg-4") + " (*.m4a; *.m4b; *.m4p; *.m4v; *.mp4)", new String[] { "m4a", "m4b", "m4p", "m4v", "mp4" });
/*  54 */   static final FileNameExtensionFilter PNG_FILTER = new FileNameExtensionFilter(getString("filter.png") + " (*.png)", new String[] { "png" });
/*  55 */   static final FileNameExtensionFilter XML_FILTER = new FileNameExtensionFilter(getString("filter.xml") + " (*.xml)", new String[] { "xml" });
/*  56 */   static final FileNameExtensionFilter XLS_FILTER = new FileNameExtensionFilter(getString("filter.xls") + " (*.xls)", new String[] { "xls" });
/*     */   
/*  58 */   static final FileNameExtensionFilter IMAGE_FILTER = getSuperFilter("filter.images", new FileNameExtensionFilter[] { JPEG_FILTER, PNG_FILTER });
/*  59 */   static final FileNameExtensionFilter TAG_ARCHIVE_FILTER = getSuperFilter("filter.tag_archives", new FileNameExtensionFilter[] { XML_FILTER, XLS_FILTER });
/*     */   
/*     */   private final CloseAction myCloseAction;
/*     */   
/*     */   private final CloseAllAction myCloseAllAction;
/*     */   private final CopyAction myCopyAction;
/*     */   private final CutAction myCutAction;
/*     */   private final DeleteAction myDeleteAction;
/*     */   private final ExportArtworkAction myExportArtworkAction;
/*     */   private final ExportTagsAction myExportTagsAction;
/*     */   private final ImportArtworkAction myImportArtworkAction;
/*     */   private final ImportTagsAction myImportTagsAction;
/*     */   private final OpenAction myOpenAction;
/*     */   private final PasteAction myPasteAction;
/*     */   private final RedoAction myRedoAction;
/*     */   private final RemoveArtworkAction myRemoveArtworkAction;
/*     */   private final SaveAction mySaveAction;
/*     */   private final SaveAllAction mySaveAllAction;
/*     */   private final SaveAsAction mySaveAsAction;
/*     */   private final SelectAllAction mySelectAllAction;
/*     */   private final UndoAction myUndoAction;
/*     */   private final JPopupMenu myEditPopup;
/*     */   private final JFileChooser myOpenSaveChooser;
/*     */   private final JFileChooser myImportExportChooser;
/*     */   private final JFileChooser myArtworkChooser;
/*     */   private final JProgressBar myProgressBar;
/*     */   private final JLabel myProgressLabel;
/*     */   private final XTabbedPane myTabManager;
/*     */   private final ArrayList<Tab> myTabs;
/*     */   
/*  89 */   private final MouseAdapter myPopupListener = new MouseAdapter() {
/*     */       public void mouseClicked(MouseEvent e) {
/*  91 */         if (e.isPopupTrigger())
/*  92 */           Tagger.this.myEditPopup.show((Component)e.getSource(), e.getX() + 5, e.getY() + 5); 
/*     */       }
/*     */       
/*     */       public void mouseReleased(MouseEvent e) {
/*  96 */         if (e.isPopupTrigger())
/*  97 */           Tagger.this.myEditPopup.show((Component)e.getSource(), e.getX() + 5, e.getY() + 5); 
/*     */       }
/*     */     };
/*     */   
/*     */   private Tagger() {
/* 102 */     super("Tagger");
/* 103 */     setSize(OPTIONS.getInt("width", 425), OPTIONS.getInt("height", 820));
/* 104 */     setLocation(OPTIONS.getInt("x", (getLocation()).x), OPTIONS.getInt("y", (getLocation()).y));
/*     */     
/* 106 */     if (OPTIONS.getBoolean("maximized", false)) {
/* 107 */       setExtendedState(6);
/*     */     }
/* 109 */     setIconImage(getImage("icon.png").getImage());
/*     */     
/* 111 */     setDefaultCloseOperation(3);
/* 112 */     setDefaultLookAndFeelDecorated(true);
/* 113 */     addWindowListener(this);
/*     */     
/*     */     try {
/* 116 */       UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
/* 117 */     } catch (Exception e) {}
/*     */     
/* 119 */     this.myCloseAction = new CloseAction(this);
/* 120 */     this.myCloseAllAction = new CloseAllAction(this);
/* 121 */     this.myCopyAction = new CopyAction();
/* 122 */     this.myCutAction = new CutAction();
/* 123 */     this.myDeleteAction = new DeleteAction();
/* 124 */     this.myExportArtworkAction = new ExportArtworkAction(this);
/* 125 */     this.myExportTagsAction = new ExportTagsAction(this);
/* 126 */     this.myImportArtworkAction = new ImportArtworkAction(this);
/* 127 */     this.myImportTagsAction = new ImportTagsAction(this);
/* 128 */     this.myOpenAction = new OpenAction(this);
/* 129 */     this.myPasteAction = new PasteAction();
/* 130 */     this.myRedoAction = new RedoAction(this);
/* 131 */     this.myRemoveArtworkAction = new RemoveArtworkAction(this);
/* 132 */     this.mySaveAction = new SaveAction(this);
/* 133 */     this.mySaveAllAction = new SaveAllAction(this);
/* 134 */     this.mySaveAsAction = new SaveAsAction(this);
/* 135 */     this.mySelectAllAction = new SelectAllAction();
/* 136 */     this.myUndoAction = new UndoAction(this);
/*     */     
/* 138 */     this.myOpenSaveChooser = new JFileChooser(OPTIONS.get("lastOpenSaveDir", null));
/* 139 */     this.myOpenSaveChooser.setAcceptAllFileFilterUsed(false);
/* 140 */     this.myOpenSaveChooser.setFileFilter(MPEG4_FILTER);
/*     */     
/* 142 */     this.myImportExportChooser = new JFileChooser(OPTIONS.get("lastImportExportDir", null));
/* 143 */     this.myImportExportChooser.setAcceptAllFileFilterUsed(false);
/* 144 */     this.myImportExportChooser.addChoosableFileFilter(TAG_ARCHIVE_FILTER);
/* 145 */     this.myImportExportChooser.addChoosableFileFilter(XLS_FILTER);
/* 146 */     this.myImportExportChooser.addChoosableFileFilter(XML_FILTER);
/* 147 */     this.myImportExportChooser.setFileFilter(TAG_ARCHIVE_FILTER);
/* 148 */     this.myImportExportChooser.setMultiSelectionEnabled(false);
/* 149 */     this.myImportExportChooser.addPropertyChangeListener("fileFilterChanged", new PropertyChangeListener() {
/*     */           public void propertyChange(PropertyChangeEvent e) {
/* 151 */             FileNameExtensionFilter filter = (FileNameExtensionFilter)Tagger.this.myImportExportChooser.getFileFilter();
/* 152 */             File suggest = new File(Tagger.this.myImportExportChooser.getCurrentDirectory().getAbsolutePath() + "/" + Tagger.this.getSelectedTab().getFile().getName() + "." + filter.getExtensions()[0]);
/* 153 */             Tagger.this.myImportExportChooser.setSelectedFile(suggest);
/*     */           }
/*     */         });
/*     */     
/* 157 */     this.myArtworkChooser = new JFileChooser(OPTIONS.get("lastArtworkDir", null));
/* 158 */     this.myArtworkChooser.setAcceptAllFileFilterUsed(false);
/* 159 */     this.myArtworkChooser.addChoosableFileFilter(IMAGE_FILTER);
/* 160 */     this.myArtworkChooser.addChoosableFileFilter(PNG_FILTER);
/* 161 */     this.myArtworkChooser.addChoosableFileFilter(JPEG_FILTER);
/* 162 */     this.myArtworkChooser.setFileFilter(IMAGE_FILTER);
/* 163 */     this.myArtworkChooser.setAccessory(new ImagePreview(this.myArtworkChooser));
/* 164 */     this.myArtworkChooser.addPropertyChangeListener("fileFilterChanged", new PropertyChangeListener() {
/*     */           public void propertyChange(PropertyChangeEvent e) {
/* 166 */             FileNameExtensionFilter filter = (FileNameExtensionFilter)Tagger.this.myArtworkChooser.getFileFilter();
/* 167 */             File suggest = new File(Tagger.this.myArtworkChooser.getCurrentDirectory().getAbsolutePath() + "/" + Tagger.this.getSelectedTab().getFile().getName() + "." + filter.getExtensions()[0]);
/* 168 */             Tagger.this.myArtworkChooser.setSelectedFile(suggest);
/*     */           }
/*     */         });
/*     */     
/* 172 */     JMenuBar menuBar = new JMenuBar();
/* 173 */     setJMenuBar(menuBar);
/*     */     
/* 175 */     JMenu fileMenu = new JMenu(getString("file.menu.label"));
/* 176 */     fileMenu.setMnemonic(getMnemonic("file.menu.mnemonic"));
/* 177 */     menuBar.add(fileMenu);
/*     */     
/* 179 */     fileMenu.add(this.myOpenAction);
/* 180 */     fileMenu.add(this.myCloseAction);
/* 181 */     fileMenu.add(this.myCloseAllAction);
/* 182 */     fileMenu.addSeparator();
/* 183 */     fileMenu.add(this.mySaveAction);
/* 184 */     fileMenu.add(this.mySaveAsAction);
/* 185 */     fileMenu.add(this.mySaveAllAction);
/* 186 */     fileMenu.addSeparator();
/* 187 */     fileMenu.add(this.myImportTagsAction);
/* 188 */     fileMenu.add(this.myExportTagsAction);
/* 189 */     fileMenu.addSeparator();
/* 190 */     fileMenu.add(new ExitAction(this));
/*     */     
/* 192 */     JMenu editMenu = new JMenu(getString("edit.menu.label"));
/* 193 */     editMenu.setMnemonic(getMnemonic("edit.menu.mnemonic"));
/* 194 */     menuBar.add(editMenu);
/*     */     
/* 196 */     editMenu.add(this.myUndoAction);
/* 197 */     editMenu.add(this.myRedoAction);
/* 198 */     editMenu.addSeparator();
/* 199 */     editMenu.add(this.myCutAction);
/* 200 */     editMenu.add(this.myCopyAction);
/* 201 */     editMenu.add(this.myPasteAction);
/* 202 */     editMenu.add(this.myDeleteAction);
/* 203 */     editMenu.addSeparator();
/* 204 */     editMenu.add(this.mySelectAllAction);
/*     */     
/* 206 */     this.myEditPopup = new JPopupMenu();
/* 207 */     this.myEditPopup.add(this.myUndoAction);
/* 208 */     this.myEditPopup.add(this.myRedoAction);
/* 209 */     this.myEditPopup.addSeparator();
/* 210 */     this.myEditPopup.add(this.myCutAction);
/* 211 */     this.myEditPopup.add(this.myCopyAction);
/* 212 */     this.myEditPopup.add(this.myPasteAction);
/* 213 */     this.myEditPopup.add(this.myDeleteAction);
/* 214 */     this.myEditPopup.addSeparator();
/* 215 */     this.myEditPopup.add(this.mySelectAllAction);
/*     */     
/* 217 */     JMenu artworkMenu = new JMenu(getString("artwork.menu.label"));
/* 218 */     artworkMenu.setMnemonic(getMnemonic("artwork.menu.mnemonic"));
/* 219 */     menuBar.add(artworkMenu);
/*     */     
/* 221 */     artworkMenu.add(this.myImportArtworkAction);
/* 222 */     artworkMenu.add(this.myExportArtworkAction);
/* 223 */     artworkMenu.add(this.myRemoveArtworkAction);
/*     */     
/* 225 */     JMenu optionsMenu = new JMenu(getString("options.menu.label"));
/* 226 */     optionsMenu.setMnemonic(getMnemonic("options.menu.mnemonic"));
/* 227 */     menuBar.add(optionsMenu);
/*     */     
/* 229 */     optionsMenu.add(new CloseOnSaveOption());
/* 230 */     optionsMenu.add(new GleanInfoOption());
/* 231 */     optionsMenu.add(new LanguageOption());
/*     */     
/* 233 */     JMenu helpMenu = new JMenu(getString("help.menu.label"));
/* 234 */     helpMenu.setMnemonic(getMnemonic("help.menu.mnemonic"));
/* 235 */     menuBar.add(helpMenu);
/*     */     
/* 237 */     if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
/* 238 */       helpMenu.add(new GoToSiteAction(this));
/*     */     }
/* 240 */     helpMenu.add(new CheckForUpdatesAction(this));
/* 241 */     helpMenu.addSeparator();
/* 242 */     helpMenu.add(new AboutAction(this));
/*     */     
/* 244 */     JPanel main = new JPanel(new BorderLayout());
/* 245 */     new FileDrop(main, null, this.myOpenAction);
/* 246 */     add(main);
/*     */     
/* 248 */     this.myTabs = new ArrayList<Tab>();
/* 249 */     this.myTabManager = new XTabbedPane(this);
/* 250 */     this.myTabManager.addChangeListener(this);
/* 251 */     main.add(this.myTabManager, "Center");
/*     */     
/* 253 */     JPanel progressPanel = new JPanel(new BorderLayout());
/* 254 */     main.add(progressPanel, "South");
/*     */     
/* 256 */     this.myProgressBar = new JProgressBar(0, 100);
/* 257 */     this.myProgressBar.setStringPainted(true);
/* 258 */     progressPanel.add(this.myProgressBar, "East");
/*     */     
/* 260 */     this.myProgressLabel = new JLabel();
/* 261 */     progressPanel.add(this.myProgressLabel, "West");
/*     */     
/* 263 */     setActionsEnabled(false);
/*     */   }
/*     */   
/*     */   File[] showImportArtworkDialog() {
/* 267 */     this.myArtworkChooser.setDialogTitle(getString("import_artwork.dialog.title"));
/* 268 */     this.myArtworkChooser.setMultiSelectionEnabled(true);
/*     */     
/* 270 */     if (this.myArtworkChooser.showOpenDialog(this) == 0) {
/* 271 */       return this.myArtworkChooser.getSelectedFiles();
/*     */     }
/* 273 */     return null;
/*     */   }
/*     */   
/*     */   File showExportArtworkDialog() {
/* 277 */     this.myArtworkChooser.setDialogTitle("export_artwork.dialog.title");
/* 278 */     this.myArtworkChooser.setMultiSelectionEnabled(false);
/*     */     
/* 280 */     File prevFile = this.myArtworkChooser.getSelectedFile();
/* 281 */     this.myArtworkChooser.getPropertyChangeListeners("fileFilterChanged")[0].propertyChange(null);
/*     */     
/* 283 */     if (this.myArtworkChooser.showSaveDialog(this) == 0) {
/* 284 */       return this.myArtworkChooser.getSelectedFile();
/*     */     }
/* 286 */     this.myArtworkChooser.setSelectedFile(prevFile);
/* 287 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   File[] showOpenDialog() {
/* 292 */     this.myOpenSaveChooser.setDialogTitle(getString("open.dialog.title"));
/* 293 */     this.myOpenSaveChooser.setMultiSelectionEnabled(true);
/*     */     
/* 295 */     if (this.myOpenSaveChooser.showOpenDialog(this) == 0) {
/* 296 */       return this.myOpenSaveChooser.getSelectedFiles();
/*     */     }
/* 298 */     return null;
/*     */   }
/*     */   
/*     */   File showSaveAsDialog() {
/* 302 */     this.myOpenSaveChooser.setDialogTitle(getString("save_as.dialog.title"));
/* 303 */     this.myOpenSaveChooser.setMultiSelectionEnabled(false);
/*     */     
/* 305 */     File prevFile = this.myOpenSaveChooser.getSelectedFile();
/* 306 */     this.myOpenSaveChooser.setSelectedFile(getSelectedTab().getFile());
/*     */     
/* 308 */     if (this.myOpenSaveChooser.showSaveDialog(this) == 0) {
/* 309 */       return this.myOpenSaveChooser.getSelectedFile();
/*     */     }
/* 311 */     this.myOpenSaveChooser.setSelectedFile(prevFile);
/* 312 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   File showImportTagsDialog() {
/* 317 */     this.myImportExportChooser.setDialogTitle(getString("import_tags.dialog.title"));
/*     */     
/* 319 */     if (this.myImportExportChooser.showOpenDialog(this) == 0) {
/* 320 */       return this.myImportExportChooser.getSelectedFile();
/*     */     }
/* 322 */     return null;
/*     */   }
/*     */   
/*     */   File showExportTagsDialog() {
/* 326 */     this.myImportExportChooser.setDialogTitle(getString("export_tags.dialog.title"));
/*     */     
/* 328 */     File prevFile = this.myImportExportChooser.getSelectedFile();
/* 329 */     this.myImportExportChooser.getPropertyChangeListeners("fileFilterChanged")[0].propertyChange(null);
/*     */     
/* 331 */     if (this.myImportExportChooser.showSaveDialog(this) == 0) {
/* 332 */       return this.myImportExportChooser.getSelectedFile();
/*     */     }
/* 334 */     this.myImportExportChooser.setSelectedFile(prevFile);
/* 335 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private static FileNameExtensionFilter getSuperFilter(String key, FileNameExtensionFilter... filters) {
/* 340 */     ArrayList<String> exts = new ArrayList<String>();
/* 341 */     String label = " (";
/*     */     
/* 343 */     for (FileNameExtensionFilter fnef : filters) {
/* 344 */       List<String> extensions = Arrays.asList(fnef.getExtensions());
/* 345 */       exts.addAll(extensions);
/*     */       
/* 347 */       for (String e : extensions) {
/* 348 */         label = label + "*." + e + "; ";
/*     */       }
/*     */     } 
/* 351 */     return new FileNameExtensionFilter(getString(key) + label.substring(0, label.length() - 2) + ")", exts.<String>toArray(new String[exts.size()]));
/*     */   }
/*     */   
/*     */   void applyToAll(Component field) {
/* 355 */     String data = getInput(field);
/*     */     
/* 357 */     for (Tab tab : this.myTabs) {
/* 358 */       setInput(tab.getField(field.getName()), data);
/* 359 */       tab.setSaved(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   void addTab(Tab tab) {
/* 364 */     this.myTabManager.addTab(tab.getFile().getName(), tab);
/* 365 */     this.myTabManager.setTabComponentAt(this.myTabManager.indexOfComponent(tab), tab.getClosePanel());
/* 366 */     this.myTabs.add(tab);
/*     */     
/* 368 */     this.myUndoAction.update();
/* 369 */     this.myRedoAction.update();
/* 370 */     tab.setUndoOn(true);
/*     */   }
/*     */   
/*     */   void removeAllTabs() {
/* 374 */     this.myTabs.clear();
/* 375 */     this.myTabManager.removeAll();
/*     */     
/* 377 */     this.myUndoAction.update();
/* 378 */     this.myRedoAction.update();
/* 379 */     setActionsEnabled(false);
/*     */   }
/*     */   
/*     */   void setSelectedTab(Tab tab) {
/* 383 */     this.myTabManager.setSelectedComponent(tab);
/* 384 */     this.myUndoAction.update();
/* 385 */     this.myRedoAction.update();
/*     */   }
/*     */   
/*     */   void removeTab(Tab tab) {
/* 389 */     this.myTabs.remove(tab);
/* 390 */     this.myTabManager.remove(tab);
/*     */     
/* 392 */     this.myUndoAction.update();
/* 393 */     this.myRedoAction.update();
/*     */     
/* 395 */     if (this.myTabs.size() == 0)
/* 396 */       setActionsEnabled(false); 
/*     */   }
/*     */   
/*     */   Tab[] getTabs() {
/* 400 */     return this.myTabs.<Tab>toArray(new Tab[this.myTabs.size()]);
/*     */   }
/*     */   
/*     */   static File resolveFile(File file, String ext) {
/* 404 */     if (file.getName().matches("\"(.+)\\.(.+)\""))
/* 405 */       return new File(file.getAbsolutePath().replace("\"", "")); 
/* 406 */     if (getExtension(file) == null) {
/* 407 */       return new File(file.getAbsolutePath() + "." + ext);
/*     */     }
/* 409 */     return file;
/*     */   }
/*     */   
/*     */   static String getExtension(File file) {
/* 413 */     String ext = null;
/* 414 */     String s = file.getName();
/* 415 */     int i = s.lastIndexOf('.');
/*     */     
/* 417 */     if (i > 0 && i < s.length() - 1) {
/* 418 */       ext = s.substring(i + 1).toLowerCase();
/*     */     }
/* 420 */     return ext;
/*     */   }
/*     */   
/*     */   void addListeners(JTextComponent c) {
/* 424 */     c.addCaretListener(this.myCopyAction);
/* 425 */     c.addFocusListener(this.myCopyAction);
/*     */     
/* 427 */     c.addCaretListener(this.myCutAction);
/* 428 */     c.addFocusListener(this.myCutAction);
/*     */     
/* 430 */     c.addCaretListener(this.myDeleteAction);
/*     */     
/* 432 */     c.addFocusListener(this.myPasteAction);
/* 433 */     c.addFocusListener(this.mySelectAllAction);
/*     */     
/* 435 */     c.addMouseListener(this.myPopupListener);
/*     */   }
/*     */   
/*     */   void setActionsEnabled(boolean enabled) {
/* 439 */     this.myCloseAction.setEnabled(enabled);
/* 440 */     this.myCloseAllAction.setEnabled(enabled);
/* 441 */     this.myImportArtworkAction.setEnabled(enabled);
/* 442 */     this.myExportArtworkAction.setEnabled(enabled);
/* 443 */     this.myRemoveArtworkAction.setEnabled(enabled);
/* 444 */     this.myExportTagsAction.setEnabled(enabled);
/* 445 */     this.myImportTagsAction.setEnabled(enabled);
/* 446 */     this.mySaveAction.setEnabled(enabled);
/* 447 */     this.mySaveAllAction.setEnabled(enabled);
/* 448 */     this.mySaveAsAction.setEnabled(enabled);
/*     */     
/* 450 */     this.myExportArtworkAction.update();
/* 451 */     this.myRemoveArtworkAction.update();
/*     */   }
/*     */   
/*     */   public void stateChanged(ChangeEvent e) {
/* 455 */     this.myExportArtworkAction.update();
/* 456 */     this.myRedoAction.update();
/* 457 */     this.myRemoveArtworkAction.update();
/* 458 */     this.myUndoAction.update();
/*     */   }
/*     */   
/*     */   void showError(String message) {
/* 462 */     JOptionPane.showMessageDialog(this, message, getString("error.dialog.title"), 0);
/*     */   }
/*     */   
/*     */   void showError(String message, Exception e) {
/* 466 */     showError(message);
/* 467 */     EasyWriter w = null;
/*     */ 
/*     */     
/* 470 */     try { w = new EasyWriter((new File("log.txt")).getAbsolutePath());
/* 471 */       w.println(e.toString());
/*     */       
/* 473 */       for (StackTraceElement ste : e.getStackTrace())
/* 474 */         w.println(ste);  }
/* 475 */     catch (Exception ex) {  }
/* 476 */     finally { if (w != null)
/* 477 */         w.close();  }
/*     */   
/*     */   }
/*     */   
/*     */   void setProgress(File file, int percent) {
/* 482 */     if (file == null) {
/* 483 */       this.myProgressLabel.setText(" ");
/* 484 */       this.myProgressBar.setString(null);
/* 485 */       this.myProgressBar.setValue(0);
/*     */     } else {
/* 487 */       this.myProgressLabel.setText(MessageFormat.format(getString("progress_bar.label"), new Object[] { file.getName() }));
/* 488 */       this.myProgressBar.setString(percent + "%");
/* 489 */       this.myProgressBar.setValue(percent);
/*     */     } 
/*     */   }
/*     */   
/*     */   JFileChooser getArtworkChooser() {
/* 494 */     return this.myArtworkChooser;
/*     */   }
/*     */   
/*     */   UndoAction getUndoAction() {
/* 498 */     return this.myUndoAction;
/*     */   }
/*     */   
/*     */   RedoAction getRedoAction() {
/* 502 */     return this.myRedoAction;
/*     */   }
/*     */   
/*     */   ExportArtworkAction getExportArtworkAction() {
/* 506 */     return this.myExportArtworkAction;
/*     */   }
/*     */   
/*     */   ImportArtworkAction getImportArtworkAction() {
/* 510 */     return this.myImportArtworkAction;
/*     */   }
/*     */   
/*     */   RemoveArtworkAction getRemoveArtworkAction() {
/* 514 */     return this.myRemoveArtworkAction;
/*     */   }
/*     */   
/*     */   SaveAction getSaveAction() {
/* 518 */     return this.mySaveAction;
/*     */   }
/*     */   
/*     */   Tab getSelectedTab() {
/* 522 */     return (Tab)this.myTabManager.getSelectedComponent();
/*     */   }
/*     */   
/*     */   static ImageIcon getImage(String filename) {
/* 526 */     return new ImageIcon(Tagger.class.getResource("images/" + filename));
/*     */   }
/*     */   
/*     */   static String getInput(Component input) {
/* 530 */     if (input == null)
/* 531 */       return null; 
/* 532 */     if (input instanceof JTextComponent)
/* 533 */       return ((JTextComponent)input).getText(); 
/* 534 */     if (input instanceof ITMSField)
/* 535 */       return ((ITMSField)input).isSelected() + ""; 
/* 536 */     if (input instanceof JComboBox)
/* 537 */       return ((JComboBox)input).getSelectedItem().toString(); 
/* 538 */     if (input instanceof JCheckBox)
/* 539 */       return ((JCheckBox)input).isSelected() + ""; 
/* 540 */     if (input instanceof RatingField)
/* 541 */       return ((RatingField)input).getText(); 
/* 542 */     if (input instanceof CastField) {
/* 543 */       return ((CastField)input).getText();
/*     */     }
/* 545 */     return null;
/*     */   }
/*     */   
/*     */   static String getString(String key) {
/* 549 */     return RESOURCE_BUNDLE.getString(key);
/*     */   }
/*     */   
/*     */   static void setInput(Component input, String value) {
/* 553 */     if (input == null)
/*     */       return; 
/* 555 */     if (input instanceof JTextComponent) {
/* 556 */       ((JTextComponent)input).setText(value);
/* 557 */     } else if (input instanceof ITMSField) {
/* 558 */       ((ITMSField)input).setText(value);
/* 559 */     } else if (input instanceof JComboBox) {
/* 560 */       ((JComboBox)input).setSelectedItem(value);
/* 561 */     } else if (input instanceof JCheckBox) {
/* 562 */       ((JCheckBox)input).setSelected(value.equals("true"));
/* 563 */     } else if (input instanceof RatingField) {
/* 564 */       ((RatingField)input).setText(value);
/* 565 */     } else if (input instanceof CastField) {
/* 566 */       ((CastField)input).setText(value);
/*     */     } 
/*     */   }
/*     */   static int getVersion() {
/* 570 */     return 20070429;
/*     */   }
/*     */   
/*     */   static int getMnemonic(String key) {
/*     */     try {
/* 575 */       return KeyEvent.class.getField("VK_" + getString(key)).getInt(null);
/* 576 */     } catch (Exception e) {
/* 577 */       return -1;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void windowClosing(WindowEvent e) {
/* 582 */     OPTIONS.put("lastArtworkDir", this.myArtworkChooser.getCurrentDirectory().getAbsolutePath());
/* 583 */     OPTIONS.put("lastImportExportDir", this.myImportExportChooser.getCurrentDirectory().getAbsolutePath());
/* 584 */     OPTIONS.put("lastOpenSaveDir", this.myOpenSaveChooser.getCurrentDirectory().getAbsolutePath());
/* 585 */     OPTIONS.putBoolean("maximized", (getExtendedState() == 6));
/* 586 */     OPTIONS.putInt("width", getWidth());
/* 587 */     OPTIONS.putInt("height", getHeight());
/* 588 */     OPTIONS.putInt("x", (getLocation()).x);
/* 589 */     OPTIONS.putInt("y", (getLocation()).y);
/*     */     
/*     */     try {
/* 592 */       OPTIONS.flush();
/* 593 */     } catch (Exception ex) {
/* 594 */       showError(getString("error.preferences"), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void windowActivated(WindowEvent e) {}
/*     */   
/*     */   public void windowClosed(WindowEvent e) {}
/*     */   
/*     */   public void windowDeactivated(WindowEvent e) {}
/*     */   
/*     */   public static void main(String[] args) {
/* 606 */     SwingUtilities.invokeLater(new Runnable() {
/*     */           public void run() {
/* 608 */             (new Tagger()).setVisible(true);
/*     */           }
/*     */         });
/*     */   } public void windowDeiconified(WindowEvent e) {} public void windowIconified(WindowEvent e) {}
/*     */   public void windowOpened(WindowEvent e) {}
/*     */   JFileChooser getImportExportChooser() {
/* 614 */     return this.myImportExportChooser;
/*     */   }
/*     */   
/*     */   ImportTagsAction getImportTagsAction() {
/* 618 */     return this.myImportTagsAction;
/*     */   }
/*     */ }


/* Location:              D:\Users\na\Desktop\TaggerJava6.zip!\TaggerJava6\Tagger.jar!\tagger\Tagger.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */