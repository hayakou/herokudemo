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
@Table(name = "triptype")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Triptype.findAll", query = "SELECT t FROM Triptype t"),
    @NamedQuery(name = "Triptype.findByTriptypeid", query = "SELECT t FROM Triptype t WHERE t.triptypeid = :triptypeid"),
    @NamedQuery(name = "Triptype.findByName", query = "SELECT t FROM Triptype t WHERE t.name = :name"),
    @NamedQuery(name = "Triptype.findByDescription", query = "SELECT t FROM Triptype t WHERE t.description = :description"),
    @NamedQuery(name = "Triptype.findByLastupdated", query = "SELECT t FROM Triptype t WHERE t.lastupdated = :lastupdated")})
public class Triptype implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "triptypeid")
    private Integer triptypeid;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "lastupdated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastupdated;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "triptypeid")
    private Collection<Trip> tripCollection;

    public Triptype() {
    }

    public Triptype(Integer triptypeid) {
        this.triptypeid = triptypeid;
    }

    public Integer getTriptypeid() {
        return triptypeid;
    }

    public void setTriptypeid(Integer triptypeid) {
        this.triptypeid = triptypeid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getLastupdated() {
        return lastupdated;
    }

    public void setLastupdated(Date lastupdated) {
        this.lastupdated = lastupdated;
    }

    @XmlTransient
    public Collection<Trip> getTripCollection() {
        return tripCollection;
    }

    public void setTripCollection(Collection<Trip> tripCollection) {
        this.tripCollection = tripCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (triptypeid != null ? triptypeid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Triptype)) {
            return false;
        }
        Triptype other = (Triptype) object;
        if ((this.triptypeid == null && other.triptypeid != null) || (this.triptypeid != null && !this.triptypeid.equals(other.triptypeid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.salesforce.atsja.zherokudemo.entity.Triptype[ triptypeid=" + triptypeid + " ]";
    }
    
}
