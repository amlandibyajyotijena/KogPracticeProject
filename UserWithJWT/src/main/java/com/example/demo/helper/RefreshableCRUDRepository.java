package com.example.demo.helper;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface RefreshableCRUDRepository<T, ID> extends CrudRepository<T, ID> {

    void refresh(T t);

    void refresh(Collection<T> s);

    void flush();
}