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
public class PersonJpaController implements Serializable {

    public PersonJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Person person) throws PreexistingEntityException, Exception {
        if (person.getTripCollection() == null) {
            person.setTripCollection(new ArrayList<Trip>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Trip> attachedTripCollection = new ArrayList<Trip>();
            for (Trip tripCollectionTripToAttach : person.getTripCollection()) {
                tripCollectionTripToAttach = em.getReference(tripCollectionTripToAttach.getClass(), tripCollectionTripToAttach.getTripid());
                attachedTripCollection.add(tripCollectionTripToAttach);
            }
            person.setTripCollection(attachedTripCollection);
            em.persist(person);
            for (Trip tripCollectionTrip : person.getTripCollection()) {
                Person oldPersonidOfTripCollectionTrip = tripCollectionTrip.getPersonid();
                tripCollectionTrip.setPersonid(person);
                tripCollectionTrip = em.merge(tripCollectionTrip);
                if (oldPersonidOfTripCollectionTrip != null) {
                    oldPersonidOfTripCollectionTrip.getTripCollection().remove(tripCollectionTrip);
                    oldPersonidOfTripCollectionTrip = em.merge(oldPersonidOfTripCollectionTrip);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPerson(person.getPersonid()) != null) {
                throw new PreexistingEntityException("Person " + person + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Person person) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Person persistentPerson = em.find(Person.class, person.getPersonid());
            Collection<Trip> tripCollectionOld = persistentPerson.getTripCollection();
            Collection<Trip> tripCollectionNew = person.getTripCollection();
            List<String> illegalOrphanMessages = null;
            for (Trip tripCollectionOldTrip : tripCollectionOld) {
                if (!tripCollectionNew.contains(tripCollectionOldTrip)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Trip " + tripCollectionOldTrip + " since its personid field is not nullable.");
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
            person.setTripCollection(tripCollectionNew);
            person = em.merge(person);
            for (Trip tripCollectionNewTrip : tripCollectionNew) {
                if (!tripCollectionOld.contains(tripCollectionNewTrip)) {
                    Person oldPersonidOfTripCollectionNewTrip = tripCollectionNewTrip.getPersonid();
                    tripCollectionNewTrip.setPersonid(person);
                    tripCollectionNewTrip = em.merge(tripCollectionNewTrip);
                    if (oldPersonidOfTripCollectionNewTrip != null && !oldPersonidOfTripCollectionNewTrip.equals(person)) {
                        oldPersonidOfTripCollectionNewTrip.getTripCollection().remove(tripCollectionNewTrip);
                        oldPersonidOfTripCollectionNewTrip = em.merge(oldPersonidOfTripCollectionNewTrip);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = person.getPersonid();
                if (findPerson(id) == null) {
                    throw new NonexistentEntityException("The person with id " + id + " no longer exists.");
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
            Person person;
            try {
                person = em.getReference(Person.class, id);
                person.getPersonid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The person with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Trip> tripCollectionOrphanCheck = person.getTripCollection();
            for (Trip tripCollectionOrphanCheckTrip : tripCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Person (" + person + ") cannot be destroyed since the Trip " + tripCollectionOrphanCheckTrip + " in its tripCollection field has a non-nullable personid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(person);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Person> findPersonEntities() {
        return findPersonEntities(true, -1, -1);
    }

    public List<Person> findPersonEntities(int maxResults, int firstResult) {
        return findPersonEntities(false, maxResults, firstResult);
    }

    private List<Person> findPersonEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Person.class));
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

    public Person findPerson(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Person.class, id);
        } finally {
            em.close();
        }
    }

    public int getPersonCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Person> rt = cq.from(Person.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
