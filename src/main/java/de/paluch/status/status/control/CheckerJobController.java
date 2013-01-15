package de.paluch.status.status.control;

import de.paluch.status.status.Util;
import de.paluch.status.status.dao.MaintenanceDao;
import de.paluch.status.status.dao.ServiceDao;
import de.paluch.status.status.dao.ServiceStateDao;
import de.paluch.status.status.entity.MaintenanceEntity;
import de.paluch.status.status.entity.ServiceCheckResultEnum;
import de.paluch.status.status.entity.ServiceEntity;
import de.paluch.status.status.entity.ServiceStateEntity;
import de.paluch.status.status.model.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 26.11.12 08:20
 */
@Transactional
public class CheckerJobController {

    @Autowired
    private ServicesFactory servicesFactory;

    @Autowired
    private ServiceDao serviceDao;

    @Autowired
    private ServiceStateDao serviceStateDao;

    @Autowired
    private MaintenanceDao maintenanceDao;

    private Logger log = Logger.getLogger(getClass());


    public void run() {


        log.debug("Starting checks");
        Services services = servicesFactory.createInstance();

        List<MaintenanceEntity> maintenances = maintenanceDao.getList();
        for (MaintenanceEntity maintenance : maintenances) {
            if (maintenance.getServiceId() == null) {
                if (maintenance.isActive()) {
                    break;
                }
            }
        }

        List<ServiceStateEntity> toCreate = new ArrayList<ServiceStateEntity>();

        for (Service service : services.getService()) {
            ServiceEntity serviceEntity = serviceDao.getByKey(service.getId());
            if (serviceEntity == null) {
                log.warn("Cannot find ServiceEntity " + service.getId());
                continue;
            }
            checkService(maintenances, toCreate, service, serviceEntity);
        }

        for (ServiceStateEntity serviceStateEntity : toCreate) {
            serviceStateDao.create(serviceStateEntity);
        }

        log.debug("Checks done");
    }

    private void checkService(List<MaintenanceEntity> maintenances, List<ServiceStateEntity> toCreate, Service service,
                              ServiceEntity serviceEntity) {

        List<ServiceStateEntity> states = new ArrayList<ServiceStateEntity>();
        Map<ServiceStateEntity, String> detailMessages = new HashMap<ServiceStateEntity, String>();
        MaintenanceEntity maintenance = Util.getMaintenance(maintenances, serviceEntity.getId());
        if (maintenance != null) {
            ServiceStateEntity serviceStateEntity = createStateEntity(ServiceCheckResultEnum.MAINTENANCE, serviceEntity.getId(), "maintenance",
                                                                      maintenance.getName(), null);
            states.add(serviceStateEntity);
        }


        boolean checkedMaintenance = isMaintenanceOnlineChecked(service, serviceEntity, states, detailMessages);

        if (!checkedMaintenance) {
            for (ServiceCheck check : service.getCheck()) {

                Checker checker = new Checker();
                checker.setService(service);
                checker.setServiceCheck(check);
                checker.checkService();

                ServiceStateEntity serviceStateEntity = createStateEntity(checker.getResult(), serviceEntity.getId(), check.getName(),
                                                                          checker.getMessage(), checker.getCheckUrl());
                states.add(serviceStateEntity);
                detailMessages.put(serviceStateEntity, checker.getDetailMessage());

            }
        }

        List<ServiceStateEntity> toRemove = new ArrayList<ServiceStateEntity>();
        List<ServiceStateEntity> hasToStay = new ArrayList<ServiceStateEntity>();
        boolean removeOKStates = false;
        for (ServiceStateEntity state : states) {

            if (toRemove.contains(state)) {
                continue;
            }
            if (state.getResult() == ServiceCheckResultEnum.WARN || state.getResult() ==
                    ServiceCheckResultEnum.FAIL) {
                removeOKStates = true;
            }

            for (ServiceStateEntity otherState : states) {
                if (state == otherState || toRemove.contains(otherState)) {
                    continue;
                }
                if (new ServiceState(state).isSimilar(new ServiceState(otherState))) {
                    toRemove.add(otherState);
                }

            }
        }

        if (removeOKStates) {
            for (ServiceStateEntity state : states) {
                if (state.getResult() == ServiceCheckResultEnum.OK) {
                    toRemove.add(state);
                }
            }
        }

        states.removeAll(toRemove);

        for (ServiceStateEntity state : states) {
            if (detailMessages.containsKey(state)) {
                state.setMessage(detailMessages.get(state));
            }
        }

        toCreate.addAll(states);

    }

    private boolean isMaintenanceOnlineChecked(Service service, ServiceEntity serviceEntity,
                                               List<ServiceStateEntity> states,
                                               Map<ServiceStateEntity, String> detailMessages) {
        boolean checkedMaintenance = false;

        if(service.getJenkinsMaintenanceCheck() == null)
        {
            return checkedMaintenance;
        }

        for (JenkinsMaintenanceCheck jenkinsCheck : service.getJenkinsMaintenanceCheck()) {
            Checker checker = new Checker();
            try {

                checker.setService(service);
                checker.setJenkinsMaintenanceCheck(jenkinsCheck);
                if (performJenkinsCheck(serviceEntity, states, detailMessages, checker)) {
                    checkedMaintenance = true;
                }
            } catch (Exception e) {
                log.warn(e.getMessage(), e);
                ServiceStateEntity serviceStateEntity = createStateEntity(ServiceCheckResultEnum.WARN,
                                                                          serviceEntity.getId(),
                                                                          "Jenkins Check",
                                                                          e.getMessage(), checker.getCheckUrl());
                states.add(serviceStateEntity);
                detailMessages.put(serviceStateEntity, e.getMessage() + ": " + jenkinsCheck.getProjectBaseUrl());
            }
        }

        return checkedMaintenance;
    }

    private boolean performJenkinsCheck(ServiceEntity serviceEntity, List<ServiceStateEntity> states,
                                        Map<ServiceStateEntity, String> detailMessages,
                                        Checker checker) {
        boolean checkedMaintenance = false;
        checker.checkJenkins();

        if (checker.getResult() == ServiceCheckResultEnum.MAINTENANCE) {
            checkedMaintenance = true;


            ServiceStateEntity serviceStateEntity = createStateEntity(checker.getResult(),
                                                                      serviceEntity.getId(),
                                                                      "Jenkins Check",
                                                                      checker.getMessage(), checker.getCheckUrl());
            states.add(serviceStateEntity);
            detailMessages.put(serviceStateEntity, checker.getDetailMessage());

        }
        return checkedMaintenance;
    }

    protected ServiceStateEntity createStateEntity(ServiceCheckResultEnum result, long serviceId, String checkKey,
                                                   String message, String checkUrl) {
        ServiceStateEntity stateEntity = new ServiceStateEntity();
        stateEntity.setCheckDate(new Date());
        stateEntity.setCheckKey(checkKey);
        stateEntity.setMessage(message);
        stateEntity.setResult(result);
        stateEntity.setServiceId(serviceId);
        stateEntity.setCheckUrl(checkUrl);
        return stateEntity;
    }


    public void cleanup() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -6);

        log.info("Service States before cleanup: " + serviceStateDao.getCount());

        List<ServiceStateEntity> list = serviceStateDao.getList(cal.getTime());
        for (ServiceStateEntity stateEntity : list) {
            serviceStateDao.delete(stateEntity);
        }

        log.info("Service States after cleanup: " + serviceStateDao.getCount());

    }
}
