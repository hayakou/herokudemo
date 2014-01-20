/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.salesforce.atsja.zherokudemo.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author hayakou
 */
@Entity
@Table(name = "trip")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Trip.findAll", query = "SELECT t FROM Trip t"),
    @NamedQuery(name = "Trip.findByTripid", query = "SELECT t FROM Trip t WHERE t.tripid = :tripid"),
    @NamedQuery(name = "Trip.findByDepdate", query = "SELECT t FROM Trip t WHERE t.depdate = :depdate"),
    @NamedQuery(name = "Trip.findByDepcity", query = "SELECT t FROM Trip t WHERE t.depcity = :depcity"),
    @NamedQuery(name = "Trip.findByDestcity", query = "SELECT t FROM Trip t WHERE t.destcity = :destcity"),
    @NamedQuery(name = "Trip.findByLastupdated", query = "SELECT t FROM Trip t WHERE t.lastupdated = :lastupdated")})
public class Trip implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "tripid")
    private Integer tripid;
    @Column(name = "depdate")
    @Temporal(TemporalType.DATE)
    private Date depdate;
    @Column(name = "depcity")
    private String depcity;
    @Column(name = "destcity")
    private String destcity;
    @Column(name = "lastupdated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastupdated;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tripid")
    private Collection<Flight> flightCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tripid")
    private Collection<Carrental> carrentalCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tripid")
    private Collection<Hotel> hotelCollection;
    @JoinColumn(name = "triptypeid", referencedColumnName = "triptypeid")
    @ManyToOne(optional = false)
    private Triptype triptypeid;
    @JoinColumn(name = "personid", referencedColumnName = "personid")
    @ManyToOne(optional = false)
    private Person personid;

    public Trip() {
    }

    public Trip(Integer tripid) {
        this.tripid = tripid;
    }

    public Integer getTripid() {
        return tripid;
    }

    public void setTripid(Integer tripid) {
        this.tripid = tripid;
    }

    public Date getDepdate() {
        return depdate;
    }

    public void setDepdate(Date depdate) {
        this.depdate = depdate;
    }

    public String getDepcity() {
        return depcity;
    }

    public void setDepcity(String depcity) {
        this.depcity = depcity;
    }

    public String getDestcity() {
        return destcity;
    }

    public void setDestcity(String destcity) {
        this.destcity = destcity;
    }

    public Date getLastupdated() {
        return lastupdated;
    }

    public void setLastupdated(Date lastupdated) {
        this.lastupdated = lastupdated;
    }

    @XmlTransient
    public Collection<Flight> getFlightCollection() {
        return flightCollection;
    }

    public void setFlightCollection(Collection<Flight> flightCollection) {
        this.flightCollection = flightCollection;
    }

    @XmlTransient
    public Collection<Carrental> getCarrentalCollection() {
        return carrentalCollection;
    }

    public void setCarrentalCollection(Collection<Carrental> carrentalCollection) {
        this.carrentalCollection = carrentalCollection;
    }

    @XmlTransient
    public Collection<Hotel> getHotelCollection() {
        return hotelCollection;
    }

    public void setHotelCollection(Collection<Hotel> hotelCollection) {
        this.hotelCollection = hotelCollection;
    }

    public Triptype getTriptypeid() {
        return triptypeid;
    }

    public void setTriptypeid(Triptype triptypeid) {
        this.triptypeid = triptypeid;
    }

    public Person getPersonid() {
        return personid;
    }

    public void setPersonid(Person personid) {
        this.personid = personid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tripid != null ? tripid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Trip)) {
            return false;
        }
        Trip other = (Trip) object;
        if ((this.tripid == null && other.tripid != null) || (this.tripid != null && !this.tripid.equals(other.tripid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.salesforce.atsja.zherokudemo.entity.Trip[ tripid=" + tripid + " ]";
    }
    
}
