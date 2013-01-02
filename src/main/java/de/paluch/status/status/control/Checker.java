package de.paluch.status.status.control;

import de.paluch.status.status.entity.ServiceCheckResultEnum;
import de.paluch.status.status.jenkins.JenkinsAPI;
import de.paluch.status.status.jenkins.JenkinsBuild;
import de.paluch.status.status.jenkins.JenkinsBuildReference;
import de.paluch.status.status.jenkins.JenkinsProject;
import de.paluch.status.status.model.JenkinsMaintenanceCheck;
import de.paluch.status.status.model.Service;
import de.paluch.status.status.model.ServiceCheck;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.jboss.resteasy.client.ProxyFactory;

import java.io.ByteArrayOutputStream;
import java.net.UnknownHostException;

/**
 * @author <a href="mailto:mark.paluch@1und1.de">Mark Paluch</a>
 * @since 22.11.12 21:16
 */
public class Checker {

    private Service service;
    private ServiceCheck serviceCheck;

    private JenkinsMaintenanceCheck jenkinsMaintenanceCheck;

    private String message;
    private String detailMessage;
    private ServiceCheckResultEnum result;
    private String checkUrl;


    public void checkService() {
        DefaultHttpClient client = new DefaultHttpClient();
        client.getParams();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 1000);
        HttpConnectionParams.setSoTimeout(client.getParams(), 10000);


        HttpGet get = new HttpGet(serviceCheck.getLocation().trim());
        checkUrl = get.getURI().toString();

        try {
            long start = System.currentTimeMillis();
            HttpResponse response = client.execute(get);

            if (response.getStatusLine().getStatusCode() >= 200 && response.getStatusLine().getStatusCode() < 400) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                response.getEntity().writeTo(baos);

                long end = System.currentTimeMillis();

                deepStatusCheck(end - start, baos.toString());

            } else {
                result = ServiceCheckResultEnum.FAIL;
                message = response.getStatusLine().getReasonPhrase();

                String uri = get.getURI().getPath();
                if (uri.equals("") || uri.equals("/")) {
                    uri = get.getURI().toString();
                }

                detailMessage = message + ": " + uri;
            }

        } catch (UnknownHostException e) {
            result = ServiceCheckResultEnum.FAIL;
            message = "Unknown host: " + e.getMessage();
        } catch (Exception e) {
            result = ServiceCheckResultEnum.FAIL;
            message = e.getMessage();
        }


    }


    public void checkJenkins() {

        JenkinsAPI api = ProxyFactory.create(JenkinsAPI.class, jenkinsMaintenanceCheck.getProjectBaseUrl().trim());

        checkUrl = jenkinsMaintenanceCheck.getProjectBaseUrl().trim();
        JenkinsProject project = api.getProject();

        if (project.getLastBuild() != null) {
            JenkinsBuildReference ref = project.getLastBuild();
            JenkinsBuild build = api.getBuild(ref.getNumber());

            if (build.isBuilding()) {
                message = "Jenkins Job is running";
                detailMessage = "Jenkins Job " + project.getDisplayName() + "#" + ref.getNumber() + " is running";
                result = ServiceCheckResultEnum.MAINTENANCE;
            }
        }
    }

    private void deepStatusCheck(long duration, String content) {
        result = ServiceCheckResultEnum.OK;
        message = "Service up and running";
        if (serviceCheck.getPerformanceWarnMs() != null && duration > serviceCheck.getPerformanceWarnMs()) {
            result = ServiceCheckResultEnum.WARN;
            message = "Performance warning";
            detailMessage = "Performance Issues: Response Time " + duration + "ms, " +
                    "Warning at: " + serviceCheck.getPerformanceWarnMs() + "ms";
        }

        if (serviceCheck.getPerformanceFailMs() != null && duration > serviceCheck.getPerformanceFailMs()) {
            result = ServiceCheckResultEnum.FAIL;
            message = "Performance failure";
            detailMessage = "Performance Issues: Response Time " + duration + "ms, " +
                    "Failure at: " + serviceCheck.getPerformanceFailMs() + "ms";
        }

        if (serviceCheck.getWarnResult() != null && content.matches(serviceCheck.getWarnResult()) && result == ServiceCheckResultEnum.OK) {
            result = ServiceCheckResultEnum.WARN;
            message = "Service-Check failed";
            detailMessage = message + ", expected: " + serviceCheck.getWarnResult();
        }

        if (serviceCheck.getExpectedResult() != null && !content.matches(serviceCheck.getExpectedResult()) && result
                == ServiceCheckResultEnum.OK) {
            result = ServiceCheckResultEnum.FAIL;
            message = "Service-Check failed";
            detailMessage = message + ", expected: " + serviceCheck.getExpectedResult();
        }
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public ServiceCheck getServiceCheck() {
        return serviceCheck;
    }

    public void setServiceCheck(ServiceCheck serviceCheck) {
        this.serviceCheck = serviceCheck;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ServiceCheckResultEnum getResult() {
        return result;
    }

    public void setResult(ServiceCheckResultEnum result) {
        this.result = result;
    }

    public JenkinsMaintenanceCheck getJenkinsMaintenanceCheck() {
        return jenkinsMaintenanceCheck;
    }

    public void setJenkinsMaintenanceCheck(JenkinsMaintenanceCheck jenkinsMaintenanceCheck) {
        this.jenkinsMaintenanceCheck = jenkinsMaintenanceCheck;
    }

    public String getDetailMessage() {
        if (detailMessage == null) {
            return getMessage();
        }
        return detailMessage;
    }

    public void setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
    }

    public String getCheckUrl() {
        return checkUrl;
    }
}
