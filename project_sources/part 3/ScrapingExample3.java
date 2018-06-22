import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.select.Elements;
import org.jsoup.helper.Validate;
import java.io.IOException;

// Like ScrapingExample2 but fetches biographical information for each winner
public class ScrapingExample3 {

    private static String[] winnerDates(String wikiPage)
        throws IOException {
        String[] dates = new String[2];
        dates[0] = "";
        dates[1] = "";
        Document doc = Jsoup
          .connect("https://en.wikipedia.org" + wikiPage)
          .get();
        Elements tables = doc.select("table.infobox");
        for (Element t: tables) {
           Elements birth = t.select("span.bday");
           if (birth.size() > 0) {
             dates[0] = birth.get(0).text();
           }
           Elements death = t.select("span.dday");
           if (death.size() > 0) {
             dates[1] = death.get(0).text();
           }
        }
        return dates;
    }

    public static void main(String[] args)
        throws IOException {
        String[] dates;
        Document doc = Jsoup
          .connect("https://en.wikipedia.org/wiki/Turing_Award")
          .get();
        Elements tables = doc.select("table.wikitable");
        for (Element t: tables) {
          Elements rows = t.select("tbody tr");
          for (Element r: rows) {
            Elements cells = r.select("td");
            if (cells.size() > 0) {
              Elements year = r.select("th");
              Elements winners = cells.get(0).select("a");
              for (Element w: winners) {
                 Attributes a = w.attributes();
                 String     href = a.get("href");
                 if (href != null && href.startsWith("/wiki/")) {
                   System.out.print(year.get(0).text() + ",");
                   System.out.print("\""+w.text()+"\",");
                   dates = winnerDates(href); 
                   if (dates[0].length() > 0) {
                     System.out.print("\""+dates[0]+"\"");
                   }
                   System.out.print(",");
                   if (dates[1].length() > 0) {
                     System.out.println("\""+dates[1]+"\"");
                   } else {
                     System.out.println("");
                   }
                 }
              }
            }
          }
        }
    }
}
