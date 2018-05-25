package com.hotels.categoryUI;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class CategoryService {
    private static CategoryService instance;
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("demo_hotels");
    private EntityManager em = emf.createEntityManager();

    private CategoryService () {
    }

    public static CategoryService getInstance () {
        if (instance == null) {
            instance = new CategoryService();
            instance.ensureTestData();
        }
        return instance;
    }

    public synchronized List<Category> findAll () {
        return em.createQuery("SELECT e from Category e", Category.class).getResultList();
    }

    public synchronized List<Category> findAll (String filter) {
        TypedQuery<Category> namedQuery = em.createNamedQuery("Category.byName", Category.class);
        namedQuery.setParameter("filter", "%" + filter + "%");
        return namedQuery.getResultList();
    }

    public synchronized long count () {
        return findAll().size();
    }

    public synchronized void delete (Category value) {
        EntityTransaction tx = em.getTransaction();
        value = em.find(Category.class, value.getId());
        
        tx.begin();
        em.remove(value);
        tx.commit();
    }

    public synchronized void save (Category entry) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        
        if (entry.getId() == null) {
            em.persist(entry);
        } else {
            em.merge(entry); 
        }
 
        tx.commit();
    }

    private void ensureTestData () {
        List<Category> findAll = findAll();
        if (findAll.isEmpty()) {
            final String[] categoryData = new String[] { "Hotel", "Hostel", "GuestHouse", "Appartments" };

            for (String category : categoryData) {
                save (new Category(category));
            }
        }
    }
}
