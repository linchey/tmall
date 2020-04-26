package com.lin.toymall.service.Impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.lin.toymall.Service.UserService;
import com.lin.toymall.bean.UmsMember;
import com.lin.toymall.bean.UmsMemberReceiveAddress;
import com.lin.toymall.service.mapper.UserMapper;
import com.lin.toymall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    RedisUtil redisUtil;
    @Override
    public List<UmsMember> getAllUser() {
        List<UmsMember> umsMembers=new ArrayList<>();
        umsMembers=userMapper.selectAll();
        return umsMembers;
    }

    @Override
    public List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId) {
        return null;
    }

    @Override
    public void addUser(UmsMember umsMember) {
        userMapper.insertUser(umsMember);
    }

    @Override
    public UmsMember findUserByName(String username) {
        UmsMember umsMember=userMapper.selectByName(username);
        return umsMember;
    }

    @Override
    public void resetPassword(String password, String username) {
        userMapper.updateUserPassword(password,username);
    }

    @Override
    public void modifyInfo(UmsMember umsMember) {
        Jedis jedis=null;
        try {
            jedis=redisUtil.getJedis();
            if(jedis!=null){
                String idFromCache = jedis.get("user:" + umsMember.getUsername() + ":id");
                if (StringUtils.isNotBlank(idFromCache)) {
                    umsMember.setId( idFromCache );
                    return;
                }
            }
                String id=userMapper.selectIdByName(umsMember.getUsername());
                umsMember.setId( id );
                userMapper.updateUserInfo(umsMember);
                flushInfoCache(umsMember);


        }finally {
            jedis.close();
        }

    }

    private void flushInfoCache(UmsMember umsMember) {
        Jedis jedis=redisUtil.getJedis();
        jedis.del( "user:" + umsMember.getId()+":"+ umsMember.getUsername()+ ":info" );
        jedis.setex("user:" +umsMember.getId()+":"+ umsMember.getUsername() +":info",60*60*24, JSON.toJSONString(umsMember));
        jedis.close();
    }

    @Override
    public UmsMember login(UmsMember umsMember) {
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();

            if(jedis!=null){
                String umsMemberStr = jedis.get("user:" +umsMember.getUsername()+":"+ umsMember.getPassword() + ":info");

                if (StringUtils.isNotBlank(umsMemberStr)) {
                    // 密码正确
                    UmsMember umsMemberFromCache = JSON.parseObject(umsMemberStr, UmsMember.class);
                    return umsMemberFromCache;
                }
            }
            // 链接redis失败，开启数据库
            UmsMember umsMemberFromDb =loginFromDb(umsMember);
            if(umsMemberFromDb!=null){
                jedis.setex("user:" +umsMember.getUsername()+":"+ umsMember.getPassword() +":info",60*60*24, JSON.toJSONString(umsMemberFromDb));
            }
            return umsMemberFromDb;
        }finally {
            jedis.close();

        }
    }

    @Override
    public void addUserToken(String token, String memberId) {
        Jedis jedis = redisUtil.getJedis();
        String oldToken=jedis.get("user:"+memberId+":token");
        if(oldToken!=null){
            jedis.del( "user:"+memberId+":token" );
        }
        jedis.setex("user:"+memberId+":token",60*60*2,token);

        jedis.close();
    }

    @Override
    public void delToken(String memberId) {
        Jedis jedis = redisUtil.getJedis();
        jedis.del( "user:"+memberId+":token" );
        jedis.close();
    }

    @Override
    public UmsMember findUser(UmsMember umsMember) {
        Jedis jedis=null;
        try {
            jedis=redisUtil.getJedis();
            if(jedis!=null){
                String umsMemberStr = jedis.get("user:" +umsMember.getId()+":"+ umsMember.getUsername() + ":info");
                if (StringUtils.isNotBlank(umsMemberStr)) {
                    // redis有该用户信息
                    UmsMember umsMemberFromCache = JSON.parseObject(umsMemberStr, UmsMember.class);
                    return umsMemberFromCache;
                }

            }
            //链接失败
            UmsMember umsMemberByDb=userMapper.selectByPrimaryKey( umsMember );
            if(umsMemberByDb!=null){
                jedis.setex("user:" +umsMember.getId()+":"+ umsMember.getUsername() +":info",60*60*24, JSON.toJSONString(umsMemberByDb));
            }
            return umsMemberByDb;
        }finally {
            jedis.close();
        }

    }

    private UmsMember loginFromDb(UmsMember umsMember) {
        List<UmsMember> umsMembers = userMapper.select( umsMember );
        if(umsMembers!=null&&umsMembers.size()!=0){
            return umsMembers.get( 0 );
        }

        return null;

    }
}
