package com.jedis.app;

import java.util.*;
import redis.clients.jedis.Jedis; 
  

class SimplePostHash{
  private Jedis jedis;

  public static String USERS = "users3"; // Key set for users' name
  
  public SimplePostHash() {
      this.jedis = new Jedis("localhost");
  }

  public void saveUser(Map<String,String> username) {
      jedis.hmset(USERS, username);
  }

  public Map<String, String> getUser(){
      return jedis.hgetAll(USERS);
  }

  public Set<String> getAllKeys() {
      return jedis.hkeys(USERS); 
  }
}

public class another_version_Hash { 
  
  private Jedis jedis; 
 
  public static String USERS = "users"; // Key set for users' name 
   
  public another_version_Hash() { 
    this.jedis = new Jedis("localhost"); 
  } 
  
  public void saveUserlist(String username) { 
    jedis.lpush(USERS, username); 
  } 
  public List<String> getUserlist() { 
    return jedis.lrange(USERS,0,-1); 
  } 
   
  public ArrayList<String> getAllKeyslist() { 
     long size=jedis.llen(USERS);
     ArrayList<String> users =new ArrayList<>();

     for(long i=0;i<size;i++){
          users.add(jedis.rpop(USERS));
     }

     return users; 
  } 

  public static void main(String[] args) { 
    another_version_Hash board = new another_version_Hash(); 
    // set some users 

    String[] users = { "Ana", "Pedro", "Maria", "Luis" }; 
    for (String user: users)  
      board.saveUserlist(user); 

    ArrayList <String> k = board.getAllKeyslist();
    List <String> sl = board.getUserlist();
  
    for (String c : k) {
      System.out.println(c);
    }
  
    System.out.println("\n");

    System.out.println(sl); 
    
    SimplePostHash board2 = new SimplePostHash();
    HashMap <String,String> name = new HashMap<>();

    name.put("USER1", "Maria");
    name.put("USER2", "Mariana");
    name.put("USER3", "Ana");
    name.put("USER4", "Carolina");

    board2.saveUser(name);

    Set <String> s = board2.getAllKeys();
    Map <String,String> s1 = board2.getUser();

    for (String c : s) {
        System.out.println(c);
    }

    System.out.println("\n");

    System.out.println(s1.values());
  
  
  }
}