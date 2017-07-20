package newTest;

import java.util.ArrayList;

public class TradingHours {
	public static ArrayList<String> getExchangeTradingHours(String target, String type){
		String startHour="";
		String endHour="";
		String timeZone="";
		
		if(type.compareTo("exchange") == 0){
			switch(target){
			case "KSE":
				startHour = "09:00:00";
				endHour = "15:15:00";
				timeZone = "JST";
				break;
			default:
				startHour = "unknown";
				endHour = "unknown";
				timeZone = "unknown";
				break;
			}
		}
		
		if(type.compareTo("product") == 0){
			switch(target){
			case "K200":
				startHour = "09:00:00";
				endHour = "15:15:00";
				timeZone = "JST";
				break;
			case "VXX":
				startHour = "09:30:00";
				endHour = "16:00:00";
				timeZone = "GMT-5:00";   /// 美国东部时间
				break;
			default:
				startHour = "unknown";
				endHour = "unknown";
				timeZone = "unknown";
				break;
			}
		}
		
		ArrayList<String> returnList = new ArrayList<String>();
		returnList.add(startHour);
		returnList.add(endHour);
		returnList.add(timeZone);
		
		return returnList;
	}
}
