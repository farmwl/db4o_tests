import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by farmwl on 16.05.2017.
 */
public class User {

    String name;                    // имя
    int balance;                    // остаток на счету
    boolean paid;                   // оплачен или нет
    ArrayList<Script> scripts;      // список скриптов принадлежащих юзеру
    //ArrayList<Ban> bans;

    public String getName() { return name; }
    public int getBalance() { return balance; }
    public boolean isPaid() { return paid; }
    public ArrayList<Script> getScripts() { return scripts; }


    User(String name, boolean paid, int balance) {
        this.name = name;
        this.paid = paid;
        this.balance = balance;
        this.scripts = new ArrayList<Script>();
    }

    void addScript(String name, int version){
        this.scripts.add(new Script(name, version));
    }

    public static int randInt(int min, int max) {
        Random rnd = new Random();
        int randomNum = rnd.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    public void genKeys4AllScript(int min_count, int max_count){
        for (int i = 0; i < this.scripts.size(); i++) {
            int rnd = randInt(min_count, max_count);
            Script script = this.scripts.get(i);
            System.out.println("GENERATE ["+rnd+"] KEYS FOR '"+script.name+"' [owner: "+this.name+"]");
            script.genKeys(rnd);
            System.out.println();
        }
    }

    public ArrayList<Ban> genBans(){
        System.out.println("GENERATE RANDOM BANS FOR '"+this.name+"'");
        ArrayList<Ban> result = new ArrayList();
        if (this.scripts.size() == 0){
            System.out.println("  ! this user have no scripts, miss him");
        }
        for (int i = 0; i < this.scripts.size(); i++) {
            Random rnd = new Random();
            Script script = this.scripts.get(i);
            if (script.keys.size() > 4){
                Key key = script.keys.get(rnd.nextInt(script.keys.size()-1));
                String hwid = key.hwid;
                result.add(new Ban(this, script, hwid));
                System.out.println("  genBan: "+this.name+"  ::  "+script.name+"  ::  "+hwid);
            }
            else System.out.println("  ! "+script.name+": not enough keys for gen bans, so miss this script");
        }
        return result;
    }

    public int keysCount(){
        int result = 0;
        for (Script script : this.getScripts()) {
            result += script.keysCount();
        }
        return result;
    }

    @Override
    public String toString() {
        return name+"  ::  paid: "+paid+"  ::  balance: "+balance+
               "  ::  scripts count: "+scripts.size()+"  ::  keys count: "+keysCount();
    }
}
