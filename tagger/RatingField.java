/*     */ package tagger;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.awt.event.ItemEvent;
/*     */ import java.awt.event.ItemListener;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Locale;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.SpringLayout;
/*     */ 
/*     */ class RatingField extends JPanel {
/*  15 */   private static final HashMap<String, LinkedHashMap<String, String>> LOOKUP = new HashMap<String, LinkedHashMap<String, String>>();
/*     */   static {
/*  17 */     LOOKUP.put("au-movie", Tab.AU_MOVIES);
/*  18 */     LOOKUP.put("au-tv", Tab.AU_TV);
/*  19 */     LOOKUP.put("ca-movie", Tab.CA_MOVIES);
/*  20 */     LOOKUP.put("ca-tv", Tab.CA_TV);
/*  21 */     LOOKUP.put("ie-movie", Tab.IE_MOVIES);
/*  22 */     LOOKUP.put("ie-tv", Tab.IE_TV);
/*  23 */     LOOKUP.put("mpaa", Tab.US_MOVIES);
/*  24 */     LOOKUP.put("nz-movie", Tab.NZ_MOVIES);
/*  25 */     LOOKUP.put("nz-tv", Tab.NZ_TV);
/*  26 */     LOOKUP.put("uk-movie", Tab.UK_MOVIES);
/*  27 */     LOOKUP.put("us-tv", Tab.US_TV);
/*     */   }
/*     */   
/*  30 */   private static final HashMap<String, String> COUNTRIES = new HashMap<String, String>();
/*     */   static {
/*  32 */     COUNTRIES.put("au-movie", (new Locale("en", "au")).getDisplayCountry(Tagger.LOCALE));
/*  33 */     COUNTRIES.put("au-tv", (new Locale("en", "au")).getDisplayCountry(Tagger.LOCALE));
/*  34 */     COUNTRIES.put("ca-movie", Locale.CANADA.getDisplayCountry(Tagger.LOCALE));
/*  35 */     COUNTRIES.put("ca-tv", Locale.CANADA.getDisplayCountry(Tagger.LOCALE));
/*  36 */     COUNTRIES.put("ie-movie", (new Locale("en", "ie")).getDisplayCountry(Tagger.LOCALE));
/*  37 */     COUNTRIES.put("ie-tv", (new Locale("en", "ie")).getDisplayCountry(Tagger.LOCALE));
/*  38 */     COUNTRIES.put("mpaa", Locale.US.getDisplayCountry(Tagger.LOCALE));
/*  39 */     COUNTRIES.put("nz-movie", (new Locale("en", "nz")).getDisplayCountry(Tagger.LOCALE));
/*  40 */     COUNTRIES.put("nz-tv", (new Locale("en", "nz")).getDisplayCountry(Tagger.LOCALE));
/*  41 */     COUNTRIES.put("uk-movie", Locale.UK.getDisplayCountry(Tagger.LOCALE));
/*  42 */     COUNTRIES.put("us-tv", Locale.US.getDisplayCountry(Tagger.LOCALE));
/*     */   }
/*     */ 
/*     */   
/*     */   private final Tab myTab;
/*     */   private final JComboBox myCountryField;
/*     */   private final JComboBox myTypeField;
/*     */   private final JComboBox myRatingField;
/*     */   private final JTextField myReasonField;
/*     */   private HashMap<String, String> myCurrentChoices;
/*     */   
/*     */   RatingField(Tab tab) {
/*  54 */     super(new SpringLayout());
/*     */     
/*  56 */     this.myTab = tab;
/*     */     
/*  58 */     this.myCountryField = new JComboBox(Tab.MOVIE_RATINGS.keySet().toArray()) {
/*     */         public Dimension getMaximumSize() {
/*  60 */           return getPreferredSize();
/*     */         }
/*     */       };
/*     */     
/*  64 */     this.myCountryField.addItemListener(new ItemListener() {
/*     */           public void itemStateChanged(ItemEvent e) {
/*  66 */             RatingField.this.myTab.setSaved(false);
/*     */             
/*  68 */             if (RatingField.this.myCountryField.getSelectedIndex() != 0) {
/*  69 */               RatingField.this.myTypeField.removeAllItems();
/*  70 */               RatingField.this.myTypeField.addItem(Tagger.getString("tag.content_rating.type.movie"));
/*     */               
/*  72 */               if (RatingField.this.myCountryField.getSelectedIndex() != 5) {
/*  73 */                 RatingField.this.myTypeField.addItem(Tagger.getString("tag.content_rating.type.tv_show"));
/*     */               }
/*  75 */               RatingField.this.myTypeField.setSelectedIndex(0);
/*     */             } else {
/*  77 */               RatingField.this.myRatingField.removeAllItems();
/*  78 */               RatingField.this.myTypeField.removeAllItems();
/*     */             } 
/*     */             
/*  81 */             RatingField.this.myRatingField.setEnabled((RatingField.this.myCountryField.getSelectedIndex() != 0));
/*  82 */             RatingField.this.myTypeField.setEnabled((RatingField.this.myCountryField.getSelectedIndex() != 0));
/*  83 */             RatingField.this.myReasonField.setEnabled((RatingField.this.myCountryField.getSelectedIndex() != 0));
/*     */           }
/*     */         });
/*     */     
/*  87 */     this.myCountryField.setToolTipText(Tagger.getString("tag.content_rating.country"));
/*  88 */     add(this.myCountryField);
/*     */     
/*  90 */     this.myTypeField = new JComboBox() {
/*     */         public Dimension getMaximumSize() {
/*  92 */           return getPreferredSize();
/*     */         }
/*     */       };
/*     */     
/*  96 */     this.myTypeField.addItemListener(new ItemListener() {
/*     */           public void itemStateChanged(ItemEvent e) {
/*  98 */             if (RatingField.this.myTypeField.getSelectedIndex() == -1) {
/*     */               return;
/*     */             }
/* 101 */             RatingField.this.myRatingField.removeAllItems();
/*     */             
/* 103 */             if (RatingField.this.myTypeField.getSelectedItem().equals(Tagger.getString("tag.content_rating.type.movie"))) {
/* 104 */               RatingField.this.myCurrentChoices = Tab.MOVIE_RATINGS.get(RatingField.this.myCountryField.getSelectedItem().toString());
/*     */             } else {
/* 106 */               RatingField.this.myCurrentChoices = Tab.TV_RATINGS.get(RatingField.this.myCountryField.getSelectedItem().toString());
/*     */             } 
/* 108 */             for (String key : RatingField.this.myCurrentChoices.keySet()) {
/* 109 */               RatingField.this.myRatingField.addItem(key);
/*     */             }
/*     */           }
/*     */         });
/* 113 */     this.myTypeField.setEnabled(false);
/* 114 */     this.myTypeField.setToolTipText(Tagger.getString("tag.content_rating.type"));
/* 115 */     add(this.myTypeField);
/*     */     
/* 117 */     this.myRatingField = new JComboBox() {
/*     */         public Dimension getMaximumSize() {
/* 119 */           return getPreferredSize();
/*     */         }
/*     */       };
/*     */     
/* 123 */     this.myRatingField.addItemListener(new ItemListener() {
/*     */           public void itemStateChanged(ItemEvent e) {
/* 125 */             RatingField.this.myTab.setSaved(false);
/*     */             
/* 127 */             if (RatingField.this.myRatingField.getSelectedIndex() != -1) {
/* 128 */               RatingField.this.myReasonField.setText(RatingField.this.myRatingField.getSelectedItem().toString());
/*     */             }
/*     */           }
/*     */         });
/* 132 */     this.myRatingField.setEnabled(false);
/* 133 */     this.myRatingField.setToolTipText(Tagger.getString("tag.content_rating"));
/* 134 */     add(this.myRatingField);
/*     */     
/* 136 */     this.myReasonField = new JTextField();
/* 137 */     this.myReasonField.setEnabled(false);
/* 138 */     this.myReasonField.setToolTipText(Tagger.getString("tag.content_rating.reason"));
/* 139 */     add(this.myReasonField);
/*     */     
/* 141 */     setOpaque(false);
/* 142 */     SpringUtilities.makeCompactGrid(this, 1, 4, 0, 0, 3, 0);
/*     */   }
/*     */   
/*     */   String getText() {
/* 146 */     if (this.myCountryField.getSelectedIndex() == 0) {
/* 147 */       return "";
/*     */     }
/* 149 */     String format = this.myCurrentChoices.get(this.myRatingField.getSelectedItem());
/* 150 */     String reason = this.myReasonField.getText();
/*     */     
/* 152 */     return format.replace("\\", "").replace("|(.*)", '|' + reason);
/*     */   }
/*     */   
/*     */   void setText(String data) {
/* 156 */     if (data == null || data.equals("")) {
/*     */       return;
/*     */     }
/* 159 */     String id = data.substring(0, data.indexOf("|"));
/* 160 */     this.myCurrentChoices = LOOKUP.get(id);
/* 161 */     String country = COUNTRIES.get(id);
/* 162 */     this.myCountryField.setSelectedItem(country);
/*     */     
/* 164 */     this.myTypeField.setSelectedIndex((id.indexOf("tv") != -1) ? 1 : 0);
/*     */     
/* 166 */     for (String text : this.myCurrentChoices.keySet()) {
/* 167 */       if (data.matches(this.myCurrentChoices.get(text))) {
/* 168 */         this.myRatingField.setSelectedItem(text);
/* 169 */         this.myReasonField.setText(data.replaceAll(this.myCurrentChoices.get(text), "$1"));
/* 170 */         this.myReasonField.setEnabled(true);
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\Users\na\Desktop\TaggerJava6.zip!\TaggerJava6\Tagger.jar!\tagger\RatingField.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */