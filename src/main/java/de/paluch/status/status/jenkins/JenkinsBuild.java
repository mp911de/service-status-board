package de.paluch.status.status.jenkins;

import javax.xml.bind.annotation.*;

/**
 * @author <a href="mailto:mark.paluch@1und1.de">Mark Paluch</a>
 * @since 26.11.12 08:30
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({ JenkinsFreestyleBuild.class, JenkinsMavenBuild.class })
public abstract class JenkinsBuild {

    @XmlElement(name = "building")
    private boolean building;

    public boolean isBuilding() {
        return building;
    }

    public void setBuilding(boolean building) {
        this.building = building;
    }
}
