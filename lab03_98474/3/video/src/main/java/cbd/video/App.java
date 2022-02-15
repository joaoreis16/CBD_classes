package cbd.video;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

public class App 
{
    public static void main( String[] args )
    {
        Cluster cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
        Session session = cluster.connect();
        System.out.println(">> Connected to Cassandra (127.0.0.1)");
        String query;

        // Exemplos para alinea a)
        System.out.println("\n\nAlínea a)");
        
        query = insert("users");
        session.execute(query);
        System.out.println(">> Data inserted");

        query = search("users", "joaoreis");
        ResultSet result = session.execute(query);
        System.out.println(result.all());

        query = update("users");
        session.execute(query);
        System.out.println(">> Data updated");

        query = search("users", "joaoreis");
        result = session.execute(query);
        System.out.println(result.all());


        // alinea b)
        System.out.println("\n\nAlínea b)");

        System.out.println("\n4. Os últimos 5 eventos de determinado vídeo realizados por um utilizador");
        query = search("eventos", "vasgo", "3");
        query = query.replace(";", " limit 5;");
        result = session.execute(query);
        System.out.println( result.all() );


        System.out.println("\n1. Os últimos 3 comentários introduzidos para um vídeo");
        query = search("comentario_video", "2");
        query = query.replace(";", " limit 3;");
        result = session.execute(query);
        System.out.println( result.all() );


        System.out.println("\n7. Todos os seguidores (followers) de determinado vídeo");
        query = search("followers", "9");
        result = session.execute(query);
        System.out.println( result.all() );


        System.out.println("\n2. Lista das tags de determinado vídeo");
        query = search("video", "5");
        result = session.execute(query);
        System.out.println( result.all() );


        session.close();
        cluster.close();
        System.out.println("\n\n>> Disconnected");
    }

    public static String insert( String table ) {

        String query = "";

        switch(table) {
            case "users":
                query = "insert into videos.users( username , email , nome , selo) values ('porto92', 'porto@porto.pt', 'Pinto da Costa',  toTimestamp(now()));";
                break;

            case "video":
                query = "insert into videos.video( id , autor , descricao , nome , selo , tags) values (11, 'Pinto da Costa', 'Golaço aos 92 de Kelvin', 'Oh chora JJ',  toTimestamp(now()), {'football', 'dramatic'} );\n"+
                        "insert into videos.video_by_autor( id , autor , descricao , nome , selo , tags) values (11, 'Pinto da Costa', 'Golaço aos 92 de Kelvin', 'Oh chora JJ',  toTimestamp(now()), {'football', 'dramatic'} );";
                break;

            case "comentarios":
                query = "insert into videos.comentarios( id , autor , comentario , id_video , selo ) values (11, 'Pinto da Costa', 'Não sei como vim aqui parar', 5, toTimestamp(now()) );\n"+
                        "insert into videos.comentario_user( autor , comentario , id_video , selo ) values ( 'Pinto da Costa', 'Não sei como vim aqui parar', 5, toTimestamp(now()) );\n"+
                        "insert into videos.comentario_video( autor , comentario , id_video , selo ) values ( 'Pinto da Costa', 'Não sei como vim aqui parar', 5, toTimestamp(now()) );";
                break;

            case "followers":
                query = "insert into videos.followers( video_id , users ) values (11, {'joaoreis', 'bernoso'} );";
                break;

            case "eventos":
                query = "insert into videos.eventos( video_id , autor , data , tempo , tipo ) values (11, 'porto92', toTimestamp(now()), toTimestamp(now()), 'pause');";
                break;

            case "rating":
                query = "insert into videos.rating( video_id, rate ) values (11, 5);";
                break;

            default:
                System.out.println(">> The table "+ table +" doesn't exist!");
        }

        return query;
    }

    public static String update( String table ) {
        String query = "";

        switch(table) {
            case "users":
                query = "update videos.users set email='reis@ovar.pt' where username = 'joaoreis';";
                break;

            case "video":
                query = "update videos.video set autor='Tuna da Universidade de Aveiro (TUA)' where id = 1;\n"+
                        "update videos.video_by_autor set autor='Tuna da Universidade de Aveiro (TUA)' where id = 1;";
                break;

            case "comentarios":
                query = "update videos.comentarios set autor='artureiro', comentario='não vou mais' where id = 3;\n"+
                        "update videos.comentario_user set autor='artureiro', comentario='não vou mais' where id = 3;\n"+
                        "update videos.comentario_video set autor='artureiro', comentario='não vou mais' where id = 3;";
                break;

            case "followers":
                query = "update videos.followers set users += {joaoreis} where video_id=6;";
                break;

            case "eventos":
                query = "update videos.eventos set tipo='play' where video_id=9;";
                break;

            default:
                System.out.println(">> The table "+ table +" doesn't exist!");
        }

        return query;
    }

    public static String search( String table, String param) {
        String query = "";

        switch(table) {
            case "users":
                query = "select * from videos.users where username='"+ param +"';";
                break;

            case "video":
                query = "select * from videos.video where id="+ param +";";
                break;

            case "video_by_autor":
                query = "select * from videos.video_by_autor where autor='"+ param +"';";
                break;

            case "comentarios":
                query = "select * from videos.comentarios where id="+ param +";";
                break;

            case "comentario_user":
                query = "select * from videos.comentario_user where autor='"+ param +"';";
                break;

            case "comentario_video":
                query = "select * from videos.comentario_video where id_video="+ param +";";
                break;

            case "followers":
                query = "select * from videos.followers where video_id="+ param +";";
                break;

            case "rating":
                query = "select video_id, avg(rate) as avg_rating, count(rate) as num_votes FROM videos.rating WHERE video_id="+ param +";";
                break;

            default:
                System.out.println(">> The table "+ table +" doesn't exist!");
        }

        return query;
    }

    public static String search( String table, String param1, String param2) {
        String query = "";

        if (table.equals("eventos")) {
            query = "select * from videos.eventos where autor='"+ param1+"' and video_id="+ param2 +";";
        }

        return query;
    }

}
