package com.ib.client_9_72;

import java.io.IOException;

public interface ETransport {
	void send(EMessage msg) throws IOException;
}
