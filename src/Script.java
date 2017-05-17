import java.util.ArrayList;
import java.util.Random;

/**
 * Created by farmwl on 16.05.2017.
 */
public class Script {
    String name;
    int version;
    ArrayList<Key> keys;
    //ArrayList bans;

    Script(String name, int version) {
        this.name = name;
        this.version = version;
        this.keys = new ArrayList();
        //this.bans = new ArrayList();
    }

    public void addKey(String key, String hwid, boolean expired){
        this.keys.add(new Key(key, hwid, expired));
    }

    public static int randInt(int min, int max) {
        Random rnd = new Random();
        int randomNum = rnd.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    public void genKeys(int count){
        for (int i = 0; i < count; i++) {
            int rnd = randInt(1048576, 16777215);  // 100000 - FFFFFF
            String hwid = Integer.toHexString(rnd).toUpperCase();
            boolean expired = (i % 2) == 0;
            Key key = new Key(this.name+"_"+hwid, hwid, expired);
            this.keys.add(key);
            System.out.println("genKey: "+key.toString());
        }
    }

    @Override
    public String toString() {
        return this.name+"  ::  ver: "+this.version+"  ::  keys count: "+this.keys.size();
    }
}
