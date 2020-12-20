/*     */ package tagger;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ 
/*     */ class AtomicParsley {
/*   8 */   static final HashMap<String, String> ATOMS = new HashMap<String, String>(52);
/*     */   static {
/*  10 */     ATOMS.put("©ART", "artist");
/*  11 */     ATOMS.put("©alb", "album");
/*  12 */     ATOMS.put("©cmt", "comment");
/*  13 */     ATOMS.put("©day", "year");
/*  14 */     ATOMS.put("©gen", "genre");
/*  15 */     ATOMS.put("©grp", "grouping");
/*  16 */     ATOMS.put("©lyr", "lyrics");
/*  17 */     ATOMS.put("©nam", "title");
/*  18 */     ATOMS.put("©too", "encodingTool");
/*  19 */     ATOMS.put("©wrt", "composer");
/*  20 */     ATOMS.put("aART", "albumArtist");
/*  21 */     ATOMS.put("akID", null);
/*  22 */     ATOMS.put("apID", null);
/*  23 */     ATOMS.put("atID", null);
/*  24 */     ATOMS.put("catg", "category");
/*  25 */     ATOMS.put("cmID", null);
/*  26 */     ATOMS.put("cnID", null);
/*  27 */     ATOMS.put("cpil", "compilation");
/*  28 */     ATOMS.put("cprt", "copyright");
/*  29 */     ATOMS.put("desc", "description");
/*  30 */     ATOMS.put("disk", "disk");
/*  31 */     ATOMS.put("egid", "podcastGUID");
/*  32 */     ATOMS.put("geID", null);
/*  33 */     ATOMS.put("iTunEXTC", null);
/*  34 */     ATOMS.put("iTunMOVI", null);
/*  35 */     ATOMS.put("iTunNORM", null);
/*  36 */     ATOMS.put("iTunSMPB", null);
/*  37 */     ATOMS.put("iTunes_CDDB_1", null);
/*  38 */     ATOMS.put("iTunes_CDDB_IDs", null);
/*  39 */     ATOMS.put("iTunes_CDDB_TrackNumber", null);
/*  40 */     ATOMS.put("keyw", "keyword");
/*  41 */     ATOMS.put("pcst", "podcastFlag");
/*  42 */     ATOMS.put("plID", null);
/*  43 */     ATOMS.put("pgap", "gapless");
/*  44 */     ATOMS.put("purd", "purchaseDate");
/*  45 */     ATOMS.put("purl", "podcastURL");
/*  46 */     ATOMS.put("rtng", "advisory");
/*  47 */     ATOMS.put("sfID", null);
/*  48 */     ATOMS.put("soaa", null);
/*  49 */     ATOMS.put("soal", null);
/*  50 */     ATOMS.put("soar", null);
/*  51 */     ATOMS.put("soco", null);
/*  52 */     ATOMS.put("sonm", null);
/*  53 */     ATOMS.put("sosn", null);
/*  54 */     ATOMS.put("stik", "stik");
/*  55 */     ATOMS.put("tmpo", "bpm");
/*  56 */     ATOMS.put("tool", null);
/*  57 */     ATOMS.put("trkn", "tracknum");
/*  58 */     ATOMS.put("tven", "TVEpisode");
/*  59 */     ATOMS.put("tves", "TVEpisodeNum");
/*  60 */     ATOMS.put("tvnn", "TVNetwork");
/*  61 */     ATOMS.put("tvsh", "TVShowName");
/*  62 */     ATOMS.put("tvsn", "TVSeasonNum");
/*     */   }
/*     */   
/*  65 */   static final BidiLinkedHashMap<String, String> I18N = new BidiLinkedHashMap<String, String>(35);
/*     */   static {
/*  67 */     I18N.put("©ART", "artist");
/*  68 */     I18N.put("©alb", "album");
/*  69 */     I18N.put("©cmt", "comment");
/*  70 */     I18N.put("©day", "year");
/*  71 */     I18N.put("©gen", "genre");
/*  72 */     I18N.put("©grp", "grouping");
/*  73 */     I18N.put("©lyr", "lyrics");
/*  74 */     I18N.put("©nam", "title");
/*  75 */     I18N.put("©too", "encoding_tool");
/*  76 */     I18N.put("©wrt", "composer");
/*  77 */     I18N.put("aART", "album_artist");
/*  78 */     I18N.put("catg", "category");
/*  79 */     I18N.put("cpil", "compilation");
/*  80 */     I18N.put("cprt", "copyright");
/*  81 */     I18N.put("desc", "description");
/*  82 */     I18N.put("disk.number", "disk.number");
/*  83 */     I18N.put("disk.total", "disk.total");
/*  84 */     I18N.put("egid", "podcast_guid");
/*  85 */     I18N.put("iTunEXTC", "content_rating");
/*  86 */     I18N.put("iTunMOVI", "cast");
/*  87 */     I18N.put("iTunNORM", "iTunNORM");
/*  88 */     I18N.put("iTunSMPB", "iTunSMPB");
/*  89 */     I18N.put("iTunes_CDDB_1", "iTunes_CDDB_1");
/*  90 */     I18N.put("iTunes_CDDB_IDs", "iTunes_CDDB_IDs");
/*  91 */     I18N.put("iTunes_CDDB_TrackNumber", "iTunes_CDDB_TrackNumber");
/*  92 */     I18N.put("keyw", "keyword");
/*  93 */     I18N.put("pcst", "podcast");
/*  94 */     I18N.put("pgap", "gapless");
/*  95 */     I18N.put("purd", "purchase_date");
/*  96 */     I18N.put("purl", "podcast_url");
/*  97 */     I18N.put("rtng", "advisory");
/*  98 */     I18N.put("soaa", "sort_album_artist");
/*  99 */     I18N.put("soal", "sort_album");
/* 100 */     I18N.put("soar", "sort_artist");
/* 101 */     I18N.put("soco", "sort_composer");
/* 102 */     I18N.put("sonm", "sort_name");
/* 103 */     I18N.put("sosn", "sort_show");
/* 104 */     I18N.put("stik", "kind");
/* 105 */     I18N.put("tmpo", "bpm");
/* 106 */     I18N.put("tool", "tool");
/* 107 */     I18N.put("trkn.number", "track.number");
/* 108 */     I18N.put("trkn.total", "track.total");
/* 109 */     I18N.put("tven", "episode_id");
/* 110 */     I18N.put("tves", "episode");
/* 111 */     I18N.put("tvnn", "network");
/* 112 */     I18N.put("tvsh", "show");
/* 113 */     I18N.put("tvsn", "season");
/*     */   }
/*     */   
/*     */   static String getExecutable() {
/* 117 */     if (System.getProperty("os.name").contains("Windows")) {
/* 118 */       return "AtomicParsley-utf8";
/*     */     }
/* 120 */     return "AtomicParsley";
/*     */   }
/*     */   
/*     */   static Process execute(String... args) throws IOException {
/* 124 */     return (new ProcessBuilder(args)).start();
/*     */   }
/*     */   
/*     */   static Process execute(List<String> args) throws IOException {
/* 128 */     return (new ProcessBuilder(args)).start();
/*     */   }
/*     */ }


/* Location:              D:\Users\na\Desktop\TaggerJava6.zip!\TaggerJava6\Tagger.jar!\tagger\AtomicParsley.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */