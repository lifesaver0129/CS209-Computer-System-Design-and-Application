import java.io.*;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lifesaver on 18/09/2017.
 */
public class Tokenizer {
    public String address = null;

    public String fullString = null;

    public int count = 0;

    public Tokenizer(String add) {
        this.address = add;
        try {
            fullString = readFileContent(address);
        } catch (IOException e) {
            e.printStackTrace();
        }
        fullString = fullString.replaceAll("[，。？：「/」.!?:',\"‘’、“”]", " ");
        fullString = fullString.replaceAll("--", " ");
        fullString = fullString.toLowerCase();

    }

    private String readFileContent(String fileName) throws IOException {
        File file = new File(fileName);
        BufferedReader bf = new BufferedReader(new FileReader(file));
        String content = "";
        StringBuilder sb = new StringBuilder();
        while (content != null) {
            content = bf.readLine();
            if (content == null) {
                break;
            }
            sb.append(content.trim());
            if (content != "") {
                sb.append(" ");
            }
        }
        bf.close();
        return sb.toString();
    }

    public String nextToken() throws IOException {
        String regEx = "[\\u4e00-\\u9fa5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(fullString);
        if (m.find()) {
            int x = 0;
            char c = fullString.charAt(x);
            int alpha = (int) c;
            if ((alpha >= 65 && alpha <= 90) || (alpha >= 97 && alpha <= 122)) {
                String str = "";
                while ((alpha >= 65 && alpha <= 90) || (alpha >= 97 && alpha <= 122)) {
                    str += String.valueOf(fullString.charAt(x));
                    x += 1;
                    c = fullString.charAt(x);
                    alpha = (int) c;
                }
                fullString = fullString.substring(x);
                return str;
            }
            String str = String.valueOf(fullString.charAt(0));
            fullString = fullString.substring(1);
            return str;
        } else {
            String[] result = fullString.split("\\s|[\\u4e00-\\u9fa5]");
            if (count == result.length) {
                return null;
            }
            for (int x = count; x < result.length; x++) {
                if (result[x].equals("") || result[x].equals(" ")) {
                    count += 1;
                    continue;
                }
                if (x + 1 < result.length && (result[x + 1].equals("t") || result[x + 1].equals("s"))) {
                    System.out.println(result[x] + "\'" + result[x + 1]);
                    result[x + 1] = "";
                    continue;
                }
                if (Pattern.matches("\\[\\d+.", result[x])) {
                    count += 1;
                    continue;
                }
                if (x == count) {
                    count += 1;
                    return result[x];
                }
            }
            throw new NoSuchElementException("No such thing.");
        }
    }

}
