import java.util.ArrayList;
import java.util.Random;

/**
 * Created by farmwl on 16.05.2017.
 */
public class User {
    String name;
    int balance;
    boolean paid;
    ArrayList<Script> scripts;


    User(String name, boolean paid, int balance) {
        this.name = name;
        this.paid = paid;
        this.balance = balance;
        this.scripts = new ArrayList();
    }

    void addScript(String name, int version){
        this.scripts.add(new Script(name, version));
    }

    public static int randInt(int min, int max) {
        Random rnd = new Random();
        int randomNum = rnd.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    void genKeys4AllScript(int min_count, int max_count){
        for (int i = 0; i < this.scripts.size(); i++) {
            int rnd = randInt(min_count, max_count);
            Script script = this.scripts.get(i);
            System.out.println("GENERATE ["+rnd+"] KEYS FOR '"+script.name+"'");
            script.genKeys(rnd);
            System.out.println();
        }
    }

    @Override
    public String toString() {
        return this.name+"  ::  paid: "+this.paid+"  ::  scripts count: "+this.scripts.size();
    }
}
