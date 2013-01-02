package de.paluch.status.status.entity;

import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author <a href="mailto:mark.paluch@1und1.de">Mark Paluch</a>
 * @since 22.11.12 20:41
 */
@Entity
@Table(name = "ServiceState")
@NamedQueries({ @NamedQuery(name = "getServiceStateByService", query = "from ServiceStateEntity state where " +
        "state.serviceId = :serviceId"),
                      @NamedQuery(name = "getServiceStateOlder", query = "from ServiceStateEntity state where" +
                              " state.checkDate < :checkDate"),
                      @NamedQuery(name = "getServiceStateByServiceAndDate", query = "from ServiceStateEntity state where" +
                              " state.serviceId = :serviceId and state.checkDate >= :fromDate and state.checkDate <= " +
                              ":toDate"),
                      @NamedQuery(name = "getServiceStateCount", query = "select count(*) from ServiceStateEntity state") })
public class ServiceStateEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "serviceId")
    @Index(name = "IDX_serviceId", columnNames = { "serviceId", "checkDate" })
    private long serviceId;

    @Column(name = "checkKey")
    private String checkKey;

    @Column(name = "result")
    private ServiceCheckResultEnum result;

    @Column(name = "message")
    private String message;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "checkDate")
    @Index(name = "IDX_checkDate", columnNames = "checkDate")
    private Date checkDate = new Timestamp(System.currentTimeMillis());

    @Column(name = "checkUrl")
    private String checkUrl;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getServiceId() {
        return serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }

    public String getCheckKey() {
        return checkKey;
    }

    public void setCheckKey(String checkKey) {
        this.checkKey = checkKey;
    }

    public ServiceCheckResultEnum getResult() {
        return result;
    }

    public void setResult(ServiceCheckResultEnum result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    public String getCheckUrl() {
        return checkUrl;
    }

    public void setCheckUrl(String checkUrl) {
        this.checkUrl = checkUrl;
    }
}
