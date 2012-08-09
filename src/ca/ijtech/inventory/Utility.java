package ca.ijtech.inventory;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import ca.ijtech.inventory.objects.Inventory;

public class Utility {

	public static String generateRefId(GregorianCalendar currentDateTime) {
		// "73197001.20110211.145420.81001"
		// code.YYYYMMDD.HHMMSS.code
		String dot = ".";
		String refId = "";

		refId += Messages.getString("from.id")
				+ dot
				+ currentDateTime.get(GregorianCalendar.YEAR)
				+ String.format("%02d", currentDateTime
						.get(GregorianCalendar.MONTH) + 1)
				+ String.format("%02d", currentDateTime
						.get(GregorianCalendar.DAY_OF_MONTH))
				+ dot
				+ String.format("%02d", currentDateTime
						.get(GregorianCalendar.HOUR_OF_DAY))
				+ String.format("%02d", currentDateTime
						.get(GregorianCalendar.MINUTE))
				+ String.format("%02d", currentDateTime
						.get(GregorianCalendar.SECOND)) + dot
				+ currentDateTime.get(GregorianCalendar.MILLISECOND);

		return refId;
	}

	public static String generateFileName(String refId) {
		String underscore = "_";
		String extension = ".xml";
		String fileName = "";

		fileName += Messages.getString("invPrefix") + underscore
				+ refId.replace(".", "_") + extension;

		return fileName;
	}

	public static String generateFileName(GregorianCalendar currentDateTime) {
		String underscore = "_";
		String extension = ".xml";
		String fileName = "";

		fileName += Messages.getString("invPrefix")
				+ underscore
				+ Messages.getString("from.id")
				+ underscore
				+ currentDateTime.get(GregorianCalendar.YEAR)
				+ String.format("%02d", currentDateTime
						.get(GregorianCalendar.MONTH) + 1)
				+ String.format("%02d", currentDateTime
						.get(GregorianCalendar.DAY_OF_MONTH))
				+ underscore
				+ String.format("%02d", currentDateTime
						.get(GregorianCalendar.HOUR_OF_DAY))
				+ String.format("%02d", currentDateTime
						.get(GregorianCalendar.MINUTE))
				+ String.format("%02d", currentDateTime
						.get(GregorianCalendar.SECOND))
				+ underscore
				+ String.format("%01d", currentDateTime
						.get(GregorianCalendar.MILLISECOND)) + extension;

		return fileName;
	}

	public static void writeInventoryToFile(Set<Inventory> inventorySet, String inventoryFileName) {

		FileOutputStream out;
		try {
			out = new FileOutputStream(inventoryFileName);
			XMLEncoder enc = new XMLEncoder(out);
			enc.writeObject(inventorySet);
			enc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static Set<Inventory> getInventoryFromFile(String inventoryFileName) {
		Set<Inventory> inventory = new HashSet<Inventory>();

		FileInputStream in;
		try {
			in = new FileInputStream(inventoryFileName);

			XMLDecoder dec = new XMLDecoder(in);
			inventory = (Set<Inventory>) dec.readObject();
			dec.close();
		} catch (FileNotFoundException e) {
			inventory = new HashSet<Inventory>();			
		}

		return inventory;
	}

}
