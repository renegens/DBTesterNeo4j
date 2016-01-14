import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.util.Scanner;


public class DBTester {


    public static int DefaultSelectLimiter = 0;


        public static void main( final String[] args )
        {




            int i = -1;
            while (i!=0){
                System.out.println("--SQL TESTER----");
                System.out.println("1 to create database entries");
                System.out.println("2 to select");
                System.out.println("--NEO4J TESTER--");
                System.out.println("3 insert entries to neo4j");
                System.out.println("4 select entries from neo4j");
                System.out.println("0 to exit");
                System.out.println("================");


                Scanner scanner = new Scanner(System.in);
                i = scanner.nextInt();

                switch (i){
                    case 0: System.out.println("bye");
                        break;
                    case 1: SqlTester.createDatabaseEntries();
                        break;
                    case 2:
                        System.out.println("Input select limit (0 for no limit): ");
                        DefaultSelectLimiter = scanner.nextInt();
                        SqlTester.selectDatabaseEntries(DefaultSelectLimiter);
                        break;
                    case 3:
                        //myNeoInstance.createDb();
                        //myNeoInstance.removeData();
                        NeoTester.createNeo4jDatabaseEntries();
                        break;
                    case 4:
                        NeoTester.selectNeo4jEntries();
                    default: System.out.println("Break");
                        break;

                }

            }

        }

//        void createDb()
//        {
//            graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
//
//            Transaction tx = graphDb.beginTx();
//            try
//            {
//                for (int i = 0; i < 10; i++) {
//
//                }
//                myFirstNodei = graphDb.createNode();
//                myFirstNode.setProperty( "name", "Duane Nickull, I Braineater" );
//                mySecondNode = graphDb.createNode();
//                mySecondNode.setProperty( "name", "Randy Rampage, Annihilator" );
//
//                myRelationship = myFirstNode.createRelationshipTo( mySecondNode, RelTypes.HAS );
//                myRelationship = myFirstNode.createRelationshipTo( mySecondNode, RelTypes.OWNS );
//                myRelationship.setProperty( "relationship-type", "knows" );
//
//                myString = ( myFirstNode.getProperty( "name" ).toString() )
//                        + " " + ( myRelationship.getProperty( "relationship-type" ).toString() )
//                        + " " + ( mySecondNode.getProperty( "name" ).toString() );
//                System.out.println(myString);
//
//                tx.success();
//            }
//            finally
//            {
//                tx.finish();
//            }
//        }

//        void removeData()
//        {
//            Transaction tx = graphDb.beginTx();
//            try
//            {
//                myFirstNode.getSingleRelationship( RelTypes.KNOWS, Direction.OUTGOING ).delete();
//                System.out.println("Removing nodes...");
//                myFirstNode.delete();
//                mySecondNode.delete();
//                tx.success();
//            }
//            finally
//            {
//                tx.finish();
//            }
//        }

        void shutDown()
        {

        }
    }


