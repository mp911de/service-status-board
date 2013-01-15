package de.paluch.status.status.control;

import de.paluch.status.status.Util;
import de.paluch.status.status.model.*;
import de.paluch.status.status.dao.MaintenanceDao;
import de.paluch.status.status.dao.ServiceDao;
import de.paluch.status.status.dao.ServiceStateDao;
import de.paluch.status.status.entity.MaintenanceEntity;
import de.paluch.status.status.entity.ServiceCheckResultEnum;
import de.paluch.status.status.entity.ServiceEntity;
import de.paluch.status.status.entity.ServiceStateEntity;
import org.joda.time.DateTimeComparator;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 23.11.12 09:09
 */
public class UIOverviewModelBuilder {
    @Autowired
    private ServiceDao serviceDao;

    @Autowired
    private ServiceStateDao serviceStateDao;

    @Autowired
    private MaintenanceDao maintenanceDao;

    @Autowired
    private ServicesFactory servicesFactory;

    private int historyDays = 4;


    public UIOverviewModel createModel() {


        Services services = servicesFactory.createInstance();
        List<ServiceEntity> serviceEntities = serviceDao.getList();
        UIOverviewModel result = new UIOverviewModel();
        result.getServices().addAll(serviceEntities);

        List<ServiceStateEntity> states = serviceStateDao.getList();
        List<MaintenanceEntity> maintenances = maintenanceDao.getList();

        for (int i = 0; i < historyDays; i++) {

            Calendar cal = Calendar.getInstance();
            cal.setTime(new LocalDateTime().toDate());

            cal.add(Calendar.DATE, -i);


            for (ServiceEntity serviceEntity : serviceEntities) {


                if (i == 0) {
                    ServiceStateEntity serviceStateEntity = getCurrentState(maintenances, states, serviceEntity);
                    result.getCurrentState().put(serviceEntity.getServiceKey(), new ServiceState(serviceStateEntity));
                }

                ServiceState state = getState(services, states, serviceEntity, cal.getTime());

                Map<String, ServiceState> map = result.getHistory().get(cal.getTime());
                if (map == null) {
                    map = new HashMap<String, ServiceState>();
                    result.getHistory().put(cal.getTime(), map);
                }

                map.put(serviceEntity.getServiceKey(), state);
            }
        }
        result.setEnvironment(services.getEnvironment());
        return result;
    }

    private ServiceStateEntity getCurrentState(List<MaintenanceEntity> maintenances, List<ServiceStateEntity> states,
                                               ServiceEntity service) {


        for (MaintenanceEntity maintenance : maintenances) {
            if (maintenance.getServiceId() == null || maintenance.getServiceId().longValue() == service.getId()) {
                if (maintenance.isActive()) {
                    ServiceStateEntity result = new ServiceStateEntity();
                    result.setResult(ServiceCheckResultEnum.MAINTENANCE);
                    result.setMessage(maintenance.getName());
                }
            }
        }

        List<ServiceStateEntity> myStates = new ArrayList<ServiceStateEntity>();

        for (ServiceStateEntity state : states) {
            if (state.getServiceId() == service.getId()) {
                myStates.add(state);
            }
        }

        Collections.sort(myStates, new Comparator<ServiceStateEntity>() {
            @Override
            public int compare(ServiceStateEntity serviceStateEntity, ServiceStateEntity serviceStateEntity2) {
                return serviceStateEntity2.getCheckDate().compareTo(serviceStateEntity.getCheckDate());
            }
        });


        if (!myStates.isEmpty()) {
            return myStates.get(0);
        }

        ServiceStateEntity result = new ServiceStateEntity();
        result.setResult(ServiceCheckResultEnum.OK);
        return result;

    }

    private ServiceState getState(Services services, List<ServiceStateEntity> states,
                                  ServiceEntity serviceEntity, Date date) {
        DateTimeComparator comparator = DateTimeComparator.getDateOnlyInstance();

        List<ServiceStateEntity> myStates = new ArrayList<ServiceStateEntity>();

        Service service = Util.getService(services, serviceEntity.getServiceKey());

        for (ServiceStateEntity state : states) {
            if (state.getServiceId() == serviceEntity.getId().longValue()) {

                int dateResult = comparator.compare(state.getCheckDate(), date);
                if (dateResult == 0) {
                    myStates.add(state);
                }
            }
        }

        double serviceLevel = Util.getServiceLevel(myStates);


        ServiceStateEntity result = new ServiceStateEntity();
        result.setResult(ServiceCheckResultEnum.OK);


        ServiceState serviceState = new ServiceState(result);
        Util.setServiceLevelResult(service, serviceLevel, serviceState);

        return serviceState;
    }




}
