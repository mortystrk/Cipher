package alphabet;

import java.util.ArrayList;

public class Alphabet {

    private ArrayList<Character> letters;

    public Alphabet() {
        letters = new ArrayList<>();
        fillAlphabet();
    }

    public char getLetter(int index) {
        return letters.get(index);
    }

    public int getCode(char letter) {
        return letters.indexOf(letter);
    }

    private void fillAlphabet() {
        letters.add('a');
        letters.add('b');
        letters.add('c');
        letters.add('d');
        letters.add('e');
        letters.add('f');
        letters.add('g');
        letters.add('h');
        letters.add('i');
        letters.add('j');
        letters.add('k');
        letters.add('l');
        letters.add('m');
        letters.add('n');
        letters.add('o');
        letters.add('p');
        letters.add('q');
        letters.add('r');
        letters.add('s');
        letters.add('t');
        letters.add('u');
        letters.add('v');
        letters.add('w');
        letters.add('x');
        letters.add('y');
        letters.add('z');
    }
}
