package de.paluch.status.status.control;

import de.paluch.status.status.dao.ServiceDao;
import de.paluch.status.status.entity.ServiceEntity;
import de.paluch.status.status.model.Service;
import de.paluch.status.status.model.Services;
import de.paluch.status.status.model.ServicesFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author <a href="mailto:mark.paluch@1und1.de">Mark Paluch</a>
 * @since 26.11.12 07:16
 */
public class InitializingController {

    private Logger log = Logger.getLogger(getClass());

    @Autowired
    private ServiceDao serviceDao;

    @Autowired
    private ServicesFactory servicesFactory;


    @Transactional
    public void init() {

        Services services = servicesFactory.createInstance();
        List<ServiceEntity> serviceEntities = serviceDao.getList();
        for (ServiceEntity serviceEntity : serviceEntities) {
            boolean found = false;
            for (Service service : services.getService()) {
                if (serviceEntity.getServiceKey().equals(service.getId())) {
                    found = true;
                    break;

                }
            }

            if (!found) {
                log.info("Removing old service " + serviceEntity.getServiceKey());
                serviceDao.delete(serviceEntity);
            }
        }


        for (Service service : services.getService()) {
            boolean found = false;
            for (ServiceEntity serviceEntity : serviceEntities) {

                if (serviceEntity.getServiceKey().equals(service.getId())) {
                    found = true;
                    serviceEntity.setName(service.getName());
                    serviceDao.update(serviceEntity);
                    break;

                }
            }

            if (!found) {
                ServiceEntity serviceEntity = new ServiceEntity();
                serviceEntity.setName(service.getName());
                serviceEntity.setServiceKey(service.getId());

                log.info("Creating new service " + serviceEntity.getServiceKey());


                serviceDao.create(serviceEntity);
            }
        }
    }

}
