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
public class TripJpaController implements Serializable {

    public TripJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Trip trip) throws PreexistingEntityException, Exception {
        if (trip.getFlightCollection() == null) {
            trip.setFlightCollection(new ArrayList<Flight>());
        }
        if (trip.getCarrentalCollection() == null) {
            trip.setCarrentalCollection(new ArrayList<Carrental>());
        }
        if (trip.getHotelCollection() == null) {
            trip.setHotelCollection(new ArrayList<Hotel>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Triptype triptypeid = trip.getTriptypeid();
            if (triptypeid != null) {
                triptypeid = em.getReference(triptypeid.getClass(), triptypeid.getTriptypeid());
                trip.setTriptypeid(triptypeid);
            }
            Person personid = trip.getPersonid();
            if (personid != null) {
                personid = em.getReference(personid.getClass(), personid.getPersonid());
                trip.setPersonid(personid);
            }
            Collection<Flight> attachedFlightCollection = new ArrayList<Flight>();
            for (Flight flightCollectionFlightToAttach : trip.getFlightCollection()) {
                flightCollectionFlightToAttach = em.getReference(flightCollectionFlightToAttach.getClass(), flightCollectionFlightToAttach.getFlightid());
                attachedFlightCollection.add(flightCollectionFlightToAttach);
            }
            trip.setFlightCollection(attachedFlightCollection);
            Collection<Carrental> attachedCarrentalCollection = new ArrayList<Carrental>();
            for (Carrental carrentalCollectionCarrentalToAttach : trip.getCarrentalCollection()) {
                carrentalCollectionCarrentalToAttach = em.getReference(carrentalCollectionCarrentalToAttach.getClass(), carrentalCollectionCarrentalToAttach.getCarrentalid());
                attachedCarrentalCollection.add(carrentalCollectionCarrentalToAttach);
            }
            trip.setCarrentalCollection(attachedCarrentalCollection);
            Collection<Hotel> attachedHotelCollection = new ArrayList<Hotel>();
            for (Hotel hotelCollectionHotelToAttach : trip.getHotelCollection()) {
                hotelCollectionHotelToAttach = em.getReference(hotelCollectionHotelToAttach.getClass(), hotelCollectionHotelToAttach.getHotelid());
                attachedHotelCollection.add(hotelCollectionHotelToAttach);
            }
            trip.setHotelCollection(attachedHotelCollection);
            em.persist(trip);
            if (triptypeid != null) {
                triptypeid.getTripCollection().add(trip);
                triptypeid = em.merge(triptypeid);
            }
            if (personid != null) {
                personid.getTripCollection().add(trip);
                personid = em.merge(personid);
            }
            for (Flight flightCollectionFlight : trip.getFlightCollection()) {
                Trip oldTripidOfFlightCollectionFlight = flightCollectionFlight.getTripid();
                flightCollectionFlight.setTripid(trip);
                flightCollectionFlight = em.merge(flightCollectionFlight);
                if (oldTripidOfFlightCollectionFlight != null) {
                    oldTripidOfFlightCollectionFlight.getFlightCollection().remove(flightCollectionFlight);
                    oldTripidOfFlightCollectionFlight = em.merge(oldTripidOfFlightCollectionFlight);
                }
            }
            for (Carrental carrentalCollectionCarrental : trip.getCarrentalCollection()) {
                Trip oldTripidOfCarrentalCollectionCarrental = carrentalCollectionCarrental.getTripid();
                carrentalCollectionCarrental.setTripid(trip);
                carrentalCollectionCarrental = em.merge(carrentalCollectionCarrental);
                if (oldTripidOfCarrentalCollectionCarrental != null) {
                    oldTripidOfCarrentalCollectionCarrental.getCarrentalCollection().remove(carrentalCollectionCarrental);
                    oldTripidOfCarrentalCollectionCarrental = em.merge(oldTripidOfCarrentalCollectionCarrental);
                }
            }
            for (Hotel hotelCollectionHotel : trip.getHotelCollection()) {
                Trip oldTripidOfHotelCollectionHotel = hotelCollectionHotel.getTripid();
                hotelCollectionHotel.setTripid(trip);
                hotelCollectionHotel = em.merge(hotelCollectionHotel);
                if (oldTripidOfHotelCollectionHotel != null) {
                    oldTripidOfHotelCollectionHotel.getHotelCollection().remove(hotelCollectionHotel);
                    oldTripidOfHotelCollectionHotel = em.merge(oldTripidOfHotelCollectionHotel);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTrip(trip.getTripid()) != null) {
                throw new PreexistingEntityException("Trip " + trip + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Trip trip) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Trip persistentTrip = em.find(Trip.class, trip.getTripid());
            Triptype triptypeidOld = persistentTrip.getTriptypeid();
            Triptype triptypeidNew = trip.getTriptypeid();
            Person personidOld = persistentTrip.getPersonid();
            Person personidNew = trip.getPersonid();
            Collection<Flight> flightCollectionOld = persistentTrip.getFlightCollection();
            Collection<Flight> flightCollectionNew = trip.getFlightCollection();
            Collection<Carrental> carrentalCollectionOld = persistentTrip.getCarrentalCollection();
            Collection<Carrental> carrentalCollectionNew = trip.getCarrentalCollection();
            Collection<Hotel> hotelCollectionOld = persistentTrip.getHotelCollection();
            Collection<Hotel> hotelCollectionNew = trip.getHotelCollection();
            List<String> illegalOrphanMessages = null;
            for (Flight flightCollectionOldFlight : flightCollectionOld) {
                if (!flightCollectionNew.contains(flightCollectionOldFlight)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Flight " + flightCollectionOldFlight + " since its tripid field is not nullable.");
                }
            }
            for (Carrental carrentalCollectionOldCarrental : carrentalCollectionOld) {
                if (!carrentalCollectionNew.contains(carrentalCollectionOldCarrental)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Carrental " + carrentalCollectionOldCarrental + " since its tripid field is not nullable.");
                }
            }
            for (Hotel hotelCollectionOldHotel : hotelCollectionOld) {
                if (!hotelCollectionNew.contains(hotelCollectionOldHotel)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Hotel " + hotelCollectionOldHotel + " since its tripid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (triptypeidNew != null) {
                triptypeidNew = em.getReference(triptypeidNew.getClass(), triptypeidNew.getTriptypeid());
                trip.setTriptypeid(triptypeidNew);
            }
            if (personidNew != null) {
                personidNew = em.getReference(personidNew.getClass(), personidNew.getPersonid());
                trip.setPersonid(personidNew);
            }
            Collection<Flight> attachedFlightCollectionNew = new ArrayList<Flight>();
            for (Flight flightCollectionNewFlightToAttach : flightCollectionNew) {
                flightCollectionNewFlightToAttach = em.getReference(flightCollectionNewFlightToAttach.getClass(), flightCollectionNewFlightToAttach.getFlightid());
                attachedFlightCollectionNew.add(flightCollectionNewFlightToAttach);
            }
            flightCollectionNew = attachedFlightCollectionNew;
            trip.setFlightCollection(flightCollectionNew);
            Collection<Carrental> attachedCarrentalCollectionNew = new ArrayList<Carrental>();
            for (Carrental carrentalCollectionNewCarrentalToAttach : carrentalCollectionNew) {
                carrentalCollectionNewCarrentalToAttach = em.getReference(carrentalCollectionNewCarrentalToAttach.getClass(), carrentalCollectionNewCarrentalToAttach.getCarrentalid());
                attachedCarrentalCollectionNew.add(carrentalCollectionNewCarrentalToAttach);
            }
            carrentalCollectionNew = attachedCarrentalCollectionNew;
            trip.setCarrentalCollection(carrentalCollectionNew);
            Collection<Hotel> attachedHotelCollectionNew = new ArrayList<Hotel>();
            for (Hotel hotelCollectionNewHotelToAttach : hotelCollectionNew) {
                hotelCollectionNewHotelToAttach = em.getReference(hotelCollectionNewHotelToAttach.getClass(), hotelCollectionNewHotelToAttach.getHotelid());
                attachedHotelCollectionNew.add(hotelCollectionNewHotelToAttach);
            }
            hotelCollectionNew = attachedHotelCollectionNew;
            trip.setHotelCollection(hotelCollectionNew);
            trip = em.merge(trip);
            if (triptypeidOld != null && !triptypeidOld.equals(triptypeidNew)) {
                triptypeidOld.getTripCollection().remove(trip);
                triptypeidOld = em.merge(triptypeidOld);
            }
            if (triptypeidNew != null && !triptypeidNew.equals(triptypeidOld)) {
                triptypeidNew.getTripCollection().add(trip);
                triptypeidNew = em.merge(triptypeidNew);
            }
            if (personidOld != null && !personidOld.equals(personidNew)) {
                personidOld.getTripCollection().remove(trip);
                personidOld = em.merge(personidOld);
            }
            if (personidNew != null && !personidNew.equals(personidOld)) {
                personidNew.getTripCollection().add(trip);
                personidNew = em.merge(personidNew);
            }
            for (Flight flightCollectionNewFlight : flightCollectionNew) {
                if (!flightCollectionOld.contains(flightCollectionNewFlight)) {
                    Trip oldTripidOfFlightCollectionNewFlight = flightCollectionNewFlight.getTripid();
                    flightCollectionNewFlight.setTripid(trip);
                    flightCollectionNewFlight = em.merge(flightCollectionNewFlight);
                    if (oldTripidOfFlightCollectionNewFlight != null && !oldTripidOfFlightCollectionNewFlight.equals(trip)) {
                        oldTripidOfFlightCollectionNewFlight.getFlightCollection().remove(flightCollectionNewFlight);
                        oldTripidOfFlightCollectionNewFlight = em.merge(oldTripidOfFlightCollectionNewFlight);
                    }
                }
            }
            for (Carrental carrentalCollectionNewCarrental : carrentalCollectionNew) {
                if (!carrentalCollectionOld.contains(carrentalCollectionNewCarrental)) {
                    Trip oldTripidOfCarrentalCollectionNewCarrental = carrentalCollectionNewCarrental.getTripid();
                    carrentalCollectionNewCarrental.setTripid(trip);
                    carrentalCollectionNewCarrental = em.merge(carrentalCollectionNewCarrental);
                    if (oldTripidOfCarrentalCollectionNewCarrental != null && !oldTripidOfCarrentalCollectionNewCarrental.equals(trip)) {
                        oldTripidOfCarrentalCollectionNewCarrental.getCarrentalCollection().remove(carrentalCollectionNewCarrental);
                        oldTripidOfCarrentalCollectionNewCarrental = em.merge(oldTripidOfCarrentalCollectionNewCarrental);
                    }
                }
            }
            for (Hotel hotelCollectionNewHotel : hotelCollectionNew) {
                if (!hotelCollectionOld.contains(hotelCollectionNewHotel)) {
                    Trip oldTripidOfHotelCollectionNewHotel = hotelCollectionNewHotel.getTripid();
                    hotelCollectionNewHotel.setTripid(trip);
                    hotelCollectionNewHotel = em.merge(hotelCollectionNewHotel);
                    if (oldTripidOfHotelCollectionNewHotel != null && !oldTripidOfHotelCollectionNewHotel.equals(trip)) {
                        oldTripidOfHotelCollectionNewHotel.getHotelCollection().remove(hotelCollectionNewHotel);
                        oldTripidOfHotelCollectionNewHotel = em.merge(oldTripidOfHotelCollectionNewHotel);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = trip.getTripid();
                if (findTrip(id) == null) {
                    throw new NonexistentEntityException("The trip with id " + id + " no longer exists.");
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
            Trip trip;
            try {
                trip = em.getReference(Trip.class, id);
                trip.getTripid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The trip with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Flight> flightCollectionOrphanCheck = trip.getFlightCollection();
            for (Flight flightCollectionOrphanCheckFlight : flightCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Trip (" + trip + ") cannot be destroyed since the Flight " + flightCollectionOrphanCheckFlight + " in its flightCollection field has a non-nullable tripid field.");
            }
            Collection<Carrental> carrentalCollectionOrphanCheck = trip.getCarrentalCollection();
            for (Carrental carrentalCollectionOrphanCheckCarrental : carrentalCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Trip (" + trip + ") cannot be destroyed since the Carrental " + carrentalCollectionOrphanCheckCarrental + " in its carrentalCollection field has a non-nullable tripid field.");
            }
            Collection<Hotel> hotelCollectionOrphanCheck = trip.getHotelCollection();
            for (Hotel hotelCollectionOrphanCheckHotel : hotelCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Trip (" + trip + ") cannot be destroyed since the Hotel " + hotelCollectionOrphanCheckHotel + " in its hotelCollection field has a non-nullable tripid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Triptype triptypeid = trip.getTriptypeid();
            if (triptypeid != null) {
                triptypeid.getTripCollection().remove(trip);
                triptypeid = em.merge(triptypeid);
            }
            Person personid = trip.getPersonid();
            if (personid != null) {
                personid.getTripCollection().remove(trip);
                personid = em.merge(personid);
            }
            em.remove(trip);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Trip> findTripEntities() {
        return findTripEntities(true, -1, -1);
    }

    public List<Trip> findTripEntities(int maxResults, int firstResult) {
        return findTripEntities(false, maxResults, firstResult);
    }

    private List<Trip> findTripEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Trip.class));
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

    public Trip findTrip(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Trip.class, id);
        } finally {
            em.close();
        }
    }

    public int getTripCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Trip> rt = cq.from(Trip.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
