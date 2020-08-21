package com.myblog.lyl.demo.mapper;

import com.myblog.lyl.demo.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @program: demo
 * @Date: 2020/8/21 15:48
 * @Author: Yuling Li
 * @Description:
 */
@Repository
@Mapper
public interface UserMapper {
    @Insert("INSERT INTO user(name,account_id,token,gmt_create,gmt_modified) values(#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified})")
    void insert(User user);
}
