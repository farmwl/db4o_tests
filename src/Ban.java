/**
 * Created by farmwl on 16.05.2017.
 */
public class Ban {
    User owner;
    Script script;
    String hwid;

    Ban(User owner, Script script, String hwid) {
        this.owner = owner;
        this.script = script;
        this.hwid = hwid;
    }
}
