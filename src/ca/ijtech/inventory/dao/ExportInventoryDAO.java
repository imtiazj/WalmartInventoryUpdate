package ca.ijtech.inventory.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ca.ijtech.inventory.ConnectionManager;
import ca.ijtech.inventory.objects.Inventory;

public class ExportInventoryDAO {
	// atlantia database
	private String database;

	public ExportInventoryDAO(String database) {
		super();
		this.database = database;
	}
	
	public Connection getConn() throws SQLException {
		return new ConnectionManager().getConnection(database);
	}
	
	public List<Inventory> getItems(String warehouse, String upc) throws SQLException {
		String databaseUPC = "";
		if ( upc.length() < 11 ){
			databaseUPC = String.format("%11s", upc).replace(" " , "0");	
		}else {
			databaseUPC = upc;
		}			
		
		String sql = "SELECT i.WHSE warehouse, u.UPC_ALTERNATE upc, i.INV_DESCRIPTION description, i.ONHAND availableToSell, " +
					 "i.INV_COMMITTED inProgress, i.BVSELLUOM uom FROM INVENTORY i " +
					 "JOIN UNIT_OF_MEASURE u ON u.part_no = i.code AND u.whse_no = i.WHSE " +
					 "WHERE i.WHSE = '" + warehouse + "' AND u.UPC_ALTERNATE like '" + databaseUPC + "%'";
		
		System.out.println(sql);
		
		Statement stmt = getConn().createStatement();
		ResultSet rs = stmt.executeQuery(sql);

		List<Inventory> inventory = new ArrayList<Inventory>();

		while (rs.next()) {
			Inventory inv = new Inventory();
			inv.setAvailableToSell(String.valueOf(rs.getInt("availableToSell")).trim());
			inv.setEnterpriseCode("CAN");
			inv.setUpc(rs.getString("upc").trim());
			inv.setOrdersInProgress(String.valueOf(rs.getInt("inProgress")).trim());
			inv.setUom(rs.getString("uom").trim());
			inv.setDescription(rs.getString("description").trim());

			inventory.add(inv);
		}

		return inventory;
		
//		List<Inventory> inventory = new ArrayList<Inventory>();
//		inventory.add(new Inventory("", "300", "1", "EA", "CAN", "description info"));
//		return inventory;
	}

}
