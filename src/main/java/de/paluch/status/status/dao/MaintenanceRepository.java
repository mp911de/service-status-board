package de.paluch.status.status.dao;

import de.paluch.status.status.entity.MaintenanceEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.repository.annotation.RestResource;

import java.util.List;

@RestResource(path = "maintenances")
public interface MaintenanceRepository extends PagingAndSortingRepository<MaintenanceEntity, Long> {

  @RestResource(path = "serviceId", rel = "serviceId")
  public List<MaintenanceEntity> findByName(@Param("serviceId") String name);

}
