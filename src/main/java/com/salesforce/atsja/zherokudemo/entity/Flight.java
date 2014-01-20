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
@Table(name = "flight")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Flight.findAll", query = "SELECT f FROM Flight f"),
    @NamedQuery(name = "Flight.findByFlightid", query = "SELECT f FROM Flight f WHERE f.flightid = :flightid"),
    @NamedQuery(name = "Flight.findByDirection", query = "SELECT f FROM Flight f WHERE f.direction = :direction"),
    @NamedQuery(name = "Flight.findByFlightnum", query = "SELECT f FROM Flight f WHERE f.flightnum = :flightnum"),
    @NamedQuery(name = "Flight.findByDeptime", query = "SELECT f FROM Flight f WHERE f.deptime = :deptime"),
    @NamedQuery(name = "Flight.findByDepairport", query = "SELECT f FROM Flight f WHERE f.depairport = :depairport"),
    @NamedQuery(name = "Flight.findByArrtime", query = "SELECT f FROM Flight f WHERE f.arrtime = :arrtime"),
    @NamedQuery(name = "Flight.findByArrairport", query = "SELECT f FROM Flight f WHERE f.arrairport = :arrairport"),
    @NamedQuery(name = "Flight.findByAirlinename", query = "SELECT f FROM Flight f WHERE f.airlinename = :airlinename"),
    @NamedQuery(name = "Flight.findByBookingstatus", query = "SELECT f FROM Flight f WHERE f.bookingstatus = :bookingstatus"),
    @NamedQuery(name = "Flight.findByLastupdated", query = "SELECT f FROM Flight f WHERE f.lastupdated = :lastupdated")})
public class Flight implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "flightid")
    private Integer flightid;
    @Basic(optional = false)
    @Column(name = "direction")
    private char direction;
    @Column(name = "flightnum")
    private String flightnum;
    @Column(name = "deptime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deptime;
    @Column(name = "depairport")
    private String depairport;
    @Column(name = "arrtime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date arrtime;
    @Column(name = "arrairport")
    private String arrairport;
    @Column(name = "airlinename")
    private String airlinename;
    @Column(name = "bookingstatus")
    private String bookingstatus;
    @Column(name = "lastupdated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastupdated;
    @JoinColumn(name = "tripid", referencedColumnName = "tripid")
    @ManyToOne(optional = false)
    private Trip tripid;

    public Flight() {
    }

    public Flight(Integer flightid) {
        this.flightid = flightid;
    }

    public Flight(Integer flightid, char direction) {
        this.flightid = flightid;
        this.direction = direction;
    }

    public Integer getFlightid() {
        return flightid;
    }

    public void setFlightid(Integer flightid) {
        this.flightid = flightid;
    }

    public char getDirection() {
        return direction;
    }

    public void setDirection(char direction) {
        this.direction = direction;
    }

    public String getFlightnum() {
        return flightnum;
    }

    public void setFlightnum(String flightnum) {
        this.flightnum = flightnum;
    }

    public Date getDeptime() {
        return deptime;
    }

    public void setDeptime(Date deptime) {
        this.deptime = deptime;
    }

    public String getDepairport() {
        return depairport;
    }

    public void setDepairport(String depairport) {
        this.depairport = depairport;
    }

    public Date getArrtime() {
        return arrtime;
    }

    public void setArrtime(Date arrtime) {
        this.arrtime = arrtime;
    }

    public String getArrairport() {
        return arrairport;
    }

    public void setArrairport(String arrairport) {
        this.arrairport = arrairport;
    }

    public String getAirlinename() {
        return airlinename;
    }

    public void setAirlinename(String airlinename) {
        this.airlinename = airlinename;
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
        hash += (flightid != null ? flightid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Flight)) {
            return false;
        }
        Flight other = (Flight) object;
        if ((this.flightid == null && other.flightid != null) || (this.flightid != null && !this.flightid.equals(other.flightid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.salesforce.atsja.zherokudemo.entity.Flight[ flightid=" + flightid + " ]";
    }
    
}
