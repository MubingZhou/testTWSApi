package newTest;

import java.util.ArrayList;

import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.controller.ApiConnection.ILogger;
import com.ib.controller.ApiController;
import com.ib.controller.ApiController.IConnectionHandler;
import com.ib.controller.ApiController.IOptHandler;
import com.ib.controller.ApiController.IOrderHandler;
import com.ib.controller.ApiController.ITimeHandler;
import com.ib.controller.ApiController.ITopMktDataHandler;
import com.ib.controller.Formats;
import com.ib.controller.NewContract;
import com.ib.controller.NewOrder;
import com.ib.controller.NewOrderState;
import com.ib.controller.NewTickType;
import com.ib.controller.OrderStatus;
import com.ib.controller.OrderType;
import com.ib.controller.Types.Action;
import com.ib.controller.Types.MktDataType;
import com.ib.controller.Types.Right;
import com.ib.controller.Types.SecType;
import com.ib.controller.Types.TimeInForce;

// EWrapper获得数据的逻辑：EClientSocket是客户端的socket，通过eConnect函数，建立一个socket，然后再创建一个dataInputStream用来接收server给出的信息，而
// 这个dataInputStream就是EReader，在EReader中，程序对信息进行一系列处理，然后调用EWrapper中的相应函数进行输出，所调用的函数的参数就是server
// 返回来的数据。
// EClientSocket发送数据的逻辑：这个很简单，就是EClientSocket中有个DataOutputStream，只需调用它的write这个函数就好了
public class NewTest {
	private static ArrayList<String> _accList = new ArrayList<String>();
	
	public static void main(String[] args) {
		String host = "";   // "" = the local host
		int port = 7496;
		int clientId = 101;  // a self-specified unique client ID
		
		//configure contract
		NewContract newCon1 = new NewContract();
		newCon1.symbol("USD");
		newCon1.secType(SecType.CASH);
		newCon1.exchange("IDEALPRO");
		newCon1.currency("JPY");
	
		// specify the order
		Order order = new Order();  //old order
		//order.account( _accList.get(0));
		order.m_action = "BUY";
		order.m_totalQuantity = 100;
		order.m_orderType = "LMT";
		order.m_lmtPrice = 17.2;
		order.m_tif = "DAY";
		//order.m_orderId = (int) Math.floor(Math.random() * 10000);
		
		// specifying the option contract
		NewContract optCon = new NewContract();    
		//optCon.conid(1245);
		optCon.symbol("BANKNIFTY");     // the symbol must be the underlying STK, if you use OPT symbol like VXX 150911P00028500, you don't get the result
		optCon.secType(SecType.OPT);
		optCon.right(Right.Call);
		optCon.strike(16800); 
		optCon.currency("INR");
		optCon.multiplier("1");
		optCon.exchange("NSE");
		optCon.expiry("20150924");
		
		NewContract optCon2 = new NewContract();    
		//optCon.conid(1245);
		optCon2.symbol("K200");     // the symbol must be the underlying STK, if you use OPT symbol like VXX 150911P00028500, you don't get the result
		optCon2.secType(SecType.OPT);
		optCon2.right(Right.Call);
		optCon2.strike(232.5); 
		optCon2.currency("KRW");
		optCon2.multiplier("500000");
		optCon2.exchange("KSE");
		optCon2.expiry("20151008");
		double optPrice = (double) 4.47;   
		double underPrice = (double) 232.79;
		
		boolean useApiController = false;
		
		if(useApiController){
			MyIConnectionHandler myIConnectionHandler = new MyIConnectionHandler();
			MyLogger inLogger = new MyLogger();
			MyLogger outLogger = new MyLogger();
			
			ApiController myController = new ApiController(myIConnectionHandler, inLogger, outLogger);
			myController.connect(host, port, clientId);
			
			if(myController.getClient().isConnected()){
				System.out.println("Connected!");
			}
			else{
				System.out.println("Not Connected!");
				return;
			}
			
			try{
				Thread.sleep(1000 * 1);
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			//get server time
			MyITimeHandler myITimeHandler = new MyITimeHandler();
			myController.reqCurrentTime(myITimeHandler);
			
			//get mkt data
				//get the data
			MyITopMktDataHandler myITopMktDataHandler = new MyITopMktDataHandler();
			//myController.reqTopMktData(newCon1, "", false, myITopMktDataHandler);
			
			// place order			
			NewOrder newOrder = new NewOrder();
			newOrder.account(_accList.get(0));
			newOrder.action(Action.BUY);
			newOrder.totalQuantity(100);
			newOrder.orderType(OrderType.LMT);
			newOrder.lmtPrice(15.30);
			newOrder.tif(TimeInForce.DAY);
			
			MyIOrderHandler myIOrderHandler = new MyIOrderHandler();
			//myController.placeOrModifyOrder(newCon1, newOrder, myIOrderHandler);
			
			
			//get implied volatility
			MyIOptHandler myIOptHandler = new MyIOptHandler();
			myController.reqOptionVolatility(optCon, optPrice, underPrice, myIOptHandler);

			
			//pause and disconnect
			try {   
				Thread.sleep(1000 * 10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			myController.disconnect();
			if(myController.getClient().isConnected()){
				System.out.println("Still Connected!");
			}
			else{
				System.out.println("DisConnected!");
				//return;
			}
		}
		else{	// not using Api Controller
			MyEWrapper myEWrapper = new MyEWrapper();
			EClientSocket myEClientSocket = new EClientSocket(myEWrapper);
			
			myEClientSocket.eConnect(host, port, clientId);
			if(myEClientSocket.isConnected()){
				System.out.println("Connected!");
			}
			else{
				System.out.println("Not Connected!");
				return;
			}
			try{
				Thread.sleep(1000 * 1);
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			_accList = myEWrapper._accList;
			
			// req time
			myEClientSocket.reqCurrentTime();
			
			// req mkt top data
			boolean snapShot = true;
			//myEClientSocket.reqMktData(0, optCon.getContract(), "", snapShot, null);
			
			Contract con2 = new Contract();
			con2.m_symbol = "K200";
			con2.m_secType = "IND";
			con2.m_exchange = "KSE";
			con2.m_currency = "KRW";
			con2.m_expiry = "20151210";
			myEClientSocket.reqMktData(0, con2, "", snapShot, null);
			
			// place order
			Contract con1 = new Contract();
			con1.m_symbol = "IBM";
			con1.m_secType = "STK";
			con1.m_exchange = "NYSE";
			con1.m_currency = "USD";
			
			order.m_account = myEWrapper._accList.get(0);
			//myEClientSocket.placeOrder(myEWrapper._nextValidId, con1, order);
			//System.out.println("orderId = " + order.m_orderId);
			
			// get imp vol
			if(false){
				myEClientSocket.calculateImpliedVolatility(0, optCon2.getContract(), optPrice, underPrice);
				int i = 0;
				while(myEWrapper._calculatedImpVol <= 0){
					try {   
						Thread.sleep((int) (1000 * 0.001));
						i++;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println("myEWrapper._calculatedImpVol = " + myEWrapper._calculatedImpVol + " i = " + i);
				myEClientSocket.calculateOptionPrice(0, optCon2.getContract(), myEWrapper._calculatedImpVol, underPrice);
				
			}
			
			// req account info
			myEClientSocket.reqAccountUpdates(true, _accList.get(0));
			
			//pause and disconnect
			try {   
				Thread.sleep(1000 * 5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			myEClientSocket.eDisconnect();
			if(myEClientSocket.isConnected()){
				System.out.println("Still Connected!");
			}
			else{
				System.out.println("DisConnected!");
				//return;
			}
		}
		
		
		System.out.println("========================== END ==========================");
	}
	
	private static class MyIConnectionHandler implements IConnectionHandler{

		@Override
		public void connected() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void disconnected() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void accountList(ArrayList<String> list) {
			_accList = list;
			if( list != null){
				for(int i = 0; i < list.size(); i++){
					System.out.println("account[" + i + "] = " + list.get(i));
				}
			}
		}

		@Override
		public void error(Exception e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void message(int id, int errorCode, String errorMsg) {
			System.out.println("\n[IConnectionHandler] message: id = " + id + " errorCode = " + errorCode + " errorMsg = "+ errorMsg);
		}

		@Override
		public void show(String string) {
			// TODO Auto-generated method stub
			
		}
		
	}

	private static class MyLogger implements ILogger{

		@Override
		public void log(String valueOf) {
			System.out.print(valueOf);
		}
		
	}

	private static class MyITimeHandler implements ITimeHandler{

		@Override
		public void currentTime(long time) {
			System.out.println("\n[time handler] currentTime = " + Formats.fmtDate(time * 1000));
		}
		
	}
	
	private static class MyITopMktDataHandler implements ITopMktDataHandler{

		@Override
		public void tickPrice(NewTickType tickType, double price, int canAutoExecute) {
			System.out.println("\n[mkt data handler] tickPrice: tickType = " + tickType + " price = " + price);
		}

		@Override
		public void tickSize(NewTickType tickType, int size) {
			System.out.println("\n[mkt data handler] tickSize: tickType = " + tickType + " size = " + size);
		}

		@Override
		public void tickString(NewTickType tickType, String value) {
			System.out.println("\n[mkt data handler] tickString: tickType = " + tickType + " value = " + value);
		}

		@Override
		public void tickSnapshotEnd() {
			System.out.println("\n[mkt data handler] tickSnapShotEnd");
		}

		@Override
		public void marketDataType(MktDataType marketDataType) {
			System.out.println("\n[mkt data handler] marketDataType = " + marketDataType);
		}
		
	}
	
	private static class MyIOptHandler extends MyITopMktDataHandler implements IOptHandler {

		@Override
		public void tickOptionComputation(NewTickType tickType, double impliedVol, double delta, double optPrice,
				double pvDividend, double gamma, double vega, double theta, double undPrice) {
			System.out.println("\n[opt data handler] tickOptionComputation: tickType = " + tickType + " impliedVol = " + impliedVol + " delta = " + delta + " optPrice = " + optPrice +
					" pvDividend = " + pvDividend + " gamma = " + gamma + " vega = " + vega + " theta = " + theta + " undPrice = " + undPrice);
		}
		
	}
	
	private static class MyIOrderHandler implements IOrderHandler{

		@Override
		public void orderState(NewOrderState orderState) {
			System.out.println("\n[order handler] orderState: orderState = " + orderState);
		}

		@Override
		public void orderStatus(OrderStatus status, int filled, int remaining, double avgFillPrice, long permId,
				int parentId, double lastFillPrice, int clientId, String whyHeld) {
			System.out.println("\n[order handler] orderStatus: status = " + status);
		}

		@Override
		public void handle(int errorCode, String errorMsg) {
			System.out.println("\n[order handler] handle: errorCode = " + errorCode + " errorMsg = " + errorMsg);
		}

		
	}
}
