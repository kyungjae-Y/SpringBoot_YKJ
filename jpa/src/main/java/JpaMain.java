import entity.Customer;

import javax.persistence.*;

public class JpaMain {

    public static void main(String[] args) {
        // 객체 sessionFactory
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("customer-ex");
        EntityManager em = emf.createEntityManager(); // session 객체
        EntityTransaction tx = em.getTransaction();
        tx.begin(); // start transaction;
        try {
            Customer c = new Customer("test");
            em.persist(c);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
            emf.close();
        }
    }
}
