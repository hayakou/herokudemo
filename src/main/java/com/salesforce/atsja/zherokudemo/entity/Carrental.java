/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.salesforce.atsja.zherokudemo.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author hayakou
 */
@Entity
@Table(name = "carrental")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Carrental.findAll", query = "SELECT c FROM Carrental c"),
    @NamedQuery(name = "Carrental.findByCarrentalid", query = "SELECT c FROM Carrental c WHERE c.carrentalid = :carrentalid"),
    @NamedQuery(name = "Carrental.findByProvider", query = "SELECT c FROM Carrental c WHERE c.provider = :provider"),
    @NamedQuery(name = "Carrental.findByCity", query = "SELECT c FROM Carrental c WHERE c.city = :city"),
    @NamedQuery(name = "Carrental.findByPickupdate", query = "SELECT c FROM Carrental c WHERE c.pickupdate = :pickupdate"),
    @NamedQuery(name = "Carrental.findByReturndate", query = "SELECT c FROM Carrental c WHERE c.returndate = :returndate"),
    @NamedQuery(name = "Carrental.findByCartype", query = "SELECT c FROM Carrental c WHERE c.cartype = :cartype"),
    @NamedQuery(name = "Carrental.findByRate", query = "SELECT c FROM Carrental c WHERE c.rate = :rate"),
    @NamedQuery(name = "Carrental.findByBookingstatus", query = "SELECT c FROM Carrental c WHERE c.bookingstatus = :bookingstatus"),
    @NamedQuery(name = "Carrental.findByLastupdated", query = "SELECT c FROM Carrental c WHERE c.lastupdated = :lastupdated")})
public class Carrental implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "carrentalid")
    private Integer carrentalid;
    @Column(name = "provider")
    private String provider;
    @Column(name = "city")
    private String city;
    @Column(name = "pickupdate")
    @Temporal(TemporalType.DATE)
    private Date pickupdate;
    @Column(name = "returndate")
    @Temporal(TemporalType.DATE)
    private Date returndate;
    @Column(name = "cartype")
    private String cartype;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "rate")
    private BigDecimal rate;
    @Column(name = "bookingstatus")
    private String bookingstatus;
    @Column(name = "lastupdated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastupdated;
    @JoinColumn(name = "tripid", referencedColumnName = "tripid")
    @ManyToOne(optional = false)
    private Trip tripid;

    public Carrental() {
    }

    public Carrental(Integer carrentalid) {
        this.carrentalid = carrentalid;
    }

    public Integer getCarrentalid() {
        return carrentalid;
    }

    public void setCarrentalid(Integer carrentalid) {
        this.carrentalid = carrentalid;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Date getPickupdate() {
        return pickupdate;
    }

    public void setPickupdate(Date pickupdate) {
        this.pickupdate = pickupdate;
    }

    public Date getReturndate() {
        return returndate;
    }

    public void setReturndate(Date returndate) {
        this.returndate = returndate;
    }

    public String getCartype() {
        return cartype;
    }

    public void setCartype(String cartype) {
        this.cartype = cartype;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getBookingstatus() {
        return bookingstatus;
    }

    public void setBookingstatus(String bookingstatus) {
        this.bookingstatus = bookingstatus;
    }

    public Date getLastupdated() {
        return lastupdated;
    }

    public void setLastupdated(Date lastupdated) {
        this.lastupdated = lastupdated;
    }

    public Trip getTripid() {
        return tripid;
    }

    public void setTripid(Trip tripid) {
        this.tripid = tripid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (carrentalid != null ? carrentalid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Carrental)) {
            return false;
        }
        Carrental other = (Carrental) object;
        if ((this.carrentalid == null && other.carrentalid != null) || (this.carrentalid != null && !this.carrentalid.equals(other.carrentalid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.salesforce.atsja.zherokudemo.entity.Carrental[ carrentalid=" + carrentalid + " ]";
    }
    
}
