package org.bhavani.constructions.helpers;

import com.google.common.reflect.TypeToken;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

public class AbstractDAO<E> {
    protected final Class<E> entityClass;
    protected final TypeToken<E> typeToken = new TypeToken<E>(getClass()) {
    };
    private final SessionFactory sessionFactory;

    public AbstractDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.entityClass = (Class<E>) typeToken.getRawType();
    }

    /**
     * perists an entity. This should not be used for updating an existing entity
     *
     * @param entity
     * @return
     */
    public E persist(E entity) {
        sessionFactory.getCurrentSession().persist(requireNonNull(entity));
        return entity;
    }

    /**
     * persists multiple entities of type E
     *
     * @param entities
     */
    public void persist(Iterable<? extends E> entities) {
        entities.forEach(this::persist);
    }

    /**
     * finds a single record identified by the given id
     *
     * @param id
     * @return
     */
    public Optional<E> get(Serializable id) {
        return ofNullable(currentSession().get(entityClass, id));
    }

    /**
     * Returns the current {@link Session}.
     *
     * @return the current session
     */
    protected Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Find a single matching record by providing named query
     *
     * @param queryName
     * @param parameters
     * @return Optional
     */
    public Optional<E> findOneByNamedQuery(String queryName, Map<String, Object> parameters) {
        javax.persistence.Query query = currentSession().createNamedQuery(queryName, entityClass);
        query.setMaxResults(1);

        parameters.forEach(query::setParameter);

        try {
            return of((E) query.getSingleResult());
        } catch (NoResultException var6) {
            return Optional.empty();
        }
    }

    /**
     * Find matching records by providing a named query. Supports pagination
     * This method does not return the total count for performance reasons
     *
     * @param queryName
     * @param parameters
     * @param pageRequest
     * @return Page
     */
    public Page<E> findAllByNamedQuery(String queryName, Map<String, Object> parameters,
                                       PageRequest pageRequest) {
        Query query = currentSession().createNamedQuery(queryName);

        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        List<E> content = query
                .setFirstResult(pageRequest.getOffset())
                .setMaxResults(pageRequest.getPageSize())
                .getResultList();

        boolean hasMore = pageRequest.getPageSize() == content.size();
        return new Page<>(null, content, hasMore);
    }
}
