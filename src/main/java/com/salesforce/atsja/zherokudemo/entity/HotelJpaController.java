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
public class HotelJpaController implements Serializable {

    public HotelJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Hotel hotel) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Trip tripid = hotel.getTripid();
            if (tripid != null) {
                tripid = em.getReference(tripid.getClass(), tripid.getTripid());
                hotel.setTripid(tripid);
            }
            em.persist(hotel);
            if (tripid != null) {
                tripid.getHotelCollection().add(hotel);
                tripid = em.merge(tripid);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findHotel(hotel.getHotelid()) != null) {
                throw new PreexistingEntityException("Hotel " + hotel + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Hotel hotel) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Hotel persistentHotel = em.find(Hotel.class, hotel.getHotelid());
            Trip tripidOld = persistentHotel.getTripid();
            Trip tripidNew = hotel.getTripid();
            if (tripidNew != null) {
                tripidNew = em.getReference(tripidNew.getClass(), tripidNew.getTripid());
                hotel.setTripid(tripidNew);
            }
            hotel = em.merge(hotel);
            if (tripidOld != null && !tripidOld.equals(tripidNew)) {
                tripidOld.getHotelCollection().remove(hotel);
                tripidOld = em.merge(tripidOld);
            }
            if (tripidNew != null && !tripidNew.equals(tripidOld)) {
                tripidNew.getHotelCollection().add(hotel);
                tripidNew = em.merge(tripidNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = hotel.getHotelid();
                if (findHotel(id) == null) {
                    throw new NonexistentEntityException("The hotel with id " + id + " no longer exists.");
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
            Hotel hotel;
            try {
                hotel = em.getReference(Hotel.class, id);
                hotel.getHotelid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The hotel with id " + id + " no longer exists.", enfe);
            }
            Trip tripid = hotel.getTripid();
            if (tripid != null) {
                tripid.getHotelCollection().remove(hotel);
                tripid = em.merge(tripid);
            }
            em.remove(hotel);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Hotel> findHotelEntities() {
        return findHotelEntities(true, -1, -1);
    }

    public List<Hotel> findHotelEntities(int maxResults, int firstResult) {
        return findHotelEntities(false, maxResults, firstResult);
    }

    private List<Hotel> findHotelEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Hotel.class));
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

    public Hotel findHotel(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Hotel.class, id);
        } finally {
            em.close();
        }
    }

    public int getHotelCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Hotel> rt = cq.from(Hotel.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
