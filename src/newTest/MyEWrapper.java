package newTest;

import java.util.ArrayList;
import java.util.Collections;

import com.ib.client.CommissionReport;
import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.client.EWrapper;
import com.ib.client.Execution;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.client.TickType;
import com.ib.client.UnderComp;
import com.ib.controller.Formats;
import com.ib.controller.NewComboLeg;
import com.ib.controller.Types.Right;

public class MyEWrapper implements EWrapper{
	public static final double _noDataIndicator_double = -999;
	public static final String _noDataIndicator_str = "NoData";
	
	public long time = (int) _noDataIndicator_double;
	public int _nextValidId = (int) _noDataIndicator_double;
	public ArrayList<String> _accList = new ArrayList<String>();
	public double _calculatedImpVol;
	public double _calculatedDelta;
	public double _calculatedGamma;
	public double _calculatedTheta;
	
	public double _askPrice = _noDataIndicator_double;
	public double _bidPrice = _noDataIndicator_double;
	public double _lastPrice = _noDataIndicator_double;
	public double _askSize = _noDataIndicator_double;
	public double _bidSize = _noDataIndicator_double;
	public double _highPrice = _noDataIndicator_double;
	public double _lowPrice = _noDataIndicator_double;
	public double _closePrice = _noDataIndicator_double;
	public double _volume = _noDataIndicator_double;
	public double _lastSize = _noDataIndicator_double;
	
	public ArrayList<ContractDetails> _ContracDetailsListCall = new ArrayList<ContractDetails>();
	public ArrayList<ContractDetails> _ContracDetailsListPut = new ArrayList<ContractDetails>();
	public boolean _getContracDetailsEnd = false;
	
	public String _orderStatus = _noDataIndicator_str;
	public double _orderRemainingAmt = _noDataIndicator_double;
	public int _reqOrderId = (int) _noDataIndicator_double;
	public ArrayList<Integer> _reqOrderIdList = new ArrayList<Integer>();
	public ArrayList<String> _reqOrderStatusList = new ArrayList<String>();
	public ArrayList<Order> _reqOpenOrderList = new ArrayList<Order>();
	public ArrayList<OrderStatus> _orderStatusList = new ArrayList<OrderStatus>();
	
	public ArrayList<PortfolioRecord> _reqPortfolio = new ArrayList<PortfolioRecord>();
	public boolean _reqaccountDownloadEnd = false;
	
	public void clearPrice(){
		_askPrice = _noDataIndicator_double;
		_bidPrice = _noDataIndicator_double;
		_lastPrice = _noDataIndicator_double;
		_askSize = _noDataIndicator_double;
		_bidSize = _noDataIndicator_double;
		_highPrice = _noDataIndicator_double;
		_lowPrice = _noDataIndicator_double;
		_closePrice = _noDataIndicator_double;
		_volume = _noDataIndicator_double;
		_lastSize = _noDataIndicator_double;
		
		_calculatedImpVol = _noDataIndicator_double;
		_calculatedDelta = _noDataIndicator_double;
		_calculatedGamma = _noDataIndicator_double;
		_calculatedTheta = _noDataIndicator_double;
	}
	
	public void clearOrderIdData(){
		_nextValidId = (int) _noDataIndicator_double;
	}
	
	public void clearOrderData(){
		_orderStatus = _noDataIndicator_str;
		_orderRemainingAmt = _noDataIndicator_double;
		_reqOrderId = (int) _noDataIndicator_double;
		_reqOrderIdList = null;	_reqOrderIdList = new ArrayList<Integer>();
		_reqOrderStatusList = null;	_reqOrderStatusList = new ArrayList<String>();
		_reqOpenOrderList = null; _reqOpenOrderList = new ArrayList<Order>();
		_orderStatusList = null; _orderStatusList = new ArrayList<OrderStatus>();
		
	}
	
	public void clearPorfolioRecordData(){
		_reqPortfolio = null;	_reqPortfolio = new ArrayList<PortfolioRecord>();
		_reqaccountDownloadEnd = false;
	}
	
	@Override
	public void error(Exception e) {
		e.printStackTrace();
	}

	@Override
	public void error(String str) {
		System.out.println("[MyEWrapper] error: str = " + str);
	}

	@Override
	public void error(int id, int errorCode, String errorMsg) {
		if(errorCode != 2104 && errorCode != 2106){
			System.out.println("[MyEWrapper] error: errorCode = " + errorCode + " errorMsg = " + errorMsg);
		}
	}

	@Override
	public void connectionClosed() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void tickPrice(int tickerId, int field, double price, int canAutoExecute) {
		System.out.println("[MyEWrapper] tickPrice: field = " + TickType.getField(field) + " price = " + price);
		switch(field){
		case TickType.BID:
			_bidPrice = price;
			break;
		case TickType.ASK:
			_askPrice = price;
			break;
		case TickType.LAST:
			_lastPrice = price;
			break;
		case TickType.HIGH:
			_highPrice = price;
			break;
		case TickType.LOW:
			_lowPrice = price;
			break;
		case TickType.CLOSE:
			_closePrice = price;
			break;
		default:
			System.out.println("[MyEWrapper] tickPrice: Unknown tick type! + type = "+ field);	
			break;
		}
	}

	@Override
	public void tickSize(int tickerId, int field, int size) {
		//System.out.println("[MyEWrapper] tickSize: field = " + TickType.getField(field) + " size = " + size);
		switch(field){
		case TickType.ASK_SIZE:
			_askSize = size;
			break;
		case TickType.BID_SIZE:
			_bidSize = size;
			break;
		case TickType.LAST_SIZE:
			_lastSize = size;
			break;
		case TickType.VOLUME:
			_volume = size;
			break;
		default:
			System.out.println("[MyEWrapper] tickSize: Unknown tick type!");
			break;
		}
	}

	@Override
	public void tickOptionComputation(int tickerId, int field, double impliedVol, double delta, double optPrice,
			double pvDividend, double gamma, double vega, double theta, double undPrice) {
	//	System.out.println("[MyEWrapper] tickOptionComputation: tickerId = " + tickerId + " impliedVol = " + impliedVol + " delta = " + delta + " optPrice = " + optPrice +
	//			" pvDividend = " + pvDividend + " gamma = " + gamma + " vega = " + vega + " theta = " + theta + " undPrice = " + undPrice);
		
		_calculatedImpVol = impliedVol;
		_calculatedDelta = delta;
		_calculatedGamma = gamma;
		_calculatedTheta = theta;
	}

	@Override
	public void tickGeneric(int tickerId, int tickType, double value) {
	//	System.out.println("[MyEWrapper] tickGeneric: tickType = " + TickType.getField(tickType) + " value = " + value);
	}

	@Override
	public void tickString(int tickerId, int tickType, String value) {
	//	System.out.println("[MyEWrapper] tickString: tickType = " + TickType.getField(tickType) + " value = " + value);
	}

	@Override
	public void tickEFP(int tickerId, int tickType, double basisPoints, String formattedBasisPoints,
			double impliedFuture, int holdDays, String futureExpiry, double dividendImpact, double dividendsToExpiry) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void orderStatus(int orderId, String status, int filled, int remaining, double avgFillPrice, int permId,
			int parentId, double lastFillPrice, int clientId, String whyHeld) {
		System.out.println("[MyEWrapper] orderStatus: orderId = " + orderId + " status = " + status);
		_orderStatus = status.trim();
		_orderRemainingAmt = remaining;
		_reqOrderId = orderId;
		_reqOrderIdList.add(orderId);
		_reqOrderStatusList.add(status);
		_orderStatusList.add(new OrderStatus(orderId, status, filled, remaining, avgFillPrice, permId,parentId, lastFillPrice, clientId, whyHeld));
	}

	@Override
	public void openOrder(int orderId, Contract contract, Order order, OrderState orderState) {
		order.m_orderId = orderId;
		_reqOpenOrderList.add(order);
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
	public void updatePortfolio(Contract contract, int position, double marketPrice, double marketValue,
			double averageCost, double unrealizedPNL, double realizedPNL, String accountName) {
		System.out.println("[MyEWrapper] updatePortfolio: contract = " + contractDescription(contract) + " position = " 
			+ position + " marketPrice = " + marketPrice + " marektValue = " + marketValue + " avgCost = " + averageCost 
			+ " unrealizedPNL = " + unrealizedPNL + " realizedPNL = " + realizedPNL + " [accountName = " + accountName + "]");
		_reqPortfolio.add(new PortfolioRecord(contract, position, marketPrice, marketValue,averageCost, unrealizedPNL, realizedPNL, accountName));
	}

	@Override
	public void updateAccountTime(String timeStamp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void accountDownloadEnd(String accountName) {
		System.out.println("[MyEWrapper] accountDownloadEnd");
		_reqaccountDownloadEnd = true;
	}

	@Override
	public void nextValidId(int orderId) {
		System.out.println("[MyEWrapper] nextValidId: orderId = " + orderId);
		_nextValidId = orderId;
	}

	@Override
	public void contractDetails(int reqId, ContractDetails contractDetails) {
		_getContracDetailsEnd = false;
		
		Contract cont = contractDetails.m_summary;
		if(cont.m_right.compareTo("C") == 0){   //call option
			_ContracDetailsListCall.add(contractDetails);
		}
		else if(cont.m_right.compareTo("P") == 0){
			_ContracDetailsListPut.add(contractDetails);
		}
		else{
			System.out.println("[MyEWrapper] contractDetails: Error! Invalid opt right!");
			return;
		}
		
		//System.out.println("[MyEWrapper] contractDetails: " + contractDetails.m_summary.m_symbol + " " + contractDetails.m_summary.m_expiry + " " 
		//		+ contractDetails.m_summary.m_strike + " " + contractDetails.m_summary.m_right);
	}

	@Override
	public void bondContractDetails(int reqId, ContractDetails contractDetails) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contractDetailsEnd(int reqId) {
	//	System.out.println("[MyEWrapper] contractDetailsEnd");
		_getContracDetailsEnd = true;
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
	public void updateMktDepthL2(int tickerId, int position, String marketMaker, int operation, int side, double price,
			int size) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateNewsBulletin(int msgId, int msgType, String message, String origExchange) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void managedAccounts(String accountsList) {
		String[] _accListTemp = accountsList.split(",");
		for(int i = 0; i < _accListTemp.length;	i++){
			System.out.println("[MyEWrapper] managedAccounts: account[" + i + "] = " + _accListTemp[i]);
			_accList.add( _accListTemp[i]);
		}
	}

	@Override
	public void receiveFA(int faDataType, String xml) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void historicalData(int reqId, String date, double open, double high, double low, double close, int volume,
			int count, double WAP, boolean hasGaps) {
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
		// time in seconds since 1970-01-01 00:00:00
		System.out.println("[MyEWrapper] currentTime = " + Formats.fmtDate(time * 1000));
		this.time = time;
	}

	@Override
	public void fundamentalData(int reqId, String data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deltaNeutralValidation(int reqId, UnderComp underComp) {
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
	public void position(String account, Contract contract, int pos, double avgCost) {
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
	public void displayGroupList(int reqId, String groups) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayGroupUpdated(int reqId, String contractInfo) {
		// TODO Auto-generated method stub
		
	}
	
	public static String contractDescription(Contract contract) {
		String desc = "";
		
		if(contract.m_symbol != null){
			desc = desc + contract.m_symbol + " ";
		}
		
		if(contract.m_secType != null){
			desc = desc + contract.m_secType + " ";
		}
		
		if(contract.m_exchange != null){
			desc = desc + contract.m_exchange + " ";
		}
		
		if(contract.m_expiry != null){
			desc = desc + contract.m_expiry + " ";
		}
		
		if(contract.m_strike != 0){
			desc = desc + contract.m_strike + " ";
		}
		
		if(contract.m_right != null){
			desc = desc + contract.m_right;
		}
		
		return desc;
	}

}

