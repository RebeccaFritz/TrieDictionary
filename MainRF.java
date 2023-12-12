import java.util.Iterator;
import java.util.Stack;

public class TrieRF implements Set<String>, Iterable<String> {

    private class Tnode {
        String l;
        Tnode lmchild;
        Tnode rsibling;
        boolean wordEnd;
    }

    Tnode root = null;
    int len = 0;

    public void add(String s) {
        root = addRecursion(s, root);
    }

    private Tnode addRecursion(String s, Tnode tn) { // lets: index of the letter
        String l = s.substring(0, 1);
        
        boolean wordEnd = false;
        if( s.length() == 1 ) { // last letter in the word
            wordEnd = true;
        }

        if( tn == null ) {
            tn = new Tnode();
            tn.l = l;
            tn.rsibling = null;
            tn.lmchild = null;
            tn.wordEnd = wordEnd;
        } 
        
        if ( tn.l.equals(l) && ! wordEnd ) {
            tn.lmchild = addRecursion(s.substring(1), tn.lmchild);
        } else if( tn.l.equals(l) && wordEnd ) {
            tn.wordEnd = wordEnd;
            len++;
        } else {
            tn.rsibling = addRecursion(s, tn.rsibling);
        }

        return tn;
    }

    public boolean contains(String s) {
        if ( s.length() == 0 ) {
            return false;
        }

        return contains(s, root);
    }

    private boolean contains(String s, Tnode t) {
        String fl = s.substring(0,1);
        
        Tnode cur = t;

        while ( cur != null && ! cur.l.equals(fl) ) {
            cur = cur.rsibling;
        }

        if ( cur == null ) {
            return false;
        }

        if ( s.length() == 1 ) {
            return cur.wordEnd;
        }

        String rs = s.substring(1);
        return contains(rs, cur.lmchild);
    }

    public void remove(String s) { // just sets the wordEnd to false
        remove(s, root);
    }

    void remove(String s, Tnode tn) {
        String fl = s.substring(0,1);
        
        Tnode cur = tn;

        while ( cur != null && ! cur.l.equals(fl) ) {
            cur = cur.rsibling;
        }

        if ( cur == null ) {
            return;
        }

        if ( s.length() == 1 ) {
            cur.wordEnd = false;
            return;
        }

        String rs = s.substring(1);
        remove(rs, cur.lmchild);
    }

    public int length() {
        return len;
    }

    class TRFIterator implements Iterator<String> {
        class Pair {
            Tnode tn;
            String s;
        }

        Stack<Pair> tnStack = new Stack<>();
        String nextString = null;

        private TRFIterator() {
            Pair rootPair = new Pair();
            rootPair.tn = root;
            rootPair.s = "";

            if( root != null ) {
                tnStack.push(rootPair);
            }

            nextHelper();
        }

        public String next() {
            return nextString;
        }

        private String nextHelper() {
            Pair pair = tnStack.pop();

            if( pair.tn.rsibling != null ) {
                Pair newPair = new Pair();
                newPair.tn = pair.tn.rsibling;
                newPair.s = pair.s;
                tnStack.push(newPair);
            }

            if( pair.tn.lmchild != null ) {
                Pair newPair = new Pair();
                newPair.tn = pair.tn.lmchild;
                newPair.s = pair.s.concat(pair.tn.l);
                tnStack.push(newPair);
            }

            if( pair.tn.wordEnd ) {
                return pair.s.concat(pair.tn.l);
            } else { // pair.tn.wordEnd has no children or siblings and is false
                return null;
            }
        }

        public boolean hasNext() {
            if( ! tnStack.empty() ) {

                nextString = nextHelper();

                while( nextString == null ) {
                    nextString = nextHelper();
                }

                return true;
            }

            return false;
        }
    }

    public Iterator iterator() {
        return new TRFIterator();
    }
}
