package newTest;

public class OrderStatus {
	public int orderId;
	public String orderStatus;
	public int filled;
	public int remaining; 
	public double avgFillPrice; 
	public int permId;
	public int parentId; 
	public double lastFillPrice; 
	public int clientId; 
	public String whyHeld;
	
	public OrderStatus(int orderId, String status, int filled, int remaining, double avgFillPrice, int permId,
			int parentId, double lastFillPrice, int clientId, String whyHeld){
		this.orderId = orderId;
		this.orderStatus = status;
		this.filled = filled;
		this.remaining = remaining;
		this.avgFillPrice = avgFillPrice;
		this.permId = permId;
		this.parentId = parentId;
		this.lastFillPrice = lastFillPrice;
		this.clientId = clientId;
		this.whyHeld = whyHeld;
	}
}
