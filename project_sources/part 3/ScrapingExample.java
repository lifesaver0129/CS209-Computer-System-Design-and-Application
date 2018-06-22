import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.helper.Validate;
import java.io.IOException;

public class ScrapingExample {

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
              System.out.print(year.get(0).text() + ",");
              System.out.println("\""+cells.get(0).text()+"\"");
            }
          }
        }
    }
}
