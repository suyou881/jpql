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
            /*
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
*/

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            em.flush();
            em.clear();

            //List로 반환된 result는 영속성 컨텍스트에서 관리가 될까 안될까? 된다!
            //list로 반환된 모든 결과값이 다 관리된다.
            List<Member> resultList = em.createQuery("select m from Member m", Member.class).getResultList();
            Member findMember = resultList.get(0);
            findMember.setAge(20);

            //쿼리는 조인쿼리가 나간다. 그런데 좋은게 아님.
            //join 같은 경우에는 성능에 많은 영향을 줄 수 있고, 튜닝할 수 있는 기회가 많기 때문에
            //한눈에 보이게 쓰는게 좋다. 예측할 수 있다는 장점도 있다.
//            em.createQuery("select m.team from Member m ", Team.class).getResultList();
            em.createQuery("select m.team from Member m join m.team t", Team.class).getResultList();

            //여러가지 값들을 가져오는 방법
            //1. query  오브젝트로 반환한다. 반환 타입을 모르기 때문에
            //오브젝트로 받으면 그 안에 오브젝트 배열로 되어있따. 따러서 오브젝트 배열로 캐스팅 시켜준다.
            //2.아래 캐스팅 시켜주는 과정이 귀찮으면 List 제너릭 타입에 Object[]를 넣어주면 된다.
            List<Object[]> scala_result = em.createQuery("select m.username, m.age from Member m").getResultList();
//            Object o = scala_result.get(0);
//            Object[] result = (Object[]) o;
            Object[] result = scala_result.get(0);
            System.out.println("username = " + result[0]);
            System.out.println("age = " + result[1]);

            //3. new 명령어로 조회하는
            //패키지 명을 포함한 전체 클래스 명을 입력해줘야 하고 순서와 타입이 일치하는 생성자 필요
            List<MemberDTO> dto_result = em.createQuery("select new jpql.MemberDTO( m.username, m.age) from Member m", MemberDTO.class)
                    .getResultList();
            MemberDTO memberDTO = dto_result.get(0);
            System.out.println("username = " + memberDTO.getUsername());
            System.out.println("age = " + memberDTO.getAge());

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}