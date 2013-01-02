package de.paluch.status.status.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author <a href="mailto:mark.paluch@1und1.de">Mark Paluch</a>
 * @since 22.11.12 21:04
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Service {

    @XmlAttribute(name = "id")
    private String id;


    @XmlAttribute(name = "warnServiceLevel")
    private int warnService;

    @XmlAttribute(name = "failServiceLevel")
    private int failServiceLevel;

    @XmlAttribute(name = "name")
    private String name;

    @XmlElement(name = "check")
    private List<ServiceCheck> check;

    @XmlElement(name = "jenkinsMaintenanceCheck")
    private List<JenkinsMaintenanceCheck> jenkinsMaintenanceCheck;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ServiceCheck> getCheck() {
        return check;
    }

    public void setCheck(List<ServiceCheck> check) {
        this.check = check;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<JenkinsMaintenanceCheck> getJenkinsMaintenanceCheck() {
        return jenkinsMaintenanceCheck;
    }

    public void setJenkinsMaintenanceCheck(List<JenkinsMaintenanceCheck> jenkinsMaintenanceCheck) {
        this.jenkinsMaintenanceCheck = jenkinsMaintenanceCheck;
    }

    public int getWarnService() {
        return warnService;
    }

    public void setWarnService(int warnService) {
        this.warnService = warnService;
    }

    public int getFailServiceLevel() {
        return failServiceLevel;
    }

    public void setFailServiceLevel(int failServiceLevel) {
        this.failServiceLevel = failServiceLevel;
    }
}
