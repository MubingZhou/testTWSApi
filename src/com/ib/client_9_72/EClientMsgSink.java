package com.ib.client_9_72;

interface EClientMsgSink {
	void serverVersion(int version, String time);
	void redirect(String host);
}
