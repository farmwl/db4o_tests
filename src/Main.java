/**
 * Created by farmwl on 16.05.2017.
 */
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Constraint;
import com.db4o.query.Predicate;
import com.db4o.query.Query;

public class Main {
    public static String DB_FILE_NAME = System.getProperty("user.dir") + "\\database.db4o";

    public static void main(String[] args) {
        dbClear();
        dbCreate();

        QBE();
        NQ();
        SODA();

    }

    private static void dbCreate() {
        ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), DB_FILE_NAME);

        try {
            System.out.println("> GENERATE DATABASE...");
            System.out.println();
            // создаем юзеров (имя, оплачен ли, баланс)
            User user1 = new User("user_1", true, 150);
            User user2 = new User("user_2", false, 300);
            User user3 = new User("user_3", true, 0);
            User user4 = new User("user_4", false, 0);
            User user5 = new User("user_5", true, 500);

            user1.addScript("script_1", 100);
            user1.addScript("script_2", 102);
            user1.addScript("script_3", 103);
            user2.addScript("script_4", 100);
            user3.addScript("script_5", 105);
            user3.addScript("script_6", 106);
            user3.addScript("script_7", 107);
            // user4 и user5 специально не имеют скриптов

            // генерим ключи для всех скриптов каждого из юзеров
            user1.genKeys4AllScript(5, 7);
            user2.genKeys4AllScript(2, 5);
            user3.genKeys4AllScript(3, 6);

            db.store(user1);
            db.store(user2);
            db.store(user3);
            db.store(user4);
            db.store(user5);

            List<User> users = db.queryByExample(User.class);
            ArrayList<Ban> bans;
            for (User user : users){
                bans = user.genBans();
                for (Ban ban : bans){
                    db.store(ban);
                }
            }

        }
        finally {
            db.close();
        }
    }

    private static void dbClear() {
        File file = new File(DB_FILE_NAME);
        if (file != null) file.delete();
    }



    private static void printQueryResult(List list) {
        if (! list.isEmpty()){
            for (Object item : list) {
                System.out.println(item);
            }
        }
        else System.out.println("query have no results!");
    }

    private static List ignoreDupEntity(List list) {
        List result = new ArrayList();
        for (Object item : list) {
            if (!result.contains(item)) result.add(item);
        }
        return result;
    }



    private static void QBE() {
        ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), DB_FILE_NAME);
        try {
            System.out.println();
            System.out.println("----------------------------------------------------------");


            // найти все скрипты с версией = 100
            System.out.println("QUERY BY EXAMPLE #1 [find all scripts with version = 100]");
            Script protoScript = new Script(null, 100);
            List result = db.queryByExample(protoScript);
            printQueryResult(ignoreDupEntity(result));
            System.out.println();


            // найти всех оплаченных пользователей
            System.out.println("QUERY BY EXAMPLE #2 [find all paid users]");
            User protoUser = new User(null, true, 0);
            result = db.queryByExample(protoUser);
            printQueryResult(ignoreDupEntity(result));
            System.out.println();


            // найти все ключи, которые истекают завтра
            System.out.println("QUERY BY EXAMPLE #3 [find all keys that will expire tomorrow]");
            result = db.queryByExample(Key.class);
            ArrayList<Key> keys = new ArrayList();
            for (Object item : result) {
                if (((Key) item).expireDate.isEqual(LocalDate.now().plusDays(1))) {
                    keys.add((Key) item);
                }
            }
            printQueryResult(keys);
            System.out.println();


        }
        finally {
            db.close();
        }
    }

    private static void NQ() {
        ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), DB_FILE_NAME);
        try {
            System.out.println();
            System.out.println("----------------------------------------------------------");


            // все скрипты, у которых 4 ключа
            System.out.println("NATIVE QUERY #1 [find all scripts with 4 keys]");
            List result = db.query(new Predicate<Script>() {
                public boolean match(Script script) {
                    return script.getKeys().size() == 4;
                }
            });
            printQueryResult(ignoreDupEntity(result));
            System.out.println();


            // найти всех неактивных юзеров (неоплаченных, без скриптов и без денег)
            System.out.println("NATIVE QUERY #2 [find all inactive users - not paid and without money and scripts]");
            result = db.query(new Predicate<User>() {
                public boolean match(User user) {
                    return user.getScripts().size() == 0 && user.getBalance() == 0 && !user.isPaid();
                }
            });
            printQueryResult(ignoreDupEntity(result));
            System.out.println();


            // найти юзера, у которого ключей на всех его скриптах > 10
            System.out.println("NATIVE QUERY #3 [find all users who have > 10 keys]");
            result = db.query(new Predicate<User>() {
                public boolean match(User user) {
                    List<Script> scripts = user.getScripts();
                    int keysCount = 0;
                    for (Script script : scripts) {
                        keysCount += script.getKeys().size();
                    }
                    return keysCount >= 10;
                }
            });
            printQueryResult(ignoreDupEntity(result));
            System.out.println();


            // найти все баны хвид которых содержит символ 'F' и которые принадлежат оплаченным юзерам
            System.out.println("NATIVE QUERY #4 [find all bans that belongs to all paid users, where hwid contains symbol 'F']");
            result = db.query(new Predicate<Ban>() {
                public boolean match(Ban ban) {
                    return ban.getHwid().contains("F") && ban.getOwner().isPaid();
                }
            });
            printQueryResult(ignoreDupEntity(result));
            System.out.println();

        }
            finally {
            db.close();
        }
    }

    private static void SODA() {
        ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), DB_FILE_NAME);
        try {
            System.out.println();
            System.out.println("----------------------------------------------------------");


            // все баны, скрипты которых имеют версию > 101
            System.out.println("SODA QUERY #1 [find all bans that belongs to scripts with version > 101]");
            Query query = db.query();
            query.constrain(Ban.class);
            query.descend("script").descend("version").constrain(101).greater();
            List result = query.execute();
            printQueryResult(ignoreDupEntity(result));
            System.out.println();


            // вывести всех юзеров по убыванию баланса
            System.out.println("SODA QUERY #2 [show all users sorted by descending balance]");
            query = db.query();
            query.constrain(User.class);
            query.descend("balance").orderDescending();
            result = query.execute();

            printQueryResult(ignoreDupEntity(result));
            System.out.println();


            // вывести всех юзеров, кто либо оплачен, либо у кого баланс > 0
            System.out.println("SODA QUERY #3 [show all users who are paid or have money]");
            query = db.query();
            query.constrain(User.class);
            Constraint constr = query.descend("balance").constrain(0).greater();
            query.descend("paid").constrain(true).or(constr);
            result = query.execute();

            printQueryResult(ignoreDupEntity(result));
            System.out.println();

        } finally {
            db.close();
        }
    }






}
