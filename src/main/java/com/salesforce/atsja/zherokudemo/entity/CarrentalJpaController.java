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
public class CarrentalJpaController implements Serializable {

    public CarrentalJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Carrental carrental) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Trip tripid = carrental.getTripid();
            if (tripid != null) {
                tripid = em.getReference(tripid.getClass(), tripid.getTripid());
                carrental.setTripid(tripid);
            }
            em.persist(carrental);
            if (tripid != null) {
                tripid.getCarrentalCollection().add(carrental);
                tripid = em.merge(tripid);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCarrental(carrental.getCarrentalid()) != null) {
                throw new PreexistingEntityException("Carrental " + carrental + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Carrental carrental) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Carrental persistentCarrental = em.find(Carrental.class, carrental.getCarrentalid());
            Trip tripidOld = persistentCarrental.getTripid();
            Trip tripidNew = carrental.getTripid();
            if (tripidNew != null) {
                tripidNew = em.getReference(tripidNew.getClass(), tripidNew.getTripid());
                carrental.setTripid(tripidNew);
            }
            carrental = em.merge(carrental);
            if (tripidOld != null && !tripidOld.equals(tripidNew)) {
                tripidOld.getCarrentalCollection().remove(carrental);
                tripidOld = em.merge(tripidOld);
            }
            if (tripidNew != null && !tripidNew.equals(tripidOld)) {
                tripidNew.getCarrentalCollection().add(carrental);
                tripidNew = em.merge(tripidNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = carrental.getCarrentalid();
                if (findCarrental(id) == null) {
                    throw new NonexistentEntityException("The carrental with id " + id + " no longer exists.");
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
            Carrental carrental;
            try {
                carrental = em.getReference(Carrental.class, id);
                carrental.getCarrentalid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The carrental with id " + id + " no longer exists.", enfe);
            }
            Trip tripid = carrental.getTripid();
            if (tripid != null) {
                tripid.getCarrentalCollection().remove(carrental);
                tripid = em.merge(tripid);
            }
            em.remove(carrental);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Carrental> findCarrentalEntities() {
        return findCarrentalEntities(true, -1, -1);
    }

    public List<Carrental> findCarrentalEntities(int maxResults, int firstResult) {
        return findCarrentalEntities(false, maxResults, firstResult);
    }

    private List<Carrental> findCarrentalEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Carrental.class));
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

    public Carrental findCarrental(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Carrental.class, id);
        } finally {
            em.close();
        }
    }

    public int getCarrentalCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Carrental> rt = cq.from(Carrental.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
