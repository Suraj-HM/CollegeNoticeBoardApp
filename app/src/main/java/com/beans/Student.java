package com.beans;

import java.io.Serializable;

public class Student implements Serializable {
    private String stuId;
    private String stuName;
    private String stuEmail;
    private String stuPwd;
    private String stuPhNo;
    private String stuDeptId;

    public String getStuId() { return stuId; }
    public void setStuId(String stuId) {
        this.stuId = stuId;
    }
    public String getStuName() {
        return stuName;
    }
    public void setStuName(String stuName) {
        this.stuName = stuName;
    }
    public String getStuEmail() {
        return stuEmail;
    }
    public void setStuEmail(String stuEmail) {
        this.stuEmail = stuEmail;
    }
    public String getStuPwd() {
        return stuPwd;
    }
    public void setStuPwd(String stuPwd) {
        this.stuPwd = stuPwd;
    }
    public String getStuPhNo() {
        return stuPhNo;
    }
    public void setStuPhNo(String stuPhNo) {
        this.stuPhNo = stuPhNo;
    }
    public String getStuDeptId() {
        return stuDeptId;
    }
    public void setStuDeptId(String stuDeptId) {
        this.stuDeptId = stuDeptId;
    }
}
