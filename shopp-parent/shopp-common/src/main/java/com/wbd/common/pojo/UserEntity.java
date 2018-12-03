package com.wbd.common.pojo;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter

public class UserEntity implements Serializable{

	/** serialVersionUID*/ 
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String username;
	private String password;
	private String phone;
	private String email;
	private Date created;
	private Date updated;
	private String openid;

}
