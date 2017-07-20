package newTest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.client.EClientSocket;
import com.ib.client.Order;
import com.ib.controller.Formats;

public class TestPlaceOrder {
	private static ArrayList<String> _accList;
	public static int startOrderId;
	public static String host = "";   // "" = the local host
	public static int port = 7496;
	public static int clientId = 89;  // a self-specified unique client ID
	public static int reqMktDataTickerId = 0;
	public static final int maxWaitingSecondsBeforeOrderFilled = 6;
	
	public static String symbol = "K200";   //K200 没有etf吧
	public static String optExpirationDate = "20151008";
	public static String futExpirationDate = "20151210";
	public static String currency = "KRW";
	public static String exchange = "KSE";
	public static String stkSecType = "IND";
	
	public static Contract ATMCallContract;
	public static Contract ATMPutContract;
	public static int orderBuyCall_orderId = -1;
	public static int orderBuyPut_orderId = -1;
	public static double callPosition = 0;  //买入call的数量
	public static double putPosition = 0;
	public static double undPosition = 0;    //相应证券的数量
	
	public static void main(String[] args) {
		try{
			productSettings(symbol);
			
			MyEWrapper myEWrapper = new MyEWrapper();
			EClientSocket myEClientSocket = new EClientSocket(myEWrapper);
			
			myEClientSocket.eConnect(host, port, clientId);		// connecting
			if(myEClientSocket.isConnected()){
				System.out.println("Connected!");
			}
			else{
				System.out.println("Not Connected!");
				return;
			}
			
			// get _nextValidId
			while(myEWrapper._nextValidId == MyEWrapper._noDataIndicator_double){Thread.sleep((int)(1000 * 0.01));}
			startOrderId = myEWrapper._nextValidId;
			System.out.println("startOrderId = " + startOrderId);
			
			// specify the stk contract
			Contract stkCont = new Contract();
			stkCont.m_symbol = symbol;
			stkCont.m_secType = stkSecType;
			stkCont.m_currency = currency;
			stkCont.m_exchange = exchange; 
			
			boolean placeBuyCallOrder = true;   // whether to place call order
			boolean placeBuyPutOrder = true;
			
			// get the current portfolio
			while(myEWrapper._accList == null || myEWrapper._accList.size() == 0){Thread.sleep((int)(1000 * 0.01));}
			_accList = myEWrapper._accList;
			
			myEWrapper.clearPorfolioRecordData();
			myEClientSocket.reqAccountUpdates(true, _accList.get(0));
			while(!myEWrapper._reqaccountDownloadEnd){Thread.sleep((int)(1000 * 0.01));}
			ArrayList<PortfolioRecord> myPortfolio = myEWrapper._reqPortfolio;
			
			ArrayList<ArrayList<PortfolioRecord>> gammaPortfolio  = new ArrayList<ArrayList<PortfolioRecord>>();
			ArrayList<PortfolioRecord> callOptList = new ArrayList<PortfolioRecord>();
			ArrayList<PortfolioRecord> putOptList = new ArrayList<PortfolioRecord>();
			ArrayList<PortfolioRecord> undList = new ArrayList<PortfolioRecord>();
			
			// 找到所有的call和put还有underlying的
				//  找出calllist和putlist
			for(PortfolioRecord p : myPortfolio){
				Contract contract = p.contract;
				if(contract.m_symbol.compareTo(symbol) == 0 && contract.m_secType.compareTo("OPT") == 0 && p.position > 0){   //找到symbol一样的opt
					if(contract.m_right.compareTo("C") == 0){
						callOptList.add(p);
					}
					if(contract.m_right.compareTo("P") == 0){
						putOptList.add(p);
					}
				}
				
				if(contract.m_symbol.compareTo(symbol) == 0 && contract.m_symbol.compareTo(stkSecType)==0){    //得到opt对应的股票。理论上只有一个
					undList.add(p);
					undPosition = undPosition + p.position;    //可能有short有long吧
				}
			}
			
			//找出call和put搭配的pair
			for(int i = 0; i < callOptList.size(); i++){
				Contract call = callOptList.get(i).contract;
				double strikeCall = call.m_strike;
				String expiryCall = call.m_expiry;
				int positionCall = callOptList.get(i).position;
				
				for(int j = 0; j < putOptList.size(); j++){
					Contract put = putOptList.get(j).contract;
					double strikePut = put.m_strike;
					String expiryPut = put.m_expiry;
					int positionPut = putOptList.get(j).position;
					
					if(strikeCall == strikePut && expiryCall.compareTo(expiryPut) == 0 && positionCall == positionPut){   // get the pair
						ArrayList<PortfolioRecord> temp = new ArrayList<PortfolioRecord>();
						temp.add(callOptList.get(i));
						temp.add(putOptList.get(j));
						
						gammaPortfolio.add(temp);
						
						callPosition = callOptList.get(i).position;
						putPosition = putOptList.get(j).position;
					}
				}
			}
			
			if(gammaPortfolio.size() == 1){   //先只考虑只建立了一个组合
				ATMCallContract = gammaPortfolio.get(0).get(0).contract;
				ATMCallContract.m_exchange = exchange;
				ATMPutContract = gammaPortfolio.get(0).get(1).contract;
				ATMPutContract.m_exchange = exchange;
			}
			boolean hasPortfolio = gammaPortfolio.size() > 0;
			System.out.println("hasPortfolio = " + hasPortfolio);
			//Thread.sleep(1000 * 100);
			
			// 将当地时间转换成交易所时间
			Date localDate = new Date();
			TimeZone localTimeZone = TimeZone.getTimeZone("GMT+8:00");    //Beijing
			TimeZone exchangeTimeZone = TimeZone.getTimeZone(TradingHours.getExchangeTradingHours(symbol, "product").get(2));
			
			long localDateMiliSecs = localDate.getTime();
			long exchangeDateMiliSecs = localDateMiliSecs  - localTimeZone.getRawOffset() + exchangeTimeZone.getRawOffset();
			Date exchangeDate = new Date(exchangeDateMiliSecs);
			
			SimpleDateFormat ft1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat ft2 = new SimpleDateFormat("yyyy-MM-dd");
			
			String exchangeYYYYMMDD = ft2.format(exchangeDate);
			Date exchangeStartHour = ft1.parse(exchangeYYYYMMDD + " " + TradingHours.getExchangeTradingHours(symbol, "product").get(0));
			Date exchangeEndHour = ft1.parse(exchangeYYYYMMDD + " " + TradingHours.getExchangeTradingHours(symbol, "product").get(1));
			
			boolean isTrading = exchangeDate.getTime() >= exchangeStartHour.getTime() && exchangeDate.getTime() <= exchangeEndHour.getTime();
			
			while(true || isTrading){
				boolean callFilled = false;
				boolean putFilled = false;
				
				if(!hasPortfolio){   //之前没有买过这个symbol
					int tempCount = 0;
					int maxWaitingSeconds = 5;
					double interval = 0.01;   // in seconds
					int maxCount = (int)(maxWaitingSeconds / interval);
					
					//  get ATM contract
					ArrayList<Contract> ATMContractList = getATMCallAndPut(myEWrapper, myEClientSocket, stkCont);
					if(ATMContractList == null || ATMContractList.size() != 2){
						System.out.println("Errors in getting ATM call and put!");
						break;
					}
					if(placeBuyCallOrder) ATMCallContract = ATMContractList.get(0);
					if(placeBuyPutOrder) ATMPutContract = ATMContractList.get(1);
					
					// get ATM contract price
					ArrayList<Double> ATMContractBuyPriceList = getATMCallAndPutPrice(myEWrapper, myEClientSocket, ATMCallContract, ATMPutContract);
					if(ATMContractBuyPriceList == null || ATMContractBuyPriceList.size() != 2){
						System.out.println("Errors in getting ATM call and put PRICE!");
						break;
					}
					double buyCallPrice = ATMContractBuyPriceList.get(0);
					double buyPutPrice = ATMContractBuyPriceList.get(1);
					
					// set the order
					System.out.println("\t===== To set orders and place orders ====");
					Order orderBuyCall = new Order();
					orderBuyCall.m_account = myEWrapper._accList.get(0);
					orderBuyCall.m_action = "BUY";
					orderBuyCall.m_totalQuantity = 1;
					orderBuyCall.m_orderType = "LMT";
					orderBuyCall.m_lmtPrice = buyCallPrice;
					orderBuyCall.m_tif = "DAY";
					callPosition =  orderBuyCall.m_totalQuantity;
					
					Order orderBuyPut = new Order();
					orderBuyPut.m_account = myEWrapper._accList.get(0);
					orderBuyPut.m_action = "BUY";
					orderBuyPut.m_totalQuantity = 1;
					orderBuyPut.m_orderType = "LMT";
					orderBuyPut.m_lmtPrice = buyPutPrice;
					orderBuyPut.m_tif = "DAY";
					putPosition = orderBuyPut.m_totalQuantity;
					
					// place the order
					if(placeBuyCallOrder){
						orderBuyCall_orderId = startOrderId;
						startOrderId++;
						myEWrapper.clearOrderData();
						myEClientSocket.placeOrder(orderBuyCall_orderId, ATMCallContract, orderBuyCall);
						int temp1 = waitUntilOrderSubmission(myEWrapper,orderBuyCall_orderId,maxWaitingSeconds);
						if(temp1 == 1){
							System.out.println("Call submitted");
							placeBuyCallOrder = false;   //下单成功，下次不用下单了
						}else{ //下单不成功
							System.out.println("Something happens in submitting call! + " + myEWrapper._orderStatus + " " + myEWrapper._reqOrderId + " " + myEWrapper._orderRemainingAmt);
							startOrderId--;
						}
					}
					
					if(placeBuyPutOrder){
						orderBuyPut_orderId = startOrderId++;
						myEWrapper.clearOrderData();
						myEClientSocket.placeOrder(orderBuyPut_orderId, ATMPutContract, orderBuyPut);
						int temp1 = waitUntilOrderSubmission(myEWrapper,orderBuyPut_orderId,maxWaitingSeconds);
						if(temp1 == 1){
							System.out.println("Put submitted");
							placeBuyPutOrder = false;   //下单成功，下次不用下单了
						}else{
							System.out.println("Something happens in submitting put!");
							startOrderId--;
						}
					}
					
					if(!(!placeBuyCallOrder && !placeBuyPutOrder)){  //出现下单不成功的现象
						System.out.println("Order not submitted!: callOrderSubmitted = " + !placeBuyCallOrder + " putOrderSubmitted = " + !placeBuyPutOrder);
						continue;
					}
					
					myEClientSocket.reqCurrentTime();
					Thread.sleep((int)(1000 * 0.5));
					long orderSubmittedTime = myEWrapper.time;
					
					// monitor the order
					System.out.println("\t===== Monitoring the orders (traded or not) ====");
					ArrayList<Integer> openOrderIdList = new ArrayList<Integer>();
					ArrayList<String> openOrderStatusList = new ArrayList<String>();
					ArrayList<Order> openOrderList = new ArrayList<Order>();
					long currentTime = 0;
					
					if(true)
					while(true){
						myEWrapper.clearOrderData();
						myEClientSocket.reqOpenOrders();
						myEClientSocket.reqCurrentTime();
						Thread.sleep((int) (1000 * 1));
						
						openOrderIdList = myEWrapper._reqOrderIdList;
						openOrderStatusList = myEWrapper._reqOrderStatusList;
						openOrderList = myEWrapper._reqOpenOrderList;
						currentTime = myEWrapper.time;
						
						int callInd = openOrderIdList.indexOf(orderBuyCall_orderId);
						int putInd = openOrderIdList.indexOf(orderBuyPut_orderId);
						
						if(!callFilled){
							if((currentTime - orderSubmittedTime) <= maxWaitingSecondsBeforeOrderFilled){
								if(callInd == -1) //such orderId not found
								{
									callFilled = true;
									System.out.println("callInd not found - call filled");
								}
								else{
									if(openOrderStatusList.get(callInd).compareTo("Filled") == 0){ //order filled
										callFilled = true;
									}
								}
							}
							else{  //超过等待时间
								if(callInd == -1){
									callFilled = true;
								}
								else if(openOrderStatusList.get(callInd).compareTo("Filled") == 0){ //order filled
									callFilled = true;
								}
							}
							
						}
						
						if(!putFilled){
							if((currentTime - orderSubmittedTime) <= maxWaitingSecondsBeforeOrderFilled){
								if(putInd == -1) //such orderId not found
								{
									putFilled = true;
									System.out.println("putInd not found - put filled");
								}
								else{
									if(openOrderStatusList.get(putInd).compareTo("Filled") == 0){ //order filled
										putFilled = true;
									}
								}
							}
							else{  //超过等待时间
								if(putInd == -1){
									putFilled = true;
								}
								else if(openOrderStatusList.get(putInd).compareTo("Filled") == 0){ //order filled
									putFilled = true;
								}
							}
							
						}
						
						System.out.println("Current time = " + Formats.fmtDate(currentTime * 1000) + " callFilled = " + callFilled + " putFilled = " + putFilled);  
						
						if(callFilled && putFilled){
							System.out.println("Call and put filled!");
							break;
						}
						if((currentTime - orderSubmittedTime) > maxWaitingSecondsBeforeOrderFilled && (!callFilled || !putFilled)){
							System.out.println("Monitoring timeout, call or put not filled!");
							break;
						}
						System.out.println("--");
					} // end of monitoring orders
					
					if(!callFilled || !putFilled){
						if(!callFilled){
							System.out.println("Call not filled! Cancel this order!");
							myEClientSocket.cancelOrder(orderBuyCall_orderId);
							placeBuyCallOrder = true;
						}
						if(!putFilled){
							System.out.println("Put not filled! Cancel this order!");
							myEClientSocket.cancelOrder(orderBuyPut_orderId);
							placeBuyPutOrder = true;
						}
						
						System.out.println("Resubmitting orders in 5 seconds");
						Thread.sleep(1000 * 5);
						continue;
					}
					//order 全部成交
				}
					
				
				
				// Monitoring the orders, calculating deltas and resizing the position
				System.out.println("\t===== Monitoring the orders, calculating deltas and resizing the position ====");
				long startTime = (new Date()).getTime();
				int tttCount = 0;
				if((callFilled && putFilled) || true){ 
					while(true){
						Thread.sleep(1000 * 3); //1s 检查一次
						// get delta
						myEWrapper.clearPrice();
						myEClientSocket.reqMktData(++reqMktDataTickerId, ATMCallContract, "", true, null);
						while(myEWrapper._calculatedDelta == MyEWrapper._noDataIndicator_double){Thread.sleep((int)(1000 * 0.01));}
						double deltaCall = myEWrapper._calculatedDelta;
						deltaCall = deltaCall * Double.parseDouble(ATMCallContract.m_multiplier) * callPosition;
						
						myEWrapper.clearPrice();
						myEClientSocket.reqMktData(++reqMktDataTickerId, ATMPutContract, "", true, null);
						while(myEWrapper._calculatedDelta == MyEWrapper._noDataIndicator_double){Thread.sleep((int)(1000 * 0.01));}
						double deltaPut = myEWrapper._calculatedDelta;
						deltaPut = deltaPut * Double.parseDouble(ATMPutContract.m_multiplier) * putPosition;
						
						System.out.println(" call delta = " + deltaCall);
						System.out.println(" put delta = " + deltaPut);
						System.out.println(" undPosition = " + undPosition);
						
						double totalDelta = deltaCall + deltaPut + undPosition;
						
						// repositioning
						if((deltaCall + deltaPut) / ((Math.abs(deltaPut) + Math.abs(deltaPut)) / 2) >= 0.02){  
							System.out.println("Now resizing the position, total delta = " + totalDelta);
							myEWrapper.clearPrice();
							myEClientSocket.reqMktData(++reqMktDataTickerId, stkCont, "", true, null);
							while(myEWrapper._askPrice == MyEWrapper._noDataIndicator_double){Thread.sleep((int)(1000 * 0.01));}
							double orderPrice = myEWrapper._askPrice;
							
							Order order = new Order();
							order.m_action = totalDelta > 0? "Sell":"BUY";
							order.m_totalQuantity =(int) (Math.floor(totalDelta / 100) * 100);
							order.m_orderType = "LMT";
							order.m_lmtPrice = orderPrice;
							order.m_tif = "DAY";
							//myEClientSocket.placeOrder(startOrderId++, stkCont, order);
							
							//  监视order是否fill了
							myEWrapper.clearOrderData();
							//while(myEWrapper._orderStatus.compareTo("Filled") != 0){Thread.sleep((int)(1000*0.01));}
							
							System.out.println("\t===");
							tttCount++;
							if(tttCount > 3)
							break;
							else continue;
						}
					}
				}
				
				break;
			} // end of outer while
			
			if(!isTrading){
				System.out.println("Exchange not trading! Exchange date = " +ft1.format(exchangeDate));
			}
			
			//pause and disconnect
			try{
				Thread.sleep(1000 * 5);
			}
			catch(Exception e){
				e.printStackTrace();
			}
			myEClientSocket.eDisconnect();
			if(myEClientSocket.isConnected()){
				System.out.println("Still Connected!");
			}
			else{
				System.out.println("DisConnected!");
			}
			System.out.println("================ END ===================");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void productSettings(String symbol){
		switch(symbol){
		case "K200":
			optExpirationDate = "20151008";
			futExpirationDate = "20151210";
			currency = "KRW";
			exchange = "KSE";
			stkSecType = "IND";
			break;
		case "VXX":	
			optExpirationDate = "20151002";
			currency = "USD";
			exchange = "SMART";
			stkSecType = "STK";
			break;
		default:
			System.out.println("Unknown Symbol!");
			break;
		}
	}
	
	public static ContractDetails getATMContractDetails(ArrayList<ContractDetails> contArray, double undPrice){
		double diff = Double.MAX_VALUE;
		ContractDetails returnCon = new ContractDetails();
		
		for(ContractDetails c : contArray){
			if(Math.abs(undPrice - c.m_summary.m_strike) < diff){
				diff = Math.abs(undPrice - c.m_summary.m_strike);
				returnCon = c;
			}
		}
		
		return returnCon;
	}
	
	public static int waitUntilOrderSubmission(MyEWrapper myEWrapper, int orderId, int maxWaitingSeconds) throws InterruptedException{
		int tempCount = 0;
		double interval = 0.01;   // in seconds
		int maxCount = (int)(maxWaitingSeconds / interval);
		
		while(! (myEWrapper._orderStatus.compareTo("Submitted") == 0 && myEWrapper._reqOrderId == orderId)){
			tempCount++; 
			Thread.sleep((int)(1000 * interval));
			//System.out.println("count = " + tempCount + " status =  " + myEWrapper._orderStatus + " orderId = " + myEWrapper._reqOrderId);
			if(tempCount > maxCount){
				System.out.println("Waiting too long to get order status!");
				return 0;   // submission failed
			}
		} 
		
		return 1; //successfully submitted
	}
	
	public static ArrayList<Contract> getATMCallAndPut(MyEWrapper myEWrapper, EClientSocket myEClientSocket, Contract stkCont) throws Exception{
		// get the latest price of underlying stock
		System.out.println("\t===== To get the latest price of underlying stock ====");
		myEWrapper.clearPrice();
		myEClientSocket.reqMktData(++reqMktDataTickerId, stkCont, "", true, null);
		while(myEWrapper._lastPrice == MyEWrapper._noDataIndicator_double){Thread.sleep((int)(1000 * 0.01));}
		double undPrice = myEWrapper._lastPrice;
		System.out.println("Last price = " + undPrice);
		
		//get the opt contract
		System.out.println("\t===== To get the ATM option contract ====");
		Contract optCont = new Contract();
		optCont.m_symbol = symbol;
		optCont.m_expiry = optExpirationDate;
		optCont.m_secType = "OPT";
		optCont.m_currency = currency;
		optCont.m_exchange = exchange;
		myEClientSocket.reqContractDetails(0, optCont);
		
		//get contract details (opt chain)
		while(!myEWrapper._getContracDetailsEnd){Thread.sleep((int)(1000 * 0.01));}
		ArrayList<ContractDetails> _ContracDetailsListCall = myEWrapper._ContracDetailsListCall;
		
		ContractDetails ATMCall = getATMContractDetails(_ContracDetailsListCall, undPrice);
		Contract ATMCallContract = ATMCall.m_summary;
		
		ArrayList<ContractDetails> _ContracDetailsListPut = myEWrapper._ContracDetailsListPut;
		ContractDetails ATMPut = getATMContractDetails(_ContracDetailsListPut, undPrice);
		Contract ATMPutContract = ATMPut.m_summary;
		
		System.out.println("ATMCall: " + ATMCall.m_summary.m_symbol + " " + " " + ATMCall.m_summary.m_expiry + " " + ATMCall.m_summary.m_strike + " " + ATMCall.m_summary.m_right
				+ " " + ATMCallContract.m_secType + " " + ATMCallContract.m_exchange + " " + ATMCallContract.m_multiplier);
		System.out.println("ATMPut: " + ATMPut.m_summary.m_symbol + " " + " " + ATMPut.m_summary.m_expiry + " " + ATMPut.m_summary.m_strike + " " + ATMPut.m_summary.m_right
				+ " " + ATMPutContract.m_secType + " " + ATMPutContract.m_exchange + " " + ATMPutContract.m_multiplier);
		//System.out.println("ATMCall: " + ATMPut.m_summary.m_symbol + " " + " " + ATMPut.m_summary.m_expiry + " " + ATMPut.m_summary.m_strike + " " + ATMPut.m_summary.m_right);
		
		
		// returning configuration
		ArrayList<Contract> returnList = new ArrayList<Contract>();
		returnList.add(ATMCallContract);
		returnList.add(ATMPutContract);
		
		return returnList;
	}
	
	public static ArrayList<Double> getATMCallAndPutPrice(MyEWrapper myEWrapper, EClientSocket myEClientSocket, Contract stkCont) throws Exception{
		ArrayList<Contract> ATMContractList = getATMCallAndPut(myEWrapper, myEClientSocket, stkCont);
		if(ATMContractList == null || ATMContractList.size() != 2){
			System.out.println("Errors in getting ATM call and put!");
			//return;
		}
		Contract ATMCallContract = ATMContractList.get(0);
		Contract ATMPutContract = ATMContractList.get(1);
		
		// get price for ATM call and ATM put
		return getATMCallAndPutPrice(myEWrapper, myEClientSocket, ATMCallContract,ATMPutContract);
	}
	
	public static ArrayList<Double> getATMCallAndPutPrice(MyEWrapper myEWrapper, EClientSocket myEClientSocket, Contract ATMCallContract, Contract ATMPutContract) throws Exception{
		// get price for ATM call and ATM put
		System.out.println("\t===== To get the price of ATM call and ATM put ====");
		myEWrapper.clearPrice();
		myEClientSocket.reqMktData(++reqMktDataTickerId, ATMCallContract, "", true, null);
		while(myEWrapper._lastPrice == MyEWrapper._noDataIndicator_double){Thread.sleep((int)(1000 * 0.01));}
		double buyCallPrice = myEWrapper._lastPrice;
		
		myEWrapper.clearPrice();
		myEClientSocket.reqMktData(++reqMktDataTickerId, ATMPutContract, "", true, null);
		while(myEWrapper._lastPrice == MyEWrapper._noDataIndicator_double){Thread.sleep((int)(1000 * 0.01));}
		double buyPutPrice = myEWrapper._lastPrice;
		System.out.println("buyCallPrice = " + buyCallPrice + " buyPutPrice = " + buyPutPrice);
		
		// returning configuration
		ArrayList<Double> returnList = new ArrayList<Double>();
		returnList.add(buyCallPrice);
		returnList.add(buyPutPrice);
		
		return returnList;
	}
}


