class Stemmer {
    private char[] b;
    private int i,     /* offset into b */
            i_end, 
            j, k;
    private static final int INC = 50;

    //Constructor
    public Stemmer() {
        b = new char[INC];
        i = 0;
        i_end = 0;
    }
    
    //Add a character to the String and then stem the string
    public void add(char ch) {
        if (i == b.length) {
            char[] new_b = new char[i + INC];
            for (int c = 0; c < i; c++) new_b[c] = b[c];
            b = new_b;
        }
        b[i++] = ch;
    }

    public void add(char[] w, int wLen) {
        if (i + wLen >= b.length) {
            char[] new_b = new char[i + wLen + INC];
            for (int c = 0; c < i; c++) new_b[c] = b[c];
            b = new_b;
        }
        for (int c = 0; c < wLen; c++) b[i++] = w[c];
    }

    public String toString() {
        return new String(b, 0, i_end);
    }

    //Returns the length of the word after the stemming process.     
    public int getResultLength() {
        return i_end;
    }

    public char[] getResultBuffer() {
        return b;
    }

    private final boolean cons(int i) {
        switch (b[i]) {
            case 'a':
            case 'e':
            case 'i':
            case 'o':
            case 'u':
                return false;
            case 'y':
                return (i == 0) ? true : !cons(i - 1);
            default:
                return true;
        }
    }
    private final int m() {
        int n = 0;
        int i = 0;
        while (true) {
            if (i > j) return n;
            if (!cons(i)) break;
            i++;
        }
        i++;
        while (true) {
            while (true) {
                if (i > j) return n;
                if (cons(i)) break;
                i++;
            }
            i++;
            n++;
            while (true) {
                if (i > j) return n;
                if (!cons(i)) break;
                i++;
            }
            i++;
        }
    }
    private final boolean ends(String s) {
        int l = s.length();
        int o = k - l + 1;
        if (o < 0) return false;
        for (int i = 0; i < l; i++) if (b[o + i] != s.charAt(i)) return false;
        j = k - l;
        return true;
    }

    private final void setto(String s) {
        int l = s.length();
        int o = j + 1;
        for (int i = 0; i < l; i++) b[o + i] = s.charAt(i);
        k = j + l;
    }

    private final void r(String s) {
        if (m() > 0) setto(s);
    }

/* 
	   days    -> day
	   results -> result
*/

    private final void step1() {
        if (b[k] == 's') {
            if (ends("sses")) k -= 2;
            else if (ends("ies")) setto("i");
            else if (b[k - 1] != 's') k--;
        }
    }
/*  
  		collegeki    -> college
  		rojulu       -> roju
  		alochanalu   -> alochana
  		chirunavullo -> chirunavu
  		sandallu     -> sandi
  		ammaaaaa     -> amma
 */
    private final void step2() {
        if (k == 0) return;
        switch (b[k - 1]) {
            case 'a':
                if (ends("aaaaa")) {
                    r("a");
                    break;
                }
                if (ends("tional")) {
                    r("tion");
                    break;
                }
                if(ends("am")) {
                	r("a");
                	break;
                }
                
                if(ends("a.")) {
                	r("a");
                	break;
                }
                break;
            case 'c':
                if (ends("enci")) {
                    r("enci");
                    break;
                }
                if (ends("anci")) {
                    r("anci");
                    break;
                }
                if(ends("c.")) {
                	r("c");
                	break;
                }
                break;
            case 'e':
                if(ends("e.")) {
                	r("e");
                	break;
                }
                break;
            case 'l':
                if (ends("bli")) {
                    r("ble");
                    break;
                }
                if (ends("alu")) {
                    r("a");
                    break;
                }
                if (ends("nlu")) {
                    r("n");
                    break;
                }
                if (ends("ulu")) {
                    r("u");
                    break;
                }
                if (ends("rlu")) {
                    r("r");
                    break;
                }
                if (ends("ullo")) {
                    r("u");
                    break;
                }
                if (ends("allo")) {
                    r("a");
                    break;
                }
                if (ends("allu")) {
                    r("i");
                    break;
                }
                if (ends("alli")) {
                    r("al");
                    break;
                }
                if (ends("entli")) {
                    r("ent");
                    break;
                }
                if (ends("eli")) {
                    r("e");
                    break;
                }
                if (ends("olu")) {
                    r("ol");
                    break;
                }
                if(ends("l.")) {
                	r("l");
                	break;
                }
                break;
            case 'o':
                if (ends("ization")) {
                    r("ize");
                    break;
                }
                if (ends("ation")) {
                    r("ation");
                    break;
                }
                if(ends("o.")) {
                	r("o");
                	break;
                }
                break;
            case 's':
                if (ends("fulness")) {
                    r("ful");
                    break;
                }
                if(ends("s.")) {
                	r("s");
                	break;
                }
                break;
            case 'g':
                if (ends("aga")) {
                    r("akudaa");
                    break;
                }
                if(ends("g.")) {
                	r("g");
                	break;
                }
                break;
            case 'k':
                if (ends("eki")) {
                    r("e");
                    break;
                }
                if (ends("aki")) {
                    r("a");
                    break;
                }
                if (ends("aku")) {
                    r("a");
                    break;
                }
                if(ends("k.")) {
                	r("k");
                	break;
                }
                break;
            case 'i':
                if (ends("ipoyam")) {
                    r("i");
                    break;
                }
                break;
        }
    }

/* deals with words ending -ic-, -full, -ness  */
    private final void step4() {
        switch (b[k]) {
            case 'e':
                if (ends("ative")) {
                    r("");
                    break;
                }
                if (ends("alize")) {
                    r("al");
                    break;
                }
                break;
            case 'l':
                if (ends("ful")) {
                    r("");
                    break;
                }
                break;
        }
    }

    private final void step5() {
        if (k == 0) return; 
        switch (b[k - 1]) {
            case 'a':
                if (ends("al")) break;
                return;
            case 'i':
                if (ends("ic")) break;
                return;
            case 'l':
                if (ends("able")) break;
                if (ends("ible")) break;
                return;
            case 'n':
                if (ends("ant")) break;
                if (ends("ement")) break;
                if (ends("ment")) break;
                if (ends("ent")) break;
                return;
            default:
                return;
        }
        if (m() > 1) k = j;
    }

    //Stem the word placed into the Stemmer buffer
    public void stem() {
        k = i - 1;
        if (k > 1) {
            step1();
            step2();
            step4();
            step5();
        }
        i_end = k + 1;
        i = 0;
    }

}
