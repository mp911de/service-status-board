package de.paluch.status.status.dao;

import de.paluch.status.status.entity.ServiceStateEntity;

import javax.persistence.Query;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:mark.paluch@1und1.de">Mark Paluch</a>
 * @since 22.11.12 20:53
 */
public class ServiceStateDao extends AbstractDao<Long, ServiceStateEntity> {
    public ServiceStateDao() {
        super(ServiceStateEntity.class);
    }

    public List<ServiceStateEntity> getList(long serviceId) {
        Query query = getEntityManager().createNamedQuery("getServiceStateByService");
        query.setParameter("serviceId", serviceId);
        return query.getResultList();
    }

    public List<ServiceStateEntity> getList(Date checkDate) {
        Query query = getEntityManager().createNamedQuery("getServiceStateOlder");
        query.setParameter("checkDate", checkDate);
        return query.getResultList();
    }

    public Number getCount() {
        Query query = getEntityManager().createNamedQuery("getServiceStateCount");
        return (Number) query.getSingleResult();
    }


    public List<ServiceStateEntity> getList(long serviceId, Date date) {


        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();

        from.setTime(date);
        from.set(Calendar.HOUR, 0);
        from.set(Calendar.MINUTE, 0);
        from.set(Calendar.SECOND, 0);
        from.set(Calendar.MILLISECOND, 0);

        to.setTime(date);
        to.set(Calendar.HOUR, 23);
        to.set(Calendar.MINUTE, 59);
        to.set(Calendar.SECOND, 59);
        to.set(Calendar.MILLISECOND, 999);


        Query query = getEntityManager().createNamedQuery("getServiceStateByServiceAndDate");
        query.setParameter("serviceId", serviceId);
        query.setParameter("fromDate", from.getTime());
        query.setParameter("toDate", to.getTime());
        return query.getResultList();
    }
}
