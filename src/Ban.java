import java.util.List;

/**
 * Created by farmwl on 16.05.2017.
 */
public class Ban {
    User owner;
    Script script;
    String hwid;

    public User getOwner() { return owner; }
    public Script getScript() { return script; }
    public String getHwid() { return hwid; }

    Ban(User owner, Script script, String hwid) {
        this.owner = owner;
        this.script = script;
        this.hwid = hwid;
    }

    @Override
    public String toString() {
        return "ban owner: "+getOwner().getName()+"  ::  script: "+getScript().getName()+
               " (ver: "+getScript().getVersion()+")  ::  hwid: "+getHwid();
    }
}
