package myTest4;

import com.ib.client_9_72.CommissionReport;
import com.ib.client_9_72.Contract;
import com.ib.client_9_72.ContractDetails;
import com.ib.client_9_72.DeltaNeutralContract;
import com.ib.client_9_72.EClientSocket;
import com.ib.client_9_72.EJavaSignal;
import com.ib.client_9_72.EWrapper;
import com.ib.client_9_72.Execution;
import com.ib.client_9_72.Order;
import com.ib.client_9_72.OrderState;
import com.ib.controller_9_72.Formats;


public class MyTest4 implements EWrapper {
	//public static TimeHandler _timeHandler;
	public static EClientSocket _client;
	
	public static void main(String[] args) {
		String host = "";   // "" = the local host
		int port = 7496;
		int cliendID = 121;  // a self-specified unique client ID
		
		System.out.println("\n=== Not using APIController ===");
		EJavaSignal mySignal = new EJavaSignal();
		MyTest4 myEWrapper = new MyTest4(mySignal);
		EClientSocket myEClientSocket = new EClientSocket(myEWrapper, mySignal);
		
		
		myEClientSocket.eConnect(host, port, cliendID + 1);
		if(myEClientSocket.isConnected()){
			System.out.println("myEClientSocket connected!");
		}
		else{
			System.out.println("myEClientSocket not connected! -> return");
			return;
		}
		
		myEClientSocket.reqCurrentTime();
		
		// pause and disconnect
		try{
			Thread.sleep(1000 * 3);
		}
		catch(Exception e){
			
		}
		myEClientSocket.eDisconnect();
		if(myEClientSocket.isConnected()){
			System.out.println("myEClientSocket connected!");
		}
		else{
			System.out.println("myEClientSocket not connected!\n=== END of not using APIController ===");
		}
		
	}
	
	public MyTest4(EJavaSignal esignal){
		_client = new EClientSocket(this, esignal);
	}

	@Override
	public void tickPrice(int tickerId, int field, double price, int canAutoExecute) {
		System.out.println("[MyEWrapper] tickPrice: tickerId = " + tickerId + " field = " + field + " price = " + price);
	}

	@Override
	public void tickSize(int tickerId, int field, int size) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tickOptionComputation(int tickerId, int field, double impliedVol, double delta, double optPrice,
			double pvDividend, double gamma, double vega, double theta, double undPrice) {
		System.out.println("[MyEWrapper] tickOptionComputation:  tickerId = " + tickerId + " field = " + field + " impliedVol = " + impliedVol + " delta = " + delta + " optPrice = " + optPrice +
				" pvDividend = " + pvDividend + " gamma = " + gamma + " vega = " + vega + " theta = " + theta + " undPrice = " + undPrice);
	}

	@Override
	public void tickGeneric(int tickerId, int tickType, double value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tickString(int tickerId, int tickType, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tickEFP(int tickerId, int tickType, double basisPoints, String formattedBasisPoints,
			double impliedFuture, int holdDays, String futureLastTradeDate, double dividendImpact,
			double dividendsToLastTradeDate) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void orderStatus(int orderId, String status, double filled, double remaining, double avgFillPrice,
			int permId, int parentId, double lastFillPrice, int clientId, String whyHeld) {
		System.out.println("[MyEWrapper] orderStatus: orderId = " + orderId + " status = " + status + " clientId = " + clientId);
	}

	@Override
	public void openOrder(int orderId, Contract contract, Order order, OrderState orderState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void openOrderEnd() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateAccountValue(String key, String value, String currency, String accountName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updatePortfolio(Contract contract, double position, double marketPrice, double marketValue,
			double averageCost, double unrealizedPNL, double realizedPNL, String accountName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateAccountTime(String timeStamp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void accountDownloadEnd(String accountName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nextValidId(int orderId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contractDetails(int reqId, ContractDetails contractDetails) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void bondContractDetails(int reqId, ContractDetails contractDetails) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contractDetailsEnd(int reqId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void execDetails(int reqId, Contract contract, Execution execution) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void execDetailsEnd(int reqId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateMktDepth(int tickerId, int position, int operation, int side, double price, int size) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateMktDepthL2(int tickerId, int position, String marketMaker, int operation, int side,
			double price, int size) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateNewsBulletin(int msgId, int msgType, String message, String origExchange) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void managedAccounts(String accountsList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receiveFA(int faDataType, String xml) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void historicalData(int reqId, String date, double open, double high, double low, double close,
			int volume, int count, double WAP, boolean hasGaps) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void scannerParameters(String xml) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void scannerData(int reqId, int rank, ContractDetails contractDetails, String distance, String benchmark,
			String projection, String legsStr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void scannerDataEnd(int reqId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void realtimeBar(int reqId, long time, double open, double high, double low, double close, long volume,
			double wap, int count) {
		// TODO Auto-generated method stub
		
	}

	// -------------------- time ------------------
	@Override
	public void currentTime(long time) {
		System.out.println("[MyEWrapper] currentTime: " +  Formats.fmtDate(time * 1000));
	}
	
	public void currentTimeUsingHandler(){
		//this._timeHandler = _timeHandler;
		_client.reqCurrentTime();
		
	}

	@Override
	public void fundamentalData(int reqId, String data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deltaNeutralValidation(int reqId, DeltaNeutralContract underComp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tickSnapshotEnd(int reqId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void marketDataType(int reqId, int marketDataType) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void commissionReport(CommissionReport commissionReport) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void position(String account, Contract contract, double pos, double avgCost) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void positionEnd() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void accountSummary(int reqId, String account, String tag, String value, String currency) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void accountSummaryEnd(int reqId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void verifyMessageAPI(String apiData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void verifyCompleted(boolean isSuccessful, String errorText) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void verifyAndAuthMessageAPI(String apiData, String xyzChallange) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void verifyAndAuthCompleted(boolean isSuccessful, String errorText) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayGroupList(int reqId, String groups) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayGroupUpdated(int reqId, String contractInfo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void error(Exception e) {
		System.out.println("[MyEWrapper] error: e = " + e);
	}

	@Override
	public void error(String str) {
		System.out.println("[MyEWrapper] error: str = " + str);
	}

	@Override
	public void error(int id, int errorCode, String errorMsg) {
		System.out.println("[MyEWrapper] error: errorCode = " + errorCode + " errorMsg = " + errorMsg);
	}

	@Override
	public void connectionClosed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void connectAck() {
		// TODO Auto-generated method stub
		
	}
}
