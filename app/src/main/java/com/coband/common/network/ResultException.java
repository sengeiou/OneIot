package com.coband.common.network;

import java.io.IOException;

/**
 * Created by tgc on 17-6-2.
 */

public class ResultException extends IOException {
    //账号不存在
    public final int NO_USER = 211;
    //密码错误
    public final int PASSWORD_ERROR = 210;

    private int errCode = 0;

    public ResultException(int errCode) {
        super();
        this.errCode = errCode;
    }

    public int getErrCode() {
        return errCode;
    }

}
