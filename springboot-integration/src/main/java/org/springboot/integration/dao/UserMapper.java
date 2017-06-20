package org.springboot.integration.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springboot.integration.entity.UserEntity;
import org.springboot.integration.vo.UserVO;

/**
 * Created by Administrator on 2017/6/17.
 */

@Mapper
public interface UserMapper {


    //由于只有userName与对应数据库里面的字段（user_name）名不一致，需要特别指示
    @Select("select * from t_user where uid=${uid}")
    @Results({
            //@Result(property = "id", column = "id"),
           // @Result(property = "provinceId", column = "province_id"),
          //  @Result(property = "cityName", column = "city_name"),
            @Result(property = "userName", column = "user_name"),
    })
    //用@Param指定查询语句中的参数，对应方法的那个参数。
    UserEntity getUserById(@Param("uid") String uid) ;


    @Select("select * from t_user")
    @Results({
            @Result(property = "userName", column = "user_name")
    })
    List<UserVO> findAll();


    int addUser(UserEntity entity);


    @Delete("delete from t_user where uid=#{uid}")
    void deleteUser(@Param("uid") Long uid);



    int addUsers(List<UserVO> users);

    int updateUserByUid(UserEntity entity);
}
