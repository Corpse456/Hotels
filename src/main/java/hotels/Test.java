package hotels;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import hotels.categoryUI.Category;

public class Test {
    public static void main (String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("demo_hotels");
        
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        
        Category categ = new Category("MyExperiment2");
        
        em.persist(categ);
        tx.commit();
        em.close();
    }
}
