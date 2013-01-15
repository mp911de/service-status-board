package de.paluch.status.status.entity;

import javax.persistence.*;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 22.11.12 20:41
 */
@Entity
@Table(name = "Service")
@NamedQueries({ @NamedQuery(name = "getServices", query = "FROM ServiceEntity service ORDER BY " +
        " service.name"), @NamedQuery(name = "getServiceByKey", query = " from ServiceEntity service " +
        "where service.serviceKey = :serviceKey")
              })
public class ServiceEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "serviceKey")
    private String serviceKey;

    @Column(name = "name")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceKey() {
        return serviceKey;
    }

    public void setServiceKey(String serviceKey) {
        this.serviceKey = serviceKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
