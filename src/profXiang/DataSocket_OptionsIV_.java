//package com.algomodJB.platform.trader.ds;
package com.algomodJB.platform.trader.ds;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import tickers.ETF.ETFs_VIX;
import __FX.FXMajor;
import ____.___FavoriteClasses_Options;
import ___exportRelated.__CleanRef;

import com.algomodJB.platform.marketdepthQuote._test.QuoteNTradeBook_Option;
import com.algomodJB.platform.marketdepthQuote._test.QuoteNTradeBook_Option.OptPriData;
import com.algomodJB.platform.marketdepthQuote._test.QuoteNTradeBook_Option.OptPriData_Status;
import com.algomodJB.platform.model.JXTraderException;
import com.algomodJB.platform.model.acct.DispatcherAccount;
import com.algomodJB.platform.model.acct.DispatcherAccount_Demo;
import com.algomodJB.platform.model.acct.DispatcherAccount_FASimuTradable;
import com.algomodJB.platform.position._C_.realTime.C_realTime_;
import com.algomodJB.platform.position._C_.searchNAssignUnder.C_searchNAssignUnderlying_;
import com.algomodJB.platform.scheduleNHour.TradingSchedule_CE;
import com.algomodJB.platform.strategy.trade.Strategy_Trade_var;
import com.algomodJB.platform.trader.ds.DataSocket_RealTimeQuote_Acct_.Res_requestMarketData;
import com.algomodJB.platform.trader.ds._test.DataSocket_base_ReqID_Auto;
import com.algomodJB.platform.trader.ds._test2.DS1;
import com.algomodJB.platform.trader.ds._test5.DataSocket_base_1;
import com.algomodJB.platform.trader.wd.WrapperData_OptionsIV_;
import com.ib.client.Contract_Dispatcher;
import com.ib.client.Contract_Dispatcher_1;
import com.ib.client.EClientSocket;
import com.ib.client.__Price_Check;
import com.ib.client._test2.Contract;
import com.ib.controller.Types;
import com.ib.controller.Types.SecType;
import com.primoi.ib.data.CEData;
import com.primoi.ib.data.CEData_QuoteBars;
import com.primoi.ib.data.__ContractExtended___2;
import com.primoi.ib.data.__ContractExtended___Dispatcher;
import com.primoi.ib.data._factory.ContractFactory_Search_1;
import com.primoi.ib.data._factory._test3.ContractFactory_Check;
import com.primoi.ib.data._factory._test3.ContractFactory_Check_Underlying;
import com.primoi.ib.data._factory.deriv.ContractFactory_CheckDeriv;
import com.primoi.ib.data._factory.underlying.ContractFactory_UnderlyingSearch;
import com.primoi.ib.data._test0.CEData_Dispatcher;
import com.primoi.ib.data._test1.__ContractExtended___;
import com.primoi.ib.data._test3.__ContractExtended___Dispatcher_1;
import com.primoi.ib.data._test3.__ContractExtended___Dispatcher_1_CE;
import com.primoi.ib.data.quote.QuoteBar;

//import com.algomodJB.platform.trader.GenericTickTypeMatch;
//import com.algomodJB.shortable.ShortableValue;
//import com.ib.client.Contract;
//import com.primoi.ib.data._factory.ContractFactory_Search_1;
//import com.primoi.ib.data._test0.CEData_Dispatcher;
//import com.primoi.ib.data._test3.__ContractExtended___Dispatcher_1;
//import com.primoi.report.Report;


import com.primoi.ib.data.quote.QuoteLastDays;
import com.primoi.ib.data.quote.Quote_BAL;
import com.primoi.ib.data.quote.Quote_Mid_wait;
import com.primoi.report.Report;
import com.primoi.report._UtilReport;
import com.primoi.util.UtilThread;
import com.primoi.util.UtilThread.CallableWithName_toRun;
import com.primoi.util._dateTime.TimeDiffThread0;
import com.primoi.util._dateTime.UtilDate;
import com.primoi.util._dateTime.UtilDate_HMS;
import com.primoi.util.collection.UtilCollection;
import com.primoi.util.test.UtilArray;
import com.primoi.util.test1.UtilNumber;

import dataOther.DataMerged;
import debug.ExceptionProcess;
import debug.Tracker;
import debug.TrackerTF;

////!!-- Remove
/**
 * @see com.algomodJB.platform.trader.ds._test2.DS1
 * 
 * -----------------------------------------------------------------------
 * @see com.ib.client.EClientSocket#calculateImpliedVolatility(int, Contract, double, double)
 * @see com.ib.client.EClientSocket#cancelCalculateImpliedVolatility(int)
 * 
 * @see com.ib.client.EClientSocket#calculateOptionPrice(int, Contract, double, double)
 * @see com.ib.client.EClientSocket#cancelCalculateOptionPrice(int)
 * 
 * @see com.algomodJB.platform.trader.wd.WrapperData_OptionsIV_
 * 
 * -----------------------------------------------------------------------
 * 
 * This also to get real-time data using
 * @see com.algomodJB.platform.position._C_.realTime.C_realTime_
 * @see com.algomodJB.platform.trader.ds.I_Realtime_DS
 * 
 * 
 * ------------------------------------------------------------------------
 * Cancel a contract's OptionPrice or ImpliedVol when it is not in any strategy
 * 
 * 
 * Actually used only in (except in MI action)
 * @see com.algomodJB.platform.position._C_.C_OptionsIV_
 * 
 * @see com.primoi.ib.data._factory.optionChain.DeltaPosByAccount_calc#getDeltaSum
 * 
 */
@SuppressWarnings({"serial" })
public class DataSocket_OptionsIV_ extends I_OptionsIV_DS implements
		__CleanRef
// 708
// WrapperData_OptionsIV_
{
	public long adjacentPortUpdateInterval_max_default = 1000L;
	public static HashMap<Integer, CEData> optionsGreeksInvalidDataHM = new
			HashMap<Integer, CEData>();
	
	
	/**
	 * Singleton
	 */
	private static I_OptionsIV_DS dataSocket_OptionsIV_;

	synchronized public static I_OptionsIV_DS getSocket1() {
		if (dataSocket_OptionsIV_ == null) {
			dataSocket_OptionsIV_ = new DataSocket_OptionsIV_();
		}

		return dataSocket_OptionsIV_;
	}

	// port 0 for Real-time
	private DataSocket_OptionsIV_() {
		super(DataSocket_base_1.clientID_OptionsIV_);

		super.setWrapperData_base(
				WrapperData_OptionsIV_.getWrapperData(this));

	}

	// //real time data
	// public Map<Integer, ContractExtendedWithHistData>
	// contractExtsDataRequested;
	public Map<Integer, Long> contractExtsAllDataRequestedTimeHM; // //time when
																	// the data
																	// was
																	// requested

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.algomodJB.platform.trader.ds.Interface_Realtime_DS#connectAfterCheck
	 * (boolean)
	 */
	@Override
	public EClientSocket connectAfterCheck(boolean wait4AcctData)
			throws JXTraderException {
		EClientSocket sock = super.connectAfterCheck(wait4AcctData);

		try {

			// //data for clientid = 0
			synchronized (this) {

				// A new thread does not work
				// Thread t = new Thread(){
				// public void run(){
				// waitNProcessManagedAccts();
				// }
				// };
				// t.setName("waitNProcessManagedAccts");
				// t.start();
			}

			// if(TrackerTF.false_())
			// debug.Tracker.trackWithErrorPrinting
			Tracker.printSysIO("317 isDemoAccount="
					+ DispatcherAccount_FASimuTradable.isSimulateAccounts()
					+ "/" + DispatcherAccount_Demo.isEFDemoAccounts()
					+ " allAccounts=" + DispatcherAccount.getAccountsAllCodes()
					+ " " + DispatcherAccount.getAccountsCurrentCodes() + " "
					+ this.hashCode() + " host=" + host + " port=" + port); // Dispatcher.allAccounts.accountsCurrentSession

			/**
			 * already in
			 * 
			 * @see com.algomodJB.platform.model.MainFrameController_3.
			 *      connect2Network(int)
			 */
			DS1.startToProcessTWSData();

			if (TrackerTF.true_()) {
				// fast, takes 4 seconds only

				new Thread(
						"connectAfterCheck.clearStoredPositionsNUpdateAccountCurrent") {
					@Override
					public void run() {
//						clearStoredPositionsNUpdateAccountCurrent(true, false);
					}
				}.start();
			}
			else
			{
				// //FIXME may slow down. Should we call it here?
				// //! This is necessary, as it may takes too long for
				// clearStoredPositionsNUpdateAccountCurrent(true, false);
			}

			return sock;

		} catch (Throwable t) {
			t.printStackTrace();

			throw new JXTraderException(
					"Caught and rethrow from connectAfterCheck(", t);
		}

	}







	/**
	 * @see com.algomodJB.platform.trader.ds.I_Realtime#addToMarketTickGeneric_RequestMODEL_OPTION(CEData)
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.algomodJB.platform.trader.ds.Interface_Realtime_DS#
	 * addToMarketTickGeneric_RequestMODEL_OPTION(com.primoi.ib.data.CEData)
	 */
	@Override
	public boolean addToMarketTickGeneric_RequestMODEL_OPTION(CEData contrE)
	{
		boolean deb = true;

		if (deb)
			return false;

//		try {
//			if (deb)
//				Tracker.printSysIO("to requesttMODEL_OPTION 1700:" + contrE);
//
//			Contract contract = __ContractExtended___Dispatcher_1
//					.getContract(contrE);
//
//			// __ContractExtended___ storedCont = contract.getM_conId()>0?
//			// contrE :
//			// ContractFactory_Search_1.searchLocalNRequestRemote(__ContractExtended___Dispatcher_1.get_ce_(contrE));
//			CEData ceData = contract.getM_conId() > 0 ? contrE
//					: CEData_Dispatcher
//							.convertToCEData(ContractFactory_Search_1
//									.searchLocalNRequestRemote(__ContractExtended___Dispatcher_1
//											.get_ce_(contrE)));
//
//			if (deb)
//				Tracker.printSysIO("to requesttMODEL_OPTION 1710:"
//						+ GenericTickTypeMatch.GTM_ModelOption_600.gt_type
//						+ " stored(hashCode)=" + contract.hashCode() + " "
//						+ contract);
//
//			if (TrackerTF.false_())
//				__ContractExtended___Dispatcher_1.get_ce_(contrE)
//						.getShortableValue().setShortableValue_Unassigned();
//
//
////			int tickerID = requestMarketDataGTOnly_StoreGT(ceData,
////					GenericTickTypeMatch.GTM_MarktPrice_221);
////			if (tickerID < 0)
////				return false;
//
//
//			// wait for data
//			try {
//
//				if (!DispatcherAccount_FASimuTradable.isSimulateAccounts())
//					synchronized (contrE) {
//						Tracker.trackWithErrorPrinting_LessExpensiveThreadWait("DataSocket_RealTimeQuote_Acct_ 1118");
//
//						while (__ContractExtended___Dispatcher_1
//								.get_ce_(contrE).getShortableValue()
//								.getShortableValue() == ShortableValue.shortableValue_Unassigned) {
//							if (deb)
//								System.out
//										.println("Waiting in addToMarketTickGeneric_RequestMODEL_OPTION..."
//												+ contrE);
//							contrE.wait();
//
//							contrE.notifyAll();
//						}
//					}
//
//				String msg = "requesttMODEL_OPTION via requestMarketDataGT_Store: underlying="
//						+ contract
//						+ "("
//						+ contract.hashCode()
//						+ ")"
//						+ " GenericTickTypeMatch.GTM_ModelOption.gt_type="
//						+ GenericTickTypeMatch.GTM_ModelOption_600.gt_type
//						+ " contrE.quoteOption=" + contrE.quoteOption;
//				// Dispatcher.setText2StatusLabel(msg);
//				if (deb)
//					Tracker.printSysIO(msg);
//
//				return true;
//
//			} catch (Exception e) {
//				// Report.getEventReporter(null).report
//				Tracker.printSysIO(debug.ExceptionProcess.combineStackTrace(e));
//
//				e.printStackTrace();
//				return false;
//			}
//
//		} catch (Throwable t) {
//			t.printStackTrace();
//
//			Report.getDataReporter(null).reportInRed(t.getMessage());
//		}
		

		return false;
	}

	// public boolean requestMODEL_OPTION( contrE) {
	// //Call this method to request market data. The market data will be
	// returned by
	// //the tickPrice(), tickSize(), tickOptionComputation(), tickGeneric(),
	// tickString()
	// //and tickEFP() methods.
	//
	// try{
	// socket.reqMktData(contrE.m_conId, contrE,
	// ""+256, false); //TickType.MODEL_OPTION //256(GreekPortfolio)
	//
	// String msg = "Requested MODEL_OPTION via market data: "+contrE+"(" +
	// contrE.hashCode()+ ")";
	// Report.getEventReporter(null).report("requestMODEL_OPTION 1213:"+msg);
	// Dispatcher.setText2StatusLabel(msg);
	// Report.getEventReporter(null).report(msg);
	//
	// return true;
	//
	//
	// }catch(Throwable t){
	// t.printStackTrace();
	// }
	//
	// return false;
	// }





	@Override
	@SuppressWarnings("unused")
	public void calculateOptionGreeksTest()
	{
		boolean toUseConstantData = true;
		
		/*
		//contract.m_conId = 90081395;	//send( contract.m_conId);	//120121P00053000_20120120_53.0P_OPT_AMEX_USD_m_conId=90081395
		ce.m_symbol = TickerETF.QQQ;	//send( contract.m_symbol);
		ce.m_secType = SecType.OPT.name();	//send( contract.m_secType);
		ce.m_expiry = "20120120";	//send( contract.m_expiry);
		ce.m_strike = 53;	//send( contract.m_strike);
		ce.m_right = Types.Right.Put.name().substring(0, 1) ;	//send( contract.m_right);
		ce.m_multiplier = "100";	//send( contract.m_multiplier);
		ce.m_exchange = ExchangeTimeZoneCurOpenT.smartS;	//send( contract.m_exchange);	AMEX	SMART
	
		//send( contract.m_primaryExch);
		//send( contract.m_currency);
		ce.m_currency = CurrencyFuncs.USD;
		//send( contract.m_localSymbol);
		 */
	
		//contract.m_conId = 90081395;	//send( contract.m_conId);	//120121P00053000_20120120_53.0P_OPT_AMEX_USD_m_conId=90081395
		//40.00 1.85 -2.20 1.83 1.92 1037 1136
		//40.00 1.50 +0.71 1.27 1.60 1535 11530
		
	
		
		Contract contract = Contract_Dispatcher_1.getInstance(0, ETFs_VIX.VXX, SecType.OPT.name(), "", "", "20111021",
				40, Types.Right.Call.name().substring(0, 1),"100", true);
		CEData ceData0 = CEData_Dispatcher.getInstance(contract.getM_conId());
	
		
		//send( contract.m_primaryExch);
		//send( contract.m_currency);
		contract.setM_currency(FXMajor.USD);
		//send( contract.m_localSymbol);
	
	
		Tracker.printSysIO("calcOptionGreeksTest 0:");
	
		__ContractExtended___ _ce_ =  ContractFactory_Search_1.searchLocalNRequestRemote(__ContractExtended___Dispatcher_1_CE.get_ce_(ceData0));
		//ContractExtendedWithHistData ceData = contract;
	
		CEData ceData  = CEData_Dispatcher.convertToCEData(_ce_);  //.get_ce_();
		
		Tracker.printSysIO("calcOptionGreeksTest 1: ceData="+_ce_.toString(true));
	
		__ContractExtended___ underlying = C_searchNAssignUnderlying_.searchNAssignUnderlying(_ce_);
		Tracker.printSysIO("calcOptionGreeksTest 2.0: underlying="+_ce_.getUnderlyingForOptionNETF());
	
	
		//ceData.setUnderlyingForOptionNETF(underlyingForOptionNETF);
		//this returns field=custOptComp(53)
	
		OptPriData optPriData = null;
		if(toUseConstantData)
		{
			optPriData = calculateOptionGreeks(
					new CEData_optionP_underlyingP(ceData, 1.85, 40.46));	//4.65, 52.19
		}
		else
		{
			optPriData = calculateOptionGreeks_afterCheckLastRun(ceData);
		}
	
		System.err.println("calcOptionGreeksTest 5: "+(optPriData!=null? optPriData.toString() : "null optPriData") );
	
	}
	
	
	

	/**
	 * Result stored to ContractExtended.quoteOption.optionsPricingsCustOptionComputation
	 * Accessible via getOptionsPricingsCustOptionComputation()
	 * 
	 * @see com.algomodJB.platform.trader.wd.WrapperData_OptionsIV_#tickOptionComputation
	 * 
	 * @param contOpt
	 * @param impliedVol
	 * @param underlyingPrice
	 * @return
	 */
	public OptPriData calculateOptionPrice_wait(final CEData contOpt,
			final double impliedVol, final double underlyingPrice)
	{
		final boolean toDebug = true;
	
		
		final Contract contractOpt = __ContractExtended___Dispatcher_1_CE.getContract(contOpt);
		final __ContractExtended___ ce = __ContractExtended___Dispatcher.getInstance(contOpt.getContrId());
		
		final I_OptionsIV_DS i_OptionsIV_DS = DataSocket_OptionsIV_.getSocket1();
		
		
		//better check this before call this method
		if(TrackerTF.false_())
			for(__ContractExtended___ ceData : i_OptionsIV_DS.reqId_calcOptionPrice_ContrLocal_HM.values())
			{
				Contract contract = __ContractExtended___Dispatcher_1.getContract(ceData);
				
				if(contractOpt.getM_conId()==contract.getM_conId())
					return new OptPriData(OptPriData_Status.alreadyRequested_OptPrice);
			}
	
		
	
		try
		{
			int reqIdToStore =  Integer.MIN_VALUE;
			
			
			if(TrackerTF.true_())
			{

				long t0 = TimeDiffThread0.currentTimeMillis();
				
				////!! This is better, as it does not freez the GUI
				ExecutorService execService = Executors.newFixedThreadPool(1);	//5

				
				CallableWithName_toRun<Integer> call = new CallableWithName_toRun<Integer>()
				{

					@Override
					public Integer call() throws Exception
					{
						
						String threadNameS = "Callable_calculateOptionPrice_wait(CEData,double, double) __WaitResult 355 hc="
								+ this.hashCode();
						
						UtilThread.appendMyThreadName(threadNameS);
						
						Tracker.printSysIO("requestContractD threadNameS="+threadNameS);


						////------------this is the contents to run--------------------------------------
						////! we use nextID, there should be no return or break flow until increaseID_recordContr()
						int reqId = DataSocket_base_ReqID_Auto.increaseID_recordContr(
								__ContractExtended___Dispatcher_1_CE.get_ce_(contOpt), true, true);
						
						
						i_OptionsIV_DS.getSocketOrAdd_Conn().calculateOptionPrice(
								reqId, contractOpt, impliedVol, underlyingPrice);	//0.25, 53.6
				
						
						
						i_OptionsIV_DS.reqId_calcOptionEndedMap.put(reqId, false);
						i_OptionsIV_DS.reqId_calcOptionPrice_ContrLocal_HM.put(reqId, ce);	//should put it again
				
						if(toDebug)
							//Report.getEventReporter(null).report
							Tracker.printSysIO("calculateOptionPrice_wait 1: "+reqId+" impliedVol="+impliedVol
									+" underlyingPrice="+underlyingPrice);
						
						
						// To test, this may cause never return
						//contract.setM_strike(contract.getM_strike()+1);
						
						///////////////////////////////////////////////////////////////////////////////
						
						
						

						try
						{
							
							//Integer witaInt1 = new Integer(1);
							synchronized (i_OptionsIV_DS.reqId_calcOptionEndedMap)	//dsRealtime.wrapperData
							{
								long tDif = 0;
				
								
//								Tracker.exit(0);
								
								
								LOOP_Wait:
								while ((!i_OptionsIV_DS.reqId_calcOptionEndedMap.containsKey(reqId) ||
										!i_OptionsIV_DS.reqId_calcOptionEndedMap.get(reqId)
//										|| (tDif=TimeDiffThread0.currentTimeMillis()-t0) <
//											1000L * ___FavoriteClasses_Options.secondsToWait_reCalcGreeks1Contract
									)
								)
								{
									
									Tracker.printSysIO(
//									Tracker.exit(0,
										"calculateOptionPrice_wait 2.1: reqId="+reqId+" tDif="+tDif);
									
									
									i_OptionsIV_DS.reqId_calcOptionEndedMap.wait();	//dsRealtime.wrapperData.wait();
								}
				
								
								if(toDebug)
								{
									//Report.getEventReporter(null).report
									Tracker.printSysIO("calculateOptionPrice_wait 3:"+reqId+" reqId_calcIV="+reqId_calcIV
											+ " tDif="+tDif+" "+i_OptionsIV_DS.reqId_calcOptionEndedMap.get(reqId));
									
//									Tracker.exit(0);
								}

								
								return reqId;
							}
							
						}
						catch(Exception e)
						{
							e.printStackTrace();
							//Report.getEventReporter(null).report(ExceptionProcess.combineStackTrace(e));
							
							if(toDebug)
								Tracker.exit(0);
							
							
							return -1;
						}
						
					

					}
				};
				
				Future<Integer> future = execService.submit(call);



				//Less important thing. return null, if no result after 30 seconds
//				if(TrackerTF.false_())
				{
					Integer ret = null;
					try
					{
						ret = future.get(___FavoriteClasses_Options.secondsToWait_reCalcGreeks1Contract, TimeUnit.SECONDS);
						
						if(ret!=null)
							reqIdToStore = ret.intValue();
					}
					catch (TimeoutException timeoutE)
					{
						
						UtilCollection.print(reqId_calcOptionEndedMap.entrySet(),
								"reqId_calcOptionEndedMap.entrySet()");
						
						
						//Report.getOptionReporter(null).report(
						//Tracker.exit(0,
						//Tracker.trackWithErrorPrinting_LessExpensive(
						Tracker.printSysIO(
								"Not returned calculateOptionPrice_wait after "+___FavoriteClasses_Options.secondsToWait_reCalcGreeks1Contract
								+ " seconds contractOpt="+contractOpt);
						
						if(notReturnedCalcOptionPriceC++==3)
						{
							if(___FavoriteClasses_Options.toDebug_notRectured_calcOptions
									&& !___FavoriteClasses_Options.toDebugIV_K200_NIFTY50
									 || TrackerTF.false_())
							{
								Tracker.exit(0);
							}
						}
							
						
						
						if(TrackerTF.false_())
						if(!__Price_Check.isValidPrice(ce.getContractInst().getM_strike()-2975))
						{
							Tracker.exit(0);
						}
						
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}

					System.out.println("639 after holding ret="+ret+" runnintMillSec="+(TimeDiffThread0.currentTimeMillis()-t0));

//					Tracker.exit(0);
				}


				//close the execService
				execService.shutdown();
				execService = null;
			
				
			}
			else
			{
				////! OldVersion
				try
				{
					////! we use nextID, there should be no return or break flow until increaseID_recordContr()
					int reqId = DataSocket_base_ReqID_Auto.increaseID_recordContr(
							__ContractExtended___Dispatcher_1_CE.get_ce_(contOpt), true, true);
					
					
					
					i_OptionsIV_DS.getSocketOrAdd_Conn().calculateOptionPrice(
							reqId, contractOpt, impliedVol, underlyingPrice);	//0.25, 53.6
			
					
					
					i_OptionsIV_DS.reqId_calcOptionEndedMap.put(reqId, false);
					i_OptionsIV_DS.reqId_calcOptionPrice_ContrLocal_HM.put(reqId, ce);	//should put it again
			
					if(toDebug)
						//Report.getEventReporter(null).report
						Tracker.printSysIO("calculateOptionPrice_wait 1: "+reqId+" impliedVol="+impliedVol
								+" underlyingPrice="+underlyingPrice);
					
					
					
					//Integer witaInt1 = new Integer(1);
					synchronized (i_OptionsIV_DS.reqId_calcOptionEndedMap)	//dsRealtime.wrapperData
					{
						long t0 = TimeDiffThread0.currentTimeMillis();
						long tDif = 0;
		
						
//						Tracker.exit(0);
						
						
						LOOP_Wait:
						while ((!i_OptionsIV_DS.reqId_calcOptionEndedMap.containsKey(reqId) ||
								!i_OptionsIV_DS.reqId_calcOptionEndedMap.get(reqId) ||
								(tDif=TimeDiffThread0.currentTimeMillis()-t0) <
									1000L * ___FavoriteClasses_Options.secondsToWait_reCalcGreeks1Contract
							)
						)
						{
							
//							if(___FavoriteClasses_Options.secondsToWait_reCalcGreeks1Contract>0 &&
//								(tDif=TimeDiffThread0.currentTimeMillis()-t0) >
//									1000L * ___FavoriteClasses_Options.secondsToWait_reCalcGreeks1Contract)
//							{
//								if(toDebug)
//									//Report.getEventReporter(null).report
//									//Tracker.printSysIO(
//									Tracker.exit(0, "calculateOptionPrice_wait 2: reqId="+reqId+" tDif="+tDif);
//
//
//								Thread.yield();
//
//								break LOOP_Wait;
//							}
							
							
							Tracker.printSysIO(
//							Tracker.exit(0,
								"calculateOptionPrice_wait 2.1: reqId="+reqId+" tDif="+tDif);
							
							
							i_OptionsIV_DS.reqId_calcOptionEndedMap.wait();	//dsRealtime.wrapperData.wait();
						}
		
						
						if(toDebug)
						{
							//Report.getEventReporter(null).report
							Tracker.printSysIO("calculateOptionPrice_wait 3:"+reqId+" reqId_calcIV="+reqId_calcIV
									+ " tDif="+tDif+" "+i_OptionsIV_DS.reqId_calcOptionEndedMap.get(reqId));
							
							Tracker.exit(0);
						}

					}
					
				}
				catch(Exception e)
				{
					e.printStackTrace();
					//Report.getEventReporter(null).report(ExceptionProcess.combineStackTrace(e));
					
					if(toDebug)
						Tracker.exit(0);
				}
				
			}
			
			
	
			
			
			
			
	
			if(contOpt.quoteOption==null || reqIdToStore<0)
			{
				Tracker.printSysIO("calculateOptionPrice_wait Invalid quoteOption="+contOpt.quoteOption
						+" reqIdToStore="+reqIdToStore);
				

//				Tracker.exit(0);
				
				
				if(!___FavoriteClasses_Options.toDebugIV_K200_NIFTY50)
				{
					return new OptPriData(OptPriData_Status.toDebugIV_K200_NIFTY50);
				}
				else
				{
					//
					OptPriData optPriDataBlank4Debug = new OptPriData(OptPriData_Status.Blank);
					
					String right = contractOpt.getM_right();
					optPriDataBlank4Debug.delta = (right.equals(Types.Right.Put.name().substring(0, 1))? -1 : 1) *
							___FavoriteClasses_Options.toDebugIVK200_NIFTY50_delta;
					
					contOpt.quoteOption = new QuoteNTradeBook_Option(contOpt);
					contOpt.quoteOption.getOptionsPricings_CustComputation().add(optPriDataBlank4Debug);
				}
				
			}


//			Tracker.exit(0);
			
			
			//retrieve greeks data. getOptionsPricingsCustOptionComputation().lastElement();
			
			OptPriData optDataGreeks = contOpt.quoteOption.getOptDataLatest_CustComputation();
			optDataGreeks.reqId = reqIdToStore;
	
			double[] greeks = new double[4];
			
			
			greeks[0] = optDataGreeks.delta;
			greeks[1] = optDataGreeks.gamma;
			greeks[2] = optDataGreeks.vega;
			greeks[3] = optDataGreeks.theta;
	
			// calculateOptionPrice_wait 4: delta=-0.5589 gamma=0.0099 vega=NaN theta=-0.4731
			// calculateOptionPrice_wait 4: delta=0.3915 gamma=0.0101 vega=NaN theta=-0.4576
			// calculateOptionPrice_wait 4: delta=-0.5615 gamma=0.0099 vega=NaN theta=-0.4722
			// calculateOptionPrice_wait 4: delta=-0.4305 gamma=0.0698 vega=0.0254 theta=-0.055
			// calculateOptionPrice_wait 4: delta=-0.4431 gamma=0.0945 vega=0.0291 theta=-0.0348	For VXX 3 weeks to expiry
			if(toDebug)
				//Report.getEventReporter(null).report
				Tracker.printSysIO("calculateOptionPrice_wait 4: delta="+UtilNumber.r4(greeks[0])+" gamma="+UtilNumber.r4(greeks[1])
						+" vega="+UtilNumber.r4(greeks[2])+" theta="+UtilNumber.r4(greeks[3]));
	
	
			if(	//TrackerTF.false_() &&
				___FavoriteClasses_Options.toDebugIV_K200_NIFTY50)
			{
//				Tracker.exit(0);
			}
			

			
			
			
			
			return optDataGreeks;
	
		}
		catch(Throwable t)
		{
			t.printStackTrace();
			
			Tracker.exit(0);
	
			return new OptPriData(OptPriData_Status.Exception);
		}
	
	}

	
	public int reqId_calcIV = 0;
	public static int notReturnedImpliedVolC = 0;
	public static int notReturnedCalcOptionPriceC = 0;
	
	
	/**
	 * It is not a subscription service, just once call.
	 * 
	 */
	@Override
	public double calculateImpliedVolatility_wait(final CEData contOpt,
			final double optionPrice, final double underlyingPrice)
	{
		final boolean toDebug = true;
	
//		Tracker.exit(0);
		
		
		final __ContractExtended___ ce = __ContractExtended___Dispatcher_1_CE.get_ce_(contOpt);
		final Contract contract = __ContractExtended___Dispatcher_1_CE.getContract(contOpt);
	
		//better check this before call this method
		if(TrackerTF.false_())
			for(__ContractExtended___ ceData : reqId_calcOptionIV_ContrLocal_HM.values()){
				if(contract.getM_conId()==__ContractExtended___Dispatcher_1.getContract(ceData).getM_conId())
					return Double.NaN;
			}
	
	
		try
		{
	

			////! we use nextID, there should be no return or break flow until increaseID_recordContr()
			//0.36811560740716176
			final int reqId = DataSocket_base_ReqID_Auto.increaseID_recordContr(
					__ContractExtended___Dispatcher_1_CE.get_ce_(contOpt), true, true);
			reqId_calcIV = reqId;
			
			
			if(toDebug)
			//Report.getOptionReporter(null).report(
			Tracker.printSysIO(
					"calculateImpliedVolatility_wait: optionPrice="+optionPrice
					+" underlyingPrice="+underlyingPrice);
			
			
	
			int contrID = contOpt.getContrId();
			
			//calculateImpliedVolatility_wait 1: reqId=15 contract=conId=140070846@1583435263 localSymbol=K200260R4.KS_symbol=K200_expiry=20140612_strikeRight=260.0P_OPT_exchExPrim=KSE-null_KRW_multiplier=500000
			//calculateImpliedVolatility_wait 1: reqId=19 contract=conId=146276157@1572959555 localSymbol=K200262F4.KS_symbol=K200_expiry=20140612_strikeRight=262.5C_OPT_exchExPrim=KSE-null_KRW_multiplier=500000
			//calculateImpliedVolatility_wait 1: reqId=21 contract=conId=146276160@1682827197 localSymbol=K200262R4.KS_symbol=K200_expiry=20140612_strikeRight=262.5P_OPT_exchExPrim=KSE-KSE_KRW_multiplier=500000
			if(toDebug)
			//Tracker.trackWithErrorPrinting_LessExpensive(
			//Report.getOptionReporter(null).report(
			Tracker.printSysIO(
				"calculateImpliedVolatility_wait 1: reqId="+reqId
				+" contract="+contract.toString(true, true));
			
	
			double impliedVol = Double.NaN;
			
			try
			{
				//Integer witaInt = new Integer(0);
				long t0 = TimeDiffThread0.currentTimeMillis();
				
				
				if(TrackerTF.true_())
				{
					////!! This is better, as it does not freez the GUI
					ExecutorService execService = Executors.newFixedThreadPool(1);	//5

					
					CallableWithName_toRun<Double> call = new CallableWithName_toRun<Double>()
					{

						@Override
						public Double call() throws Exception
						{
							
							String threadNameS = "Callable_calculateImpliedVolatility_wait(final CEData, double, double)__WaitResult 355 hc="
									+ this.hashCode();
							
							UtilThread.appendMyThreadName(threadNameS);
							
							
							Tracker.printSysIO("requestContractD threadNameS="+threadNameS);


							////------------this is the contents to run--------------------------------------
							reqId_calcOptionEndedMap.put(reqId, false);
							reqId_calcOptionIV_ContrLocal_HM.put(reqId, ce);
							
							
							I_OptionsIV_DS i_OptionsIV_DS = getSocket1();
							i_OptionsIV_DS.getSocketOrAdd_Conn().calculateImpliedVolatility(
									reqId, contract, optionPrice, underlyingPrice);	//0.25, 53.6
							
							// To test, this may cause never return
							//contract.setM_strike(contract.getM_strike()+1);
							
							///////////////////////////////////////////////////////////////////////////////
							
							
							
							try
							{

								synchronized(reqId_calcOptionEndedMap)
								{
									@SuppressWarnings("unused")
									int waitCount = 0;

									LOOP_waitCalcIV:
									while (!reqId_calcOptionEndedMap.containsKey(reqId) ||
											!reqId_calcOptionEndedMap.get(reqId))
									{
										
										reqId_calcOptionEndedMap.wait();		//dsRealtime.wrapperData.wait();
									}
									
									


									if(toDebug)Tracker.printSysIO("calculateImpliedVolatility_wait: " + threadNameS
											+ " Callable 2.5: "+reqId
											+" "+reqId_calcOptionEndedMap.get(reqId));
									
									
									if(TrackerTF.false_() && ____.___FavoriteClasses_Options.toDebugIV_K200_NIFTY50)
										Tracker.exit(0);
									
								}

								if(contOpt.quoteOption!=null)
								{
									OptPriData optDataIV = contOpt.quoteOption.getOptDataLatest_CustComputation();
									
									return optDataIV.impliedVol;
								}
								
								return Double.NaN;

							}
							catch(Exception e)
							{
								e.printStackTrace();
								
								return Double.NaN;
							}

						}
					};
					
					Future<Double> future = execService.submit(call);



					//Less important thing. return null, if no result after 30 seconds
//					if(TrackerTF.false_())
					{
						Double ret = null;
						try
						{
							ret = future.get(___FavoriteClasses_Options.secondsToWait_reCalcGreeks1Contract, TimeUnit.SECONDS);
							
							if(ret!=null)
								impliedVol = ret.doubleValue();
						}
						catch (TimeoutException timeoutE)
						{
							
							
							//Report.getOptionReporter(null).report(
							//Tracker.exit(0,
							//Tracker.trackWithErrorPrinting_LessExpensive(
							Tracker.printSysIO(
									"Not returned ImpliedVol after "+___FavoriteClasses_Options.secondsToWait_reCalcGreeks1Contract
									+ " seconds in calculateImpliedVolatility_wait contract="+contract);
							
							if(___FavoriteClasses_Options.toDebug_notRectured_calcOptions &&
									notReturnedImpliedVolC++==3
									 || TrackerTF.false_())
							{
								UtilCollection.print(reqId_calcOptionEndedMap.entrySet(),
									"reqId_calcOptionEndedMap.entrySet()");
								
								
								Tracker.exit(0);
							}
								
							
							
							if(TrackerTF.false_())
							if(!__Price_Check.isValidPrice(ce.getContractInst().getM_strike()-2975))
							{
								Tracker.exit(0);
							}
							
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}

						System.out.println("671 after holding ret="+ret+" runnintMillSec="+(TimeDiffThread0.currentTimeMillis()-t0));

//						Tracker.exit(0);
					}


					//close the execService
					execService.shutdown();
					execService = null;
				}
				else
				{
					synchronized (reqId_calcOptionEndedMap)	//dsRealtime.wrapperData
					{
						UtilThread.appendMyThreadName("calculateImpliedVolatility_wait optionPrice="+optionPrice
								+" underlyingPrice="+underlyingPrice+" "
								+contract.toString(true, true));
						
						
						long tDif=TimeDiffThread0.currentTimeMillis()-t0;
						
						
						Tracker.printSysIO("calculateImpliedVolatility_wait 1.2 tDif="+tDif
								+" "+reqId_calcOptionEndedMap
								+" contract="+contract);
						
						
//						if(toDebug)
						if(____.___FavoriteClasses_Options.toDebug_K200CalcOptions
								&& contrID!=140070846 && contrID!=146276157 && contrID!=146276160
//							|| ____.___FavoriteClasses_Contracts.toDebugGammaScalpK200
						)
							Tracker.exit(0,
//							Tracker.printSysIO(
								"calculateImpliedVolatility_wait 1.5: t0="+t0+" tDif="+tDif
								+" "+___FavoriteClasses_Options.secondsToWait_reCalcGreeks1Contract);
						
		
		
						LOOP_waitCalcIV:
						while (!reqId_calcOptionEndedMap.containsKey(reqId) ||
								!reqId_calcOptionEndedMap.get(reqId))
						{
							if(___FavoriteClasses_Options.secondsToWait_reCalcGreeks1Contract>0 &&
									((tDif=TimeDiffThread0.currentTimeMillis()-t0)>___FavoriteClasses_Options.secondsToWait_reCalcGreeks1Contract*1000L))
							{
								Tracker.printSysIO("reqId_OptionCalcEndedMap.wait() tDif="+tDif);
								
								break LOOP_waitCalcIV;
							}
							
							
							reqId_calcOptionEndedMap.wait();		//dsRealtime.wrapperData.wait();
							
						}
						
						
		
						//Report.getEventReporter(null).report
						if(toDebug)Tracker.printSysIO("calculateImpliedVolatility_wait 2: "+reqId+" tDif="+tDif+" "+reqId_calcOptionEndedMap.get(reqId));
					
						
						if(TrackerTF.false_() && ____.___FavoriteClasses_Options.toDebugIV_K200_NIFTY50)
							Tracker.exit(0);
					}
					
					
					
					if(contOpt.quoteOption!=null)
					{
						Tracker.printSysIO("calculateImpliedVolatility_wait 2.1:"+(contOpt.quoteOption!=null?contOpt.quoteOption.toString(10): contOpt.quoteOption));
						Tracker.printSysIO("calculateImpliedVolatility_wait 2.2:"+(contOpt.quoteOption!=null?contOpt.quoteOption.getOptionsPricings_CustComputation(): contOpt.quoteOption) );
			
						//getOptionsPricingsCustOptionComputation().lastElement();
						OptPriData optDataIV = contOpt.quoteOption.getOptDataLatest_CustComputation();
						impliedVol = optDataIV.impliedVol;
						//Report.getEventReporter(null).report
			
					}
					
					
					
				}
				
				
				
				if(TrackerTF.false_() && ____.___FavoriteClasses_Options.toDebug_K200CalcOptions)
					Tracker.exit(0);
	
			}
			catch(Exception e)
			{
				e.printStackTrace();
				
				Tracker.exit(0, "Wait in calculateImpliedVolatility_wait");
				//Report.getEventReporter(null).report(ExceptionProcess.combineStackTrace(e));
			}
	
	
			@SuppressWarnings("unused")
			CEData _ce_  = contOpt;  //.get_ce_();
			if(toDebug)Tracker.printSysIO("calculateImpliedVolatility_wait 2.0:"+ce.getUnderlyingForOptionNETF()
					+" contOpt="+ce.toString(true));
	
	
	
			if(toDebug)Tracker.printSysIO("calculateImpliedVolatility_wait 3: "+UtilNumber.f3S(impliedVol)+" "+reqId);
	
			
			if(TrackerTF.false_())
			if(!__Price_Check.isValidPrice(impliedVol))
				Tracker.exit(0, "impliedVol="+impliedVol);
			
	
			return impliedVol;
			
		}
		catch (Throwable t)
		{
			t.printStackTrace();
			// Do not allow exceptions come back to the socket -- it will cause disconnects
			if(_UtilReport.toReport)Report.getEventReporter(null).report(ExceptionProcess.combineStackTrace(t));
	
			Tracker.exit(0);
			
			return Double.NaN;
		}
	
	}


	
	
	/**
	 * It first gets implied Volatility with options' and underlying's prices, then use the IV to get Greeks
	 * 
	 * @see #calculateImpliedVolatility_wait
	 * @see com.ib.client._EWrapperAdapter#tickOptionComputation(int, int, double, double, double, double, double, double, double, double)
	 * 
	 * check previous calculation with:
	 * alreadyRequested
	 * 
	 * 
	 */
	@Override
	public OptPriData calculateOptionGreeks(CEData_optionP_underlyingP ceData_optionP_underlyingP)
	{
		boolean toDebug = true;
		
		CEData contrOpt = ceData_optionP_underlyingP.contrOpt;
		int contrId = contrOpt.getContrId();
		
		double optionPrice = UtilNumber.r5(ceData_optionP_underlyingP.optionPrice);
		double underlyingPrice = UtilNumber.r5(ceData_optionP_underlyingP.underlyingPrice);
	
		if(toDebug)
			//Tracker.exit(0,
			Tracker.printSysIO(
					"calculateOptionGreeks optionPrice="+optionPrice+" underlyingPrice="+underlyingPrice);
		
		
		
		
		__ContractExtended___ ce = __ContractExtended___Dispatcher_1_CE.get_ce_(contrOpt);
		Contract contract = __ContractExtended___Dispatcher_1_CE.getContract(contrOpt);
	
	
		LinkedList<CEData_optionP_underlyingP> ceData_optionP_underlyingP_LL =
				contrId_CEData_optionP_underlyingP_LL_HM.get(contrId);
		
		if(ceData_optionP_underlyingP_LL!=null)
		{
			CEData_optionP_underlyingP ceData_optionP_underlyingP1ValidRes_Last = null;
			
			Loop_Stored:
			for(int i=0; i<ceData_optionP_underlyingP_LL.size(); i++)
			{
				ceData_optionP_underlyingP1ValidRes_Last = ceData_optionP_underlyingP_LL.get(i);
				
				if(ceData_optionP_underlyingP1ValidRes_Last.optPriData!=null &&
						ceData_optionP_underlyingP1ValidRes_Last.optPriData.validGreeks())
				{
					break Loop_Stored;
				}
			}
			
			
			if(ceData_optionP_underlyingP1ValidRes_Last!=null)
			{
				int diff = ceData_optionP_underlyingP.compareTo(ceData_optionP_underlyingP1ValidRes_Last);
				
				if(diff==0)
				{
					
					int contrIdLast = ceData_optionP_underlyingP1ValidRes_Last.contrOpt.getContrId();
					
					///!! assign quoteOption
					CEData contrOptLast = CEData_Dispatcher.getInstance(contrIdLast);
					if(contrOpt.quoteOption==null && contrOptLast.quoteOption!=null)
					{
						ce.copyFieldsMore(__ContractExtended___Dispatcher.getInstance(contrIdLast));
						
						contrOpt.quoteOption = contrOptLast.quoteOption;;
					}
					
					

					if(toDebug)
//						Tracker.exit(0,
						Tracker.printSysIO(
								"calculateOptionGreeks alreadyRequestedGreeks_samePs diff="+diff);
					
					long timeSinceLastReq = TimeDiffThread0.currentTimeMillis() - ceData_optionP_underlyingP1ValidRes_Last.reqTimeL;
					
					if(toDebug)Tracker.printSysIO("calculateOptionGreeks alreadyRequestedGreeks_samePs timeSinceLastReq="+timeSinceLastReq);
					
					
					if(timeSinceLastReq < Strategy_Trade_var.toWaitGreeksSec * 1000L)
					{
						if(contrOpt.quoteOption==null)
						{
							return new OptPriData(OptPriData_Status.CalcGreeks_lessThanWaitPeirod);
						}
						
						return contrOpt.quoteOption.getOptDataLatest_both();		//---------------------------------------
					}
					else
					{
						if(toDebug)Tracker.printSysIO("calculateOptionGreeks alreadyRequestedGreeks_samePs exceed "+Strategy_Trade_var.toWaitGreeksSec
							+ " timeSinceLastReq="+timeSinceLastReq);
						
					}
			
				}
			}

			
		}
	
		
	
		
		double impliedVol = Double.NaN;
		/**
		 * Should get from website?
		 * TrackerTF.false_() &&
		 */
		boolean isDerivativeK200 = ContractFactory_CheckDeriv.isDerivativeK200(contract.getM_symbol(),
				contract.getM_secType(), contract.getM_currency());
		
		boolean isDerivativeNIFTY50 = ContractFactory_CheckDeriv.isDerivativeNIFTY50(contract.getM_symbol(),
				contract.getM_secType(), contract.getM_currency());
		
		
		if(___FavoriteClasses_Options.toDebugIV_K200_NIFTY50 &&
				(isDerivativeK200 || isDerivativeNIFTY50))
		{
			// 0.222222
			impliedVol = ___FavoriteClasses_Options.toDebugIV_K200_NIFTY50_val;
		}
		else
		{
			impliedVol = calculateImpliedVolatility_wait(contrOpt, optionPrice, underlyingPrice);
		}
	
		
		if(TrackerTF.false_())
			Tracker.exit(0);
	
		
		if(toDebug)
//			Tracker.exit(0,
			Tracker.printSysIO(
				"calculateOptionGreeks impliedVol="+UtilNumber.r4(impliedVol)
				+" optionPrice="+optionPrice+" underlyingPrice="+underlyingPrice);
	
		
		
		if(UtilNumber.isMaxMin_infiniteNaN(impliedVol))
		{
			if(TrackerTF.false_())
				Tracker.exit(0);
			
			Tracker.printErrIO("calculateOptionGreeks NaN impliedVol="+UtilNumber.r4(impliedVol)
				+" optionPrice="+optionPrice+" underlyingPrice="+underlyingPrice);
			
			return new OptPriData(OptPriData_Status.Invalid_impliedVol);
		}
		
		
		OptPriData optPriData = calculateOptionPrice_wait(contrOpt, impliedVol, underlyingPrice);
		
		if(optPriData==null || OptPriData.status!=OptPriData_Status.Good)
		{
			return optPriData;
		}
		
		
		ceData_optionP_underlyingP.reqId = optPriData.reqId;
		ceData_optionP_underlyingP.reqTimeL = TimeDiffThread0.currentTimeMillis();
		ceData_optionP_underlyingP.optPriData =optPriData;
		
		///!! store
		if(ceData_optionP_underlyingP_LL==null)
		{
			ceData_optionP_underlyingP_LL = new LinkedList<CEData_optionP_underlyingP>();
			contrId_CEData_optionP_underlyingP_LL_HM.put(contrId, ceData_optionP_underlyingP_LL);
		}
		ceData_optionP_underlyingP_LL.add(ceData_optionP_underlyingP);
		

		
		if(optPriData!=null && optPriData.validGreeks() &&
				(____.___FavoriteClasses_Options.toDebug_K200CalcOptions && isDerivativeK200))
			Tracker.exit(0, "After calculateOptionPrice_wait optPriData="+optPriData.toStringMoreGreeks()
					+" "+contrOpt);
		
		
		
		if(optPriData==null || TrackerTF.false_())
		{
			
			return new OptPriData(OptPriData_Status.Invalid_res_calculateOptionPrice_wait);
		}
	
	
		optPriData.price = optionPrice;
		optPriData.underlyingP = underlyingPrice;
	
		
		
		if(TrackerTF.false_())Tracker.exit(0, "After calculateOptionPrice_wait optPriData="+optPriData.toStringMoreGreeks()
				+" "+contrOpt);


		return optPriData;
	}

	
	/**
	 * Used when market is closed
	 * 
	 * HSI's index stop tradin at 4PM, while its OPT (on index) stop trading at 4:15PM
	 * 
	 * @param ceOpt
	 * @return
	 */
	public double getP_OptLastClose(__ContractExtended___ ceOpt)
	{

		boolean toDebug = true;
		
		boolean toMergeIntraday = false;
		
		
//		__ContractExtended___ ceOpt = __ContractExtended___Dispatcher_1_CE.get_ce_(ceDataOpt);
		
		

		//use historical data: 15min
		//Dispatcher.getSocket().requestHistoricalData(tickerID, contractE, endDateTime, duration, barSize, whatToShow, useRTH, formatDate, waitInsideThisMethod)
		QuoteBar[] quoteRecent2 = ceOpt.getQuoteRecent2();
		if(quoteRecent2==null)
			quoteRecent2 = QuoteLastDays.getLastOHLCFromDBYahooGoogleIB(ceOpt.getContrId(), toMergeIntraday, true);
		
		
		if(toDebug)Tracker.printSysIO("3527 after ceOpt="+ceOpt.toString(true, true));

		CEData_QuoteBars ceData_QB = CEData_QuoteBars.getCeData_QuoteBar(
				// ceDataOpt.getContrId()
				ceOpt.getContrId()
			);
		QuoteBar qbLast = ceData_QB.getQuoteHistoryOfBars().getLastQuote();
		
		

		if(toDebug)Tracker.printSysIO("3528 quoteLastDays="+quoteRecent2
				+" qhBarF="+ceData_QB.getQuoteHistoryOfBars().getFirstQuote()
				+" qhBarL="+qbLast);

		
		

    	

		double optionPrice = Double.NaN;
		
		if(quoteRecent2!=null && quoteRecent2[0]!=null)
		{
			if(toDebug)System.err.println("3529 quoteRecent="+quoteRecent2[0]+" quoteRecent1="+quoteRecent2[1]);
			
			if(!UtilNumber.isMaxMin_infiniteNaN(quoteRecent2[0].getaC()))
			{
				optionPrice = quoteRecent2[0].getaC();
			}
			else
			{
				optionPrice = quoteRecent2[0].getC();
			}
		}
		else
		{
			optionPrice = qbLast.getC();
			if(UtilNumber.isMaxMin_infiniteNaN(optionPrice))
				optionPrice = qbLast.getaC();
			
			if(toDebug)System.err.println("3530 optionPrice="+optionPrice);
		}



		
		if(toDebug)System.err.println("3550 optionPrice="+optionPrice);
		
		
		if(___FavoriteClasses_Options.toDebugUnderlyLast & TrackerTF.false_())
    		Tracker.exit(0, "Use last day's Option quotes");
    	
		
		return optionPrice;
	
	}
	
	
	/**
	 * Used when market is closed
	 * 
	 * get yesterday's quotes from Yahoo does not work for today's after hour trading,
	 * as at the time, today's close is not available yet.
	 * It works only for pre-open trading, when yesterday's data are already available.
	 * 
	 * @param ceUnderlying
	 * @return
	 */
	public double getP_UnderlyLastClose(__ContractExtended___ ceUnderlying)
	{
//		__ContractExtended___ ceUnderlying = ContractFactory_UnderlyingSearch.searchNAssignUnderlying(ceOpt, true);
		
		boolean toDebug = true;
		
		boolean toMergeIntraday = false;
		
		

		//use historical data: 15min
		//Dispatcher.getSocket().requestHistoricalData(tickerID, contractE, endDateTime, duration, barSize, whatToShow, useRTH, formatDate, waitInsideThisMethod)
		

		double underlyingPrice = Double.NaN;
		


		////! The old version
		if(___FavoriteClasses_Options.toDebugUnderlyLast_useIB
//				&& TrackerTF.false_()
		)
		{
			QuoteBar qbLastUnderlying = QuoteLastDays.getQuoteLastAvailable(
					//__ContractExtended___Dispatcher_1_CE.get_ce_(ceDataUnderlying),
					ceUnderlying,
					___FavoriteClasses_Options.toDebugUnderlyLast_useIB);
			
			
			if(qbLastUnderlying!=null)
			{
				underlyingPrice = qbLastUnderlying.getC();
				if(UtilNumber.isMaxMin_infiniteNaN(underlyingPrice))
					underlyingPrice = qbLastUnderlying.getaC();
			}

				
		}
		else
		{
			
			QuoteBar qbLastUnderlying = null;
			
			if(TrackerTF.true_())
			{
				LinkedList<QuoteBar> quoteBarLL = DataMerged.mergedGoogYahooData(ceUnderlying, 14);
				qbLastUnderlying = quoteBarLL.getLast();
				
			}
			else
			{
				QuoteBar[] quoteRecent2 = ceUnderlying.getQuoteRecent2();
				if(quoteRecent2==null)
					quoteRecent2 = QuoteLastDays.getLastOHLCFromDBYahooGoogleIB(ceUnderlying.getContrId(), toMergeIntraday, false);
				
				
				if(toDebug)Tracker.printSysIO("3541 after ceUnderlying="+ceUnderlying.toString(true, true));
		
				CEData_QuoteBars ceUnderlying_QB = CEData_QuoteBars.getCeData_QuoteBar(
						//ceDataUnderlying.getContrId()
						ceUnderlying.getContrId()
					);
				qbLastUnderlying = ceUnderlying_QB.getQuoteHistoryOfBars().getLastQuote();
		
				
				if(toDebug)Tracker.printSysIO("3542 quoteRecent2="+quoteRecent2
						+" qhBarF="+ceUnderlying_QB.getQuoteHistoryOfBars().getFirstQuote()
						+" qbLastUnderlying="+qbLastUnderlying);
		
				
				
				if(quoteRecent2!=null && quoteRecent2[0]!=null)
				{
					if(toDebug)System.err.println("3543 quoteRecent="+quoteRecent2[0]+" quoteRecent1="+quoteRecent2[1]);
					underlyingPrice = quoteRecent2[0].getaC();
				}
				
				
			}

			
			
			
			if(qbLastUnderlying!=null)
			{
				underlyingPrice = qbLastUnderlying.getC();
				
				if(UtilNumber.isMaxMin_infiniteNaN(underlyingPrice))
					underlyingPrice = qbLastUnderlying.getaC();
			}

			
			if(toDebug)System.err.println("3545 underlyingPrice="+underlyingPrice);
			
		}
		
		
		if(toDebug)System.err.println("3550 last underlyingPrice="+underlyingPrice);
		
		
    	if(___FavoriteClasses_Options.toDebugUnderlyLast & TrackerTF.false_())
    		Tracker.exit(0, "Use last day's Underlying quotes useIB="
    			+___FavoriteClasses_Options.toDebugUnderlyLast_useIB);
    	
		
		return underlyingPrice;
	
	}
	
	
	
	public static int invalidOptPriceC = 0;
	
	/**
	 *  1 for getMidQWaits10SecOrLastClose, 2 for BAL with Future, 3 for BAL no Futures (sleep and wait)
	 */
	enum TypeOfGetPrices
	{
		MidQWaits10SecOrLastClose,
		BAL_viaSleepNWait_viaFuture,
		BAL_viaSleepNWait
	};
	
	
	/**
	 * This will first get market data, then to calculate.
	 * It returns in tickOptionComputation the TickType 10 11 12 13, and also 53
	 * 
	 * In case, no data requested for a Option contract
	 * @see com.algomodJB.platform.trader.ds.I_Realtime_DS#requestMarketData(CEData)
	 * 
	 * It may takes couple of minutes to return. Use a new thread?
	 * 
	 */
	@Override
	public OptPriData calculateOptionGreeks(CEData ceDataOpt,
			boolean toUseRealtimeP)
	{
		final boolean toDebug = true;
		final boolean toDebugFut = true;
		
		//// typeOfGetPrices = MidQWaits10SecOrLastClose works even for FDemo account
		TypeOfGetPrices typeOfGetPrices = TypeOfGetPrices.MidQWaits10SecOrLastClose;
		boolean toWait = true;
		
		
	
		Contract contract = __ContractExtended___Dispatcher_1_CE.getContract(ceDataOpt);
	
		
		if(toDebug)
		{
			Tracker.printSysIO("3520 toUseRealtimeP="+toUseRealtimeP
					+" "+UtilDate_HMS.getHMiS_mill()
					+" ceDataOpt="+ceDataOpt.toString(true, true)
					+" contract="+contract);
			//Tracker.trackWithErrorPrinting_LessExpensive("3520 ceDataOpt="+ceDataOpt);
			
//			Tracker.exit(0, "calculateOptionGreeks(CEData, boolean)");
		}
		
	
	
		if(ceDataOpt==null || contract.getM_conId()<=0)
		{
			ceDataOpt.get_dataOptionsTrackAL().add("Invalid option contract="+ceDataOpt);
			
			Tracker.exit(0, "calculateOptionGreeks(CEData, boolean)");
			
			return new OptPriData(OptPriData_Status.Invalid_OptCEData_calcGreeks);
		}

		
//		CEData _ce_  = ceDataOpt;  //.get_ce_();
		__ContractExtended___ ceOpt = __ContractExtended___Dispatcher_1_CE.get_ce_(ceDataOpt);
		__ContractExtended___ ceUnderlying = ContractFactory_UnderlyingSearch.searchNAssignUnderlying(ceOpt, true);
		
		
		if(TrackerTF.false_() || toDebug)
			Tracker.printSysIO("3521 ceUnderlying=" + ceUnderlying + " " + ceUnderlying.toString(true, true, true));
	
		if(toDebug)
			Tracker.printSysIO("3522 ceOpt=" + ceOpt.toString(true, true, true)
				+" ceDataOpt="+ceDataOpt);
	
		
		Contract contrUnderlying= ceUnderlying.getContract();
		if(toDebug)
			Tracker.printSysIO("3523 contrUnderlying=" + contrUnderlying);
		
		
		CEData ceDataUnderlying = null;
		////! check ceUnderlying.m_symbol is necessary to avoid blank contract with only m_conId
		if(ceUnderlying!=null && ceUnderlying.getContrId()>0 &&
				contrUnderlying.getM_symbol()!=null && contrUnderlying.getM_symbol().length()>0)
		{
			ceDataUnderlying = CEData_Dispatcher.convertToCEData(ceUnderlying);	//ContractExtendedWithHistData.convertToCEData(ceUnderlying);
		
			
			if(toDebug)
				Tracker.printSysIO("3524 ceDataUnderlying=" + ceDataUnderlying);
		}
		else
		{
			ceDataUnderlying = CEData_Dispatcher.convertToCEData(
					C_searchNAssignUnderlying_.searchNAssignUnderlying(ceOpt));
			
			
			if(toDebug)
				Tracker.printSysIO("3524.5 ceDataUnderlying=" + ceDataUnderlying);
		}

	
		Contract contractUnderlying = Contract_Dispatcher.getStored(ceDataUnderlying.getContrId());
		
		if(toDebug)
			Tracker.printSysIO("3524.7 contractUnderlying=" + contractUnderlying);
		
		//3524.7 contractUnderlying=conId=51497778@28697918 localSymbol=NIFTY50_symbol=NIFTY50_IND_exchExPrim=NSE-null_INR_multiplier=1
		
		//ContractExtended ceUnderlying = ceDataOpt.getUnderlyingForOptionNETF();
	
	
		if(TrackerTF.false_())Tracker.exit(0);
	
	
		double optionPrice = Double.NaN;
		double underlyingPrice = Double.NaN;
	
		boolean realtimeDataAvailOpt = TradingSchedule_CE.isRealtimeAvailable(ceOpt);
		boolean realtimeDataAvailUnderly = TradingSchedule_CE.isRealtimeAvailable(ceUnderlying);
		boolean realtimeDataAvail4Both = realtimeDataAvailOpt && realtimeDataAvailUnderly;
		boolean isIndOrStkUnderlying = ContractFactory_Check.isIndexOrSTK(contractUnderlying.getM_secType());
		
		if(toDebug)Tracker.printSysIO("3525 not"
				+ " realtimeDataAvailUnderly="+realtimeDataAvailUnderly
				+ " realtimeDataAvail4Both="+realtimeDataAvail4Both
				+ " ceDataUnderlying="
				+ (ceDataUnderlying!=null? contractUnderlying.toString(true, true, true):""));
		
		if(toDebug)Tracker.printSysIO("3526 not "
				+ " realtimeDataAvailOpt="+realtimeDataAvailOpt
				+ " ceOpt="+(ceOpt!=null? ceOpt.toString(true, true, true):""));
		
		

		
		
		////! Use last day's quote instead
		if(isIndOrStkUnderlying && (!realtimeDataAvail4Both || !toUseRealtimeP))
		{
			underlyingPrice = getP_UnderlyLastClose(ceUnderlying);
			
			if(!__Price_Check.isValidPrice(underlyingPrice))
				Tracker.printErrIO("1335 Invalid yesterday underlyingPrice="+underlyingPrice);
				
			//281 error tickerId=9 errorCount=68 msg=errorCode=162=Historical Market Data Service error message:
			//	No data of type EODChart is available for the exchange 'BEST' and the security type 'Option' and '1 y' and '1 day'
			optionPrice = getP_OptLastClose(ceOpt);
			
			if(!__Price_Check.isValidPrice(optionPrice))
				Tracker.printErrIO("1336 Invalid yesterday optionPrice="+optionPrice);
		}
		else
		{	////for real-time update
			I_Realtime_DS i_Realtime_DS = C_realTime_.getSocket1();
			Res_requestMarketData succOption = i_Realtime_DS.requestMarketData(ceDataOpt);
	
			if(toDebug)System.err.println("3530 succOption="+succOption
					+" ceDataOpt="+ceDataOpt.toString(true, true));
	
			//        	if(TrackerTF.true_())
			//        		return null;

			
			Res_requestMarketData succUnderlying = null;
			//0 for already requested before
			if(succOption.reqId_or_code>=0 && ceDataUnderlying!=null)
			{
				succUnderlying = i_Realtime_DS.requestMarketData(ceDataUnderlying);
	
				if(toDebug)System.err.println("3532 succUnderlying="+succUnderlying
						+" underly="+__ContractExtended___Dispatcher_1_CE.get_ce_(ceDataUnderlying).toString(true, true));
	
				
				if(TrackerTF.false_())
				{
					//
					if(succUnderlying==Res_requestMarketData.unknownError
							&& succUnderlying.reqId_or_code>0)
					{
						i_Realtime_DS.cancelMarketData(ceDataOpt);
						i_Realtime_DS.cancelMarketData(ceDataUnderlying);
	
						ceDataOpt.get_dataOptionsTrackAL().add("Invalid underlying="+succUnderlying);
						
						return new OptPriData(OptPriData_Status.Invalid_UnderlyP_Request);	////!
					}
				}
	
	
			}
	
	
			if(toDebug)System.err.println("calculateOptionGreeks:  succOption="+succOption+" succUnderlying="+succUnderlying);
			if(TrackerTF.false_())
				Tracker.exit(0);
	
			
			

			//BAL
			if(typeOfGetPrices==TypeOfGetPrices.BAL_viaSleepNWait_viaFuture || typeOfGetPrices==TypeOfGetPrices.BAL_viaSleepNWait)
			{
				if(typeOfGetPrices==TypeOfGetPrices.BAL_viaSleepNWait_viaFuture)
				{
					final CEData ceDataUnderlying_ = ceDataUnderlying;
					final CEData ceDataOpt_ = ceDataOpt;
		
					ExecutorService execService = Executors.newFixedThreadPool(1);	//5
		
					CallableWithName_toRun<double[][]> call = new CallableWithName_toRun<double[][]>()
					{
		
						@Override
						public double[][] call() throws Exception
						{
							UtilThread.appendMyThreadName(
									"Callable_calculateOptionGreeks(ContractExtendedWithHistData, boolean) 837 hc="+this.hashCode());
		
		
							////! both are successed
							double[] balUnderlying = Quote_BAL.getPriceBidAskLast(ceDataUnderlying_, -1, "underlying");
							double[] balOpt = Quote_BAL.getPriceBidAskLast(ceDataOpt_, -1, "opt");
		
		
							try
							{
								int waitC = 0;
								int waitCThreashold = 15;
								
								if(toDebugFut)
									Tracker.printSysIO("848 waitCThreashold="+waitCThreashold
										+" balOpt="+balOpt+" balUnderlying="+balUnderlying);
		
								
//								Loop_Wait:
								while(balOpt==null || balUnderlying==null)
								{
									waitC++;

									//wait();

									////! this is necessary to request real-time quotes?	false &&
									if(waitC>waitCThreashold)
									{
										//break Loop_Wait;

										if(toDebugFut)Tracker.printSysIO("Waited "+waitCThreashold+" times. Will return null.");

										optionsGreeksInvalidDataHM.put(__ContractExtended___Dispatcher_1_CE.getContract(ceDataOpt_).getM_conId(), ceDataOpt_);
										ceDataOpt_.get_dataOptionsTrackAL().add("Return null after wait"+waitCThreashold+" times for prices");
										
										return null;
									}
									

									try
									{
										if(balOpt==null)
										{
											balOpt = Quote_BAL.getPriceBidAskLast(ceDataOpt_, -1, "opt");
										}
										
										
										if(balUnderlying==null)
										{
											balUnderlying = Quote_BAL.getPriceBidAskLast(ceDataUnderlying_, -1, "underlying");
										}

										
										TimeDiffThread0.sleep(200);
										Thread.yield();
										

										System.err.println("Future Waiting in calculateOptionGreeks: "+waitC
												+" balOpt="+balOpt+" balUnderlying="+balUnderlying
												+" underlying="+ceDataUnderlying_+" opt="+ceDataOpt_);

									} catch (Exception e) {
										e.printStackTrace();
									}
									
								}	//while
								
		
								return new double[][]{balUnderlying, balOpt};
								
							}
							catch(Exception e)
							{
								e.printStackTrace();
								
								ceDataOpt_.get_dataOptionsTrackAL().add("Return null on Exception in getting prices");
								
								return null;
							}
		
						}
					};
		
					Future<double[][]> future = execService.submit(call);
		
		
					//Less important thing. return null, if no result after 30 seconds
					double[][] futureRes = null;
					
					try
					{
						futureRes = future.get();
					}
					catch (Exception e)
					{
						e.printStackTrace();
		
						Tracker.exit(0,
//						Tracker.printSysIO(
								"Not returned calcXXXGreeks after 10 seconds in calculateOptionGreeks");
					}
		
					Tracker.printSysIO("1251 after holding ret="+futureRes);
		
		
					//close the execService
					execService.shutdown();
					execService = null;
				}
				else if(typeOfGetPrices==TypeOfGetPrices.BAL_viaSleepNWait)	///! The old version to sleep and wait for certain seconds
				{
					double[] balUnderlying_ = Quote_BAL.getPriceBidAskLast(ceDataUnderlying, -1, "underlying");
					double[] balOpt_ = Quote_BAL.getPriceBidAskLast(ceDataOpt, -1, "opt");
		
					int waitC = 0;
					int waitCThreashold = 5;
		
//					Loop_Wait:
						while(balOpt_==null || balUnderlying_==null){
							waitC++;
		
							////! this is necessary to request real-time quotes?	false &&
							if(waitC>waitCThreashold)
							{
								//break Loop_Wait;
		
								if(toDebug)System.err.println("---Waited "+waitCThreashold+" times. Will return null.");
		
								return new OptPriData(OptPriData_Status.Invalid_WaitMore_viaSleepNWait);
							}
		
		
							TimeDiffThread0.sleep(200);
							
		
							balOpt_ = Quote_BAL.getPriceBidAskLast(ceDataOpt, -1, "opt");
							balUnderlying_ = Quote_BAL.getPriceBidAskLast(ceDataUnderlying, -1, "underlying");
		
							TimeDiffThread0.sleep(1000);
		
							System.err.println("......Waiting in calculateOptionGreeks: "+waitC
								+" balOpt="+balOpt_+" balUnderlying="+balUnderlying_);
		
		
						}
		
		
				}
		
		
				double[] balUnderlying = Quote_BAL.getPriceBidAskLast(ceDataUnderlying, -1, "optAfterWait");
				double[] balOpt = Quote_BAL.getPriceBidAskLast(ceDataOpt, -1, "optAfterWait");
		
		
		
				if(balOpt!=null)
				{
					//use option's mid-quote
					optionPrice = (balOpt[0]+balOpt[1])/2;
					
					
					if(toDebug)UtilArray.printArray(balOpt, "balOpt 981");
					
					
					if(toDebug)Tracker.printSysIO("4131: optionPrice="+optionPrice+" "+balOpt[0]+" "+balOpt[1]+" "+balOpt[2]);
				}
				else
				{
					if(toDebug)Tracker.printSysIO("4132: null balOpt ceDataOpt="+ceDataOpt.toString(true, true));
				}
		
				
				
				if(balUnderlying!=null)
				{
					UtilArray.printArray(balUnderlying, "balUnderlying 993");
					
					underlyingPrice = balUnderlying[2];	//use underlying's  last price
					
					if(toDebug)Tracker.printSysIO("4133: underlyingPrice="+underlyingPrice);
				}
				else
				{
					if(toDebug)Tracker.printSysIO("4133: Null balUnderlying. ceDataUnderlying="+ceDataUnderlying);
				}
				
			} // BAL---end	if(typeOfGetPrices==TypeOfGetPrices.BAL_viaSleepNWait_viaFuture || typeOfGetPrices==TypeOfGetPrices.BAL_viaSleepNWait)
			else if(typeOfGetPrices==TypeOfGetPrices.MidQWaits10SecOrLastClose)
			{
				optionPrice = Quote_Mid_wait.getMidQWaits10SecOrLastClose(ceDataOpt, false,
						toWait, false);
				
				
//				Tracker.exit(0,
				Tracker.printSysIO(
						"calculateOptionGreeks 10sec optionPrice="+optionPrice+" "+ceDataOpt.toString(true, true));
				
				
				

				underlyingPrice = Quote_Mid_wait.getMidQWaits10SecOrLastClose(ceDataUnderlying, false,
						toWait, false);

				Tracker.printSysIO(
						"calculateOptionGreeks 10sec underlyingPrice="+underlyingPrice+" "+ceDataOpt.toString(true, true));
			
			}
	
			
		}	////for real-time update	-----end

		
		

		OptPriData optPriDataOld = null;
		String oldPricesS = null;
		if(ceDataOpt.quoteOption!=null)
		{
			optPriDataOld = ceDataOpt.quoteOption.getOptDataLatest_CustComputation();
			
			if(optPriDataOld!=null)
				oldPricesS = "Old optPrice=" + UtilNumber.r3(optPriDataOld.price)
					+ " modelPrice=" + UtilNumber.r3(optPriDataOld.modelPrice)
					+ " underlyingP=" + optPriDataOld.underlyingP
					+ " now="+UtilDate.getYMD_HMiS();
		}
		
		
		
		////! 3550 optionPrice=-1.0 underlyingPrice=54.295  //When there is no option quote, optionPrice=-1.0
		if(optionPrice<0 || UtilNumber.isMaxMin_infiniteNaN(optionPrice))
		{
			if(TrackerTF.false_())
			{
				QuoteBar qbY = null;
				if(TrackerTF.true_())
				{
					qbY = __ContractExtended___2.getQuoteYesterdayOrBefore(ceOpt);
				}
				else
				{
					///! Old version
					qbY = QuoteLastDays.getQuoteLastAvailable(ceOpt, false);
				}
				
				if(qbY!=null)
					optionPrice = qbY.getC();
			}
			
	

			
			String invalidOptPrice = "Invalid optionPrice="+optionPrice + " oldPrices=["+oldPricesS+"]";
			
			
			ceDataOpt.get_dataOptionsTrackAL().add(invalidOptPrice);
			
			
//			if(invalidOptPriceC%5==0)
//			Tracker.exit(0,
			Tracker.printSysIO(
					"calculateOptionGreeks "+invalidOptPrice+" invalidOptPriceC="+invalidOptPriceC
					+" toUseOldOptionPrice_ifNotUpdated="+___FavoriteClasses_Options.toUseOldOptionPrice_ifNotUpdated
					+" "+ceDataOpt.toString(true, true));
			
			
			
			if(optPriDataOld!=null && ___FavoriteClasses_Options.toUseOldOptionPrice_ifNotUpdated)
			{
				long timeSinceOld = TimeDiffThread0.currentTimeMillis() - optPriDataOld.time;
				
				Tracker.printSysIO("Invalid optionPrice timeSinceOld="+timeSinceOld
						+" "+UtilDate.getYMD_HMiS()
						+" "+UtilDate.getYMD_HMiS(optPriDataOld.time));
				
				
				return optPriDataOld;
			}
			else
			{
				return new OptPriData(OptPriData_Status.Invalid_OptP);
			}
			
		}
		
		
	
		if(toDebug)
		{
			Tracker.printSysIO("3551 optionPrice="+optionPrice+" underlyingPrice="+underlyingPrice+" "+toUseRealtimeP
					+" "+oldPricesS);
			Tracker.printSysIO("3552 underlyingPrice="+underlyingPrice+" toUseRealtimeP="+toUseRealtimeP+" ceUnderlying="+ceUnderlying);
			Tracker.printSysIO("3553 ceDataOpt="+ceDataOpt);
			
			
		}
		
		
		if(TrackerTF.false_() && ___FavoriteClasses_Options.toDebug_K200CalcOptions)
			Tracker.exit(0, "1202");
		
	
		
		
		
		if(underlyingPrice<=0 || UtilNumber.isMaxMin_infiniteNaN(underlyingPrice))
		{
			//underlyingPrice=-1 for K200: 		3560 can not proceed due to invalid prices. optionPrice=11.425 underlyingPrice=-1
			//underlyingPrice=NaN for NIFTY50: 	3560 can not proceed due to invalid prices. optionPrice=11.425 underlyingPrice=NaN
			System.err.println("3560 can not proceed due to invalid prices. optionPrice="+optionPrice
					+" underlyingPrice="+underlyingPrice
					+" "+ceDataUnderlying);
	
			//to debug
			if(TrackerTF.false_())
				Tracker.exit(0);
	
			
			String invalidUnderlyingPrice = "Invalid underlyingPrice="+underlyingPrice;
			
			ceDataOpt.get_dataOptionsTrackAL().add(invalidUnderlyingPrice);
			
			
			Tracker.printSysIO("calculateOptionGreeks "+invalidUnderlyingPrice);
			
			
			
			return new OptPriData(OptPriData_Status.Invalid_UnderlyP);
		}
	
		
		
		
		if(___FavoriteClasses_Options.toDebug_optionPriceTooLarge>0)
		{
			if(optionPrice>___FavoriteClasses_Options.toDebug_optionPriceTooLarge)
			{
				Tracker.exit(0, "Too large optionPrice="+optionPrice
					+" toDebug_optionPriceTooLarge="+___FavoriteClasses_Options.toDebug_optionPriceTooLarge);
			}
			
		}
		
	
		
		CEData_optionP_underlyingP ceData_optionP_underlyingP =
				new CEData_optionP_underlyingP(ceDataOpt, optionPrice, underlyingPrice);
		
		OptPriData optPriData = calculateOptionGreeks(ceData_optionP_underlyingP);
		

		
		
		//TrackerTF.false_()
		if(TrackerTF.false_() && (optionPrice>0.01 && underlyingPrice>0.010) )
			Tracker.exit(0, "calculateOptionGreeks optPriData="+optPriData.toString());
		
		
		
		
		
		//if run once only, to cancel data
		if(TrackerTF.false_())
		{
			C_realTime_.getSocket1().cancelMarketData(ceDataOpt);
	
			if(toUseRealtimeP)
				C_realTime_.getSocket1().cancelMarketData(ceDataUnderlying);
		}
	
	
		
		return optPriData;
	
	}

	
	
	Hashtable<Integer, Long> contrId_lastCalcTimeL_HT = new Hashtable<Integer, Long>();
	
	/**
	 * First we check if we need toUseRealtimeP: trading hours
	 * 
	 * We also check need to wait for some time
	 * @see ____.___FavoriteClasses_Options#secondsToWait_reCalcGreeks1Contract
	 * 
	 * @see com.algomodJB.platform.strategy.options.StrategyOption#calcImplVol()
	 * 
	 * OldName: calculateOptionGreeks_afterCheck
	 * 
	 */
	@Override
	public OptPriData calculateOptionGreeks_afterCheckLastRun(CEData ceDataOpt)
	{
		boolean toDebug = true;
		
		int contrId = ceDataOpt.getContrId();
		
		/**
		 * All these have been moved to
		 * 
		 * @see #calculateOptionGreeks(CEData, boolean)
		 */
		if(TrackerTF.false_())
		{
			__ContractExtended___ ce = __ContractExtended___Dispatcher.getInstance(contrId, true);
			
			//India index,
			boolean noRealTimeData4Under = ContractFactory_Check_Underlying.isIndIndia_Underlying(ce);
			
			boolean isRealtimeAvailable = TradingSchedule_CE.isRealtimeAvailable(ce);
			boolean toUseRealtimeP = !noRealTimeData4Under && isRealtimeAvailable;
			
			Tracker.printSysIO("contractPosGreeksMI: noRealTimeData4Under="+noRealTimeData4Under
					+ " isTrading="+isRealtimeAvailable+" toUseRealtimeP="+toUseRealtimeP);
		}

		
		
		if(toDebug)Tracker.printSysIO("calculateOptionGreeks_afterCheck ceDataOpt="+ceDataOpt);
		
		
		
		Long tLast = contrId_lastCalcTimeL_HT.get(contrId);
		
		long t1 = TimeDiffThread0.currentTimeMillis();
		
		OptPriData optPriData = null;
		
		if(tLast!=null)
		{
			if(___FavoriteClasses_Options.secondsToWait_reCalcGreeks1Contract>0 &&
					t1-tLast<___FavoriteClasses_Options.secondsToWait_reCalcGreeks1Contract*1000)
			{
				if(toDebug)Tracker.printSysIO("calculateOptionGreeks_afterCheck t1="+t1
						+" tLast="+tLast+" "+(t1-tLast));
				
				
				if(ceDataOpt.quoteOption!=null && ceDataOpt.quoteOption.size()>0)
				{
					optPriData = ceDataOpt.quoteOption.getOptionsPricings_CustComputation().lastElement();
					
					if(toDebug)Tracker.printSysIO("calculateOptionGreeks_afterCheck optPriData="+optPriData);
					
					
					return optPriData;
				}
				else
				{
					return null;
				}
				
			}
			
		}

		
		
		if(toDebug)Tracker.printSysIO("calculateOptionGreeks_afterCheck before calc t1="+t1
				+" tLast="+tLast+" "+(tLast==null? null : t1-tLast));
		
		
		contrId_lastCalcTimeL_HT.put(contrId, t1);
		optPriData = calculateOptionGreeks(ceDataOpt, true);
		
		
		return optPriData;
	}




	// @Override
	// public __ContractExtended___ searchContByReqID(int reqId,
	// boolean toShowIDStr) {
	// return null;
	// }
	//
	//
	//
	// @Override
	// public __ContractExtended___ searchContByReqID(int reqId,
	// boolean toShowIDStr, boolean marketDataOnly) {
	// return null;
	// }

	
	
	/**
	 * return new double[]{underlyingPrice, optionPrice};
	 */
	@Deprecated
	public double[] getPricesYesteday(CEData ceDataOpt, CEData ceDataUnderlying)
	{
		boolean toDebug = true;
		
		boolean toMergeIntraday = false;
		
		
		__ContractExtended___ ceOpt = __ContractExtended___Dispatcher_1_CE.get_ce_(ceDataOpt);
		__ContractExtended___ ceUnderlying = ContractFactory_UnderlyingSearch.searchNAssignUnderlying(ceOpt, true);
		
		

		//use historical data: 15min
		//Dispatcher.getSocket().requestHistoricalData(tickerID, contractE, endDateTime, duration, barSize, whatToShow, useRTH, formatDate, waitInsideThisMethod)
		QuoteBar[] quoteRecent2 = ceOpt.getQuoteRecent2();
		
		if(quoteRecent2==null)
			quoteRecent2 = QuoteLastDays.getLastOHLCFromDBYahooGoogleIB(ceOpt.getContrId(), toMergeIntraday, true);
		
		
		if(toDebug)Tracker.printSysIO("3527 after ceOpt="+ceOpt.toString(true, true));

		CEData_QuoteBars ceData_QB = CEData_QuoteBars.getCeData_QuoteBar(ceDataOpt.getContrId());
		QuoteBar qbLast = ceData_QB.getQuoteHistoryOfBars().getLastQuote();

		if(toDebug)Tracker.printSysIO("3528 quoteLastDays="+ceOpt.getQuoteRecent2()
				+" qhBarF="+ceData_QB.getQuoteHistoryOfBars().getFirstQuote()
				+" qhBarL="+qbLast);

		
		

    	

		double optionPrice = Double.NaN;
		double underlyingPrice = Double.NaN;
		
		if(quoteRecent2!=null && quoteRecent2[0]!=null)
		{
			if(toDebug)System.err.println("3529 quoteRecent="+quoteRecent2[0]+" quoteRecent1="+quoteRecent2[1]);
			
			if(!UtilNumber.isMaxMin_infiniteNaN(quoteRecent2[0].getaC()))
			{
				optionPrice = quoteRecent2[0].getaC();
			}
			else
			{
				optionPrice = quoteRecent2[0].getC();
			}
		}
		else
		{
			optionPrice = qbLast.getC();
			if(UtilNumber.isMaxMin_infiniteNaN(optionPrice))
				optionPrice = qbLast.getaC();
			
			if(toDebug)System.err.println("3530 optionPrice="+optionPrice);
		}





		////! The old version
		if(TrackerTF.false_())
		{
			QuoteBar qbYestUnderlying = QuoteLastDays.getQuoteLastAvailable(
					__ContractExtended___Dispatcher_1_CE.get_ce_(ceDataUnderlying), false);
			
			if(qbYestUnderlying!=null)
			{
				underlyingPrice = qbYestUnderlying.getC();
				if(UtilNumber.isMaxMin_infiniteNaN(underlyingPrice))
					underlyingPrice = qbYestUnderlying.getaC();
			}

				
		}
		else
		{
			
			QuoteBar qbLastUnderlying = null;
			
			if(TrackerTF.true_())
			{
				LinkedList<QuoteBar> quoteBarLL = DataMerged.mergedGoogYahooData(ceUnderlying, 14);
				qbLastUnderlying = quoteBarLL.getLast();
				
			}
			else
			{
				QuoteBar[] quoteRecent2Underly = ceUnderlying.getQuoteRecent2();
				if(quoteRecent2Underly==null)
					quoteRecent2Underly = QuoteLastDays.getLastOHLCFromDBYahooGoogleIB(ceUnderlying.getContrId(), toMergeIntraday, false);
				
				
				if(toDebug)Tracker.printSysIO("3541 after ceUnderlying="+ceUnderlying.toString(true, true));
		
				CEData_QuoteBars ceUnderlying_QB = CEData_QuoteBars.getCeData_QuoteBar(ceDataUnderlying.getContrId());
				qbLastUnderlying = ceUnderlying_QB.getQuoteHistoryOfBars().getLastQuote();
		
				if(toDebug)Tracker.printSysIO("3542 quoteLastDays="+ceUnderlying.getQuoteRecent2()
						+" qhBarF="+ceUnderlying_QB.getQuoteHistoryOfBars().getFirstQuote()
						+" qbLastUnderlying="+qbLastUnderlying);
		
				
				if(quoteRecent2Underly!=null && quoteRecent2Underly[0]!=null)
				{
					if(toDebug)System.err.println("3543 quoteLastDays0="+quoteRecent2Underly[0]+" quoteLastDaysUnderlying="+quoteRecent2Underly[1]);
					underlyingPrice = quoteRecent2Underly[0].getaC();
				}
				
				
			}

			
			
			
			if(qbLastUnderlying!=null)
			{
				underlyingPrice = qbLastUnderlying.getC();
				
				if(UtilNumber.isMaxMin_infiniteNaN(underlyingPrice))
					underlyingPrice = qbLastUnderlying.getaC();
			}

			
			if(toDebug)System.err.println("3545 prices underlyingPrice="+underlyingPrice);
			
		}
		
		
		if(toDebug)System.err.println("3550 optionPrice="+optionPrice + " underlyingPrice="+underlyingPrice);
		
		
    	if(TrackerTF.true_())
    		Tracker.exit(0, "Use last day's quotes");
    	
		
		return new double[]{underlyingPrice, optionPrice};
	}
	
	
}
