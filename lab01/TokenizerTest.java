import static org.junit.Assert.*;

/**
 * Created by lifesaver on 20/09/2017.
 */


public class TokenizerTest {
     @org.junit.Test
     public void nextToken() throws Exception {
        Tokenizer test = new Tokenizer("dickens.txt");
        assertEquals("that",test.nextToken());
        assertEquals("punctual",test.nextToken());
        test.nextToken();
        assertEquals("of",test.nextToken());
    }

    @org.junit.Test
    public void nextToken2() throws Exception {
        Tokenizer test2 = new Tokenizer("cervantes.txt");
        assertEquals("capítulo", test2.nextToken());
        assertEquals("primero", test2.nextToken());
        test2.nextToken();
        assertEquals("trata", test2.nextToken());
    }

    @org.junit.Test
    public void nextToken3() throws Exception {
        Tokenizer test3 = new Tokenizer("java_en.txt");
        test3.nextToken();
        test3.nextToken();
        test3.nextToken();
        assertEquals("general-purpose", test3.nextToken());
    }

    @org.junit.Test
    public void nextToken4() throws Exception {
        Tokenizer test2 = new Tokenizer("java_tr.txt");
        assertEquals("java", test2.nextToken());
        assertEquals("sun", test2.nextToken());
        test2.nextToken();
        assertEquals("mühendislerinden", test2.nextToken());
    }

    @org.junit.Test
    public void nextToken5() throws Exception {
        Tokenizer test2 = new Tokenizer("java_zh.txt");
        assertEquals("java", test2.nextToken());
        assertEquals("是", test2.nextToken());
        test2.nextToken();
        assertEquals("種", test2.nextToken());
    }

}