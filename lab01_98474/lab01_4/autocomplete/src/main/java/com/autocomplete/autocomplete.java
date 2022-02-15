package com.autocomplete;
import redis.clients.jedis.Jedis;
import java.util.Set;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class autocomplete {

    private static Jedis jedis;
    private static String key;

    public static void startRedis() {
        jedis = new Jedis();
        jedis.flushDB();
        key = "names";
    }

    public static void saveFromFile() throws FileNotFoundException {
        File f = new File("names.txt");
        Scanner sc = new Scanner(f);

        while (sc.hasNext()){
            String name = sc.nextLine();

            // utilizar um SORTED SET para ordenar os values
            jedis.zadd(key, 0, name);
        }

        sc.close();
    }

    public static void startInterface() {
        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.print("Search for ('Enter' for quit): ");
            String input = sc.nextLine();

            if (input.equals("")) {
                sc.close();
                System.exit(0);
            }
            Set<String> names = getAllNames();
            for (String name : names) {
                if (name.startsWith(input)) System.out.println(name);
            }
        }
    }

    public static Set<String> getAllNames() {
        return jedis.zrange(key, 0, -1);
    }

    public static void main( String[] args ) throws FileNotFoundException
    {
        startRedis();
        saveFromFile();
        startInterface();
    }
}
    