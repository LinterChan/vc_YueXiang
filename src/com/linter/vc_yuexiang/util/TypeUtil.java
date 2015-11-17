package com.linter.vc_yuexiang.util;

/**
 * 服务器端返回结果的工具类
 * 
 * @author LinterChen linterchen@vanchu.net
 * @date 2015-11-16
 */
public class TypeUtil {
	/**
	 * 注册：用户已存在
	 */
	public static final int REG_USER_EXIST = 0;
	/**
	 * 注册：注册成功
	 */
	public static final int REG_SUCCESS = 1;
	/**
	 * 注册：注册失败
	 */
	public static final int REG_FAIL = 2;
	
	/**
	 * 登录：用户不存在
	 */
	public static final int LOGIN_USER_NOT_EXIST = 3;
	/**
	 * 登录：密码错误
	 */
	public static final int LOGIN_PASSWORD_WORRY = 4;
	/**
	 * 登录：登录成功
	 */
	public static final int LOGIN_SUCCESS = 5;
	/**
	 * 登录：登录失败
	 */
	public static final int LOGIN_FAIL = 6;
}
