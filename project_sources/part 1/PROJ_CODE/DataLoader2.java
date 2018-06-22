//
//  DataLoader, Stream version 
//
import java.util.ArrayList;
import java.util.TreeSet;
import java.text.ParseException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class DataLoader2 {
    private final ArrayList<Quake> earthQuakes = new ArrayList<Quake>();
    private final TreeSet<String>  regionSet = new TreeSet<String>();
    private       String           minDate = "9999-99-99";
    private       String           maxDate = "0000-00-00";

    private void readQuakes() {
        // Only called once, by the constructor 
        String    thisDate;
        String    thisDay;
        float     thisMagnitude;
        String    thisRegion;

        regionSet.add(Quake.WORLDWIDE);
        try (BufferedReader reader =
                new BufferedReader(new FileReader("earthquakes.csv"))) {
           String     line = null;
           int        linecnt = 0;
           String[]   fields;
           while ((line = reader.readLine()) != null) {
             if (linecnt > 0) { // Skip header line
               if (line.length() > 0) {
                 fields = line.split(",");
                 thisDate = fields[1].replace("\"", "")
                                     .substring(0,19);
                 thisDay = thisDate.substring(0, 10).trim();
                 thisMagnitude = Float.parseFloat(fields[5]);
                 thisRegion = fields[6].replace("\"", "")
                                       .trim();
                 regionSet.add(thisRegion);
                 earthQuakes.add(new Quake(Integer.parseInt(fields[0]),
                                           thisDate, 
                                           Float.parseFloat(fields[2]),
                                           Float.parseFloat(fields[3]),
                                           Integer.parseInt(fields[4]),
                                           thisMagnitude,
                                           thisRegion));
                 if (thisDay.compareTo(minDate) < 0) {
                   minDate = thisDay;
                 } else if (thisDay.compareTo(maxDate) > 0) {
                   maxDate = thisDay;
                 }
               }
             }
             linecnt++;
           }
        } catch (Exception e) {
           System.err.println("Error: " + e.getMessage());
           e.printStackTrace();
        }
    }

    public DataLoader2() {
        // Load everything in memory
        readQuakes();
    }

    public String getMinDate() {
        return minDate;
    }

    public String getMaxDate() {
        return maxDate;
    }

    public ArrayList<Quake> getQuakes(String fromDay,
                                      String toDay,
                                      float  magnitude,
                                      String region) {
        // We add everything to a "filtered list" each time. 
        ArrayList<Quake> filteredQuakes = new ArrayList<Quake>();

        earthQuakes.stream()
                   .filter((q) -> (region.equals(Quake.WORLDWIDE)
                                   || region.equals(q.getRegion())))
                   .filter((q) -> (fromDay.compareTo(q.getDate()
                                                      .substring(0,10)
                                                      .trim()) <= 0))
                   .filter((q) -> (toDay.compareTo(q.getDate()
                                                    .substring(0,10)
                                                    .trim()) >= 0))
                   .filter((q) -> (q.getMagnitude() >= magnitude))
                   .forEach((q) -> {filteredQuakes.add(q);});
        return filteredQuakes;
    }

    public TreeSet<String> getRegions() {
        return regionSet;
    }
}
