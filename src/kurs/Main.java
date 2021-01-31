package kurs;

//А тут сегодня будем работать с соленым md5 и файлами.
//Нужно написать две функции.
//Первая принимает на вход строку, ""солит"" ее и хеширует в md5 (да-да, он устарел, но для тренировки
//ничем не плох). Так как md5 не поддается обратной расшифровке, нужно в файлике рядом в любом удобном
//вам формате запоминать изначальную строку, соль и хеш. Будет круто, если соль будет динамической и
//меняться по какой-то логике в зависимости от строки.
//Вторая функция должна получать на вход хеш и искать его в нашем файле. Если такое хеш уже был
//однажды сгенерен, функция должна возвращать соль и изначальное значение. Иначе кидать исключение.

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.Scanner;

public class Main {

    private static final String FILE_NAME = "file.txt";

    public static void main(String[] args) throws IOException {
        //putPassword("12345");
        //putPassword("67890");
        System.out.println(findInFile(FILE_NAME, "2d848cd2c21a6d0d91c6b389de82342a"));
        System.out.println(findInFile(FILE_NAME, "1f0feca00609f0ec84de5b5f94e85c1f"));
    }

    private static void putPassword(String password) {
        String salt = randomSalt(10);
        String md5 = null;
        try {
            md5 = md5(password + salt);
            appendToFile(FILE_NAME, password, salt, md5);
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
    }

    private static String randomSalt(int len) {
        final String letters = "abcdefghijklmnopqrstuvwxyz0123456789[]{}!@#$%^&:;";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(letters.charAt(random.nextInt(letters.length())));
        }
        return sb.toString();
    }

    private static String md5(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[]hashInBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private static void appendToFile(String fName, String password, String salt, String md5) throws IOException {
        FileWriter fw = new FileWriter(fName, true);
        fw.write(String.format("%s\t%s\t%s\n", password, salt, md5));
        fw.close();
    }

    private static String findInFile(String fName, String md5) throws IOException {
        String res = null;
        FileReader fr = new FileReader(fName);
        Scanner sc = new Scanner(fr);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] arr = line.split("\t");
            if (md5.equals(arr[2])) {
                res = arr[0] + " " + arr[1];
            }
        }
        fr.close();
        return res;
    }
}
