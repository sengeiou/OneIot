package com.coband.common.network;

/**
 * Created by ivan on 3/9/18.
 */

public class ResponseCode {

    public static final int SUCCESS = 0;
    public static final int ErrNoPermission = 1000; // 操作无权限
    public static final int ErrTokeExpired = 1001; // JWT 过期了
    public static final int ErrUnauthorized = 1002;  // 授权失败，未授权成功
    public static final int ErrCreateJWTFailed = 1003;  // 创建 JWT 失败
    public static final int ErrRegisterFailed = 1004;  // 注册失败
    public static final int ErrLoginFailed = 1005;  // 登录失败
    public static final int ErrEmailAddrNeedVerify = 1006;   // 邮件地址未验证，需提示用户检查邮箱
    public static final int ErrWrongAccountOrPassword = 1007;    // 密码或者账号输入错误
    public static final int ErrAccountInvalid = 1008;    // 无效的账号
    public static final int ErrUpdateFailed = 1009;   // 更新用户信息失败
    public static final int ErrAccountHasBeenUsed = 1010;   // 此账号已经被占用，手机或者邮箱已经注册
    public static final int ErrEmailAddrVerifyFailed = 1011;  // 验证邮件的时候，参数错误或者链接已过期，并允许用户重新发送验证邮件。
    public static final int ErrDeviceNotFound = 3000; // 没找到此设备
    public static final int ErrBindFailed = 3001; // 绑定失败
    public static final int ErrUnBindFailed = 3002;// 解绑定失败
    public static final int ErrProductNotFound = 3003;// 无此设备的产品信息
    public static final int ErrDBAccessFailed = 4000; // 数据库访问错误
    public static final int ErrNotFound = 4001; // 数据库里面没找到
    public static final int ErrHasBeenExist = 4002;// 插入的时候发现数据已经存在
    public static final int ErrDBSearchFailed = 4003; // 搜索查询数据库失败
    public static final int ErrWrongParam = 5000; // API 参数错误
    public static final int ErrParseFailed = 5001; // 参数解析错误
    public static final int ErrSendSMSCodeFailed = 6000;
    public static final int ErrSendEmailFailed = 6001;
    public static final int ErrPushToAndroidFailed = 6002;
    public static final int ErrPushToIOSFailed = 6003;
    public static final int ErrVerifyCodeWrongOrExpired = 6004;
    public static final int ErrNotSupport = 9000;  // 此功能暂时不支持
}
