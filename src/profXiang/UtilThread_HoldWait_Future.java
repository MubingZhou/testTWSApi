package com.primoi.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.primoi.util.UtilThread.CallableWithName_toRun;
import com.primoi.util._dateTime.TimeDiffThread0;
import com.primoi.util.test1.UtilNumber;
import com.primoi.util.UtilThread_AllJava;

import debug.Tracker;
import debug.TrackerT;
import debug.TrackerTF;


/**
 * @see __javaLang.threading._Future._NoteFuture
 * 
 * @author JX
 *
 */
public class UtilThread_HoldWait_Future
{
	
	/**
	 * OldName: holdForCheck
	 */
	public static Boolean holdForBool(final Callable<Boolean> booleanCallable, final int secMost)
	{
		final boolean toDebug = true;
	
		
		if(toDebug)
		{
			Tracker.trackWithErrorPrinting_LessExpensive("holdForBool secMost="+secMost);
			
		}
		
		
		
		String msgHoldFor = Tracker.trackWithErrorPrinting_LessExpensive("holdForBool");
	
		if(secMost<=0)
			Tracker.exit(0, "Need secMost to calc timeToRun_booleanCallable. secMost="+secMost);
		
		
		final long timeToHold = secMost*1000+300;
		final long sleepMillSec = 250L;
		final long timeToRun_booleanCallable = timeToHold/sleepMillSec + 1;
		
		
		CallableWithName_toRun<Boolean> call = new CallableWithName_toRun<Boolean>()
		{
	
			@Override
			public Boolean call() throws Exception
			{
				UtilThread.appendMyThreadName("Callable_holdForBool 25 hc="+this.hashCode());
	
				
				try
				{
					Boolean resBool = null;
					
					for(int i=0; i<timeToRun_booleanCallable; i++)
					{
						resBool=booleanCallable.call();          //\\! ÔõÃ´Àí½â£¿
						
						TimeDiffThread0.sleep(sleepMillSec);
						
						if(resBool)
							return resBool;
					}
					
					
					///! Old version
					if(TrackerTF.false_())
					while(! (resBool=booleanCallable.call()) )
					{
						TimeDiffThread0.sleep(sleepMillSec);
						Thread.yield();
					}
					
					
					return resBool;
				}
				catch(Exception e)
				{
					e.printStackTrace();
					
					return false;
				}
	
			}
	
		};
		
		ExecutorService execService = Executors.newFixedThreadPool(1);	//5
		
		Future<Boolean> future = execService.submit(call);
	
	
	
		//Less important thing. return null, if no result after 30 seconds
		Boolean ret = null;
		try
		{
			
			ret = future.get(timeToHold, TimeUnit.MILLISECONDS);
			
		}
		catch (TimeoutException timeoutE)
		{
			Tracker.printSysIO("holdForCheck secMost="+secMost);
		}
		catch (Exception e)
		{
			if(toDebug)System.out.println("holdForCheck: Not returned after "+secMost
					+ " seconds");
		}
	
		if(toDebug)System.out.println("holdForBool after holding ret="+ret+" "+booleanCallable
			+" Thread="+Thread.currentThread().getName());
	
	
		//close the execService
		try
		{
			execService.awaitTermination(sleepMillSec, TimeUnit.MILLISECONDS);
			
			execService.shutdown();
//			execService.shutdownNow();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		execService = null;
//		booleanCallable.
	
		return ret;
	}
	
	

	public static int holdFor(final int secMost)
	{
		final boolean toDebug = true;
	
		
		ExecutorService execService = Executors.newFixedThreadPool(1);	//5
	
		CallableWithName_toRun<Integer> call = new CallableWithName_toRun<Integer>()
		{
	
			@Override
			public Integer call() throws Exception
			{
				UtilThread.appendMyThreadName("Callable_holdFor 24 hc="+this.hashCode());
	
				try
				{
					TimeDiffThread0.sleep(secMost*1000L);
	
					if(TrackerTF.false_() && toDebug)
						System.out.println(UtilThread_AllJava.printThreadInfo()[1]);
	
					
					return 0;
				}
				catch(Exception e)
				{
					e.printStackTrace();
					return -1;
				}
	
			}
	
		};
		Future<Integer> future = execService.submit(call);
	
	
	
		//Less important thing. return null, if no result after 30 seconds
		Integer ret = null;
		try
		{
			ret = future.get(secMost*1000+300, TimeUnit.MILLISECONDS);
		}
		catch (TimeoutException timeoutE)
		{
			Tracker.printSysIO("holdFor secMost="+secMost);
		}
		catch (Exception e)
		{
			if(toDebug)System.out.println("UtilThread_HoldWait_Future: Not returned after "+secMost
					+ " seconds");
		}
	
		if(toDebug)System.out.println("after holding ret="+ret);
	
	
		//close the execService
		execService.shutdown();
		execService = null;
	
		return ret;
	}
	
	
	
	public static void main(String[] args)
	{
		long t0 = TimeDiffThread0.currentTimeMillis();
		
		UtilThread_HoldWait_Future.holdFor(5);
		
		TrackerT.secTaken(t0, "UtilThread_Future", true);
		
		
		UtilNumber.r2(0);
	}

}
