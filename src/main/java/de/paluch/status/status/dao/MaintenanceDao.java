package de.paluch.status.status.dao;

import de.paluch.status.status.entity.MaintenanceEntity;

import javax.persistence.Query;
import java.util.List;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 22.11.12 20:53
 */
public class MaintenanceDao extends AbstractDao<Long, MaintenanceEntity> {
    public MaintenanceDao() {
        super(MaintenanceEntity.class);
    }

    public List<MaintenanceEntity> getList(long serviceId) {
        Query query = getEntityManager().createNamedQuery("getMaintenanceByServiceId");
        query.setParameter("serviceId", serviceId);
        return query.getResultList();
    }


}
