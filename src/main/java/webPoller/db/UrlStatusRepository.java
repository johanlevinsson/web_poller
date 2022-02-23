package webPoller.db;

import org.springframework.data.repository.CrudRepository;

public interface UrlStatusRepository extends CrudRepository<UrlStatusObject, Integer> {
}
