package com.programme.Fortress.Function.Business.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("BNK_GSBH")
public class CompanyNum {

    @TableId(value = "f_qybh")
    private String version;
    @TableField(value = "f_qymc")
    private String versionName;
    @TableField(value = "f_mbdz")
    private String targetAdd;
    @TableField(value = "f_sftp_ip")
    private String sftpIP;
    @TableField(value = "f_sftp_post")
    private String sftpPost;
    @TableField(value = "f_user")
    private String sftpUser;
    @TableField(value = "f_password")
    private String sftpPw;
    @TableField(value = "f_budget_site")
    private String budgetSite;
    @TableField(value = "f_budget_user")
    private String budgetUser;
    @TableField(value = "f_budget_password")
    private String budgetPs;
    @TableField(value = "f_switch")
    private String Switch;
}
