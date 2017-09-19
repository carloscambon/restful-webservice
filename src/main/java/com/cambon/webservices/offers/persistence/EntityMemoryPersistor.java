package com.cambon.webservices.offers.persistence;

import com.cambon.webservices.offers.model.Entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EntityMemoryPersistor<T extends Entity> implements Persistor<T> {

	Map<String, T> objectsMap = new HashMap<>();

	@Override
	public void create(T entity) {

		objectsMap.put(entity.getId(), entity);
	}

	@Override
	public T update(T entity) {

		T existingOffer = objectsMap.get(entity.getId());
		if (existingOffer == null) {
			return null;
		} else {
			objectsMap.put(entity.getId(), entity);
			return entity;
		}
	}

	@Override
	public T delete(String id) {

		return objectsMap.remove(id);
	}

	@Override
	public Collection list() {

		return objectsMap.values();
	}

	@Override
	public T get(String id) {

		return objectsMap.get(id);
	}
}
