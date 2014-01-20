/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.salesforce.atsja.zherokudemo.entity;

import com.salesforce.atsja.zherokudemo.entity.exceptions.IllegalOrphanException;
import com.salesforce.atsja.zherokudemo.entity.exceptions.NonexistentEntityException;
import com.salesforce.atsja.zherokudemo.entity.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author hayakou
 */
public class TriptypeJpaController implements Serializable {

    public TriptypeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Triptype triptype) throws PreexistingEntityException, Exception {
        if (triptype.getTripCollection() == null) {
            triptype.setTripCollection(new ArrayList<Trip>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Trip> attachedTripCollection = new ArrayList<Trip>();
            for (Trip tripCollectionTripToAttach : triptype.getTripCollection()) {
                tripCollectionTripToAttach = em.getReference(tripCollectionTripToAttach.getClass(), tripCollectionTripToAttach.getTripid());
                attachedTripCollection.add(tripCollectionTripToAttach);
            }
            triptype.setTripCollection(attachedTripCollection);
            em.persist(triptype);
            for (Trip tripCollectionTrip : triptype.getTripCollection()) {
                Triptype oldTriptypeidOfTripCollectionTrip = tripCollectionTrip.getTriptypeid();
                tripCollectionTrip.setTriptypeid(triptype);
                tripCollectionTrip = em.merge(tripCollectionTrip);
                if (oldTriptypeidOfTripCollectionTrip != null) {
                    oldTriptypeidOfTripCollectionTrip.getTripCollection().remove(tripCollectionTrip);
                    oldTriptypeidOfTripCollectionTrip = em.merge(oldTriptypeidOfTripCollectionTrip);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTriptype(triptype.getTriptypeid()) != null) {
                throw new PreexistingEntityException("Triptype " + triptype + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Triptype triptype) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Triptype persistentTriptype = em.find(Triptype.class, triptype.getTriptypeid());
            Collection<Trip> tripCollectionOld = persistentTriptype.getTripCollection();
            Collection<Trip> tripCollectionNew = triptype.getTripCollection();
            List<String> illegalOrphanMessages = null;
            for (Trip tripCollectionOldTrip : tripCollectionOld) {
                if (!tripCollectionNew.contains(tripCollectionOldTrip)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Trip " + tripCollectionOldTrip + " since its triptypeid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Trip> attachedTripCollectionNew = new ArrayList<Trip>();
            for (Trip tripCollectionNewTripToAttach : tripCollectionNew) {
                tripCollectionNewTripToAttach = em.getReference(tripCollectionNewTripToAttach.getClass(), tripCollectionNewTripToAttach.getTripid());
                attachedTripCollectionNew.add(tripCollectionNewTripToAttach);
            }
            tripCollectionNew = attachedTripCollectionNew;
            triptype.setTripCollection(tripCollectionNew);
            triptype = em.merge(triptype);
            for (Trip tripCollectionNewTrip : tripCollectionNew) {
                if (!tripCollectionOld.contains(tripCollectionNewTrip)) {
                    Triptype oldTriptypeidOfTripCollectionNewTrip = tripCollectionNewTrip.getTriptypeid();
                    tripCollectionNewTrip.setTriptypeid(triptype);
                    tripCollectionNewTrip = em.merge(tripCollectionNewTrip);
                    if (oldTriptypeidOfTripCollectionNewTrip != null && !oldTriptypeidOfTripCollectionNewTrip.equals(triptype)) {
                        oldTriptypeidOfTripCollectionNewTrip.getTripCollection().remove(tripCollectionNewTrip);
                        oldTriptypeidOfTripCollectionNewTrip = em.merge(oldTriptypeidOfTripCollectionNewTrip);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = triptype.getTriptypeid();
                if (findTriptype(id) == null) {
                    throw new NonexistentEntityException("The triptype with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Triptype triptype;
            try {
                triptype = em.getReference(Triptype.class, id);
                triptype.getTriptypeid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The triptype with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Trip> tripCollectionOrphanCheck = triptype.getTripCollection();
            for (Trip tripCollectionOrphanCheckTrip : tripCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Triptype (" + triptype + ") cannot be destroyed since the Trip " + tripCollectionOrphanCheckTrip + " in its tripCollection field has a non-nullable triptypeid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(triptype);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Triptype> findTriptypeEntities() {
        return findTriptypeEntities(true, -1, -1);
    }

    public List<Triptype> findTriptypeEntities(int maxResults, int firstResult) {
        return findTriptypeEntities(false, maxResults, firstResult);
    }

    private List<Triptype> findTriptypeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Triptype.class));
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

    public Triptype findTriptype(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Triptype.class, id);
        } finally {
            em.close();
        }
    }

    public int getTriptypeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Triptype> rt = cq.from(Triptype.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
