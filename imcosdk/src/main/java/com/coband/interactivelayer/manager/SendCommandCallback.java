package com.coband.interactivelayer.manager;

/**
 * Created by mai on 17-7-18.
 */

public interface SendCommandCallback {
    /**
     * Whether the command was sent successfully
     *
     * @param status true is successfully, false is failed
     */
    void onCommandSend(boolean status);

    /**
     * Whether the bracelet is connected when the command is sent
     *
     */
    void onDisconnected();

    /**
     * Error when sending command
     *
     * @param error error code
     */
    void onError(Throwable error);
}
