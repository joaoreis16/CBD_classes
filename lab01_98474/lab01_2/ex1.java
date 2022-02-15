package lab01.lab01_2;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Scanner;

public class ex1 {
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {

        File f = new File("lab01/names.txt");
        Scanner sc = new Scanner(f);

        HashMap<Character, Integer> map = new HashMap<Character, Integer>();

        while (sc.hasNextLine()){
            String name = sc.nextLine();
            char initial_char = Character.toUpperCase( name.charAt(0) );

            if (map.containsKey(initial_char)) {
                int count = map.get(initial_char); 
                map.put(initial_char, count+1);

            } else {
                map.put(initial_char, 1);
            }
        }

        sc.close();

        PrintWriter writer = new PrintWriter("lab01/names_counting.txt", "UTF-8");

        for (HashMap.Entry<Character, Integer> entry : map.entrySet()) {
            String redis_code = String.format("SET %s %d", entry.getKey(), entry.getValue());
            writer.println(redis_code);
        }

        writer.close();
    }
}