package jpql;

import javax.persistence.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hi");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            //타입 정보를 받을 수 있을 때 --> TypeQuery
            TypedQuery<Member> TypeQuery = em.createQuery("select m from Member m", Member.class);
            TypedQuery<String> TypeQuery1 = em.createQuery("select m.username from Member m", String.class);
            //타입 정보를 받을 수 없을 때 --> Query
            Query query = em.createQuery("select m.username, m.age from Member m");


            //
            List<Member> resultList = TypeQuery.getResultList();
            for (Member member1 : resultList) {
                System.out.println("member1 = " + member1);
            }

            //주의점
            //query.getResultList()는 결과가 하나 이상이면 리스트를 반환하고
            //결과가 없으면 빈 리스트를 반환한다.
            //query.getSingleResult() 는 결과가 정확히 하나여야 하며 단일 객체를 반환해야 한다.
            //결과가 없으면 --> javax.persistence.NoResultException
            //결과가 둘 이상이면 --> javax.persistence.NonUniqueResultException


            //파라미터 바인딩
            //문자열도 가능하고 위치로도 가능하지만,
            //문자열 기준으로 사용하는걸 권장.
//            TypedQuery<Member> param_query = em.createQuery("select m from Member m where m.username=:username", Member.class);
//            param_query.setParameter("username", "member1");
//            Member singleResult = param_query.getSingleResult();

            //보통 체이닝해서 많이 사용한다.
            Member singleResult = em.createQuery("select m from Member m where m.username=:username", Member.class)
                    .setParameter("username", "member1")
                    .getSingleResult();
            System.out.println("singleResult = " + singleResult);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}