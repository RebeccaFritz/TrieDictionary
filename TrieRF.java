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
        root = addRecursion(s, root, 0, s.length());
    }

    private Tnode addRecursion(String s, Tnode tn, int lets, int sLen) {
        String l = s.substring(0, 1);
        
        boolean wordEnd = false;
        if( lets == sLen-1 ) { // last letter in the word
            wordEnd = true;
        }

        if( tn == null ) {
            Tnode node = new Tnode();
            node.l = l;
            node.rsibling = null;
            node.lmchild = null;
            node.wordEnd = wordEnd;

            if( ! wordEnd ) {
                tn.lmchild = addRecursion(s.substring(1), node.lmchild, ++lets, sLen);
            } else {
                return node;
            }
        }

        if( tn.l.equals(l) && ! wordEnd ) {
            tn.lmchild = addRecursion(s.substring(1), tn.lmchild, ++lets, sLen);
        } else if( tn.l.equals(l) && wordEnd ) {
            tn.wordEnd = wordEnd;
            len++;
        } else {
            tn.rsibling = addRecursion(s, tn.rsibling, lets, sLen);
        }

        return tn;
    }

    public boolean contains(String e) {
        if ( e.length() == 0 ) {
            return false;
        }

        return contains(e, root);
    }

    private boolean contains(String e, Tnode t) {
        String fl = e.substring(0,1);
        
        Tnode cur = t;

        while ( cur != null && ! cur.l.equals(fl) ) {
            cur = cur.rsibling;
        }

        if ( cur == null ) {
            return false;
        }

        if ( e.length() == 1 ) {
            return cur.wordEnd;
        }

        String rs = e.substring(1);
        return contains(rs, cur.lmchild);
    }

    public void remove(String s) { // just sets the wordEnd to false
        int sLen = s.length();
        Tnode cur = root;

        for(int i = 0; i < sLen; i++) {
            String curLet = s.substring(i, i+1);

            if( cur.l.equals(curLet) && i == sLen-1 ) { // if cur.l is curLet and curLet is the last letter mark the node as false
                cur.wordEnd = false;
                len--;
                return;
            } else if( cur.l.equals(curLet) ) { // if the letter is in the set and it is not the last letter go down
                cur = cur.lmchild;
            } else { // if the letter is not in the set, add it then set cur to it's child
                while( ! cur.l.equals(curLet) ) {
                    cur = cur.rsibling;
                    if(cur == null) { // if cur == null the word is not in the set and the method stops running
                        return;
                    }
                }
                cur = cur.lmchild;
            }
        }
    }

    public int length() {
        return len;
    }

    class TRFIterator implements Iterator<String> {
        Stack<Tnode> iterStack = new Stack<>();

        public TRFIterator() {
            if( root != null ) {
                iterStack.push(root);
            }
        }

        public String next() { 
            return next(iterStack.pop(), "");
        }

        private String next(Tnode tn, String s) {
            while(!tn.wordEnd) {

                if( tn.rsibling != null ) {
                    next(tn.rsibling, s);
                }

                if( tn.lmchild != null ) {
                    next(tn.lmchild, s.concat(tn.l));
                }
            }

            return s.concat(tn.l);
        }

        public boolean hasNext() {
            return ! iterStack.empty();
        }
    }

    public Iterator iterator() {
        return new TRFIterator();
    }
}
