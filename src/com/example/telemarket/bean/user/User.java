package com.example.telemarket.bean.user;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-3-29
 * Time: 下午5:16
 * 用户
 */
public class User extends PersistenceObject {
    private int userId;
    private String username;
    private String password;
    private String nickName;
    private String deptName;
    private int deptId;
    private boolean isDefaultLogin = false;
    private String message;//登录之后返回的信息
    private String post;//职务
    private String headImg = "-2";//头像
    private String email;//邮箱
    private String roleIds; //角色
    private String domain;//区域
    private String province;    //省
    private String city; //市
    private String county;   //县/区
    private RoleBean roleBean;
    private String owerSign;//个性签名
    private String phone;    //号码
    private String workrestDate;//值日时间
    private String isWork;//是否上班
    private String startTime = null;//开始时间
    private String finishTime;//结束时间
    private String zoneType;//上班类型
    private String sex;//性别

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public boolean isDefaultLogin() {
        return isDefaultLogin;
    }

    public void setDefaultLogin(boolean defaultLogin) {
        isDefaultLogin = defaultLogin;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getEmail() {
        return email;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public RoleBean getRoleBean() {
        return roleBean;
    }

    public void setRoleBean(RoleBean roleBean) {
        this.roleBean = roleBean;
    }

    public String getOwerSign() {
        return owerSign;
    }

    public void setOwerSign(String owerSign) {
        this.owerSign = owerSign;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWorkrestDate() {
        return workrestDate;
    }

    public void setWorkrestDate(String workrestDate) {
        this.workrestDate = workrestDate;
    }

    public String getWork() {
        return isWork;
    }

    public void setWork(String work) {
        isWork = work;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getZoneType() {
        return zoneType;
    }

    public void setZoneType(String zoneType) {
        this.zoneType = zoneType;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "username=" + username + " password=" + password + " sex=" + sex + " userId=" + userId + " nickName=" + nickName
                + " post=" + post;
    }
}
