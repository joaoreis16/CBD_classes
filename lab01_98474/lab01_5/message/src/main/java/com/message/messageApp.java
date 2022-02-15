package com.message;

import java.util.Scanner;
import redis.clients.jedis.Jedis;

public class messageApp 
{

    public static boolean control = true;
    private static Jedis jedis;
    private static String loggedUser = "";

    // keys usadas
    private static String user_key = "users";                      // "users" : [set dos users]
    private static String followers_key = "seguidores:";           // "seguidores:<username>" : [set de seguidores]
    private static String message_key = "mensagens:";              // "mensagens:<username>" : [lista de mensagens]
    
    public static void main( String[] args )
    {
        startRedis();
        Scanner sc = new Scanner(System.in);
        
        System.out.println("----- Welcome -----");
        System.out.println("Login (insert your username): ");
        loggedUser = sc.nextLine();
        jedis.sadd(user_key, loggedUser);

        followers_key = followers_key + loggedUser;
        message_key = message_key + loggedUser;

        while (control) {
            control = menu(sc);
        }

        sc.close();
        System.exit(0);
    }

    public static boolean menu(Scanner sc) {

        String msg = "\nAccount: " + loggedUser + " | Following: "+ jedis.scard(followers_key) +"\n" +
                     "1 - Create a new user\n" +
                     "2 - Follow an user\n" +
                     "3 - Send message\n" +
                     "4 - Read messages\n" +
                     "5 - Unfollow an user\n" +
                     "6 - Following\n" +
                     "7 - Logout\n" +
                     ">> ";

        System.out.print(msg);

        try {
            int input = sc.nextInt();

            switch(input) {
                case 1:
                    opcao1();
                    break;

                case 2:
                    opcao2();
                    break;
                    
                case 3:
                    opcao3();
                    break;
                
                case 4:
                    opcao4();
                    break;

                case 5:
                    opcao5();
                    break;

                case 6:
                    opcao6();
                    break;

                case 7:
                    return false;
                
                default:
                    System.out.println("[ERROR]: Invalid option");
            }

            return true;
        }
        catch (Exception e) {
            System.out.println("[ERROR]: Only numbers allowed!");
            return false;
        }
    }

    public static void startRedis() {
        jedis = new Jedis();
    }

    public static void opcao1() {

        System.out.println("-------- Users --------");
        for (String u : jedis.smembers(user_key))
            System.out.println(u);

        if (jedis.smembers(user_key).isEmpty()) System.out.println("[INFO]: List of users is empty");
        System.out.println("-----------------------");

        Scanner scan = new Scanner(System.in);
        System.out.print("Username: ");
        String username = scan.nextLine();

        if (jedis.smembers(user_key).contains(username)) {
            System.out.println("[ERROR]: this user already exits :(");

        } else {
            jedis.sadd(user_key, username);
            System.out.println("[INFO]: User '"+ username +"' has been saved successfully!");

        }            
    }

    public static void opcao2() {
        // a estrutura usada é um Set
        Scanner scan = new Scanner(System.in);

        System.out.print("User to follow: ");
        String username = scan.nextLine();

        if (jedis.smembers(user_key).contains(username)) {
            jedis.sadd(followers_key, username);

        } else { 
            System.out.println("[ERROR]: this user doesn't exist :(");
        }
    }

    public static void opcao3() {

        Scanner scan = new Scanner(System.in);

        System.out.print("Write here: ");
        String message = scan.nextLine();

        jedis.lpush(message_key, message);
        System.out.println("[INFO]: messsage published successfully!");

    }

    public static void opcao4() {

        for (String user : jedis.smembers(followers_key))
            for (String message : jedis.lrange("mensagens:"+ user, 0, -1))
                System.out.println("["+ user +"]: "+ message);

    }

    public static void opcao5() {

        Scanner scan = new Scanner(System.in);

        System.out.print("User to unfollow: ");
        String username = scan.nextLine();

        if (jedis.smembers(user_key).contains(username)) {
            jedis.srem(followers_key, username);

        } else { 
            System.out.println("[ERROR]: this user doesn't exist :(");
        }
    }

    public static void opcao6() {
        for (String u : jedis.smembers(followers_key))
            System.out.println(u);
    
        if (jedis.smembers(user_key).isEmpty()) System.out.println("[INFO]: List is empty");
    }
}
