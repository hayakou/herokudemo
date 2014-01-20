/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.salesforce.atsja.zherokudemo.entity;

import java.io.Serializable;
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
@Table(name = "hotel")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Hotel.findAll", query = "SELECT h FROM Hotel h"),
    @NamedQuery(name = "Hotel.findByHotelid", query = "SELECT h FROM Hotel h WHERE h.hotelid = :hotelid"),
    @NamedQuery(name = "Hotel.findByHotelname", query = "SELECT h FROM Hotel h WHERE h.hotelname = :hotelname"),
    @NamedQuery(name = "Hotel.findByCheckindate", query = "SELECT h FROM Hotel h WHERE h.checkindate = :checkindate"),
    @NamedQuery(name = "Hotel.findByCheckoutdate", query = "SELECT h FROM Hotel h WHERE h.checkoutdate = :checkoutdate"),
    @NamedQuery(name = "Hotel.findByGuests", query = "SELECT h FROM Hotel h WHERE h.guests = :guests"),
    @NamedQuery(name = "Hotel.findByBookingstatus", query = "SELECT h FROM Hotel h WHERE h.bookingstatus = :bookingstatus"),
    @NamedQuery(name = "Hotel.findByLastupdated", query = "SELECT h FROM Hotel h WHERE h.lastupdated = :lastupdated")})
public class Hotel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "hotelid")
    private Integer hotelid;
    @Column(name = "hotelname")
    private String hotelname;
    @Column(name = "checkindate")
    @Temporal(TemporalType.DATE)
    private Date checkindate;
    @Column(name = "checkoutdate")
    @Temporal(TemporalType.DATE)
    private Date checkoutdate;
    @Column(name = "guests")
    private Integer guests;
    @Column(name = "bookingstatus")
    private String bookingstatus;
    @Column(name = "lastupdated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastupdated;
    @JoinColumn(name = "tripid", referencedColumnName = "tripid")
    @ManyToOne(optional = false)
    private Trip tripid;

    public Hotel() {
    }

    public Hotel(Integer hotelid) {
        this.hotelid = hotelid;
    }

    public Integer getHotelid() {
        return hotelid;
    }

    public void setHotelid(Integer hotelid) {
        this.hotelid = hotelid;
    }

    public String getHotelname() {
        return hotelname;
    }

    public void setHotelname(String hotelname) {
        this.hotelname = hotelname;
    }

    public Date getCheckindate() {
        return checkindate;
    }

    public void setCheckindate(Date checkindate) {
        this.checkindate = checkindate;
    }

    public Date getCheckoutdate() {
        return checkoutdate;
    }

    public void setCheckoutdate(Date checkoutdate) {
        this.checkoutdate = checkoutdate;
    }

    public Integer getGuests() {
        return guests;
    }

    public void setGuests(Integer guests) {
        this.guests = guests;
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
        hash += (hotelid != null ? hotelid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Hotel)) {
            return false;
        }
        Hotel other = (Hotel) object;
        if ((this.hotelid == null && other.hotelid != null) || (this.hotelid != null && !this.hotelid.equals(other.hotelid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.salesforce.atsja.zherokudemo.entity.Hotel[ hotelid=" + hotelid + " ]";
    }
    
}
