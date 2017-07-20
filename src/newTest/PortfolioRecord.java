package newTest;

import com.ib.client.Contract;

public class PortfolioRecord {
	public Contract contract;
	public int position;
	public double marketPrice;
	public double marketValue;
	public double averageCost;
	public double unrealizedPNL;
	public double realizedPNL;
	public String accountName;
	
	public PortfolioRecord(Contract contract, int position, double marketPrice, double marketValue,
			double averageCost, double unrealizedPNL, double realizedPNL, String accountName) {
		this.contract = contract;
		this.position = position;
		this.marketPrice = marketPrice;
		this.marketValue = marketValue;
		this.averageCost = averageCost;
		this.unrealizedPNL = unrealizedPNL;
		this.realizedPNL = realizedPNL;
		this.accountName = accountName;
	}
}
