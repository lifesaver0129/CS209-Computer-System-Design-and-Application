import java.io.*;
import java.util.*;

/**
 * Created by lifesaver on 16/10/2017.
 */
public class LanguageGuesser {
    public static void main(String args[]) throws IOException {
        Properties props = new Properties();
        try (BufferedReader conf
                     = new BufferedReader(new FileReader("language.cnf"))) {
            props.load(conf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File folder = new File(props.getProperty("stop_words_dir"));
        if (!folder.isDirectory()) {
            System.out.println("Folder not existed");
            return;
        }
        List<File> textFiles = new ArrayList<>();
        for (File file : folder.listFiles()) {
            if (file.getName().endsWith(".txt")) {
                textFiles.add(file);
            }
        }
        HashMap<String, String> dict = new HashMap();
        HashMap<String, Integer> count = new HashMap();
        for (int i = 0; i < textFiles.size(); i++) {
            String curr_name = textFiles.get(i).getName();
            File curr_file = textFiles.get(i);
            count.put(curr_name, 0);
            String temp;
            try {
                BufferedReader reader = new BufferedReader(new FileReader(curr_file));
                while ((temp = reader.readLine()) != null) {
                    dict.put(temp, curr_name);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Tokenizer judge = new Tokenizer(args[0]);
        String tmp = judge.nextToken();
        while(tmp!=null){
            if (dict.containsKey(tmp)){
                count.put(dict.get(tmp),count.get(dict.get(tmp))+1);
            }
            tmp = judge.nextToken();
        }
        List<HashMap.Entry<String, Integer>> fin = new ArrayList<>();
        for(HashMap.Entry<String, Integer> entry : count.entrySet()){
            fin.add(entry);
        }
        fin.sort((o1, o2) -> o2.getValue()-o1.getValue());
        String ans = fin.get(0).getKey();
        ans =ans.replace(".txt","");
        System.out.println(ans);
    }
}
