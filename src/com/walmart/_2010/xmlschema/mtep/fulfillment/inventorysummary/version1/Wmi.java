//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.10.05 at 09:49:59 AM PDT 
//


package com.walmart._2010.xmlschema.mtep.fulfillment.inventorysummary.version1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.walmart._2010.xmlschema.mtep.wmiheader.Wmiheader;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.walmart.com/2010/XMLSchema/MTEP/fulfillment/InventorySummary/version1.0}wmiheader"/>
 *         &lt;element ref="{http://www.walmart.com/2010/XMLSchema/MTEP/fulfillment/InventorySummary/version1.0}wmiiteminventory"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "wmiheader",
    "wmiiteminventory"
})
@XmlRootElement(name = "wmi")
public class Wmi {

    @XmlElement(namespace = "http://www.walmart.com/2010/XMLSchema/MTEP/fulfillment/InventorySummary/version1.0", required = true)
    protected Wmiheader wmiheader;
    @XmlElement(namespace = "http://www.walmart.com/2010/XMLSchema/MTEP/fulfillment/InventorySummary/version1.0", required = true)
    protected Wmiiteminventory wmiiteminventory;

    /**
     * Gets the value of the wmiheader property.
     * 
     * @return
     *     possible object is
     *     {@link Wmiheader }
     *     
     */
    public Wmiheader getWmiheader() {
        return wmiheader;
    }

    /**
     * Sets the value of the wmiheader property.
     * 
     * @param value
     *     allowed object is
     *     {@link Wmiheader }
     *     
     */
    public void setWmiheader(Wmiheader value) {
        this.wmiheader = value;
    }

    /**
     * Gets the value of the wmiiteminventory property.
     * 
     * @return
     *     possible object is
     *     {@link Wmiiteminventory }
     *     
     */
    public Wmiiteminventory getWmiiteminventory() {
        return wmiiteminventory;
    }

    /**
     * Sets the value of the wmiiteminventory property.
     * 
     * @param value
     *     allowed object is
     *     {@link Wmiiteminventory }
     *     
     */
    public void setWmiiteminventory(Wmiiteminventory value) {
        this.wmiiteminventory = value;
    }

}