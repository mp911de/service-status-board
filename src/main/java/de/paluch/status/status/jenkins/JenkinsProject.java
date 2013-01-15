package de.paluch.status.status.jenkins;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.List;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 26.11.12 08:26
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({ JenkinsFreeStyleProject.class, JenkinsMavenProject.class })
public abstract class JenkinsProject {

    @XmlElement(name = "description")
    private String description;

    @XmlElement(name = "displayName")
    private String displayName;


    @XmlElement(name = "buildable")
    private boolean buildable;

    @XmlElement(name = "build")
    private List<JenkinsBuildReference> builds;

    @XmlElement(name = "lastBuild")
    private JenkinsBuildReference lastBuild;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isBuildable() {
        return buildable;
    }

    public void setBuildable(boolean buildable) {
        this.buildable = buildable;
    }

    public List<JenkinsBuildReference> getBuilds() {
        return builds;
    }

    public void setBuilds(List<JenkinsBuildReference> builds) {
        this.builds = builds;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public JenkinsBuildReference getLastBuild() {
        return lastBuild;
    }

    public void setLastBuild(JenkinsBuildReference lastBuild) {
        this.lastBuild = lastBuild;
    }
}
