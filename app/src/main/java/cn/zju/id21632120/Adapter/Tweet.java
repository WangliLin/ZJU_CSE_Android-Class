package cn.zju.id21632120.Adapter;

/**
 * Tweet类
 * Created by Wangli on 2017/6/18.
 */

public class Tweet {

    public String ID;
    public String User;
    public String Message;
    public String CreateTime;


    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }
}
