package ca.ijtech.inventory.objects;


public class Inventory {

	private String itemSkuId;
	private String availableToSell;
	private String ordersInProgress;
	private String uom;
	private String enterpriseCode;
    private String description;
	
    private String skuid;
    private String upc;
    private String win;
	
	public Inventory() {
		super();
	}

	public Inventory(String itemSkuId, String availableToSell, String ordersInProgress, String uom, String enterpriseCode, String description) {
		super();
		this.itemSkuId = itemSkuId;
		this.availableToSell = availableToSell;
		this.ordersInProgress = ordersInProgress;
		this.uom = uom;
		this.enterpriseCode = enterpriseCode;
		this.description = description;
	}

	public String getItemSkuId() {
		return itemSkuId;
	}

	public void setItemSkuId(String itemSkuId) {
		this.itemSkuId = itemSkuId;
	}

	public String getAvailableToSell() {
		return availableToSell;
	}

	public void setAvailableToSell(String availableToSell) {
		this.availableToSell = availableToSell;
	}

	public String getOrdersInProgress() {
		return ordersInProgress;
	}

	public void setOrdersInProgress(String ordersInProgress) {
		this.ordersInProgress = ordersInProgress;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public String getEnterpriseCode() {
		return enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	public String getSkuid() {
		return skuid;
	}

	public void setSkuid(String skuid) {
		this.skuid = skuid;
	}

	public String getUpc() {
		return upc;
	}

	public void setUpc(String upc) {
		this.upc = upc;
	}

	public String getWin() {
		return win;
	}

	public void setWin(String win) {
		this.win = win;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return getItemSkuId() + " " + getUpc() + " " + getWin();
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.hashCode() == obj.hashCode();
	}

	@Override
	public int hashCode() {
		return this.getUpc().hashCode();
	}

}
