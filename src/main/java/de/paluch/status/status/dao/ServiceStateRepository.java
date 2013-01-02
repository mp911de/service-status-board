package de.paluch.status.status.dao;

import de.paluch.status.status.entity.ServiceStateEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.repository.annotation.RestResource;

@RestResource(path = "states")
public interface ServiceStateRepository extends PagingAndSortingRepository<ServiceStateEntity, Long> {


}