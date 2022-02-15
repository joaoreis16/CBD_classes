package com.jedis.app;

import java.util.Set;
import redis.clients.jedis.Jedis;

public class SimplePost_Set {

    private Jedis jedis;
    public static String USERS = "users3"; // Key set for users' name

    public SimplePost_Set() {
        this.jedis = new Jedis("localhost");
    }

    public void saveUser(String username) {
        jedis.sadd(USERS, username);
        System.out.println("User "+ username +" saved!");
    }

    public Set<String> getUser() {
        return jedis.smembers(USERS);
    }

    public Set<String> getAllKeys() {
        return jedis.keys("*");
    }
    public static void main(String[] args) {
        SimplePost_Set board = new SimplePost_Set();
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