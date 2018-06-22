import java.io.*;

/**
 * Created by lifesaver on 25/09/2017.
 */
public class InputExercise {

    //Using Byte
    public static void UseInputStream() {
        File file = new File("java_zh.txt");
        if (file.isFile()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                //FileInputStream fis = new FileInputStream("java_zh.txt");
                byte[] buf = new byte[256];
                StringBuffer sb = new StringBuffer();
                int len = 0;
                while ((len = fis.read(buf)) != -1) {
                    sb.append(new String(buf, 0, len, "utf-8"));
                    //sb.append(new String(buf));
                }
                System.out.println(sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("File not exist!");
        }
    }

    //Using Character
    public static void UseReadFile() {
        FileReader fr = null;
        FileWriter fw = null;
        try {
            fr = new FileReader("java_zh.txt");
            //fw = new FileWriter("java_zh_new.txt");
            char[] buf = new char[1024];
            int len;
            while ((len = fr.read(buf)) != -1) {
                fw.write(buf, 0, len);
                //fw.write(buf);
            }
            while ((len = fr.read()) != -1) {
                System.out.print((char) len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fr != null)
                try {
                    fr.close();
                } catch (Exception e2) {
                    throw new RuntimeException("Close failure");
                }
            if (fw != null)
                try {
                    fw.close();
                } catch (IOException e) {
                    throw new RuntimeException("Close failure");
                }
        }
    }

    public static void UseBufferReader(){
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("java_zh.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (br != null) {
                try {
                    br.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
