import java.util.Scanner;
import java.io.IOException;
import java.io.FileInputStream;

public class MainRF {

    public static void main(String[] args) {
        TrieRF words = new TrieRF();

        Scanner scan = new Scanner(System.in);

        boolean done = false;
        while ( ! done ) {
            // Get the command
            String cmdstr = scan.next();
            String cmd = cmdstr.substring(0,1).toLowerCase();
            String v;

            switch ( cmd ) {
                case "a":
                    v = scan.next();
                    words.add(v);
                    break;
                case "d":
                    v = scan.next();
                    words.remove(v);
                    break;
                case "c":
                    v = scan.next();
                    if ( words.contains(v) ) {
                        System.out.printf("%s is in the set.\n", v);
                    }
                    else {
                        System.out.printf("%s is not in the set.\n", v);
                    }
                    break;
                case "l":
                    System.out.println(words.length());
                    break;
                case "r":
                    String fn = scan.nextLine().trim();
                    Scanner wordFile;
                    try {
                        wordFile = new Scanner(new FileInputStream(fn));
                    } catch (IOException e) {
                        System.err.printf("Can't open file '%s': %s\n", fn, e.toString());
                        break;
                    }

                    // Successfully opened word file
                    System.out.printf("Reading %s", fn);
                    int readWords = 0;
                    while (wordFile.hasNext()) {
                        words.add(wordFile.next());
                        if ( ++readWords % 1000 == 0 ) {
                            System.out.printf(".");
                        }
                    }
                    System.out.println(".\n");
                    wordFile.close();
                    break;
                case "q":
                    done = true;
                    break;
                case "p":
                    for ( String s : words ) {
                        System.out.println(s);
                    }
                    break;
                default:
                    System.err.printf("Unknown command '%s'.\n", cmdstr);
                    break;
            }
            
        }
        
    }

}
