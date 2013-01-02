package de.paluch.status.status.model;

import javax.xml.bind.annotation.*;

/**
 * @author <a href="mailto:mark.paluch@1und1.de">Mark Paluch</a>
 * @since 22.11.12 21:05
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class JenkinsMaintenanceCheck {

    @XmlValue
    private String projectBaseUrl;

    public String getProjectBaseUrl() {
        return projectBaseUrl;
    }

    public void setProjectBaseUrl(String projectBaseUrl) {
        this.projectBaseUrl = projectBaseUrl;
    }
}
