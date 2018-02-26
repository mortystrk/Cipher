package key;

import bstu.panasuk.Help;

import java.util.Random;

public class Key {

    private int a;
    private int b;
    private int reverseA;

    public Key() {
        a = 0;
        b = 0;
        reverseA = 0;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public int getReverseA() {
        return reverseA;
    }

    public void setReverseA(int reverseA) {
        this.reverseA = reverseA;
    }

    public void generate() {

        int m = 26;
        Random random = new Random();

        int gcd;

        do {
            a = random.nextInt(m);
            gcd = gcd(a, m);
        } while(gcd != 1);

        b = random.nextInt(m);
        reverseA = Help.help.get(a);
    }

    private static int gcd(int a, int b) {
        if (b == 0)
            return a;
        return gcd(b, a % b);
    }
}
