import java.sql.*;
import java.time.ZonedDateTime;
import java.util.Random;

public class SqlTester {

    public enum Products {Mouse, VGA, Monitor, Keyboard, CPU };
    public enum MyColors {Black, Red, Green, Blue, White};

    public static int Iterations = 2500000;

    static void createDatabaseEntries() {


        String url = "jdbc:mysql://localhost:3306/sqltester";
        String username = "root";
        String password = "cangetin";

        System.out.println("Connecting database...");
        long beforeQuery = System.currentTimeMillis();

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Database connected!");

            Statement statment = connection.createStatement();
            System.out.println("Writing Query...");

            for (MyColors color : MyColors.values()){
                statment.execute("INSERT INTO mydb.colors (colorname) VALUES ('"+color+"')" );


            }


            int productIterator = 1;
            int colorIterator = 1;
            for (Products product : Products.values()){
                statment.execute("INSERT INTO mydb.products (productname) VALUES ('"+product+"')" );

                for (MyColors color : MyColors.values()){
                    statment.execute("INSERT INTO `mydb`.`products_has_colors` (`products_idproducts`, `colors_idcolors`) VALUES ('"+productIterator+"', '"+colorIterator+"')");
                    colorIterator++;
                }
                productIterator++;
                colorIterator=1;
            }


            for (int i = 1; i < Iterations; i++) {
                if(i%10000==0 && i!=0){
                    long afterQuery = System.currentTimeMillis();
                    long totalTimetemp = afterQuery - beforeQuery;
                    System.out.println(i +" of " + Iterations +" records in "+ totalTimetemp+" ms ("+totalTimetemp/1000+" seconds)");
                }

                statment.execute("INSERT INTO mydb.users (fname) VALUES ('user "+i+"')");


                    //String products = String.valueOf(Products.values()[randInt()]);

                int rand = randInt();
                int tempRand = randInt();
                while (rand == tempRand){
                    rand = randInt();
                }
                statment.execute("INSERT INTO `mydb`.`users_has_products` (`users_idusers`, `products_idproducts`) VALUES ('"+i+"', '"+rand+"')");
                statment.execute("INSERT INTO `mydb`.`users_has_products` (`users_idusers`, `products_idproducts`) VALUES ('"+i+"', '"+tempRand+"')");

            }
            statment.close();
            System.out.println("Finished...");
            connection.close();

        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }


        long afterQuery = System.currentTimeMillis();
        long totalTime = afterQuery - beforeQuery;

        System.out.println("------");
        System.out.println(Iterations +" records in "+ totalTime+" ms ("+totalTime/1000+" seconds)");


        String x = ZonedDateTime.now().toString();
        System.out.println("Time now "+String.valueOf(x));

    }

    static void selectDatabaseEntries(int selectLimiter) {


        String url = "jdbc:mysql://localhost:3306/mydb";
        String username = "root";
        String password = "cangetin";
        String query = "";
        //Limiter check
        if (selectLimiter!=0){
            query = "select distinct fname, productname, colorname from users, products, colors, users_has_products, products_has_colors\n" +
                    "where users.idusers = users_has_products.users_idusers and " +
                    "products.idproducts = users_has_products.products_idproducts and colors.colorname = 'Black' and " +
                    "products.productname = 'CPU'" + " LIMIT " + selectLimiter + ";";

        }else {
            query = "select distinct fname, productname, colorname from users, products, colors, users_has_products, products_has_colors\n" +
                    "where users.idusers = users_has_products.users_idusers and " +
                    "products.idproducts = users_has_products.products_idproducts and colors.colorname = 'Black' and " +
                    "products.productname = 'CPU';";
        }

        System.out.println("Connecting database...");

        long beforeQuery = System.currentTimeMillis();

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Database connected!");

            Statement statment = connection.createStatement();
            System.out.println("Running query for: " +query);

            ResultSet resultSet = statment.executeQuery(query);




            int resultCounter = 0;
            while (resultSet.next()) {
                //do something with each result
                resultCounter++;
            }

            statment.close();
            System.out.println("Size of result " + resultCounter);
            System.out.println("Finished...");
            connection.close();

        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }


        long afterQuery = System.currentTimeMillis();
        long totalTime = afterQuery - beforeQuery;


        System.out.println("Total time "+String.valueOf(totalTime) + " ms" + " (" + totalTime/1000 + "seconds)");

    }

    public static int randInt() {

        // NOTE: This will (intentionally) not run as written so that folks
        // copy-pasting have to think about how to initialize their
        // Random instance.  Initialization of the Random instance is outside
        // the main scope of the question, but some decent options are to have
        // a field that is initialized once and then re-used as needed or to
        // use ThreadLocalRandom (if using at least Java 1.7).
        Random random = new Random();

        int enumMax = Products.values().length;
        int min = 1;
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = random.nextInt((enumMax - min) + 1) + min;

        return randomNum;
    }

    static void testJDBC(){
        System.out.println("Loading driver...");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded!");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Cannot find the driver in the classpath!", e);
        }
    }
}
