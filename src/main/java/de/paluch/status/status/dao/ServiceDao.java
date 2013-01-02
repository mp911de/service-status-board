package de.paluch.status.status.dao;

import de.paluch.status.status.entity.ServiceEntity;

import javax.persistence.Query;
import java.util.List;

/**
 * @author <a href="mailto:mark.paluch@1und1.de">Mark Paluch</a>
 * @since 22.11.12 20:53
 */
public class ServiceDao extends AbstractDao<Long, ServiceEntity> {
    public ServiceDao() {
        super(ServiceEntity.class);
    }

    public List<ServiceEntity> getList() {
        Query query = getEntityManager().createNamedQuery("getServices");
        return query.getResultList();
    }

    public ServiceEntity getByKey(String serviceKey) {

        Query query = getEntityManager().createNamedQuery("getServiceByKey");
        query.setParameter("serviceKey", serviceKey);
        return (ServiceEntity) query.getSingleResult();
    }
}
