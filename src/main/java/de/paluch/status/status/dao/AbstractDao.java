package de.paluch.status.status.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


/**
 * Abstract Dao with basic methods.
 * 
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @param <K>
 *            Key-Type
 * @param <E>
 *            Entity-Type
 */
public class AbstractDao<K, E> {

    private final Class<E> entityClass;

    @PersistenceContext(unitName = "ssb")
    private EntityManager entityManager;

    /**
     * @param entityClass
     */
    public AbstractDao(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * @return the entityManager
     */
    public EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * Enable Query-Cache for a specific query.
     * 
     * @param query
     */
    protected void enableQueryCache(Query query) {
        query.setHint("org.hibernate.cacheable", true);
    }

    /**
     * @return true/false to invoke flush after create call. Disabled by default.
     */
    protected boolean flushAfterCreate() {
        return false;
    }

    /**
     * @param entity
     * @return creates an entity
     * @throws DiyMwPersistenceException
     */
    public E create(E entity) throws DiyMwPersistenceException {
        try {
            getEntityManager().persist(entity);
            if (flushAfterCreate()) {
                getEntityManager().flush();
            }
            return entity;
        } catch (javax.persistence.PersistenceException e) {
            throw new DiyMwPersistenceException(e);
        }
    }

    /**
     * @param id
     * @return the entity found by id
     * @throws DiyMwPersistenceException
     */
    public E getById(K id) throws DiyMwPersistenceException {
        try {
            return getEntityManager().find(entityClass, id);
        } catch (javax.persistence.PersistenceException e) {
            throw new DiyMwPersistenceException(e);
        }
    }

    /**
     * @param entity
     * @return the updated entity
     * @throws DiyMwPersistenceException
     */
    public E update(E entity) throws DiyMwPersistenceException {
        try {
            return getEntityManager().merge(entity);
        } catch (javax.persistence.PersistenceException e) {
            throw new DiyMwPersistenceException(e);
        }
    }

    /**
     * @param entity
     * @throws DiyMwPersistenceException
     */
    public void delete(E entity) throws DiyMwPersistenceException {
        try {
            getEntityManager().remove(entity);
        } catch (javax.persistence.PersistenceException e) {
            throw new DiyMwPersistenceException(e);
        }
    }

    /**
     * Retrieve a List of given entity.
     * 
     * @return List<E>
     * @throws DiyMwPersistenceException
     */
    @SuppressWarnings("unchecked")
    public List<E> getList() throws DiyMwPersistenceException {
        try {
            Query query = getEntityManager().createQuery("from  " + entityClass.getSimpleName());
            enableQueryCache(query);
            return query.getResultList();
        } catch (javax.persistence.PersistenceException e) {
            throw new DiyMwPersistenceException(e);
        }
    }

    /**
     * @param id
     * @return thre if an entity with the given id exists
     * @throws DiyMwPersistenceException
     */
    public boolean exists(K id) throws DiyMwPersistenceException {
        return getById(id) == null ? false : true;
    }

    /**
     * @param entity
     * @throws DiyMwPersistenceException
     */
    public void refresh(E entity) throws DiyMwPersistenceException {
        try {
            getEntityManager().refresh(entity);
        } catch (javax.persistence.PersistenceException e) {
            throw new DiyMwPersistenceException(e);
        }
    }

}
