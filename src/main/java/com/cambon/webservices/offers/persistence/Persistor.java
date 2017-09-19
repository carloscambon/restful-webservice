package com.cambon.webservices.offers.persistence;

import com.cambon.webservices.offers.model.Entity;

import java.util.Collection;

public interface Persistor<T extends Entity> {

	void create(T entity);

	T update(T entity);

	T get(String id);

	Collection<T> list();

 	T delete(String id);
}
