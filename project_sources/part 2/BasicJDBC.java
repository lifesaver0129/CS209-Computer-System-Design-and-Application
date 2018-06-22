import java.sql.*;

public class BasicJDBC {
    //
    static Connection    con = null;

    private static void openDB(String dbPath) {
       try {
         // CLASSPATH must be properly set, for instance on
         // a Linux system or a Mac:
         // $ export CLASSPATH=.:sqlite-jdbc-version-number.jar
         // Alternatively, run the program with
         // $ java -cp .:sqlite-jdbc-version-number.jar BasicJDBC
         Class.forName("org.sqlite.JDBC");
       } catch(Exception e) {
         System.err.println("Cannot find the driver.");
         System.exit(1);
       }
       try {
         con = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
         con.setAutoCommit(false);
         System.err.println("Successfully connected to the database.");
       } catch (Exception e) {
         System.err.println(e.getMessage());
         System.exit(1);
       }
    }

    private static void closeDB() {
       if (con != null) {
         try {
           con.close();
           con = null;
         } catch (Exception e) {
           // Forget about it
         }
       }
    }

    private static void justDoIt() {
        // We query the sqlite_master table that contains
        // the names of all other tables in the database,
        // and for each table we count how many rows it
        // contains.
        if (con != null) {
          try {
            Statement stmt1;
            ResultSet rs1;
            Statement stmt2;
            ResultSet rs2;
            int       tabcnt = 0;
            stmt1 = con.createStatement();
            rs1 = stmt1.executeQuery("select name from sqlite_master"
                                     +" where type='table'");
            while (rs1.next()) {
              stmt2 = con.createStatement();
              rs2 = stmt2.executeQuery("select count(*) from "
                                       + rs1.getString(1));
              if (rs2.next()) {
                System.out.println(rs1.getString(1) + ":\t" +
                                   rs2.getInt(1) + " rows");
              }
              rs2.close();
              tabcnt++;
            }
            rs1.close();
            if (tabcnt == 0) {
              System.out.println("No tables in the file");
            }
          } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
          }
        }
    }

    public static void main(String[] args) {
      if (args.length > 0) {
        openDB(args[0]);
        justDoIt();
        closeDB();
      } 
    }
}
