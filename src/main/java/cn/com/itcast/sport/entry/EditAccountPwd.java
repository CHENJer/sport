package cn.com.itcast.sport.entry;

public class EditAccountPwd {

    private Integer id;

    private String username;

    private String beforePwd;

    private String updatePwd;

    private String confirmPwd;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBeforePwd() {
        return beforePwd;
    }

    public void setBeforePwd(String beforePwd) {
        this.beforePwd = beforePwd;
    }

    public String getUpdatePwd() {
        return updatePwd;
    }

    public void setUpdatePwd(String updatePwd) {
        this.updatePwd = updatePwd;
    }

    public String getConfirmPwd() {
        return confirmPwd;
    }

    public void setConfirmPwd(String confirmPwd) {
        this.confirmPwd = confirmPwd;
    }
}