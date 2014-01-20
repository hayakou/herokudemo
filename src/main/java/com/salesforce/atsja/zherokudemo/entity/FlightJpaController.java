/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.salesforce.atsja.zherokudemo.entity;

import com.salesforce.atsja.zherokudemo.entity.exceptions.NonexistentEntityException;
import com.salesforce.atsja.zherokudemo.entity.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author hayakou
 */
public class FlightJpaController implements Serializable {

    public FlightJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Flight flight) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Trip tripid = flight.getTripid();
            if (tripid != null) {
                tripid = em.getReference(tripid.getClass(), tripid.getTripid());
                flight.setTripid(tripid);
            }
            em.persist(flight);
            if (tripid != null) {
                tripid.getFlightCollection().add(flight);
                tripid = em.merge(tripid);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFlight(flight.getFlightid()) != null) {
                throw new PreexistingEntityException("Flight " + flight + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Flight flight) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Flight persistentFlight = em.find(Flight.class, flight.getFlightid());
            Trip tripidOld = persistentFlight.getTripid();
            Trip tripidNew = flight.getTripid();
            if (tripidNew != null) {
                tripidNew = em.getReference(tripidNew.getClass(), tripidNew.getTripid());
                flight.setTripid(tripidNew);
            }
            flight = em.merge(flight);
            if (tripidOld != null && !tripidOld.equals(tripidNew)) {
                tripidOld.getFlightCollection().remove(flight);
                tripidOld = em.merge(tripidOld);
            }
            if (tripidNew != null && !tripidNew.equals(tripidOld)) {
                tripidNew.getFlightCollection().add(flight);
                tripidNew = em.merge(tripidNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = flight.getFlightid();
                if (findFlight(id) == null) {
                    throw new NonexistentEntityException("The flight with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Flight flight;
            try {
                flight = em.getReference(Flight.class, id);
                flight.getFlightid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The flight with id " + id + " no longer exists.", enfe);
            }
            Trip tripid = flight.getTripid();
            if (tripid != null) {
                tripid.getFlightCollection().remove(flight);
                tripid = em.merge(tripid);
            }
            em.remove(flight);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Flight> findFlightEntities() {
        return findFlightEntities(true, -1, -1);
    }

    public List<Flight> findFlightEntities(int maxResults, int firstResult) {
        return findFlightEntities(false, maxResults, firstResult);
    }

    private List<Flight> findFlightEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Flight.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Flight findFlight(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Flight.class, id);
        } finally {
            em.close();
        }
    }

    public int getFlightCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Flight> rt = cq.from(Flight.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
