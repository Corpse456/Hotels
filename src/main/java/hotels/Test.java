package hotels;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import hotels.categoryUI.Category;
import hotels.holelsUI.Hotel;

public class Test {
    public static void main (String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("demo_hotels");
        
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        
        Category categ = new Category("Binded14");
        Hotel hotel = new Hotel("NewHib", "InMysql", 5, 4354L, categ, "dsd", "Empty as usual");
        
        em.persist(categ);
        
        em.persist(hotel);
        
        tx.commit();
        em.close();
        emf.close();
    }
}
