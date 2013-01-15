package de.paluch.status.status.entity;

import org.joda.time.DateTimeComparator;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 22.11.12 20:41
 */
@Entity
@Table(name = "Maintenance")
@NamedQueries({ @NamedQuery(name = "getMaintenanceByServiceId", query = "FROM MaintenanceEntity maintenance" +
        " WHERE maintenance.serviceId = :serviceId ORDER BY maintenance.startTime")
              })
public class MaintenanceEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "serviceId")
    private Long serviceId;

    @Column(name = "name")
    private String name;

    @Column(name = "recurring")
    private boolean recurring;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "startTime")
    private Date startTime;

    @Column(name = "durationMinutes")
    private int durationMinutes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }


    public boolean isActive() {
        return isActive(new Date());
    }

    public boolean isActive(Date now) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getStartTime());
        cal.add(Calendar.MINUTE, getDurationMinutes());

        Date end = cal.getTime();

        if (isRecurring()) {
            int lower = DateTimeComparator.getTimeOnlyInstance().compare(now, getStartTime());
            int upper = DateTimeComparator.getTimeOnlyInstance().compare(now, end);

            if (lower >= 0 && upper <= 0) {
                return true;
            }
        } else {

            if (getStartTime().before(now) && end.after(now)) {
                return true;
            }
        }

        return false;
    }
}
