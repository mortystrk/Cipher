package bstu.panasuk;

import alphabet.Alphabet;
import key.Key;
import mode.CipherMode;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Main {

    static CipherMode mode;
    static String path = new File("").getAbsolutePath();
    static String keyFile = "key.txt";
    static String encryptFile = "encrypt.txt";
    static String decryptFile = "decrypt.txt";
    static String openText = "openText.txt";
    static Key key;
    static Alphabet alphabet;
    static Help help;


    public static void main(String[] args) {

        File keyF = new File(path + "\\" + keyFile);
        File encrypt = new File(path + "\\" + encryptFile);
        File decrypt = new File(path + "\\" + decryptFile);
        File openTextF = new File(path + "\\" + openText);

        help = new Help();
        key = new Key();
        alphabet = new Alphabet();

        checkFiles(keyF, encrypt, decrypt, openTextF);

        setMode();

        switch (mode) {

            case ENCRYPT:
                if (openTextF.length() == 0) {
                    System.out.println("Файл с открытым текстом пустой");
                    return;
                }
                if (keyF.length() == 0) {
                    key.generate();
                    try {
                        FileWriter fw = new FileWriter(keyF.getAbsoluteFile());
                        fw.write(key.getA() + "\r\n" + key.getB() + "\r\n" + key.getReverseA());
                        fw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        List<String> linesFromFile = Files.readAllLines(Paths.get(keyFile), StandardCharsets.UTF_8);
                        for (String line : linesFromFile) {
                            if (key.getA() == 0) {
                                key.setA(Integer.parseInt(line));
                            } else if (key.getB() == 0){
                                key.setB(Integer.parseInt(line));
                            } else {
                                key.setReverseA(Integer.parseInt(line));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    List<String> linesFromFile = Files.readAllLines(Paths.get(openText), StandardCharsets.UTF_8);
                    String textForEncrypt;
                    StringBuffer textFromFile = new StringBuffer();

                    for (String line : linesFromFile)
                        textFromFile.append(line);

                    textForEncrypt = textFromFile.toString();

                    String ciphertext = encrypt(textForEncrypt, key, alphabet);

                    FileWriter fw = new FileWriter(encrypt.getAbsoluteFile());
                    fw.write(ciphertext);
                    fw.close();

                } catch (IOException e) {
                    System.out.println("Error\n Не удалось прочитать файл с ключом");
                } finally {
                    break;
                }
            case DECRYPT:
                if (key.getA() == 0 || key.getB() == 0 || key.getReverseA() == 0) {
                    key.setA(0);
                    key.setB(0);
                    key.setReverseA(0);

                    try {
                        List<String> linesFromFile = Files.readAllLines(Paths.get(keyFile), StandardCharsets.UTF_8);

                        if (linesFromFile.isEmpty()) {
                            System.out.println("Ключ утерян");
                            return;
                        }

                        for (String line : linesFromFile) {
                            if (key.getA() == 0) {
                                key.setA(Integer.parseInt(line));
                            } else if (key.getB() == 0){
                                key.setB(Integer.parseInt(line));
                            } else {
                                key.setReverseA(Integer.parseInt(line));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                String decryptText = decrypt(encrypt, key, alphabet);

                if (decryptText == null) {
                    return;
                }

                try {
                    FileWriter fw = new FileWriter(decrypt.getAbsolutePath());
                    fw.write(decryptText);
                    fw.close();
                } catch (IOException e) {
                    System.out.println("Error. Decrypt - FileWriter");
                } finally {
                    break;
                }
                default:
                    break;
        }
    }

    public static void checkFiles(File ... files){

        for (File file : files) {
            if(!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    System.out.println("Не удалось открыть файл");
                }
            }
        }
    }

    public static void setMode() {

        Scanner in = new Scanner(System.in);
        System.out.println("Выберите мод:\n 1)Encrypt;\n 2)Decrypt;");

        String choice = in.nextLine();

        try {
            int convertingChoice = Integer.parseInt(choice);

            switch (convertingChoice) {

                case 1:
                    mode = CipherMode.ENCRYPT;
                    System.out.println("ENCRYPT");
                    break;
                case 2:
                    mode = CipherMode.DECRYPT;
                    System.out.println("DECRYPT");
                    break;
                    default:
                        System.out.println("Error");
                        setMode();
            }
        } catch (Exception e) {
            System.out.println("Error");
            setMode();
        }
    }

    public static String encrypt(String text, Key key, Alphabet alphabet) {

        int a = key.getA();
        int b = key.getB();
        int m = 26;
        int counter = 0;
        char[] startText = text.toCharArray();
        char[] ciphertext = new char[startText.length];

        for (char letter : startText) {
            int index = (a * alphabet.getCode(letter) + b) % m;
            ciphertext[counter] = alphabet.getLetter(index);
            counter++;
        }

        return new String(ciphertext);
    }

    public static String decrypt(File f, Key key, Alphabet alphabet) {

        int reverseA = key.getReverseA();
        int b = key.getB();
        int m = 26;
        int counter = 0;

        try {
            List<String> cipherLinesFromFile = Files.readAllLines(Paths.get(f.toURI()), StandardCharsets.UTF_8);
            StringBuffer ciphertextFromFile = new StringBuffer();

            for (String line : cipherLinesFromFile)
                ciphertextFromFile.append(line);

            char[] ciphertext;
            ciphertext = ciphertextFromFile.toString().toCharArray();

            char[] decryptText = new char[ciphertext.length];

            for (char letter : ciphertext) {
                int index = (reverseA * (alphabet.getCode(letter) - b));

                if (index < 0) {
                    do {
                        index += m;
                    } while (index < 0);
                }

                index %= m;

                decryptText[counter] = alphabet.getLetter(index);
                counter++;
            }

            return new String(decryptText);
        } catch (IOException e) {
            System.out.println("Невозможно прочитать файл");
            return null;
        }
    }
}
