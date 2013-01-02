package de.paluch.status.status;

import de.paluch.status.status.control.CheckerJobController;
import de.paluch.status.status.dao.ServiceStateDao;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

/**
 * @author <a href="mailto:mark.paluch@1und1.de">Mark Paluch</a>
 * @since 22.11.12 21:32
 */

public class CleanupJob implements StatefulJob {

    private Logger log = Logger.getLogger(getClass());
    private ServiceStateDao serviceStateDao;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {


        log.info("Running cleanup");
        CheckerJobController checkerJobController = (CheckerJobController) jobExecutionContext
                .getJobDetail().getJobDataMap().get("checkerJobController");
        checkerJobController.cleanup();

    }

    public ServiceStateDao getServiceStateDao() {
        return serviceStateDao;
    }

    public void setServiceStateDao(ServiceStateDao serviceStateDao) {
        this.serviceStateDao = serviceStateDao;
    }
}
