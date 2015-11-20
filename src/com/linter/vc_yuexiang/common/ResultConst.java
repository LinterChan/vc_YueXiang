package com.linter.vc_yuexiang.common;

/**
 * 服务器端返回结果的常量类
 * 
 * @author LinterChen linterchen@vanchu.net
 * @date 2015-11-16
 */
public class ResultConst {
	/** 注册：该邮箱不存在 */
	public static final int REG_EMAIL_NOT_EXIST = 0;
	/** 注册：用户已存在 */
	public static final int REG_USER_EXIST = 1;
	/** 注册：注册成功 */
	public static final int REG_SUCCESS = 2;
	/** 注册：注册失败 */
	public static final int REG_FAIL = 3;

	/** 登录：用户不存在 */
	public static final int LOGIN_USER_NOT_EXIST = 4;
	/** 登录：密码错误 */
	public static final int LOGIN_PASSWORD_WORRY = 5;
	/** 登录：登录成功 */
	public static final int LOGIN_SUCCESS = 6;
	/** 登录：登录失败 */
	public static final int LOGIN_FAIL = 7;

	/** 判断注册信息正确性：邮箱格式错误 */
	public static final int REG_EMAIL_NOT_CORRECT = 8;
	/** 判断注册信息正确性：用户名为空 */
	public static final int REG_USER_NULL = 9;
	/** 判断注册信息正确性：密码为空 */
	public static final int REG_PASSWORD_NULL = 10;
	
	/** 判断登录信息正确性：邮箱格式错误 */
	public static final int LOGIN_EMAIL_NOT_CORRECT = 11;
	/** 判断登录信息正确性：用户名为空 */
	public static final int LOGIN_USER_NULL = 12;
	/** 判断登录信息正确性：密码为空 */
	public static final int LOGIN_PASSWORD_NULL = 13;
}
