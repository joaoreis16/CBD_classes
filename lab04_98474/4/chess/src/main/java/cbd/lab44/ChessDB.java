package cbd.lab44;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

import java.io.FileWriter;
import java.util.Map;

public class ChessDB {

    public static void removeAllData(Session session) {
        session.run("MATCH (n) DETACH DELETE n");
        System.out.println(">> All data deleted!");
    }

    public static void insertDataFromFile(Session session, String file) {

        String load_csv = "LOAD CSV WITH HEADERS FROM "+ file +" AS Row ";
        String data = "";

        // insert entities
        data += "MERGE (wp:White_Player {id: Row.white_id, rating: Row.white_rating}) ";
        data += "MERGE (bp:Black_Player {id: Row.black_id, rating: Row.black_rating}) ";
        data += "MERGE (o:Opening {id: Row.opening_code, num_moves: Row.opening_moves, fullname: Row.opening_fullname, shortname: Row.opening_shortname}) ";
        data += "MERGE (m:Moves {game_id: Row.game_id, moves: Row.moves} )";

        // insert relations
        data += "MERGE (wp) -[:PLAYWITH { winner: Row.winner, victory_status: Row.victory_status, turns: Row.turns, rated: Row.rated}]- (bp) ";
        data += "MERGE (wp) -[:STRATEGY]-> (o) ";
        data += "MERGE (bp) -[:STRATEGY]-> (o) ";
        data += "MERGE (o) -[:MOVES]-> (m) ";

        session.run(load_csv + data);
        System.out.println(">> All data inserted!");
    }

    public static void main( String... args ) throws Exception {

        String user = "neo4j"; 
        String password = "lab44"; 
        String address = "bolt://localhost:7687";
        String file_path = "'file:///chess_games.csv'";
        FileWriter out = new FileWriter("CBD_L44c_output.txt");

        Driver driver = GraphDatabase.driver( address, AuthTokens.basic( user, password ) );
        Session session = driver.session();
        System.out.println(">> Connected to Neo4j!");

        
        insertDataFromFile(session, file_path);

        // queries
        // 1
        out.write("1. Listar todos os jogadores que ganharam por 'Mate'.\n\n");

        String query =  "match (w:White_Player)-[r:PLAYWITH{victory_status:'Mate'}]-(b:Black_Player) "+
                        "return "+
                        "case r.winner "+
                            "when 'Black' then b "+
                            "when 'White' then w "+
                        "end "+
                        "as winner";

        Result result = session.run(query);

        for (Record r : result.list()) {
            String data = convert2String( r.get( "winner" ).asMap() ) +"\n";
            out.write(data);
        }

        // 2
        out.write("\n\n2. Listar todos os jogadores com as peças pretas que usaram a estratégia 'Sicilian Defense'.\n\n");

        query = "match (b:Black_Player)-[:STRATEGY]->(o {shortname:'Sicilian Defense'})"+
                "return b as Sicilian_Defense_players";

        result = session.run(query);

        for (Record r : result.list()) {
            String data = convert2String( r.get( "Sicilian_Defense_players" ).asMap() ) +"\n";
            out.write(data);
        }


        // 3
        out.write("\n\n3. Quantos jogos disputados tem os jogadores com as peças brancas com mais de 2000 de rating ordenado pelo número de jogo decrescentemente?\n\n");

        query = "match(w:White_Player)-[r:PLAYWITH]-() "+
                "where toInteger(w.rating) > 2000 "+
                "return w.id as name, count(w) as num_games "+
                "order by num_games desc ";

        result = session.run(query);

        for (Record r : result.list()) {
            String data = "{ 'name': '"+ r.get( "name" ).asString() +"', 'num_games': "+ r.get( "num_games" ).asInt() +" }\n";
            out.write(data);
        }


        // 4
        out.write("\n\n4. Qual é o top 10 maiores ratings de jogadores de xadrez, ou seja, os 10 maiores ratings alcançados por jogadores?\n\n");

        query = "match (w:White_Player)-[]-(b:Black_Player)"+
                "with toInteger(w.rating) as wrating, toInteger(b.rating) as brating, w, b "+
                "return distinct case wrating > brating " +
                    "when toBoolean('true') then w "+
                    "when toBoolean('false') then b "+
                "end as best_players "+
                "order by toInteger(best_players.rating) desc "+
                "limit 10";

        result = session.run(query);

        for (Record r : result.list()) {
            String data = convert2String( r.get( "best_players" ).asMap() ) +"\n";
            out.write(data);
        }


        // 5
        out.write("\n\n5. Quais são as várias maneiras para se usar a estratégia 'Italian Game' ?\n\n");

        query = "match (o:Opening{fullname:'Italian Game'})-[:MOVES]-(m:Moves) "+
                "return m.moves as moves";

        result = session.run(query);

        for (Record r : result.list()) {
            String data = "{ 'moves': '"+ r.get( "moves" ).asString() +"' }\n";
            out.write(data);
        }


        // 6
        out.write("\n\n6. O menor número de jogadas num jogo para dar 'Mate' e a respetiva estratégia. \n\n");

        query = "match (w:White_Player)-[r:PLAYWITH{victory_status:'Mate'}]-(b:Black_Player) "+
                "with r.turns as jogadas, w "+
                "match (w)-[:STRATEGY]->(o:Opening) "+
                "return distinct jogadas, o.fullname as strategy "+
                "order by toInteger(jogadas) "+
                "limit 1";

        result = session.run(query);

        for (Record r : result.list()) {
            String data = "{ 'jogadas': "+ r.get( "jogadas" ).asString() +", 'strategy': '"+ r.get( "strategy" ).asString()  +"' }\n";
            out.write(data);
        }


        // 7
        out.write("\n\n7. Número de jogos desistidos pelos jogador com id 'daniel_likes_chess'. \n\n");

        query = "match (w:White_Player{id:'daniel_likes_chess'})-[r:PLAYWITH{victory_status:'Resign'}]-(b:Black_Player)"+
                "return count(r) as games";

        result = session.run(query);

        for (Record r : result.list()) {
            String data = "{ 'games': "+ r.get( "games" ).asInt() +" }\n";
            out.write(data);
        }


        // 8
        out.write("\n\n8. Jogos com rating (rated) e que terminaram devido à falta de tempo de jogadores, com rating menor que 1800, com as peças brancas que aplicaram um estratégia relacionada com a 'Queen'. \n\n");

        query = "match(w:White_Player)-[r{victory_status:'Out of Time'}]-(b:Black_Player) "+
                "where toBoolean(r.rated) and ( toInteger(w.rating) < 1800 ) "+
                "match (w)-[]-(o:Opening) "+
                "where o.shortname starts with 'Queen' "+
                "return distinct w as white_player,r as game_status,b as black_player,o.fullname as strategy";

        result = session.run(query);

        for (Record r : result.list()) {
            String data = "{ "; 
            data += "'white_player': "+ convert2String( r.get( "white_player" ).asMap() )+", ";
            data += "'game_status': "+ convert2String( r.get( "game_status" ).asMap() ) +", ";
            data += "'black_player': "+ convert2String( r.get( "black_player" ).asMap() ) +", ";
            data += "'strategy': '"+ r.get( "strategy" ).asString()  +"' ";
            data +="}\n";
            out.write(data);
        }


        // 9
        out.write("\n\n9. Número de jogos que usaram o movimento 'Roque' (O-O) de jogadores de peças brancas com rating entre 1500 e 1800. \n\n");

        query = "match (w:White_Player)-[r]-(o:Opening) "+
                "where toInteger(w.rating) >= 1500 and toInteger(w.rating) <= 1800 "+
                "match (o)-[]-(m:Moves) "+
                "where m.moves contains 'O-O' "+
                "return count(m) as num_Roque";

        result = session.run(query);

        for (Record r : result.list()) {
            String data = "{ 'num_Roque': "+ r.get( "num_Roque" ).asInt() +" }\n";
            out.write(data);
        }


        // 10
        out.write("\n\n10. Jogador de peças pretas e o número de jogos desse jogador onde ganhou o jogo utilizando o seguinte movimento: Qg7. \n\n");

        query = "match (w:White_Player)-[r]-(b:Black_Player) "+
                "match (b)-[]-(o:Opening) "+
                "match (o)-[]-(m:Moves) "+
                "where m.moves contains 'Qg7' and r.winner = 'Black' "+
                "return b.id as player, count(m) as games "+
                "order by games desc";

        result = session.run(query);

        for (Record r : result.list()) {
            String data = "{ 'player': '"+ r.get( "player" ).asString() +"', 'games': "+ r.get( "games" ).asInt()  +" }\n";
            out.write(data);
        }

        System.out.println(">> All queries done!");

        removeAllData(session);

        out.close();
        driver.close();
        System.out.println(">> Disconnected from Neo4j!");
    }

    // função retirada da Internet (https://www.baeldung.com/java-map-to-string-conversion)
    public static String convert2String(Map<?, ?> map) {
        StringBuilder mapAsString = new StringBuilder("{");
        for (Object key : map.keySet()) {
            mapAsString.append('"'+ String.valueOf(key) + '"'+": " +'"'+ String.valueOf(map.get(key)) +'"'+ ", ");
        }
        mapAsString.delete(mapAsString.length()-2, mapAsString.length()).append("}");
        return mapAsString.toString();
    }
}
