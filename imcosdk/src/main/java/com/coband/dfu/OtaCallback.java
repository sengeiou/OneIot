package com.coband.dfu;


public abstract class OtaCallback {
	/** Callback indicating when OtaProxy client has connected/disconnected to/from a remote
     * OtaProxy server.
     *
     * @param status the service connection state, true/false
     * @param dfu 	 OtaProxy object
     */
	public void onServiceConnectionStateChange(boolean status, OtaProxy dfu) {
    }
	
	/** Callback indicating when OTA process have some error.
     *
     * @param e	 	the error code
     */
	public void onError(int e) {
    }

	/**  Callback indicating when OTA process success.
    *
    * @param s	 	the success code : 1 is firmware update success , 0 is firmware not need update
    */
	public void onSuccess(int s) {
	}
	
	/** Callback indicating when OTA process state changed.
    *
    * @param state	 	the current state
    * {@link OtaProxy.ProcessState#STA_ORIGIN}
    * {@link OtaProxy.ProcessState#STA_REMOTE_ENTER_OTA}
    * {@link OtaProxy.ProcessState#STA_FIND_OTA_REMOTE}
    * {@link OtaProxy.ProcessState#STA_CONNECT_OTA_REMOTE}
    * {@link OtaProxy.ProcessState#STA_START_OTA_PROCESS}
    * {@link OtaProxy.ProcessState#STA_OTA_UPGRADE_SUCCESS}
    */
	public void onProcessStateChanged(@OtaProxy.ProcessState int state) {
	}
	
	/** Callback indicating when OTA download image progress changed.
    *
    * @param progress	 	the success code
    */
	public void onProgressChanged(int progress) {
	}
}
