package com.programme.Fortress.Function.ToolCase.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.programme.Fortress.Function.ToolCase.dao.SysUserMapper;
import com.programme.Fortress.Function.ToolCase.entity.ResultBean;
import com.programme.Fortress.Function.ToolCase.entity.SysMessage;
import com.programme.Fortress.Function.ToolCase.entity.SysUser;
import com.programme.Fortress.Other.Exception.ResultUtil;
import com.programme.Fortress.Util.BaseValidationUtils;
import com.programme.Fortress.Util.StringUtil;
import com.programme.Fortress.Util.TypeChange.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public String getUser() {
        List<SysUser> users = sysUserMapper.selectList(null);
        return JSON.toJSONString(users);
    }

    @Override
    public ResultBean addUser(SysUser sysUser) {
        try {
            if (!BaseValidationUtils.isMobile(sysUser.getPhone())) return ResultUtil.fail("请输入正确的手机号!");
            Integer count = sysUserMapper.selectCount(new QueryWrapper<SysUser>().eq("user_id", sysUser.getUserId()));
            if(count > 0){
                return ResultUtil.fail("新增失败，用户已存在!");
            }else {
                Long id = sysUserMapper.getDual();
                sysUser.setId(id);//赋值ID
                int byId =sysUserMapper.insert(sysUser);
                return ResultUtil.success(null,"新增用户成功");
            }
        }catch (Exception e){
            log.error("新增系统用户异常",e);
            return ResultUtil.fail(e.getMessage());
        }
    }

    @Override
    public boolean loginSystem(String userid,String password){
        SysUser sysUser = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("user_id", userid));
        if (StringUtils.isEmpty(sysUser)){
            log.info("系统不存在该用户[{}]",userid);
            return false;
        }else {
            /*String md5 = MD5Utils.convertMD5(sysUser.getPassword());
            if(password.equals(md5)) */
                return true;
        }
        /*log.info("账户或密码错误",userid);
        return false;*/
    }

    @Override
    public ResultBean deleteUser(String Id) {
        try {
            int byId = sysUserMapper.deleteById(Id);
            return ResultUtil.success(null,"删除用户成功");
        }catch (Exception e){
            log.error("删除系统用户异常",e);
            return ResultUtil.fail(e.getMessage());
        }
    }

    @Override
    public ResultBean upDateUser(SysUser sysUser) {
        try {
            //updateById莫名其妙的全部不校驗null值了，先这样补值
            SysUser user = sysUserMapper.selectById(sysUser.getId());
            sysUser.setCreatedTime(user.getCreatedTime());

            int byId = sysUserMapper.updateById(sysUser);
            return ResultUtil.success(null,"修改用户成功");
        }catch (Exception e){
            log.error("删除系统用户异常",e);
            return ResultUtil.fail(e.getMessage());
        }
    }

    @Override
    public Map alertPhoneInfor(String type){
        try {
            List<SysUser> users = sysUserMapper.selectList(new QueryWrapper<SysUser>().in("user_type", type , "ADMIN"));
            String phones="";
            for (int i = 0; i < users.size(); i++) {
                if (i == 0) {
                    phones = users.get(i).getPhone();
                }else {
                    phones = phones +","+ users.get(i).getPhone();
                }
            }
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("phone",phones);
            hashMap.put("num",users.size());
            return hashMap;
        }catch (Exception e){
            log.error("查询[{}]信息异常",type,e);
            return null;
        }
    }
}
