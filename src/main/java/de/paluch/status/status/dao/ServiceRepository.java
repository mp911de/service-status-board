package de.paluch.status.status.dao;

import de.paluch.status.status.entity.ServiceEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.repository.annotation.RestResource;

import java.util.List;

@RestResource(path = "services")
public interface ServiceRepository extends PagingAndSortingRepository<ServiceEntity, Long> {

  @RestResource(path = "serviceKey", rel = "serviceKey")
  public List<ServiceEntity> findByName(@Param("serviceKey") String serviceKey);

}
