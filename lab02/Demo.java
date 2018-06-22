import java.util.Collection;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.HashMap;

public class Demo {
    public static void main(String[] args) {
        // <?> is a wild card -it stands for "any type"
        Collection<?>[] collections = {new HashSet<String>(),
                new ArrayList<String>(),
                new HashMap<String, String>().values()};
        Super subToSuper = new Sub();
        for(Collection<?> collection: collections) {
            System.out.print(subToSuper.getType(collection) + " ");
        }
        System.out.println("");
    }

    abstract static class Super {

        public static String getType(Collection<?> collection) {
            return "Super:collection";
        }

        public String getType(ArrayList<?> list) {
            return "Super:arrayList";
        }

        public String getType(HashSet<?> set) {
            return "Super:hashSet";
        }
    }

    static class Sub extends Super {
        public static String getType(Collection<?> collection) {
            return "Sub:collection";
        }
    }
}