package microon.services.repository.api.java;

import microon.spi.scala.activeobject.Void;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.concurrent.Future;

public interface RepositoryService<T, ID extends Serializable> {

    <S extends T> Future<S> save(S entity);

    <S extends T> Future<Iterable<S>> saveMany(Iterable<S> entities);

    Future<T> findOne(ID id);

    Future<Boolean> exists(ID id);

    Future<Iterable<T>> findAll();

    Future<Iterable<T>> findAll(Iterable<ID> ids);

    Future<Long> count();

    Future<Void> delete(ID id);

    Future<Void> delete(T entity);

    Future<Void> delete(Iterable<? extends T> entities);

    Future<Void> deleteAll();

    Future<Iterable<T>> findAll(Sort sort);

    Future<Page<T>> findAll(Pageable pageable);

    Future<Long> countByQuery(Object query);

    Future<Iterable<T>> findAllByQuery(Object query);

    Future<Page<T>> findAllByQuery(Object query, Pageable pageable);

    Future<Iterable<T>> findAllByQuery(Object query, Sort sort);

    Future<T> findOneByQuery(Object query);

}