package com.dc.rest.imdbservice.repository;

import com.dc.rest.imdbservice.entity.PrimaryCast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/***
 ** Author: Dominic Coutinho
 ** Description: This class loads primary cast details in bulk based on the batch size
 */
@Repository
public class PrimaryCastBatchRepository {

    @Autowired
    private EntityManager entityManager;
    
    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize;

    @Transactional(timeout=900)
    public <T extends PrimaryCast> Collection<T> bulkSave(Collection<T> entities) {
	final List<T> savedEntities = new ArrayList<T>(entities.size());
	int i = 0;
	int count = 0;

	    EntityTransaction entityTransaction = entityManager.getTransaction();
	try {
	    entityTransaction.begin();
	    System.out.println("isaCtIve"+entityTransaction.isActive());

	    for (T t : entities) {
		savedEntities.add(persistOrMerge(t));
		i++;
		if (i % batchSize == 0) {
		    count++;
		    System.out.println("Flush a batch of inserts and release memory.");
		    // Flush a batch of inserts and release memory.
		    entityManager.flush();
		    entityManager.clear();
		}

	    }
	    entityTransaction.commit();
	    System.out.println("data inserted in " + count + "iterations");
	    return savedEntities;
	} catch (RuntimeException e) {
	    if (entityTransaction.isActive()) {
		entityTransaction.rollback();
	    }
	    throw e;
	} finally {
	    entityManager.close();
	}

    }

    private <T extends PrimaryCast> T persistOrMerge(T t) {
	if (t.getCastId() == null) {
	    entityManager.persist(t);
	    return t;
	} else {
	    return entityManager.merge(t);
	}
    }
}
