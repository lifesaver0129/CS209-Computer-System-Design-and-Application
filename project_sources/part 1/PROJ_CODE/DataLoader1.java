//
//  DataLoader, reads every time from the file
//  Logic is more like what it will be when accessing
//  with JDBC (reading from the database everytime) -
//  except that the initial load would probably restricted
//  on a date range.

import java.util.ArrayList;
import java.util.TreeSet;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class DataLoader1 {
    private final ArrayList<Quake> earthQuakes = new ArrayList<Quake>();
    private final TreeSet<String>  regionSet = new TreeSet<String>();
    private       String           minDate = "9999-99-99";
    private       String           maxDate = "0000-00-00";

    private void readQuakes(String fromDay,
                            String toDay,
                            float  magnitude,
                            String region) {
        String    thisDate;
        String    thisDay;
        float     thisMagnitude;
        String    thisRegion;
        boolean   loadRegions = false;
        boolean   loadQuake;

        // System.out.println("from " + fromDay + ", to " + toDay
        //                    + ", magnitude >= " + magnitude
        //                    + ", region " + region);
        if (regionSet.size() == 0) {
          regionSet.add(Quake.WORLDWIDE);
          loadRegions = true;
        }
        try (BufferedReader reader =
                new BufferedReader(new FileReader("earthquakes.csv"))) {
           String     line = null;
           int        linecnt = 0;
           String[]   fields;
           while ((line = reader.readLine()) != null) {
             if (linecnt > 0) { // Skip header line
               loadQuake = true;
               if (line.length() > 0) {
                 fields = line.split(",");
                 thisDate = fields[1].replace("\"", "")
                                     .substring(0,19);
                 thisDay = thisDate.substring(0, 10).trim();
                 thisMagnitude = Float.parseFloat(fields[5]);
                 thisRegion = fields[6].replace("\"", "")
                                       .trim();
                 if (loadRegions) {
                   regionSet.add(thisRegion);
                 }
                 // System.out.println("Region OK: "
                 //                    + (region.equals(Quake.WORLDWIDE)
                 //                      || thisRegion.equals(region)));
                 // System.out.println("Date OK  : "
                 //                    + ((thisDay.compareTo(fromDay) >= 0)
                 //                       && (thisDay.compareTo(toDay) <= 0)));
                 // System.out.println(fromDay + "/" + thisDay + "/" + toDay);
                 // System.out.println("Magn OK  : "
                 //                    + (thisMagnitude >= magnitude));
                 //
                 // Note that the original load will probably fail
                 // the test on dates (as minDate is set very high and
                 // maxDate is set very low)
                 //
                 if ((region.equals(Quake.WORLDWIDE)
                      || thisRegion.equals(region))
                     && (thisDate.compareTo(fromDay) >= 0)
                     && (thisDate.compareTo(toDay) <= 0)
                     && (thisMagnitude >= magnitude)) {
                   earthQuakes.add(new Quake(Integer.parseInt(fields[0]),
                                             thisDate, 
                                             Float.parseFloat(fields[2]),
                                             Float.parseFloat(fields[3]),
                                             Integer.parseInt(fields[4]),
                                             thisMagnitude,
                                             thisRegion));
                 }
                 if (thisDay.compareTo(minDate) < 0) {
                   minDate = thisDay;
                 } else if (thisDay.compareTo(maxDate) > 0) {
                   maxDate = thisDay;
                 }
               }
             }
             linecnt++;
           }
           // System.out.println("Min date: " + minDate);
           // System.out.println("Max date: " + maxDate);
        } catch (Exception e) {
           System.err.println("Error: " + e.getMessage());
           e.printStackTrace();
        }
    }

    public DataLoader1() {
        // Load everything in memory initially
        readQuakes(minDate, maxDate, 0f, Quake.WORLDWIDE);
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
        // We could have a second list and use streams to populate
        // it. That would be fine, and I'd be happy to see students do
        // that. The problem I have is that it means the full list in
        // memory, plus a second collection that is a subset of the
        // first one. Doesn't seem too good to me.
        // I'm reloading each time from the datafile, which is of
        // course I/O inefficient but closer to what will be done
        // at a later stage when querying a database.
        // First clear the ArrayList
        earthQuakes.clear();
        readQuakes(fromDay, toDay, magnitude, region);
        return earthQuakes;
    }

    public TreeSet<String> getRegions() {
        return regionSet;
    }
}
