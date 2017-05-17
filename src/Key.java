import java.time.LocalDate;
import java.util.Random;

/**
 * Created by farmwl on 16.05.2017.
 */
public class Key {
    String key;         // сам ключ
    String hwid;        // hardware id к которому привязан ключ
    boolean expired;    // просрочен ключ или нет
    LocalDate expireDate;    // дата истечения ключа

    Key(String key, String hwid, boolean expired) {
        this.key = key;
        this.hwid = hwid;
        Random rnd = new Random();
        if (expired) this.expireDate = LocalDate.now().minusDays(1+rnd.nextInt(3));
        else this.expireDate = LocalDate.now().plusDays(1+rnd.nextInt(3));
        this.expired = LocalDate.now().isAfter(this.expireDate);
    }

    @Override
    public String toString() {
        return this.key+"  ::  expired: "+this.expired+"  ::  expireDate: "+this.expireDate;
    }
}
