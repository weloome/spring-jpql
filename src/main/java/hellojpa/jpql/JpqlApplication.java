package hellojpa.jpql;

import jakarta.persistence.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class JpqlApplication {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            member.setTeam(team);
            em.persist(member);

            em.flush();
            em.clear();

            String query = "select " +
                                "case t.name " +
                                    "when 'teamA' then '인센티브110%'" +
                                    "when 'teamB' then '인센티브120%'" +
                                    "     else '인센티브105%' " +
                                "end " + 
                            " from Team t";
            List<String> result = em.createQuery(query, String.class).getResultList();
            
            for (String s : result) {
                System.out.println("s = " + s);
            }
            

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

}
