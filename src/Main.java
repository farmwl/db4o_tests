/**
 * Created by farmwl on 16.05.2017.
 */
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.query.Predicate;
import com.db4o.query.Query;

public class Main {
    public static String DB_FILE_NAME = System.getProperty("user.dir") + "\\database.db4o";

    public static void main(String[] args) {

        dbClear();
        dbCreate();

        QBE();
        nativeQuery();
        //SODA();

    }

    private static void dbCreate() {
        ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), DB_FILE_NAME);

        // создаем юзеров (имя, оплачен ли, баланс)
        User user1 = new User("user_1", true, 150);
        User user2 = new User("user_2", false, 0);
        User user3 = new User("user_3", true, 0);
        User user4 = new User("user_4", false, 50);
        User user5 = new User("user_5", true, 50);

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


//        User ban1 = new Ban(user1, );

        db.store(user1);
        db.store(user2);
        db.store(user3);
        db.store(user4);
        db.store(user5);

        db.close();
    }

    private static void dbClear() {
        File file = new File(DB_FILE_NAME);
        if (file != null) file.delete();
    }



    private static void printQueryResult(List list) {
        for (Object item : list) {
            System.out.println(item);
        }
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
        Key key;
        for (int i = 0; i < result.size(); i++) {
            key = (Key) result.get(i);
            if (key.expireDate.isEqual(LocalDate.now().plusDays(1))) {
                keys.add(key);
            }
        }
        printQueryResult(keys);
        System.out.println();

        db.close();
    }

    private static void nativeQuery() {
        ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), DB_FILE_NAME);

        System.out.println();
        System.out.println("----------------------------------------------------------");

        //
        System.out.println("NATIVE QUERY #1 []");

        System.out.println();


        //
        System.out.println("NATIVE QUERY #2 []");

        System.out.println();


        //
        System.out.println("NATIVE QUERY #3 []");

        System.out.println();

        db.close();
    }







}
