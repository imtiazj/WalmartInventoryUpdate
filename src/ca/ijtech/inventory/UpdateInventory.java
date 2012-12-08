package ca.ijtech.inventory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import ca.ijtech.inventory.dao.ExportInventoryDAO;
import ca.ijtech.inventory.objects.Inventory;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import com.walmart._2010.xmlschema.mtep.fulfillment.dsv.itemxref.version1.ItemsXRef;
import com.walmart._2010.xmlschema.mtep.fulfillment.dsv.itemxref.version1.SKU;
import com.walmart._2010.xmlschema.mtep.fulfillment.inventorysummary.version1.ObjectFactory;
import com.walmart._2010.xmlschema.mtep.fulfillment.inventorysummary.version1.Wininventory;
import com.walmart._2010.xmlschema.mtep.fulfillment.inventorysummary.version1.Wmi;
import com.walmart._2010.xmlschema.mtep.fulfillment.inventorysummary.version1.Wmiiteminventory;
import com.walmart._2010.xmlschema.mtep.fulfillment.inventorysummary.version1.Wininventory.Availability;
import com.walmart._2010.xmlschema.mtep.wmiheader.Wmiheader;
import com.walmart._2010.xmlschema.mtep.wmiheader.Wmiheader.From;
import com.walmart._2010.xmlschema.mtep.wmiheader.Wmiheader.To;
import com.walmart._2010.xmlschema.mtep.wmiheader.Wmiheader.From.Contactinfo;
import com.walmart._2010.xmlschema.mtep.wmiheader.Wmiheader.From.Contactinfo.Contactphone;
import com.walmart._2010.xmlschema.mtep.wmiheader.Wmiheader.From.Contactinfo.Emailaddress;

public class UpdateInventory {

	private List<String> itemXRefFile = new ArrayList<String>();

	public UpdateInventory() {
		System.out.println("Start Inventory Update \n"); //$NON-NLS-1$
		GregorianCalendar currentDateTime = null;

		//download files from the ftp
		try {
			downloadFilesFromFTP();
		} catch (IOException e) {
			e.printStackTrace();
		}

//		itemXRefFile.add("_WMCA_ITEMXREF_12799_20110930_160626_1684313.xml");
//		itemXRefFile.add("_WMCA_ITEMXREF_12799_20111003_160626_1684313.xml");

		// get the list of SKU, UPC and WINS from the filesystem
		// build a list of inventory to fetch from the database
		Set<Inventory> inventory = Utility.getInventoryFromFile(Messages.getString("inventoryFileName"));
		for (int i = 0; i < itemXRefFile.size(); i++) {
			ItemsXRef itemsXRef = null;

			try {
				itemsXRef = unmarshalItemXRef(itemXRefFile.get(i));
				addNewInventoryToSet(itemsXRef, inventory);
			} catch (JAXBException e) {
				e.printStackTrace();
			}

		}

		Utility.writeInventoryToFile(inventory, Messages.getString("inventoryFileName"));

		try {
			getDataFromDatabase(inventory);
			currentDateTime = createInventoryUpdateXML(inventory);
			if ( null != currentDateTime ){
				ftpFile(currentDateTime);
			}
		}catch (Exception e){
			e.printStackTrace();
		}

		//delete files downloaded from ftp
		for (int i = 0; i < itemXRefFile.size(); i++) {
			try {
				deleteFileFromFTP(itemXRefFile.get(i));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void addNewInventoryToSet(ItemsXRef itemsXRef, Set<Inventory> inventory) {
		List<SKU> newData = itemsXRef.getItems().getSKU();

		for (SKU data : newData) {
			Inventory inv = new Inventory();
			inv.setItemSkuId(data.getSKUID());
			inv.setUpc(data.getUPC());
			inv.setWin(data.getWIN());

			inventory.add(inv);
		}
	}

	private void downloadFilesFromFTP() throws IOException {
		FTPClient ftp = new FTPClient();
		ftp.connect(Messages.getString("ftpUrl"));
		boolean connect = ftp.login(Messages.getString("username"), Messages.getString("password"));

		System.out.println("Connect: " + connect);

		if (connect) {
			ftp.enterLocalPassiveMode();
			ftp.changeWorkingDirectory(Messages.getString("inboundDirectory")); //$NON-NLS-1$

			FTPFile[] ftpFiles = ftp.listFiles();

			for (FTPFile file : ftpFiles) {
				boolean successDownload = false;
				if (file.isFile()) {
					System.out.println("Downloading " + file.getName());
					successDownload = ftp.retrieveFile(file.getName(), new FileOutputStream(file.getName()));
					if (successDownload) {
						itemXRefFile.add(file.getName());
					}
				}
			}

			ftp.disconnect();
		}
	}

	private void ftpFile(GregorianCalendar currentDateTime) throws SocketException, IOException {
		String fileName = Utility.generateFileName(currentDateTime);
		FTPClient ftp = new FTPClient();
		ftp.connect(Messages.getString("ftpUrl"));
		boolean connect = ftp.login(Messages.getString("username"), Messages.getString("password"));

		// System.out.println("FTP: " + Messages.getString("ftpUrl"));
		// System.out.println("Username: " + Messages.getString("username"));
		// System.out.println("Password: " + Messages.getString("password"));
		// System.out.println("Connected: " + connect);
		// System.out.println("Uploading file: " + fileName);

		if (connect) {
			ftp.enterLocalPassiveMode();
			ftp.changeWorkingDirectory(Messages.getString("outboundDirectory")); //$NON-NLS-1$

			boolean success = ftp.storeFile(fileName, new FileInputStream(
					fileName)); //$NON-NLS-1$
			System.out.println("FTP Success? " + success); //$NON-NLS-1$

			ftp.disconnect();
		}
	}

	private void getDataFromDatabase(Set<Inventory> inventory) throws SQLException {
		for (Inventory data : inventory) {
//			System.out.println(data.getSkuid() + " " + data.getUpc() + " " + data.getWin());
			
			List<Inventory> inv = getItemFromDatabase(data.getUpc());
			if (inv.size() > 0) {
				Inventory item = inv.get(0);

				data.setAvailableToSell(item.getAvailableToSell());
				data.setEnterpriseCode(item.getEnterpriseCode());
				data.setOrdersInProgress(item.getOrdersInProgress());
				data.setUom(item.getUom());
				data.setDescription(item.getDescription());
			}
		}
	}

	private GregorianCalendar createInventoryUpdateXML(Set<Inventory> inventory) throws JAXBException, FileNotFoundException {
		GregorianCalendar currentDateTime = (GregorianCalendar) GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC")); //$NON-NLS-1$
		JAXBContext jc = JAXBContext.newInstance("com.walmart._2010.xmlschema.mtep.fulfillment.inventorysummary.version1"); //$NON-NLS-1$
		Wmi wmi = setupXMLObjects(currentDateTime);

		populateXMLObjects(wmi, currentDateTime, inventory);

		if ( wmi.getWmiiteminventory().getWininventory().size() == 0 ){
			// writing to file
			currentDateTime = null;
		}else{
			writeToFile(currentDateTime, jc, wmi);			
		}


		return currentDateTime;
	}

	private void populateXMLObjects(Wmi wmi, GregorianCalendar currentDateTime, Set<Inventory> inventory) {
		wmi.getWmiheader().setFileid(Utility.generateRefId(currentDateTime));
		wmi.getWmiheader().setVersion(Messages.getString("header.version"));
		wmi.getWmiheader().setEnterprisecode(Messages.getString("header.enterpriseCode"));
		wmi.getWmiheader().setMessagetype(Messages.getString("header.messageType"));
		wmi.getWmiheader().setGendate(new XMLGregorianCalendarImpl(currentDateTime));

		wmi.getWmiheader().getTo().setToid(new BigInteger(Messages.getString("to.id")));
		wmi.getWmiheader().getTo().setToname(Messages.getString("to.name"));

		wmi.getWmiheader().getFrom().setFromid(new BigInteger(Messages.getString("from.id")));
		wmi.getWmiheader().getFrom().setFromname(Messages.getString("from.name"));

		wmi.getWmiheader().getFrom().getContactinfo().setContactname(Messages.getString("from.contactName"));

		wmi.getWmiheader().getFrom().getContactinfo().getContactphone().setPhonenumber(Messages.getString("from.contactPhone"));
		wmi.getWmiheader().getFrom().getContactinfo().getContactphone().setExtension(new BigInteger(Messages.getString("from.contactExt")));

		wmi.getWmiheader().getFrom().getContactinfo().getEmailaddress().setPrimaryemail(Messages.getString("from.emailAddress"));

		// Inventory Items
		ObjectFactory wmiObjectFactory = new ObjectFactory();
		Wmiiteminventory wmiItemInventory = wmiObjectFactory.createWmiiteminventory();

		// begin loop
		for (Inventory invdata : inventory) {
			List<Wininventory> winInventory = wmiItemInventory.getWininventory();
			Wininventory inv = wmiObjectFactory.createWininventory();
			Availability availability = wmiObjectFactory.createWininventoryAvailability();

			try {
				inv.setWin(invdata.getWin());
				inv.setUpc(invdata.getUpc());
				inv.setSku(invdata.getItemSkuId());
				inv.setFacilityid(Messages.getString("inventory.facilityId"));
				inv.setDatetime(new XMLGregorianCalendarImpl(currentDateTime));
				inv.setDescription(invdata.getDescription().length() > 60 
						? invdata.getDescription().substring(0, 60) : invdata.getDescription());

				availability.setAvailabilitycode(Messages.getString("inventory.availabilityCode"));
				availability.setUom(invdata.getUom());
				
				int ats =  getIntValue(invdata.getAvailableToSell()) - getIntValue(invdata.getOrdersInProgress());
				availability.setAts(new BigInteger(String.valueOf(ats)));

				availability.setMindays(new BigInteger(Messages.getString("inventory.minDays")));
				availability.setMaxdays(new BigInteger(Messages.getString("inventory.maxDays")));
				availability.setStartdate(new XMLGregorianCalendarImpl(currentDateTime));
				System.out.println("Available to sell: " + invdata.getUpc() + " : " + ats);
			} catch (Exception e) {
				continue;
			}
			
			inv.setAvailability(availability);
			winInventory.add(inv);
		}
		// end loop

		wmi.setWmiiteminventory(wmiItemInventory);

	}

	private int getIntValue(String value){
		int intValue = 0;
		try {
			intValue = Integer.valueOf(value).intValue();
		} catch (Exception e) {
		}
		
		return intValue;
	}
	
	private Wmi setupXMLObjects(GregorianCalendar currentDateTime)
			throws JAXBException {
		Wmi wmi = null;
		Wmiheader wmiHeader = null;
		From from = null;
		To to = null;
		Contactinfo contactInfo = null;
		Contactphone contactPhone = null;
		Emailaddress emailAddress = null;

		ObjectFactory wmiObjectFactory = new ObjectFactory();
		com.walmart._2010.xmlschema.mtep.wmiheader.ObjectFactory wmiHeaderFactory = new com.walmart._2010.xmlschema.mtep.wmiheader.ObjectFactory();

		// top level elements (header, iteminventory)
		wmi = wmiObjectFactory.createWmi();
		wmiHeader = wmiHeaderFactory.createWmiheader();

		// second level for header (to, from)
		from = wmiHeaderFactory.createWmiheaderFrom();
		to = wmiHeaderFactory.createWmiheaderTo();

		// third level for header.from (contactinfo)
		contactInfo = wmiHeaderFactory.createWmiheaderFromContactinfo();

		// fourth level for header.from.contactinfo.contactphone
		contactPhone = wmiHeaderFactory
				.createWmiheaderFromContactinfoContactphone();
		emailAddress = wmiHeaderFactory
				.createWmiheaderFromContactinfoEmailaddress();

		contactInfo.setContactphone(contactPhone);
		contactInfo.setEmailaddress(emailAddress);

		from.setContactinfo(contactInfo);

		wmiHeader.setFrom(from);
		wmiHeader.setTo(to);

		wmi.setWmiheader(wmiHeader);

		return wmi;

	}

	private void writeToFile(GregorianCalendar currentDateTime, JAXBContext jc, Wmi wmi) throws JAXBException, PropertyException, FileNotFoundException {
		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		// m.marshal(wmi, System.out);
		m.marshal(wmi, new FileOutputStream(Utility.generateFileName(currentDateTime)));
	}

	private void deleteFileFromFTP(String file) throws IOException {
		FTPClient ftp = new FTPClient();
		ftp.connect(Messages.getString("ftpUrl"));
		boolean connect = ftp.login(Messages.getString("username"), Messages
				.getString("password"));

		System.out.println("Connect: " + connect);

		if (connect) {
			ftp.enterLocalPassiveMode();
			ftp.changeWorkingDirectory(Messages.getString("inboundDirectory")); //$NON-NLS-1$

			ftp.deleteFile(file);

			ftp.disconnect();
		}
	}

	private ItemsXRef unmarshalItemXRef(String file) throws JAXBException {
		JAXBContext jc = JAXBContext
				.newInstance("com.walmart._2010.xmlschema.mtep.fulfillment.dsv.itemxref.version1"); //$NON-NLS-1$
		Unmarshaller unmarshaller = jc.createUnmarshaller();

		ItemsXRef itemsXRef = (ItemsXRef) unmarshaller
				.unmarshal(new File(file));

		return itemsXRef;
	}

	private List<Inventory> getItemFromDatabase(String upc) throws SQLException {
		String warehouse = Messages.getString("warehouse");
		ExportInventoryDAO dao = new ExportInventoryDAO(Messages.getString("database.systemDNS"));

		List<Inventory> inventory = dao.getItems(warehouse, upc);

		return inventory;

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new UpdateInventory();
	}

}
