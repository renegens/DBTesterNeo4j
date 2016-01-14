
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.io.fs.FileUtils;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Random;

public class NeoTester {



    public enum Products {Mouse, VGA, Monitor, Keyboard, CPU };
    public enum MyColors {Black, Red, Green, Blue, White};

    public static int Iterations = 2500000;
    private static final String DB_PATH = "/Users/renegens/Documents/Neo4j/default.graphdb";

    private static enum RelTypes implements RelationshipType
    {
        HAS, OWNS
    }

    static void createNeo4jDatabaseEntries() {

        String myString;
        GraphDatabaseService graphDb;
        Node myFirstNode;
        Node mySecondNode;
        Relationship myRelationship;

        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );

        Transaction tx = graphDb.beginTx();
        Node userNode = null;
        Node colorNode  = null;
        Node productNode  = null;


        long beforeQuery = System.currentTimeMillis();


        ArrayList<Node> colorArrayList = new ArrayList<>();
        for(MyColors mycolor:MyColors.values()){
            colorNode = graphDb.createNode();
            colorNode.setProperty("colorname",mycolor+"");
            Label myLabel = DynamicLabel.label("colors");
            colorNode.addLabel(myLabel);
            colorArrayList.add(colorNode);
        }

        ArrayList<Node> productArrayList = new ArrayList<>();
        for(Products product: Products.values()){
            productNode = graphDb.createNode();
            productNode.setProperty("productname",product+"");
            Label myLabel = DynamicLabel.label("products");
            productNode.addLabel(myLabel);
            for(Node theColorNode:colorArrayList){
                myRelationship = productNode.createRelationshipTo(theColorNode, RelTypes.HAS);
            }
            productArrayList.add(productNode);
        }

        for (int i = 0; i < Iterations; i++) {
            //write in batches of 100.000
            if((i%100000)==0 && i!=0){
                tx.success();
                tx.finish();
                graphDb.shutdown();
                graphDb = null;
                tx=null;
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //
                graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
                tx = graphDb.beginTx();
                System.out.println("Restarting db");
                System.out.println(i);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Db resuming");
            }
            userNode = graphDb.createNode();
            userNode.setProperty("username","User "+i);
            Label myLabel = DynamicLabel.label("users");
            userNode.addLabel(myLabel);


            int rand = randInt();
            int tempRand = randInt();
            while (rand == tempRand){
                rand = randInt();
            }
            Node one = productArrayList.get(rand);
            Node two = productArrayList.get(tempRand);
            myRelationship = userNode.createRelationshipTo( one, RelTypes.OWNS );
            myRelationship = userNode.createRelationshipTo( two, RelTypes.OWNS );

        }



        tx.success();

        tx.finish();

        graphDb.shutdown();
        System.out.println("graphDB shut down.");

        long afterQuery = System.currentTimeMillis();

        long totalTimetemp = afterQuery - beforeQuery;
        System.out.println("neo4j records in "+ totalTimetemp+" ms ("+totalTimetemp/1000+" seconds)");

//            myString = ( myFirstNode.getProperty( "name" ).toString() )
//                    + " " + ( myRelationship.getProperty( "relationship-type" ).toString() )
//                    + " " + ( mySecondNode.getProperty( "name" ).toString() );
//            System.out.println(myString);







    }


    public static void selectNeo4jEntries() {
        GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
        int resultCounter = 0;

        try (
                Transaction ignored = db.beginTx();
//              Result result = db.execute( "MATCH (User)-[r:OWNS]->(Mouse)-[o:HAS]->(Blue) return r limit 2" ) )
              //Result result = db.execute( "MATCH (user)-[:OWNS]-(products)-[:HAS]-(colors) WHERE  products.productname = \"VGA\" AND colors.colorname = \"Black\"\n RETURN user.username" ) )
              //Result result = db.execute( "MATCH (user)-[:OWNS]-(products)-[:HAS]-(colors) WHERE  products.productname = \"VGA\" AND colors.colorname = \"Black\"\n RETURN   count(*)" )
              Result result = db.execute( "MATCH (n) RETURN n LIMIT 100")
        ) {
            while (result.hasNext()){
                System.out.println(String.valueOf(result));
            }

            System.out.println(String.valueOf(resultCounter));
        }catch (Exception e){
            e.printStackTrace();
            db.shutdown();
        }

        db.shutdown();
    }

    public static int randInt() {

        // NOTE: This will (intentionally) not run as written so that folks
        // copy-pasting have to think about how to initialize their
        // Random instance.  Initialization of the Random instance is outside
        // the main scope of the question, but some decent options are to have
        // a field that is initialized once and then re-used as needed or to
        // use ThreadLocalRandom (if using at least Java 1.7).
        Random random = new Random();

        int enumMax = Products.values().length-1;
        int min = 0;
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = random.nextInt((enumMax - min) + 1) + min;

        return randomNum;
    }

}
