package org.bhavani.constructions.dao.api;

import org.bhavani.constructions.dao.entities.PassBookEntity;

import java.util.List;
import java.util.Optional;

public interface PassBookEntityDao {

    Optional<PassBookEntity> getLatestPassBookEntity(String accountName);

    void savePassBookEntities(List<PassBookEntity> passBookEntities);

    List<PassBookEntity> getLatestPassBookForAll();

    List<PassBookEntity> getAccountPasBook(String accountName);
}
