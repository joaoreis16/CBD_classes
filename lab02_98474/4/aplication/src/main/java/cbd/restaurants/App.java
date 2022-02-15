package cbd.restaurants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.operation.OrderBy;
import static com.mongodb.client.model.Aggregates.*;

import org.bson.Document;
import org.bson.conversions.Bson;

public class App {
    public static void main( String[] args ) throws FileNotFoundException {
        MongoCollection<Document> collection = MongoClients.create().getDatabase("cbd").getCollection("restaurants");

        Document doc = new Document("address", new Document("building", "9322").append("coord", Arrays.asList(-77.84938439999999, 42.688197 ))
                                                                                .append("rua", "University Street")
                                                                                .append("zipcode", "3810"))
                            .append("localidade", "Aveiro")
                            .append("gastronomia", "Portuguese")
                            .append("grades", Arrays.asList(new Document("date", "2013-12-27T00:00:00Z").append("grade", "A").append("score", 10) ))
                            .append("nome", "AFUAV")
                            .append("restaurant_id", "42358514");


        System.out.println("----------------- ALINEA A -----------------");
        inserir(collection, doc);
        editar(collection, "nome", "Doutores Engenheiros", "Ramona");
        pesquisar(collection, new Document("localidade","Aveiro"));

        System.out.println("\n----------------- ALINEA B -----------------");

        System.out.println("Para localidade...");
        long startTime = System.nanoTime();
        pesquisar(collection, new Document("localidade","Manhattan"));
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("Tempo (sem indice): "+ totalTime);

        criarIndice(collection, "localidade");

        startTime = System.nanoTime();
        pesquisar(collection, new Document("localidade","Manhattan"));
        endTime   = System.nanoTime();
        totalTime = endTime - startTime;
        System.out.println("Tempo (com indice): "+ totalTime);

        System.out.println("\nPara gastronomia...");
        startTime = System.nanoTime();
        pesquisar(collection, new Document("gastronomia","Portuguese"));
        endTime   = System.nanoTime();
        totalTime = endTime - startTime;
        System.out.println("Tempo (sem indice): "+ totalTime);

        criarIndice(collection, "gastronomia");

        startTime = System.nanoTime();
        pesquisar(collection, new Document("gastronomia","Portuguese"));
        endTime   = System.nanoTime();
        totalTime = endTime - startTime;
        System.out.println("Tempo (com indice): "+ totalTime);


        System.out.println("\nPara nome...");
        startTime = System.nanoTime();
        pesquisar(collection, new Document("nome","restaurant"));
        endTime   = System.nanoTime();
        totalTime = endTime - startTime;
        System.out.println("Tempo (sem indice): "+ totalTime);

        criarIndiceTexto(collection, "nome");

        startTime = System.nanoTime();
        pesquisar(collection, new Document("nome","restaurant"));
        endTime   = System.nanoTime();
        totalTime = endTime - startTime;
        System.out.println("Tempo (com indice): "+ totalTime);


        System.out.println("\n----------------- ALINEA C -----------------");

        System.out.println("\n1. Liste todos os documentos da coleção.");
        FindIterable cursor = collection.find();
        for (Object o : cursor) {
            System.out.println(o);
        }

        System.out.println("4. Indique o total de restaurantes localizados no Bronx.");
        FindIterable docs = collection.find( new Document("localidade", "Bronx") );
        int count = 0;
        for (Object d : docs) count += 1;
        System.out.println(">> "+ count);

        System.out.println("\n5. Apresente os primeiros 15 restaurantes localizados no Bronx, ordenados por ordem crescente de nome");
        Document query = new Document("localidade", "Bronx");
        cursor = collection.find(query).sort(new BasicDBObject("nome", OrderBy.ASC.getIntRepresentation())).limit(15);
        for (Object o : cursor) {
            System.out.println(o);
        }        

        System.out.println("\n10. Liste o restaurant_id, o nome, a localidade e gastronomia dos restaurantes cujo nome começam por 'Wil'.");
        BasicDBObject fields = new BasicDBObject();
        fields.put("restaurant_id", 1); fields.put("nome", 1); fields.put("localidade", 1); fields.put("gastronomia", 1); fields.put("_id", 0); 

        BasicDBObject regexQuery = new BasicDBObject();
        regexQuery.put("nome", new BasicDBObject("$regex", "Wil"));

        cursor = collection.find(regexQuery);
        for (Object o : cursor) {
            System.out.println(o);
        }

        System.out.println("\n19. Conte o total de restaurante existentes em cada localidade.");

        AggregateIterable cursor1 = collection.aggregate( Arrays.asList(new Document("$group", new Document().append("_id", "$localidade").append("numRes",new Document("$sum",1)))) );
        for (Object o : cursor1) {
            System.out.println(o);
        }


        System.out.println("\n----------------- ALINEA D -----------------");

        PrintWriter wr = new PrintWriter(new File("CBD_L204_98474.txt"));  

        int nLocalidades = countLocalidades(collection);
        wr.println("Número de localidades distintas: "+ nLocalidades);

        Map<String, Integer> map =  countRestByLocalidade(collection);
        wr.println("\nNúmero de restaurantes por localidade:");
        Set<String> keys = map.keySet();
        for (String k: keys) {
            wr.println(" -> "+ k +" - "+ map.get(k));
        }

        List<String> lst = getRestWithNameCloserTo(collection, "Park");
        wr.println("\nNome de restaurantes contendo 'Park' no nome:");
        for (String res: lst) {
            wr.println(" -> "+ res );
        }

        wr.close();
    }
    

    // Funções alinea a)
    public static void inserir(MongoCollection col, Document doc) {
        col.insertOne(doc);
    }

    public static void editar(MongoCollection collection, String param, String old, String novo) {
        collection.updateOne(Filters.eq(param, old),  new Document("$set", new Document(param, novo)));
    }

    public static void pesquisar(MongoCollection col, Document doc) {
        col.find(doc);
    }

    // Funções alinea b)
    public static void criarIndice(MongoCollection col, String str) {
        col.createIndex(Indexes.ascending(str));
        System.out.println(">> Indice para "+ str +" criado!");
    }

    public static void criarIndiceTexto(MongoCollection col, String str) {
        col.createIndex(Indexes.text(str));
        System.out.println(">> Indice de texto para "+ str +" criado!");
    }

    // Funções alinea d)

    public static int countLocalidades(MongoCollection col) {
        List<Document> lista = new ArrayList<>();

        Document group = new Document( "$group",new Document("_id", "$localidade"));
        lista.add(group);

        AggregateIterable cursor = col.aggregate(lista);

        int count = 0;
        for (Object o : cursor) {
            count +=1;
        }

        return count;
    }

    public static Map<String, Integer> countRestByLocalidade(MongoCollection col) {
        List<Document> lista = new ArrayList<>();

        Document group = new Document("$group", new Document()
                                    .append("_id", "$localidade")
                                    .append("numeroRestaurantes",new Document("$sum",1)));
        lista.add(group);

        AggregateIterable cursor = col.aggregate(lista);

        Map<String, Integer> count = new HashMap<>();

        for (Object o : cursor) {
            Document doc = (Document) o;
            count.put((String)doc.get("_id"), (int)doc.get("numeroRestaurantes"));
        }

        return count;
    }

    public static List<String> getRestWithNameCloserTo(MongoCollection col, String name) {
        List<String> lst = new ArrayList<>();
        Document newdoc = new Document("$text", new Document("$search", name ));

        FindIterable cursor = col.find(newdoc);
        for (Object o : cursor) {
            Document doc = (Document) o;
            lst.add((String)doc.get("nome"));
        }

        return lst;
    }

}

