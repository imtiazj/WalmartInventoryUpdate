//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.10.05 at 10:04:52 AM PDT 
//


package com.walmart._2010.xmlschema.mtep.fulfillment.dsv.itemxref.version1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.walmart._2010.xmlschema.mtep.dsv.common.itemxrefheader.MessageHeaderType;


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
 *         &lt;element name="Header" type="{http://www.walmart.com/2010/XMLSchema/MTEP/DSV/common/itemXrefHeader}messageHeaderType"/>
 *         &lt;element name="Items">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://www.walmart.com/2010/XMLSchema/MTEP/fulfillment/DSV/ItemXRef/Version1}SKU" maxOccurs="unbounded"/>
 *                   &lt;element name="EnterpriseCode" type="{http://www.walmart.com/2010/XMLSchema/MTEP/DSV/common/datatypes}EnterpriseCode_type"/>
 *                   &lt;element name="RecordCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
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
    "header",
    "items"
})
@XmlRootElement(name = "ItemsXRef")
public class ItemsXRef {

    @XmlElement(name = "Header", required = true)
    protected MessageHeaderType header;
    @XmlElement(name = "Items", required = true)
    protected ItemsXRef.Items items;

    /**
     * Gets the value of the header property.
     * 
     * @return
     *     possible object is
     *     {@link MessageHeaderType }
     *     
     */
    public MessageHeaderType getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     * 
     * @param value
     *     allowed object is
     *     {@link MessageHeaderType }
     *     
     */
    public void setHeader(MessageHeaderType value) {
        this.header = value;
    }

    /**
     * Gets the value of the items property.
     * 
     * @return
     *     possible object is
     *     {@link ItemsXRef.Items }
     *     
     */
    public ItemsXRef.Items getItems() {
        return items;
    }

    /**
     * Sets the value of the items property.
     * 
     * @param value
     *     allowed object is
     *     {@link ItemsXRef.Items }
     *     
     */
    public void setItems(ItemsXRef.Items value) {
        this.items = value;
    }


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
     *         &lt;element ref="{http://www.walmart.com/2010/XMLSchema/MTEP/fulfillment/DSV/ItemXRef/Version1}SKU" maxOccurs="unbounded"/>
     *         &lt;element name="EnterpriseCode" type="{http://www.walmart.com/2010/XMLSchema/MTEP/DSV/common/datatypes}EnterpriseCode_type"/>
     *         &lt;element name="RecordCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
        "sku",
        "enterpriseCode",
        "recordCount"
    })
    public static class Items {

        @XmlElement(name = "SKU", namespace = "http://www.walmart.com/2010/XMLSchema/MTEP/fulfillment/DSV/ItemXRef/Version1", required = true)
        protected List<SKU> sku;
        @XmlElement(name = "EnterpriseCode", required = true)
        protected String enterpriseCode;
        @XmlElement(name = "RecordCount")
        protected int recordCount;

        /**
         * Gets the value of the sku property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the sku property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getSKU().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link SKU }
         * 
         * 
         */
        public List<SKU> getSKU() {
            if (sku == null) {
                sku = new ArrayList<SKU>();
            }
            return this.sku;
        }

        /**
         * Gets the value of the enterpriseCode property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getEnterpriseCode() {
            return enterpriseCode;
        }

        /**
         * Sets the value of the enterpriseCode property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setEnterpriseCode(String value) {
            this.enterpriseCode = value;
        }

        /**
         * Gets the value of the recordCount property.
         * 
         */
        public int getRecordCount() {
            return recordCount;
        }

        /**
         * Sets the value of the recordCount property.
         * 
         */
        public void setRecordCount(int value) {
            this.recordCount = value;
        }

    }

}