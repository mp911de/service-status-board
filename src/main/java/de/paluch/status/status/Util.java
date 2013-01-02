package de.paluch.status.status;

import de.paluch.status.status.entity.MaintenanceEntity;
import de.paluch.status.status.entity.ServiceCheckResultEnum;
import de.paluch.status.status.entity.ServiceStateEntity;
import de.paluch.status.status.model.Service;
import de.paluch.status.status.model.ServiceState;
import de.paluch.status.status.model.Services;

import java.util.List;

/**
 * @author <a href="mailto:mark.paluch@1und1.de">Mark Paluch</a>
 * @since 26.11.12 05:55
 */
public class Util {

    public static MaintenanceEntity getMaintenance(List<MaintenanceEntity> maintenances, long serviceId) {
        for (MaintenanceEntity maintenance : maintenances) {
            if (maintenance.getServiceId() != null && maintenance.getServiceId().longValue() == serviceId) {
                if (maintenance.isActive()) {
                    break;
                }
            }
        }
        return null;
    }

    public static String getImage(ServiceCheckResultEnum result) {
        String imageName = "tick-circle";
        if (result == ServiceCheckResultEnum.WARN) {
            imageName = "exclamation";
        }

        if (result == ServiceCheckResultEnum.FAIL) {
            imageName = "cross-circle";
        }

        if (result == ServiceCheckResultEnum.MAINTENANCE) {
            imageName = "clock";
        }

        return imageName;
    }

    public static double getServiceLevel(List<ServiceStateEntity> serviceStateEntities) {
        double maintenance = 0;
        double all = serviceStateEntities.size();
        double warnOrFail = 0;


        for (ServiceStateEntity state : serviceStateEntities) {


            all++;

            if (state.getResult() == ServiceCheckResultEnum.MAINTENANCE) {
                maintenance++;
            }

            if (state.getResult() == ServiceCheckResultEnum.WARN || state.getResult() ==
                    ServiceCheckResultEnum.FAIL) {
                warnOrFail++;
            }

        }


        double serviceLevel = 0;

        if (all - maintenance != 0) {
            serviceLevel = 100 - ((warnOrFail / (all - maintenance)) * 100);
        }

        return serviceLevel;
    }

    public static void setServiceLevelResult(Service service, double serviceLevel, ServiceState serviceState) {

        if (serviceLevel > 0) {
            if (service.getWarnService() > 0 || service.getFailServiceLevel() > 0) {
                if (service.getWarnService() > 0 && serviceLevel < service.getWarnService()) {
                    serviceState.setResult(ServiceCheckResultEnum.WARN);
                }

                if (service.getFailServiceLevel() > 0 && serviceLevel < service.getFailServiceLevel()) {
                    serviceState.setResult(ServiceCheckResultEnum.FAIL);
                }
            }
            serviceState.setServiceLevel(serviceLevel);
        }
    }

    public static Service getService(Services services, String serviceKey) {

        for (Service service : services.getService()) {

            if (service.getId().equals(serviceKey)) {
                return service;
            }
        }
        return null;
    }


}
