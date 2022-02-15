package com.autocomplete;

import redis.clients.jedis.Jedis;
import java.util.Map.Entry;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class autocomplete_pt {

    private static Jedis jedis;
    private static String key;

    public static void startRedis() {
        jedis = new Jedis();
        jedis.flushDB();
        key = "namesPT";
    }

    public static void saveFromFile() throws FileNotFoundException {
        File f = new File("nomes-pt-2021.csv");
        Scanner sc = new Scanner(f);

        HashMap<String, String> map = new HashMap<>();

        while (sc.hasNext()){
            String name = sc.nextLine();

            String nome = name.split(";")[0];
            // int registos = Integer.parseInt(name.split(";")[1]);
            String registos = name.split(";")[1];

            map.put(nome, registos);            
        }

        jedis.hmset(key, map);

        sc.close();
    }

    public static void startInterface() {
        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.print("Search for ('Enter' for quit): ");
            String input = sc.nextLine().toLowerCase();

            if (input.equals("")) {
                sc.close();
                System.exit(0);
            }
            Map<String, String> names = getAllNames();
            names = sortByValue(names);

            for (Entry<String, String> entry : names.entrySet()) {
                String nome = entry.getKey().toLowerCase();
                String registo = entry.getValue();
                
                if (nome.startsWith(input)) System.out.println(String.format("%20s | %s", nome, registo));
            }
        }
    }

    public static Map<String, String> getAllNames() {
        return jedis.hgetAll(key);
    }

    private static Map<String, String> sortByValue(Map<String, String> map)   {  
        
        List<Entry<String, String>> list = new LinkedList<Map.Entry<String, String>>(map.entrySet());  
         
        Collections.sort(list, new Comparator<Map.Entry<String, String>>() {  

            @Override
            public int compare(Entry<String, String> arg0, Entry<String, String> arg1) {
                Integer i1 = Integer.parseInt(arg0.getValue());
                Integer i2 = Integer.parseInt(arg1.getValue());

                if (i1 < i2)
                    return 1;

                else if (i1 > i2)
                    return -1;

                return 0;
            }   
            });  
    
        Map<String, String> sortedMap = new LinkedHashMap<String, String>();  
        for (Map.Entry<String, String> entry : list)   
        {  
            sortedMap.put(entry.getKey(), String.valueOf(entry.getValue()));  
        }  

        return sortedMap;
    }  

    public static void main( String[] args ) throws FileNotFoundException
    {
        startRedis();
        saveFromFile();
        startInterface();
    }
}
    