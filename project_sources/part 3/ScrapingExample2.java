import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.helper.Validate;
import java.io.IOException;

// Checks attributes and returns one row per recipient
public class ScrapingExample2 {

    public static void main(String[] args)
        throws IOException {
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
                 System.out.print(year.get(0).text() + ",");
                 System.out.println("\""+w.text()+"\"");
              }
            }
          }
        }
    }
}
