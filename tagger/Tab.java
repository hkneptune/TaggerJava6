/*     */ package tagger;
/*     */ import com.jgoodies.forms.builder.DefaultFormBuilder;
/*     */ import com.jgoodies.forms.layout.FormLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.FlowLayout;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Locale;
/*     */ import javax.swing.BoxLayout;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTabbedPane;
/*     */ import javax.swing.JTextArea;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.event.UndoableEditEvent;
/*     */ import javax.swing.event.UndoableEditListener;
/*     */ import javax.swing.text.AbstractDocument;
/*     */ import javax.swing.text.AttributeSet;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.DocumentFilter;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import javax.swing.undo.UndoManager;
/*     */ 
/*     */ class Tab extends JTabbedPane implements UndoableEditListener {
/*  39 */   private static final DocumentSizeFilter DOCUMENT_FILTER = new DocumentSizeFilter(255);
/*  40 */   private static final DocumentSizeFilter DOCUMENT_NUMBER_FILTER = new DocumentSizeFilter(255) {
/*     */       public void insertString(DocumentFilter.FilterBypass fb, int offs, String str, AttributeSet a) throws BadLocationException {
/*  42 */         if (str.matches("[0-9]+")) {
/*  43 */           super.insertString(fb, offs, str, a);
/*     */         } else {
/*  45 */           Toolkit.getDefaultToolkit().beep();
/*     */         } 
/*     */       }
/*     */       public void replace(DocumentFilter.FilterBypass fb, int offs, int length, String str, AttributeSet a) throws BadLocationException {
/*  49 */         if (str.matches("[0-9]+")) {
/*  50 */           super.replace(fb, offs, length, str, a);
/*     */         } else {
/*  52 */           Toolkit.getDefaultToolkit().beep();
/*     */         } 
/*     */       }
/*     */     };
/*  56 */   static final BidiLinkedHashMap<String, String> KINDS = new BidiLinkedHashMap<String, String>(9);
/*     */   static {
/*  58 */     KINDS.put("", "");
/*  59 */     KINDS.put(Tagger.getString("kind.audiobook"), "Audiobook");
/*  60 */     KINDS.put(Tagger.getString("kind.booklet"), "Booklet");
/*  61 */     KINDS.put(Tagger.getString("kind.movie"), "Movie");
/*  62 */     KINDS.put(Tagger.getString("kind.music_video"), "Music Video");
/*  63 */     KINDS.put(Tagger.getString("kind.normal"), "Normal");
/*  64 */     KINDS.put(Tagger.getString("kind.short_film"), "Short Film");
/*  65 */     KINDS.put(Tagger.getString("kind.tv_show"), "TV Show");
/*  66 */     KINDS.put(Tagger.getString("kind.whacked_bookmark"), "Whacked Bookmark");
/*     */   }
/*     */   
/*  69 */   static final BidiLinkedHashMap<String, String> ADVISORIES = new BidiLinkedHashMap<String, String>(3);
/*     */   static {
/*  71 */     ADVISORIES.put("", "");
/*  72 */     ADVISORIES.put(Tagger.getString("advisory.clean"), "clean");
/*  73 */     ADVISORIES.put(Tagger.getString("advisory.explicit"), "explicit");
/*     */   }
/*     */   
/*  76 */   static final LinkedHashMap<String, String> AU_MOVIES = new LinkedHashMap<String, String>(6);
/*     */   static {
/*  78 */     AU_MOVIES.put("G", "au-movie\\|G\\|100\\|(.*)");
/*  79 */     AU_MOVIES.put("PG", "au-movie\\|PG\\|200\\|(.*)");
/*  80 */     AU_MOVIES.put("M", "au-movie\\|M\\|350\\|(.*)");
/*  81 */     AU_MOVIES.put("MA 15+", "au-movie\\|MA 15\\+\\|375\\|(.*)");
/*  82 */     AU_MOVIES.put("R18+", "au-movie\\|R18\\+\\|400\\|(.*)");
/*  83 */     AU_MOVIES.put(Tagger.getString("tag.content_rating.unrated"), "au-movie\\|UNRATED\\|900\\|(.*)");
/*     */   }
/*     */   
/*  86 */   static final LinkedHashMap<String, String> AU_TV = new LinkedHashMap<String, String>(8);
/*     */   static {
/*  88 */     AU_TV.put("P", "au-tv\\|P\\|100\\|(.*)");
/*  89 */     AU_TV.put("C", "au-tv\\|C\\|200\\|(.*)");
/*  90 */     AU_TV.put("G", "au-tv\\|G\\|300\\|(.*)");
/*  91 */     AU_TV.put("PG", "au-tv\\|PG\\|400\\|(.*)");
/*  92 */     AU_TV.put("M", "au-tv\\|M\\|500\\|(.*)");
/*  93 */     AU_TV.put("MA 15+", "au-tv\\|MA 15\\+\\|550\\|(.*)");
/*  94 */     AU_TV.put("AV 15+", "au-tv\\|AV 15\\+\\|570\\|(.*)");
/*  95 */     AU_TV.put(Tagger.getString("tag.content_rating.unrated"), "au-tv\\|UNRATED\\|900\\|(.*)");
/*     */   }
/*     */   
/*  98 */   static final LinkedHashMap<String, String> CA_MOVIES = new LinkedHashMap<String, String>(6);
/*     */   static {
/* 100 */     CA_MOVIES.put("G", "ca-movie\\|G\\|100\\|(.*)");
/* 101 */     CA_MOVIES.put("PG", "ca-movie\\|PG\\|200\\|(.*)");
/* 102 */     CA_MOVIES.put("14", "ca-movie\\|14\\|325\\|(.*)");
/* 103 */     CA_MOVIES.put("18", "ca-movie\\|18\\|400\\|(.*)");
/* 104 */     CA_MOVIES.put("R", "ca-movie\\|R\\|500\\|(.*)");
/* 105 */     CA_MOVIES.put(Tagger.getString("tag.content_rating.unrated"), "ca-movie\\|UNRATED\\|900\\|(.*)");
/*     */   }
/*     */   
/* 108 */   static final LinkedHashMap<String, String> CA_TV = new LinkedHashMap<String, String>(7);
/*     */   static {
/* 110 */     CA_TV.put("C", "ca-tv\\|C\\|100\\|(.*)");
/* 111 */     CA_TV.put("C8", "ca-tv\\|C8\\|200\\|(.*)");
/* 112 */     CA_TV.put("G", "ca-tv\\|G\\|300\\|(.*)");
/* 113 */     CA_TV.put("PG", "ca-tv\\|PG\\|400\\|(.*)");
/* 114 */     CA_TV.put("14+", "ca-tv\\|14\\+\\|500\\|(.*)");
/* 115 */     CA_TV.put("18+", "ca-tv\\|18\\+\\|600\\|(.*)");
/* 116 */     CA_TV.put(Tagger.getString("tag.content_rating.unrated"), "ca-tv\\|UNRATED\\|900\\|(.*)");
/*     */   }
/*     */   
/* 119 */   static final LinkedHashMap<String, String> IE_MOVIES = new LinkedHashMap<String, String>(7);
/*     */   static {
/* 121 */     IE_MOVIES.put("G", "ie-movie\\|G\\|100\\|(.*)");
/* 122 */     IE_MOVIES.put("PG", "ie-movie\\|PG\\|200\\|(.*)");
/* 123 */     IE_MOVIES.put("12", "ie-movie\\|12\\|300\\|(.*)");
/* 124 */     IE_MOVIES.put("15", "ie-movie\\|15\\|350\\|(.*)");
/* 125 */     IE_MOVIES.put("16", "ie-movie\\|16\\|375\\|(.*)");
/* 126 */     IE_MOVIES.put("18", "ie-movie\\|18\\|400\\|(.*)");
/* 127 */     IE_MOVIES.put(Tagger.getString("tag.content_rating.unrated"), "ie-movie\\|UNRATED\\|900\\|(.*)");
/*     */   }
/*     */   
/* 130 */   static final LinkedHashMap<String, String> IE_TV = new LinkedHashMap<String, String>(6);
/*     */   static {
/* 132 */     IE_TV.put("GA", "ie-tv\\|GA\\|100\\|(.*)");
/* 133 */     IE_TV.put("Ch", "ie-tv\\|Ch\\|200\\|(.*)");
/* 134 */     IE_TV.put("YA", "ie-tv\\|YA\\|400\\|(.*)");
/* 135 */     IE_TV.put("PS", "ie-tv\\|PS\\|500\\|(.*)");
/* 136 */     IE_TV.put("MA", "ie-tv\\|MA\\|600\\|(.*)");
/* 137 */     IE_TV.put(Tagger.getString("tag.content_rating.unrated"), "ie-tv\\|UNRATED\\|900\\|(.*)");
/*     */   }
/*     */   
/* 140 */   static final LinkedHashMap<String, String> NZ_MOVIES = new LinkedHashMap<String, String>(9);
/*     */   static {
/* 142 */     NZ_MOVIES.put("G", "nz-movie\\|G\\|100\\|(.*)");
/* 143 */     NZ_MOVIES.put("PG", "nz-movie\\|PG\\|200\\|(.*)");
/* 144 */     NZ_MOVIES.put("M", "nz-movie\\|M\\|300\\|(.*)");
/* 145 */     NZ_MOVIES.put("R13", "nz-movie\\|R13\\|325\\|(.*)");
/* 146 */     NZ_MOVIES.put("R15", "nz-movie\\|R15\\|350\\|(.*)");
/* 147 */     NZ_MOVIES.put("R16", "nz-movie\\|R16\\|375\\|(.*)");
/* 148 */     NZ_MOVIES.put("R18", "nz-movie\\|R18\\|400\\|(.*)");
/* 149 */     NZ_MOVIES.put("R", "nz-movie\\|R\\|500\\|(.*)");
/* 150 */     NZ_MOVIES.put(Tagger.getString("tag.content_rating.unrated"), "nz-movie\\|UNRATED\\|900\\|(.*)");
/*     */   }
/*     */   
/* 153 */   static final LinkedHashMap<String, String> NZ_TV = new LinkedHashMap<String, String>(4);
/*     */   static {
/* 155 */     NZ_TV.put("G", "nz-tv\\|G|200\\|(.*)");
/* 156 */     NZ_TV.put("PGR", "nz-tv\\|PGR\\|400\\|(.*)");
/* 157 */     NZ_TV.put("AO", "nz-tv\\|AO\\|600\\|(.*)");
/* 158 */     NZ_TV.put(Tagger.getString("tag.content_rating.unrated"), "nz-tv\\|UNRATED\\|900\\|(.*)");
/*     */   }
/*     */   
/* 161 */   static final LinkedHashMap<String, String> UK_MOVIES = new LinkedHashMap<String, String>(9);
/*     */   static {
/* 163 */     UK_MOVIES.put("U", "uk-movie\\|U\\|100\\|(.*)");
/* 164 */     UK_MOVIES.put("Uc", "uk-movie\\|Uc\\|150\\|(.*)");
/* 165 */     UK_MOVIES.put("PG", "uk-movie\\|PG\\|200\\|(.*)");
/* 166 */     UK_MOVIES.put("12", "uk-movie\\|12\\|300\\|(.*)");
/* 167 */     UK_MOVIES.put("12a", "uk-movie\\|12a\\|325\\|(.*)");
/* 168 */     UK_MOVIES.put("15", "uk-movie\\|15\\|350\\|(.*)");
/* 169 */     UK_MOVIES.put("18", "uk-movie\\|18\\|400\\|(.*)");
/* 170 */     UK_MOVIES.put("E", "uk-movie\\|E\\|600\\|(.*)");
/* 171 */     UK_MOVIES.put(Tagger.getString("tag.content_rating.unrated"), "uk-movie\\|UNRATED\\|900|(.*)");
/*     */   }
/*     */   
/* 174 */   static final LinkedHashMap<String, String> US_MOVIES = new LinkedHashMap<String, String>(6);
/*     */   static {
/* 176 */     US_MOVIES.put("G", "mpaa\\|G\\|100\\|(.*)");
/* 177 */     US_MOVIES.put("PG", "mpaa\\|PG\\|200\\|(.*)");
/* 178 */     US_MOVIES.put("PG-13", "mpaa\\|PG-13\\|300\\|(.*)");
/* 179 */     US_MOVIES.put("R", "mpaa\\|R\\|400\\|(.*)");
/* 180 */     US_MOVIES.put("NC-17", "mpaa\\|NC-17\\|500\\|(.*)");
/* 181 */     US_MOVIES.put(Tagger.getString("tag.content_rating.unrated"), "mpaa\\|UNRATED\\|900\\|(.*)");
/*     */   }
/*     */   
/* 184 */   static final LinkedHashMap<String, String> US_TV = new LinkedHashMap<String, String>(7);
/*     */   static {
/* 186 */     US_TV.put("TV-Y7", "us-tv\\|TV-Y7\\|100\\|(.*)");
/* 187 */     US_TV.put("TV-Y", "us-tv\\|TV-Y\\|200\\|(.*)");
/* 188 */     US_TV.put("TV-G", "us-tv\\|TV-G\\|300\\|(.*)");
/* 189 */     US_TV.put("TV-PG", "us-tv\\|TV-PG\\|400\\|(.*)");
/* 190 */     US_TV.put("TV-14", "us-tv\\|TV-14\\|500\\|(.*)");
/* 191 */     US_TV.put("TV-MA", "us-tv\\|TV-MA\\|600\\|(.*)");
/* 192 */     US_TV.put(Tagger.getString("tag.content_rating.unrated"), "us-tv\\|UNRATED\\|900\\|(.*)");
/*     */   }
/*     */   
/* 195 */   static final LinkedHashMap<String, HashMap<String, String>> MOVIE_RATINGS = new LinkedHashMap<String, HashMap<String, String>>(7);
/*     */   static {
/* 197 */     MOVIE_RATINGS.put("", null);
/* 198 */     MOVIE_RATINGS.put((new Locale("en", "au")).getDisplayCountry(Tagger.LOCALE), AU_MOVIES);
/* 199 */     MOVIE_RATINGS.put(Locale.CANADA.getDisplayCountry(Tagger.LOCALE), CA_MOVIES);
/* 200 */     MOVIE_RATINGS.put((new Locale("en", "ie")).getDisplayCountry(Tagger.LOCALE), IE_MOVIES);
/* 201 */     MOVIE_RATINGS.put((new Locale("en", "nz")).getDisplayCountry(Tagger.LOCALE), NZ_MOVIES);
/* 202 */     MOVIE_RATINGS.put(Locale.UK.getDisplayCountry(Tagger.LOCALE), UK_MOVIES);
/* 203 */     MOVIE_RATINGS.put(Locale.US.getDisplayCountry(Tagger.LOCALE), US_MOVIES);
/*     */   }
/*     */   
/* 206 */   static final HashMap<String, HashMap<String, String>> TV_RATINGS = new HashMap<String, HashMap<String, String>>(5);
/*     */   static {
/* 208 */     TV_RATINGS.put((new Locale("en", "au")).getDisplayCountry(Tagger.LOCALE), AU_TV);
/* 209 */     TV_RATINGS.put(Locale.CANADA.getDisplayCountry(Tagger.LOCALE), CA_TV);
/* 210 */     TV_RATINGS.put((new Locale("en", "ie")).getDisplayCountry(Tagger.LOCALE), IE_TV);
/* 211 */     TV_RATINGS.put((new Locale("en", "nz")).getDisplayCountry(Tagger.LOCALE), NZ_TV);
/* 212 */     TV_RATINGS.put(Locale.US.getDisplayCountry(Tagger.LOCALE), US_TV);
/*     */   }
/*     */   
/* 215 */   static final HashMap<String, String> SORTS = new HashMap<String, String>(6); private boolean myUndoOn;
/*     */   static {
/* 217 */     SORTS.put("soaa", "albumartist");
/* 218 */     SORTS.put("soal", "album");
/* 219 */     SORTS.put("soar", "artist");
/* 220 */     SORTS.put("soco", "composer");
/* 221 */     SORTS.put("sonm", "name");
/* 222 */     SORTS.put("sosn", "show");
/*     */   }
/*     */   
/*     */   private final MouseAdapter myLabelListener;
/*     */   private final ClosePanel myClosePanel;
/*     */   private final HashMap<String, JComponent> myFields;
/*     */   private final File myFile;
/*     */   private final JCoverFlow myArtPanel;
/*     */   private final ArrayList<File> myArt;
/*     */   private final UndoManager myUndoManager;
/*     */   private final Tagger myTagger;
/*     */   
/*     */   class ClosePanel
/*     */     extends JPanel implements ActionListener {
/*     */     private final JLabel myLabel;
/*     */     
/*     */     ClosePanel() {
/* 239 */       super(new FlowLayout(0, 3, 3));
/* 240 */       setOpaque(false);
/*     */       
/* 242 */       JButton myButton = new JButton(Tagger.getImage("close-normal.png"));
/* 243 */       myButton.addActionListener(this);
/* 244 */       myButton.setBorder((Border)null);
/* 245 */       myButton.setContentAreaFilled(false);
/* 246 */       myButton.setFocusPainted(false);
/* 247 */       myButton.setOpaque(false);
/* 248 */       myButton.setPressedIcon(Tagger.getImage("close-pressed.png"));
/* 249 */       myButton.setRolloverIcon(Tagger.getImage("close-hover.png"));
/* 250 */       myButton.setToolTipText(Tagger.getString("close.menu.label"));
/*     */       
/* 252 */       this.myLabel = new JLabel(Tab.this.myFile.getName());
/* 253 */       this.myLabel.setOpaque(false);
/*     */       
/* 255 */       add(this.myLabel);
/* 256 */       add(myButton);
/*     */     }
/*     */     
/*     */     public void actionPerformed(ActionEvent e) {
/* 260 */       Tab.this.myTagger.removeTab(Tab.this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Tab(Tagger tagger, File file) {
/* 267 */     this.myTagger = tagger;
/* 268 */     this.myFile = file;
/* 269 */     this.myUndoOn = false;
/*     */     
/* 271 */     this.myClosePanel = new ClosePanel();
/* 272 */     this.myFields = new HashMap<String, JComponent>(56);
/* 273 */     this.myUndoManager = new UndoManager();
/*     */     
/* 275 */     this.myLabelListener = new MouseAdapter() {
/*     */         public void mousePressed(MouseEvent e) {
/* 277 */           if (e.isControlDown()) {
/* 278 */             boolean saved = Tab.this.myClosePanel.myLabel.getText().equals(Tab.this.myFile.getName());
/* 279 */             Tab.this.myTagger.applyToAll(((JLabel)e.getSource()).getLabelFor());
/* 280 */             Tab.this.setSaved(saved);
/*     */           } 
/*     */         }
/*     */       };
/*     */     
/* 285 */     JComponent genre = new JTextField();
/*     */     
/* 287 */     this.myFields.put("©ART", new JTextField());
/* 288 */     this.myFields.put("©alb", new JTextField());
/* 289 */     this.myFields.put("©cmt", new JTextArea());
/* 290 */     this.myFields.put("©day", new JTextField());
/* 291 */     this.myFields.put("©gen", genre);
/* 292 */     this.myFields.put("©grp", new JTextField());
/* 293 */     this.myFields.put("©lyr", new JTextArea());
/* 294 */     this.myFields.put("©nam", new JTextField());
/* 295 */     this.myFields.put("©too", new JTextField());
/* 296 */     this.myFields.put("©wrt", new JTextField());
/* 297 */     this.myFields.put("aART", new JTextField());
/* 298 */     this.myFields.put("akID", new ITMSField(this.myTagger));
/* 299 */     this.myFields.put("apID", new ITMSField(this.myTagger));
/* 300 */     this.myFields.put("atID", new ITMSField(this.myTagger));
/* 301 */     this.myFields.put("catg", new JTextField());
/* 302 */     this.myFields.put("cmID", new ITMSField(this.myTagger));
/* 303 */     this.myFields.put("cnID", new ITMSField(this.myTagger));
/* 304 */     this.myFields.put("cpil", new JCheckBox(Tagger.getString("tag.compilation")));
/* 305 */     this.myFields.put("cprt", new JTextField());
/* 306 */     this.myFields.put("desc", new JTextArea());
/* 307 */     this.myFields.put("disk.number", new JTextField(3));
/* 308 */     this.myFields.put("disk.total", new JTextField(3));
/* 309 */     this.myFields.put("egid", new JTextField());
/* 310 */     this.myFields.put("geID", new ITMSField(this.myTagger));
/* 311 */     this.myFields.put("gnre", genre);
/* 312 */     this.myFields.put("iTunEXTC", new RatingField(this));
/* 313 */     this.myFields.put("iTunMOVI", new CastField(this));
/* 314 */     this.myFields.put("iTunNORM", new JTextField());
/* 315 */     this.myFields.put("iTunSMPB", new JTextField());
/* 316 */     this.myFields.put("iTunes_CDDB_1", new JTextField());
/* 317 */     this.myFields.put("iTunes_CDDB_IDs", new JTextField());
/* 318 */     this.myFields.put("iTunes_CDDB_TrackNumber", new JTextField());
/* 319 */     this.myFields.put("keyw", new JTextField());
/* 320 */     this.myFields.put("pcst", new JCheckBox(Tagger.getString("tag.podcast")));
/* 321 */     this.myFields.put("pgap", new JCheckBox(Tagger.getString("tag.gapless")));
/* 322 */     this.myFields.put("plID", new ITMSField(this.myTagger));
/* 323 */     this.myFields.put("purd", new JTextField());
/* 324 */     this.myFields.put("purl", new JTextField());
/* 325 */     this.myFields.put("rtng", new JComboBox(ADVISORIES.keySet().toArray()));
/* 326 */     this.myFields.put("sfID", new ITMSField(this.myTagger));
/* 327 */     this.myFields.put("soaa", new JTextField());
/* 328 */     this.myFields.put("soal", new JTextField());
/* 329 */     this.myFields.put("soar", new JTextField());
/* 330 */     this.myFields.put("soco", new JTextField());
/* 331 */     this.myFields.put("sonm", new JTextField());
/* 332 */     this.myFields.put("sosn", new JTextField());
/* 333 */     this.myFields.put("stik", new JComboBox(KINDS.keySet().toArray()));
/* 334 */     this.myFields.put("tmpo", new JTextField());
/* 335 */     this.myFields.put("tool", new JTextField());
/* 336 */     this.myFields.put("trkn.number", new JTextField(3));
/* 337 */     this.myFields.put("trkn.total", new JTextField(3));
/* 338 */     this.myFields.put("tven", new JTextField());
/* 339 */     this.myFields.put("tves", new JTextField(3));
/* 340 */     this.myFields.put("tvnn", new JTextField());
/* 341 */     this.myFields.put("tvsh", new JTextField());
/* 342 */     this.myFields.put("tvsn", new JTextField(3));
/*     */     
/* 344 */     for (String atom : this.myFields.keySet()) {
/* 345 */       JComponent f = this.myFields.get(atom);
/* 346 */       f.setName(atom);
/*     */       
/* 348 */       if (f instanceof JTextComponent) {
/* 349 */         JTextComponent field = (JTextComponent)f;
/* 350 */         this.myTagger.addListeners(field);
/*     */         
/* 352 */         AbstractDocument ad = (AbstractDocument)field.getDocument();
/* 353 */         ad.addUndoableEditListener(this);
/*     */         
/* 355 */         if (!f.getName().equals("©lyr")) {
/* 356 */           field.setToolTipText(Tagger.getString("tag.character_limit"));
/* 357 */           ad.setDocumentFilter(DOCUMENT_FILTER);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 362 */     wire(this.myFields.get("tmpo"));
/* 363 */     wire(this.myFields.get("disk.number"));
/* 364 */     wire(this.myFields.get("disk.total"));
/* 365 */     wire(this.myFields.get("trkn.number"));
/* 366 */     wire(this.myFields.get("trkn.total"));
/* 367 */     wire(this.myFields.get("tves"));
/* 368 */     wire(this.myFields.get("tvsn"));
/*     */     
/* 370 */     FormLayout generalLayout = new FormLayout("r:p, 3dlu, p:g, 3dlu, r:p, 3dlu, p, 3dlu, p, 3dlu, p", "");
/* 371 */     generalLayout.setColumnGroups(new int[][] { { 7, 11 } });
/* 372 */     DefaultFormBuilder generalBuilder = new DefaultFormBuilder(generalLayout, Tagger.RESOURCE_BUNDLE);
/* 373 */     generalBuilder.setDefaultDialogBorder();
/*     */     
/* 375 */     generalBuilder.appendI15d("tag.name", this.myFields.get("©nam"), 9);
/* 376 */     generalBuilder.nextLine();
/*     */     
/* 378 */     generalBuilder.appendI15d("tag.artist", this.myFields.get("©ART"), 9);
/* 379 */     generalBuilder.nextLine();
/*     */     
/* 381 */     generalBuilder.appendI15d("tag.album_artist", this.myFields.get("aART"), 9);
/* 382 */     generalBuilder.nextLine();
/*     */     
/* 384 */     generalBuilder.appendI15d("tag.album", this.myFields.get("©alb"), 9);
/* 385 */     generalBuilder.nextLine();
/*     */     
/* 387 */     generalBuilder.appendI15d("tag.release_date", this.myFields.get("©day"));
/* 388 */     generalBuilder.appendI15d("tag.track.number", this.myFields.get("trkn.number"));
/* 389 */     generalBuilder.appendI15d("tag.track.total", this.myFields.get("trkn.total"));
/* 390 */     generalBuilder.nextLine();
/*     */     
/* 392 */     generalBuilder.appendI15d("tag.purchase_date", this.myFields.get("purd"));
/* 393 */     generalBuilder.appendI15d("tag.disc.number", this.myFields.get("disk.number"));
/* 394 */     generalBuilder.appendI15d("tag.disc.total", this.myFields.get("disk.total"));
/* 395 */     generalBuilder.nextLine();
/*     */     
/* 397 */     generalBuilder.appendI15d("tag.grouping", this.myFields.get("©grp"));
/* 398 */     generalBuilder.appendI15d("tag.bpm", this.myFields.get("tmpo"), 5);
/* 399 */     generalBuilder.nextLine();
/*     */     
/* 401 */     generalBuilder.appendI15d("tag.composer", this.myFields.get("©wrt"), 9);
/* 402 */     generalBuilder.nextLine();
/*     */     
/* 404 */     JTextField tven = (JTextField)this.myFields.get("tven");
/*     */     
/* 406 */     JTextArea lyrics = (JTextArea)this.myFields.get("©lyr");
/* 407 */     lyrics.setFont(tven.getFont());
/* 408 */     lyrics.setLineWrap(true);
/* 409 */     lyrics.setMargin(tven.getMargin());
/* 410 */     lyrics.setRows(3);
/* 411 */     lyrics.setWrapStyleWord(true);
/* 412 */     generalBuilder.appendI15d("tag.lyrics", new JScrollPane(lyrics), 9).setLabelFor(lyrics);
/* 413 */     generalBuilder.nextLine();
/*     */     
/* 415 */     JTextArea comments = (JTextArea)this.myFields.get("©cmt");
/* 416 */     comments.setFont(tven.getFont());
/* 417 */     comments.setLineWrap(true);
/* 418 */     comments.setMargin(tven.getMargin());
/* 419 */     comments.setRows(3);
/* 420 */     comments.setWrapStyleWord(true);
/* 421 */     generalBuilder.appendI15d("tag.comments", new JScrollPane(comments), 9).setLabelFor(comments);
/* 422 */     generalBuilder.nextLine();
/*     */     
/* 424 */     generalBuilder.appendI15d("tag.kind", this.myFields.get("stik"));
/* 425 */     generalBuilder.appendI15d("tag.advisory", this.myFields.get("rtng"), 5);
/* 426 */     generalBuilder.nextLine();
/*     */     
/* 428 */     generalBuilder.appendI15d("tag.copyright", this.myFields.get("cprt"), 9);
/* 429 */     generalBuilder.nextLine();
/*     */     
/* 431 */     generalBuilder.appendI15d("tag.encoding_tool", this.myFields.get("©too"), 9);
/* 432 */     generalBuilder.nextLine();
/*     */     
/* 434 */     generalBuilder.appendI15d("tag.genre", this.myFields.get("©gen"));
/* 435 */     JCheckBox gapless = (JCheckBox)this.myFields.get("pgap");
/* 436 */     gapless.setOpaque(false);
/* 437 */     generalBuilder.append(gapless);
/* 438 */     JCheckBox compilation = (JCheckBox)this.myFields.get("cpil");
/* 439 */     compilation.setOpaque(false);
/* 440 */     generalBuilder.append(compilation, 5);
/*     */     
/* 442 */     generalBuilder.getPanel().setOpaque(false);
/* 443 */     addTab(Tagger.getString("section.general"), generalBuilder.getPanel());
/*     */     
/* 445 */     DefaultFormBuilder videoBuilder = new DefaultFormBuilder(new FormLayout("r:p, 3dlu, p:g, 3dlu, r:p, 3dlu, p", ""), Tagger.RESOURCE_BUNDLE);
/* 446 */     videoBuilder.setDefaultDialogBorder();
/*     */     
/* 448 */     videoBuilder.appendI15d("tag.show", this.myFields.get("tvsh"), 5);
/* 449 */     videoBuilder.nextLine();
/*     */     
/* 451 */     videoBuilder.appendI15d("tag.network", this.myFields.get("tvnn"));
/* 452 */     videoBuilder.appendI15d("tag.season", this.myFields.get("tvsn"));
/* 453 */     videoBuilder.nextLine();
/*     */     
/* 455 */     videoBuilder.appendI15d("tag.episode_id", tven);
/* 456 */     videoBuilder.appendI15d("tag.episode", this.myFields.get("tves"));
/* 457 */     videoBuilder.nextLine();
/*     */     
/* 459 */     videoBuilder.appendI15d("tag.content_rating", this.myFields.get("iTunEXTC"), 5);
/* 460 */     videoBuilder.nextLine();
/*     */     
/* 462 */     JTextArea description = (JTextArea)this.myFields.get("desc");
/* 463 */     description.setFont(tven.getFont());
/* 464 */     description.setLineWrap(true);
/* 465 */     description.setMargin(tven.getMargin());
/* 466 */     description.setRows(3);
/* 467 */     description.setWrapStyleWord(true);
/* 468 */     videoBuilder.appendI15d("tag.description", new JScrollPane(description), 5).setLabelFor(description);
/* 469 */     videoBuilder.nextLine();
/*     */     
/* 471 */     videoBuilder.appendI15d("tag.cast", this.myFields.get("iTunMOVI"), 5);
/*     */     
/* 473 */     videoBuilder.getPanel().setOpaque(false);
/* 474 */     addTab(Tagger.getString("section.video"), videoBuilder.getPanel());
/*     */     
/* 476 */     DefaultFormBuilder podcastBuilder = new DefaultFormBuilder(new FormLayout("r:p, 3dlu, p:g", ""), Tagger.RESOURCE_BUNDLE);
/* 477 */     podcastBuilder.setDefaultDialogBorder();
/*     */     
/* 479 */     JCheckBox podcast = (JCheckBox)this.myFields.get("pcst");
/* 480 */     podcast.setOpaque(false);
/* 481 */     podcastBuilder.append(podcast);
/* 482 */     podcastBuilder.nextLine();
/*     */     
/* 484 */     podcastBuilder.appendI15d("tag.podcast_url", this.myFields.get("purl"));
/* 485 */     podcastBuilder.nextLine();
/*     */     
/* 487 */     podcastBuilder.appendI15d("tag.podcast_guid", this.myFields.get("egid"));
/* 488 */     podcastBuilder.nextLine();
/*     */     
/* 490 */     podcastBuilder.appendI15d("tag.category", this.myFields.get("catg"));
/* 491 */     podcastBuilder.nextLine();
/*     */     
/* 493 */     podcastBuilder.appendI15d("tag.keywords", this.myFields.get("keyw"));
/*     */     
/* 495 */     podcastBuilder.getPanel().setOpaque(false);
/* 496 */     addTab(Tagger.getString("section.podcast"), podcastBuilder.getPanel());
/*     */     
/* 498 */     DefaultFormBuilder sortingBuilder = new DefaultFormBuilder(new FormLayout("r:p, 3dlu, p:g", ""), Tagger.RESOURCE_BUNDLE);
/* 499 */     sortingBuilder.setDefaultDialogBorder();
/*     */     
/* 501 */     sortingBuilder.appendI15d("tag.sort_name", this.myFields.get("sonm"));
/* 502 */     sortingBuilder.nextLine();
/*     */     
/* 504 */     sortingBuilder.appendI15d("tag.sort_artist", this.myFields.get("soar"));
/* 505 */     sortingBuilder.nextLine();
/*     */     
/* 507 */     sortingBuilder.appendI15d("tag.sort_album_artist", this.myFields.get("soaa"));
/* 508 */     sortingBuilder.nextLine();
/*     */     
/* 510 */     sortingBuilder.appendI15d("tag.sort_album", this.myFields.get("soal"));
/* 511 */     sortingBuilder.nextLine();
/*     */     
/* 513 */     sortingBuilder.appendI15d("tag.sort_composer", this.myFields.get("soco"));
/* 514 */     sortingBuilder.nextLine();
/*     */     
/* 516 */     sortingBuilder.appendI15d("tag.sort_show", this.myFields.get("sosn"));
/*     */     
/* 518 */     sortingBuilder.getPanel().setOpaque(false);
/* 519 */     addTab(Tagger.getString("section.sorting"), sortingBuilder.getPanel());
/*     */     
/* 521 */     DefaultFormBuilder itmsBuilder = new DefaultFormBuilder(new FormLayout("r:p, 3dlu, p:g", ""), Tagger.RESOURCE_BUNDLE);
/* 522 */     itmsBuilder.setDefaultDialogBorder();
/*     */     
/* 524 */     itmsBuilder.append("akID", this.myFields.get("akID"));
/* 525 */     itmsBuilder.nextLine();
/*     */     
/* 527 */     itmsBuilder.append("apID", this.myFields.get("apID"));
/* 528 */     itmsBuilder.nextLine();
/*     */     
/* 530 */     itmsBuilder.append("atID", this.myFields.get("atID"));
/* 531 */     itmsBuilder.nextLine();
/*     */     
/* 533 */     itmsBuilder.append("cmID", this.myFields.get("cmID"));
/* 534 */     itmsBuilder.nextLine();
/*     */     
/* 536 */     itmsBuilder.append("cnID", this.myFields.get("cnID"));
/* 537 */     itmsBuilder.nextLine();
/*     */     
/* 539 */     itmsBuilder.append("geID", this.myFields.get("geID"));
/* 540 */     itmsBuilder.nextLine();
/*     */     
/* 542 */     itmsBuilder.append("plID", this.myFields.get("plID"));
/* 543 */     itmsBuilder.nextLine();
/*     */     
/* 545 */     itmsBuilder.append("sfID", this.myFields.get("sfID"));
/* 546 */     itmsBuilder.nextLine();
/*     */     
/* 548 */     itmsBuilder.append("iTunNORM", this.myFields.get("iTunNORM"));
/* 549 */     itmsBuilder.nextLine();
/*     */     
/* 551 */     itmsBuilder.append("iTunSMPB", this.myFields.get("iTunSMPB"));
/* 552 */     itmsBuilder.nextLine();
/*     */     
/* 554 */     itmsBuilder.append("iTunes_CDDB_1", this.myFields.get("iTunes_CDDB_1"));
/* 555 */     itmsBuilder.nextLine();
/*     */     
/* 557 */     itmsBuilder.append("iTunes_CDDB_IDs", this.myFields.get("iTunes_CDDB_IDs"));
/* 558 */     itmsBuilder.nextLine();
/*     */     
/* 560 */     itmsBuilder.append("iTunes_CDDB_TrackNumber", this.myFields.get("iTunes_CDDB_TrackNumber"));
/* 561 */     itmsBuilder.nextLine();
/*     */     
/* 563 */     itmsBuilder.append("tool", this.myFields.get("tool"));
/*     */     
/* 565 */     itmsBuilder.getPanel().setOpaque(false);
/* 566 */     addTab(Tagger.getString("section.itms"), itmsBuilder.getPanel());
/*     */     
/* 568 */     this.myArt = new ArrayList<File>();
/*     */     
/* 570 */     JPanel myArtTab = new JPanel();
/* 571 */     myArtTab.setLayout(new BoxLayout(myArtTab, 1));
/* 572 */     myArtTab.setOpaque(false);
/*     */     
/* 574 */     this.myArtPanel = new JCoverFlow(this.myTagger);
/* 575 */     this.myArtPanel.setPreferredSize(new Dimension((this.myArtPanel.getPreferredSize()).width, 300));
/* 576 */     myArtTab.add(this.myArtPanel);
/*     */     
/* 578 */     addTab(Tagger.getString("section.artwork"), myArtTab);
/*     */     
/* 580 */     for (Component c : generalBuilder.getPanel().getComponents()) {
/* 581 */       if (c instanceof JLabel) {
/* 582 */         c.addMouseListener(this.myLabelListener);
/* 583 */         ((JLabel)c).setToolTipText(Tagger.getString("copy_to_all.tooltip"));
/*     */       } 
/*     */     } 
/*     */     
/* 587 */     for (Component c : videoBuilder.getPanel().getComponents()) {
/* 588 */       if (c instanceof JLabel) {
/* 589 */         c.addMouseListener(this.myLabelListener);
/* 590 */         ((JLabel)c).setToolTipText(Tagger.getString("copy_to_all.tooltip"));
/*     */       } 
/*     */     } 
/*     */     
/* 594 */     for (Component c : podcastBuilder.getPanel().getComponents()) {
/* 595 */       if (c instanceof JLabel) {
/* 596 */         c.addMouseListener(this.myLabelListener);
/* 597 */         ((JLabel)c).setToolTipText(Tagger.getString("copy_to_all.tooltip"));
/*     */       } 
/*     */     } 
/*     */     
/* 601 */     for (Component c : sortingBuilder.getPanel().getComponents()) {
/* 602 */       if (c instanceof JLabel) {
/* 603 */         c.addMouseListener(this.myLabelListener);
/* 604 */         ((JLabel)c).setToolTipText(Tagger.getString("copy_to_all.tooltip"));
/*     */       } 
/*     */     } 
/*     */     
/* 608 */     setOpaque(false);
/* 609 */     new FileDrop(this, null, this.myTagger.getImportTagsAction());
/*     */   }
/*     */   
/*     */   ArrayList<File> getArt() {
/* 613 */     return this.myArt;
/*     */   }
/*     */   
/*     */   CoverFlowPicture getSelectedArtwork() {
/* 617 */     return this.myArtPanel.getSelectedPicture();
/*     */   }
/*     */   
/*     */   void addArtwork(File file) throws Exception {
/* 621 */     this.myArt.add(file);
/* 622 */     this.myArtPanel.add(file);
/*     */   }
/*     */   
/*     */   int getArtworkCount() {
/* 626 */     return this.myArt.size();
/*     */   }
/*     */   
/*     */   void removeArtwork(CoverFlowPicture cfp) {
/* 630 */     this.myArt.remove(cfp.getFile());
/* 631 */     this.myArtPanel.remove(cfp);
/*     */   }
/*     */   
/*     */   void setSaved(boolean saved) {
/* 635 */     this.myClosePanel.myLabel.setText(this.myFile.getName() + (saved ? "" : " *"));
/*     */   }
/*     */   
/*     */   private void wire(JComponent c) {
/* 639 */     ((AbstractDocument)((JTextField)c).getDocument()).setDocumentFilter(DOCUMENT_NUMBER_FILTER);
/*     */   }
/*     */   
/*     */   void setUndoOn(boolean on) {
/* 643 */     this.myUndoOn = on;
/*     */   }
/*     */   
/*     */   public void undoableEditHappened(UndoableEditEvent e) {
/* 647 */     if (this.myUndoOn) {
/* 648 */       this.myUndoManager.addEdit(e.getEdit());
/* 649 */       this.myTagger.getUndoAction().update();
/* 650 */       this.myTagger.getRedoAction().update();
/*     */     } 
/*     */   }
/*     */   
/*     */   UndoManager getUndoManager() {
/* 655 */     return this.myUndoManager;
/*     */   }
/*     */   
/*     */   JComponent[] getFields() {
/* 659 */     return (JComponent[])this.myFields.values().toArray((Object[])new JComponent[this.myFields.size()]);
/*     */   }
/*     */   
/*     */   JComponent getField(String field) {
/* 663 */     return this.myFields.get(field);
/*     */   }
/*     */   
/*     */   ClosePanel getClosePanel() {
/* 667 */     return this.myClosePanel;
/*     */   }
/*     */   
/*     */   File getFile() {
/* 671 */     return this.myFile;
/*     */   }
/*     */ }


/* Location:              D:\Users\na\Desktop\TaggerJava6.zip!\TaggerJava6\Tagger.jar!\tagger\Tab.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */