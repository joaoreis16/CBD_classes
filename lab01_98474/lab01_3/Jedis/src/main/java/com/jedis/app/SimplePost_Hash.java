package com.jedis.app;

import java.util.List;
import java.util.Set;
import redis.clients.jedis.Jedis;

public class SimplePost_Hash {
    private Jedis jedis;

    public static String USERS = "users2"; // Key set for users' name
    static int i = 0;

    public SimplePost_Hash() {
        this.jedis = new Jedis("localhost");
    }

    public void saveUser(String username) {
        String user = "user" + String.valueOf(i);
        jedis.hset(USERS, user , username);
        System.out.println("User "+ username +" saved!");
        i++;
    }

    public List<String> getUser() {
        return jedis.hvals(USERS);
    }

    public Set<String> getAllKeys() {
        return jedis.keys("*");
    }
    
    public static void main(String[] args) {
        SimplePost_Hash board = new SimplePost_Hash();
        // set some users
        String[] users = { "Ana", "Pedro", "Maria", "Luis" };
        
        for (String user: users)
            board.saveUser(user);

        System.out.println("imprime todas as chaves(keys):");
        board.getAllKeys().stream().forEach(System.out::println);

        System.out.println("imprime a lista associada à chave indicada:");
        board.getUser().stream().forEach(System.out::println);
    }
}
