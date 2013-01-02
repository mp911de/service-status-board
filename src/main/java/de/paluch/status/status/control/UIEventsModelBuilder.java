package de.paluch.status.status.control;

import de.paluch.status.status.Util;
import de.paluch.status.status.dao.MaintenanceDao;
import de.paluch.status.status.dao.ServiceDao;
import de.paluch.status.status.dao.ServiceStateDao;
import de.paluch.status.status.entity.ServiceCheckResultEnum;
import de.paluch.status.status.entity.ServiceEntity;
import de.paluch.status.status.entity.ServiceStateEntity;
import de.paluch.status.status.model.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.*;

/**
 * @author <a href="mailto:mark.paluch@1und1.de">Mark Paluch</a>
 * @since 23.11.12 09:09
 */
public class UIEventsModelBuilder {
    @Autowired
    private ServiceDao serviceDao;

    @Autowired
    private ServiceStateDao serviceStateDao;

    @Autowired
    private MaintenanceDao maintenanceDao;

    @Autowired
    private ServicesFactory servicesFactory;


    public UIEventsModel createModel(String serviceKey, Date date) {

        Services services = servicesFactory.createInstance();

        ServiceEntity serviceEntity = serviceDao.getByKey(serviceKey);

        if (serviceEntity == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        UIEventsModel result = new UIEventsModel();
        result.setService(serviceEntity);

        List<ServiceStateEntity> states = serviceStateDao.getList(serviceEntity.getId(), date);

        List<ServiceState> serviceStates = new ArrayList<ServiceState>();
        boolean allOk = true;
        for (ServiceStateEntity state : states) {
            if (state.getResult() != ServiceCheckResultEnum.OK) {
                allOk = false;
            }

            serviceStates.add(new ServiceState(state));
        }

        Collections.sort(serviceStates, new Comparator<ServiceState>() {
            @Override
            public int compare(ServiceState serviceStateEntity, ServiceState serviceStateEntity2) {
                return serviceStateEntity2.getCheckDate().compareTo(serviceStateEntity.getCheckDate());
            }
        });


        result.setServiceLevel(Util.getServiceLevel(states));
        result.setServiceStates(serviceStates);
        result.setAllOK(allOk);

        result.setEnvironment(services.getEnvironment());
        return result;
    }




}
