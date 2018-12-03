package com.wbd.member.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.wbd.common.pojo.UserEntity;

@Mapper
public interface MemberDao {
	/**
	 * 根据用户id查询
	 * <p>Title: findByID</p>  
	 * <p>Description: </p>  
	 * @param userId
	 * @return
	 */
	@Select("select  id,username,password,phone,email,created,updated,openid from mb_user where id =#{userId}")
	UserEntity findByID(@Param("userId") Long userId);

	/**
	 * 插入
	 * <p>Title: insertUser</p>  
	 * <p>Description: </p>  
	 * @param userEntity
	 * @return
	 */
	@Insert("INSERT  INTO `mb_user`  (username,password,phone,email,created,updated) VALUES (#{username}, #{password},#{phone},#{email},#{created},#{updated});")
	Integer insertUser(UserEntity userEntity);
	
	/**
	 * 根据用户名和密码查询信息
	 * <p>Title: login</p>  
	 * <p>Description: </p>  
	 * @param username
	 * @param password
	 * @return
	 */
	@Select("select  id,username,password,phone,email,created,updated,openid from mb_user where username=#{username} and password=#{password}")
	UserEntity login(@Param("username") String username, @Param("password") String password);
	/**
	 * 根据openid查询用户信息
	 * <p>Title: findByOpenIdUser</p>  
	 * <p>Description: </p>  
	 * @param openid
	 * @return
	 */
	@Select("select  id,username,password,phone,email,created,updated ,openid from mb_user where openid =#{openid}")
	UserEntity findByOpenIdUser(@Param("openid") String openid);
	
	/**
	 * 根据用户id更新openid
	 * <p>Title: updateByOpenIdUser</p>  
	 * <p>Description: </p>  
	 * @param openid
	 * @param userId
	 * @return
	 */
	@Update("update mb_user set openid=#{openid} where id=#{userId}")
    Integer updateByOpenIdUser(@Param("openid") String openid,@Param("userId") Integer userId);
}
