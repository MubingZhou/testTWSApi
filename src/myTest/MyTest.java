package myTest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.ib.client_9_72.*;
import com.ib.client_9_72.Types.Action;
import com.ib.client_9_72.Types.BarSize;
import com.ib.client_9_72.Types.DurationUnit;
import com.ib.client_9_72.Types.FundamentalType;
import com.ib.client_9_72.Types.MktDataType;
import com.ib.client_9_72.Types.Right;
import com.ib.client_9_72.Types.SecType;
import com.ib.client_9_72.Types.TimeInForce;
import com.ib.client_9_72.Types.WhatToShow;
import com.ib.contracts_9_72.*;
import com.ib.controller_9_72.*;
import com.ib.controller_9_72.ApiConnection.ILogger;
import com.ib.controller_9_72.ApiController.IAccountHandler;
import com.ib.controller_9_72.ApiController.IConnectionHandler;
import com.ib.controller_9_72.ApiController.IContractDetailsHandler;
import com.ib.controller_9_72.ApiController.IFundamentalsHandler;
import com.ib.controller_9_72.ApiController.IHistoricalDataHandler;
import com.ib.controller_9_72.ApiController.ILiveOrderHandler;
import com.ib.controller_9_72.ApiController.IOptHandler;
import com.ib.controller_9_72.ApiController.IOrderHandler;
import com.ib.controller_9_72.ApiController.IRealTimeBarHandler;
import com.ib.controller_9_72.ApiController.ITimeHandler;
import com.ib.controller_9_72.ApiController.ITopMktDataHandler;
import com.ib.controller_9_72.ApiController.TopMktDataAdapter;


public class MyTest implements IConnectionHandler{
	private final ArrayList<String> accList = new ArrayList<String>();
	public static double _strike;
	public static double _optPrice;
	public static double _delta;
	public static double _gamma;
	
	public static void main(String[] args) {
		String host = "";   // "" = the local host
		int port = 7496;
		int cliendID = 121;  // a self-specified unique client ID
		
		MyLogger inLogger = new MyLogger();
		MyLogger outLogger = new MyLogger();
		
		MyTest myTest = new MyTest();
		ApiController myController = new ApiController(myTest, inLogger, outLogger);
		myController.connect(host, port, cliendID, null);
		
		EClientSocket myClient = myController.client();    
		if(myClient.isConnected()){
			System.out.println("Is connected!");
			try {
				Thread.sleep(1000 * 3);   
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else{
			System.out.println("Not connected!");
			return;
		}
		// get the time
		//System.out.println(myClient.TwsConnectionTime());    //the time when API started to connect to TWS 
		//这里的逻辑是，其实EClientSocket有方法reqCurrentTime，调用之后，信息通过EWrapper的currentTime方法传递过来。
		//只不过这里的myController也定义了一个类似的reqCurrentTime，这个reqCurrentTime调用了EClientSocket的reqCurrentTime，而且输入参数是个ITimeHandler
		//而ITimeHandler也需要实现currentTime方法，ITimeHandler中的time，就是我们想要的time
		//过程是 EWrapper 先调用自己的 currentTime，然后自己的currentTime又调用了ITimeHandler的currentTime
		MyITimeHandler myITimeHandler = new MyITimeHandler();
		myController.reqCurrentTime( myITimeHandler);
		
			//get server version
		//System.out.println("serverVersion() = " + myController.client().serverVersion());
			//get API connection time
		//System.out.println("API connection time = " + myController.client().TwsConnectionTime());
		
		// get the market data
		int tickerID = 0;
		Contract con1 = new Contract();   // get the contract
		con1.symbol("IBM");
		con1.secType("STK");
		con1.exchange("NYSE");
		con1.currency("USD");
		
		MyITopMktDataHandler myITopMktDataHandler = new MyITopMktDataHandler();
		myController.reqTopMktData(con1, "", false, myITopMktDataHandler);
		//myController.cancelTopMktData(myITopMktDataHandler);  // cancel requiring top market data
		
		//get historical data
		String endDateTime = "20150901 23:00:00 CST";
		int duration = 1;
		DurationUnit durationUnit = DurationUnit.MONTH;
		BarSize barSize = BarSize._1_hour;
		WhatToShow whatToShow = WhatToShow.TRADES;
		boolean rthOnly = false;
		
		//MyIHistoricalDataHandler myIHistoricalDataHandler = new MyIHistoricalDataHandler();
		//myController.reqHistoricalData(con1, endDateTime, duration, durationUnit, barSize, whatToShow, rthOnly, myIHistoricalDataHandler);
		//myController.cancelHistoricalData(myIHistoricalDataHandler);   // cancel requiring historical data
		
		//place a new order
		Order order = new Order();
		order.account( myTest.accList.get(0));
		order.action(Action.BUY);
		order.totalQuantity(100);
		order.orderType(OrderType.LMT);
		order.lmtPrice(20.5);
		order.tif(TimeInForce.DAY);
		//int orderID = 112;
		//order.orderId(orderID);
		
		MyIOrderHandler myIOrderHandler = new MyIOrderHandler();
		//myController.placeOrModifyOrder(con1, order, myIOrderHandler);  //add order
		try {
			Thread.sleep(1000 * 0);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		order.lmtPrice(25.4);  
		//myController.placeOrModifyOrder(con1, order, myIOrderHandler);  // modify order
		try {
			Thread.sleep(1000 * 0);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		//myController.cancelOrder(123459);
		//myController.cancelAllOrders();    //cancel all orders
		
		//get open orders
		MyILiveOrderHandler myILiveOrderHandler = new MyILiveOrderHandler();
		//myController.reqLiveOrders(myILiveOrderHandler);
		
		//get portfolio information
		MyIAccountHandler myIAccountHandler = new MyIAccountHandler();
		//myController.reqAccountUpdates(true, myTest.accList.get(1), myIAccountHandler);
		
		//get contract details
		MyIContractDetailsHandler myIContractDetailsHandler = new MyIContractDetailsHandler();
		//myController.reqContractDetails(con1, myIContractDetailsHandler);
		
		//get real time bar
		MyIRealTimeBarHandler myIRealTimeBarHandler = new MyIRealTimeBarHandler();
		//myController.reqRealTimeBars(con1, whatToShow, rthOnly, myIRealTimeBarHandler);
		
		//get company fundamentals
		MyIFundamentalsHandler myIFundamentalsHandler = new MyIFundamentalsHandler();
		//myController.reqFundamentals(con1, FundamentalType.ReportsFinSummary, myIFundamentalsHandler);
		
		//OPtion calculation
		MyIOptHandler myIOptHandler = new MyIOptHandler();
		Contract optCon = new Contract();    // specifying the option contract
		//optCon.conid(1245);
		optCon.symbol("SPX");     // the symbol must be the underlying STK, if you use OPT symbol like VXX 150911P00028500, you don't get the result
		optCon.secType(SecType.OPT);
		optCon.right(Right.Call);
		optCon.strike(1980); 
		//optCon.currency("USD");
		//optCon.multiplier("100");
		optCon.exchange("SMART");
		optCon.lastTradeDateOrContractMonth("20150917");
		myController.reqOptionVolatility(optCon, 29.4, 1979.44, myIOptHandler);
		//myController.reqOptionComputation(optCon, 0.25, 1921.38, myIOptHandler);   
		//2015-09-08 ：使用该函数计算的price（或者volatility）和TWS上的不一样，因为TWS上的当前日期是09-04,5-8号美国休市，而使用API计算的是用本地的时间，也就8号，这个差异已经验证了。
		//System.out.println("optCon = " + optCon.description());
		
		
		// pause and disconnect
		try {   
			Thread.sleep(1000 * 10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		myController.disconnect();
		if(myClient.isConnected()){
			System.out.println("Is connected!");
		}
		else{
			System.out.println("Not connected!");
		}
		System.out.println("========================== END ==========================");
	}

	@Override
	public void connected() {
		System.out.println("MyTest.connected()");
		
	}

	@Override
	public void disconnected() {
		System.out.println("MyTest.disconnected()");
	}

	@Override
	public void accountList(ArrayList<String> list) {
		System.out.println("MyTest.accountList() list.size() = " + list.size());
		for(int i = 0; i < list.size(); i++){
			System.out.println("\ti = " + i + " list[i] = " + list.get(i));
		}
		accList.addAll(list);
	}

	@Override
	public void error(Exception e) {
		System.out.println("MyTest.error() e = " + e);
	}

	@Override
	public void message(int id, int errorCode, String errorMsg) {
		//System.out.println("MyTest.message()：id = " + id + " errorCode = " + errorCode + " errorMsg = " + errorMsg);
	}

	@Override
	public void show(String string) {
		System.out.println("MyTest.show()");
	}

	
	private static class MyLogger implements ILogger {

		@Override
		public void log(String valueOf) {
		//	System.out.println("[logger] " + valueOf);
		}
		
	}
	
	private static class MyITimeHandler implements ITimeHandler{

		@Override
		public void currentTime(long time) {
			System.out.println( "Server date/time is " + Formats.fmtDate(time * 1000) );
		}
		
	}
	
	private static class MyITopMktDataHandler extends TopMktDataAdapter{

		double m_bid;
		double m_ask;
		double m_last;
		long m_lastTime;
		int m_bidSize;
		int m_askSize;
		double m_close;
		int m_volume;
		boolean m_frozen;
		
		@Override
		public void tickPrice(TickType tickType, double price, int canAutoExecute) {
			System.out.println("\tIn tickPrice() " + tickType + " " + price);
			switch( tickType) {
				case BID:
					m_bid = price;
					break;
				case ASK:
					m_ask = price;
					break;
				case LAST:
					m_last = price;
					break;
				case CLOSE:
					m_close = price;
					break;
				default: break;	
			}
		}

		@Override
		public void tickSize(TickType tickType, int size) {
			System.out.println("\tIn tickSize() " + tickType + " " + size);
			switch( tickType) {
				case BID_SIZE:
					m_bidSize = size;
					break;
				case ASK_SIZE:
					m_askSize = size;
					break;
				case VOLUME:
					m_volume = size;
					break;
	            default: break; 
			}
		}

		@Override
		public void tickString(TickType tickType, String value) {
			System.out.println("\tIn tickString() " + tickType + " " + value);
			switch( tickType) {
				case LAST_TIMESTAMP:
					m_lastTime = Long.parseLong( value) * 1000;
					break;
	            default: break; 
			}
		}

		@Override
		public void tickSnapshotEnd() {
			System.out.println("\tIn tickSnapshotEnd()");
			
		}

		@Override
		public void marketDataType(MktDataType marketDataType) {
			System.out.println("\tIn marketDataType()");
			m_frozen = marketDataType == MktDataType.Frozen;
		}
		
	}
	
	private static class MyIHistoricalDataHandler implements IHistoricalDataHandler{

		@Override
		public void historicalData(Bar bar, boolean hasGaps) {
			SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
			//System.out.println("time = " + ft.format(new Date(bar.time() * 1000)) + " open = " + bar.open() + " high = " + bar.high() + " low = " + bar.low() + " close = " + bar.close());
		}

		@Override
		public void historicalDataEnd() {
			// TODO Auto-generated method stub
			
		}
		
	}

	private static class MyIOrderHandler implements IOrderHandler{

		@Override
		public void orderState(OrderState orderState) {
			System.out.println("\tOrderState = " + orderState);
		}

		@Override
		public void orderStatus(OrderStatus status, double filled, double remaining, double avgFillPrice, long permId,
				int parentId, double lastFillPrice, int clientId, String whyHeld) {
			System.out.println("\tOrderStatus = " + status + " filled = " + filled + " remaining = " + remaining + " avgFillPrice = " + avgFillPrice);
		}

		@Override
		public void handle(int errorCode, String errorMsg) {
			System.out.println("\tIn order handler, errorCode = " + errorCode + " errorMsg = " + errorMsg);
		}
		
	}
	
	private static class MyILiveOrderHandler implements ILiveOrderHandler{

		@Override
		public void openOrder(Contract contract, Order order, OrderState orderState) {
			System.out.println("[live order handler] openOrder: contract = " + contract.description() + " order.orderId() = " + order.orderId());
		}

		@Override
		public void openOrderEnd() {
			System.out.println("[live order handler] openOrderEnd: ");
		}

		@Override
		public void orderStatus(int orderId, OrderStatus status, double filled, double remaining, double avgFillPrice,
				long permId, int parentId, double lastFillPrice, int clientId, String whyHeld) {
			System.out.println("[live order handler] orderStatus: orderId = " + orderId + " OrderStatus = " + status + " clientId = " + clientId);
		}

		@Override
		public void handle(int orderId, int errorCode, String errorMsg) {
			System.out.println("[live order handler] handle: orderId = " + orderId + " errorCode = " + errorCode + " errorMsg = " + errorMsg);
		}
		
	}

	private static class MyIAccountHandler implements IAccountHandler{

		@Override
		public void accountValue(String account, String key, String value, String currency) {
			System.out.println("[account handler] accountValue: account = " + account + " key = " + key + " value = " + value + " currency = " + currency);
		}

		@Override
		public void accountTime(String timeStamp) {
			System.out.println("[account handler] accountTime: timeStamp = " + timeStamp);
		}

		@Override
		public void accountDownloadEnd(String account) {
			System.out.println("[account handler] accountDownloadEnd: account = " + account);
		}

		@Override
		public void updatePortfolio(Position position) {
			System.out.println("[account handler] updatePortfolio: " + position.contract().description() + " position = " + position.position() + " unreal_p&l = " + position.unrealPnl());
		}
		
	}
	private static class MyIContractDetailsHandler implements IContractDetailsHandler{

		@Override
		public void contractDetails(ArrayList<ContractDetails> list) {
			if(list != null){
				ContractDetails c = list.get(0);
				System.out.println("[contract details handler] contractDetails:length of list = " + list.size() + " contract = " + c.contract().description() + 
						" mkt name = " + c.marketName() + " valid exch = " + c.validExchanges()	);
			}
		}
		
	}
	
	private static class MyIRealTimeBarHandler implements IRealTimeBarHandler{

		@Override
		public void realtimeBar(Bar bar) {
			System.out.println("[real time bar handler] realtimeBar: time = " + bar.formattedTime() + " open = " + 
					bar.open() + " high = " + bar.high() + " low = " + bar.low() + " close = " + bar.close());
		}
		
	}

	private static class MyIFundamentalsHandler implements IFundamentalsHandler{

		@Override
		public void fundamentals(String str) {
			System.out.println("[fundamentals handler] str = "+ str);
		}
		
	}

	private static class MyIOptHandler implements IOptHandler{

		@Override
		public void tickPrice(TickType tickType, double price, int canAutoExecute) {
			System.out.println("[Option Handler] tickPrice: tickType = " + tickType + " price = " + price + " canAutoExe = " + canAutoExecute);
		}

		@Override
		public void tickSize(TickType tickType, int size) {
			System.out.println("[Option Handler] tickSize: tickType = " + tickType + " size = " + size); 
		}

		@Override
		public void tickString(TickType tickType, String value) {
			System.out.println("[Option Handler] tickString: tickType = " + tickType + " value = " + value);
		}

		@Override
		public void tickSnapshotEnd() {
			System.out.println("[Option Handler] tickSnapshotEnd");
		}

		@Override
		public void marketDataType(MktDataType marketDataType) {
			System.out.println("[Option Handler] marketDataType: marketDataType = " + marketDataType);
		}

		@Override
		public void tickOptionComputation(TickType tickType, double impliedVol, double delta, double optPrice,
				double pvDividend, double gamma, double vega, double theta, double undPrice) {
			System.out.println("[Option Handler] tickOptionComputation: tickType = " + tickType + " impliedVol = " + impliedVol + " delta = " + delta + " optPrice = " + optPrice +
					" pvDividend = " + pvDividend + " gamma = " + gamma + " vega = " + vega + " theta = " + theta + " undPrice = " + undPrice);
		_optPrice = optPrice;
		
		}
		
	}
	
	public static class MyEWrapper implements EWrapper{

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

		@Override
		public void currentTime(long time) {
			System.out.println("[MyEWrapper] currentTime: " +  Formats.fmtDate(time * 1000));
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
			// TODO Auto-generated method stub
			
		}

		@Override
		public void error(String str) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void error(int id, int errorCode, String errorMsg) {
			// TODO Auto-generated method stub
			
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
}



