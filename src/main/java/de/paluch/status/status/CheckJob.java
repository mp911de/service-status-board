package de.paluch.status.status;

import de.paluch.status.status.control.CheckerJobController;
import de.paluch.status.status.control.InitializingController;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author <a href="mailto:mark.paluch@1und1.de">Mark Paluch</a>
 * @since 22.11.12 21:32
 */
@Transactional
public class CheckJob implements StatefulJob {


    private InitializingController initializingController;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {


        CheckerJobController checkerJobController = (CheckerJobController) jobExecutionContext.getJobDetail().getJobDataMap().get
                ("checkerJobController");
        InitializingController initializingController = (InitializingController) jobExecutionContext.getJobDetail()
                                                                                                    .getJobDataMap().get
                        ("initializingController");

        initializingController.init();
        checkerJobController.run();


    }


}
