package pl.com.tt.intern.soccer.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.com.tt.intern.soccer.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    List<User> findByIdIn(List<Long> userIds);

    Optional<User> findByUsername(String username);


    List<User> findByUsernameIn(Set<String> list);

    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);

    @Query(value = "SELECT COUNT(u)  FROM User u WHERE u.id <=:id")
    int findAllAttachedToMeByUserId(@Param("id") Long id);


    @Query(nativeQuery = true, value = "SELECT  u.username, u.email, ui.lost, ui.won, DENSE_RANK() OVER (\n" +
            "    ORDER BY  ui.won DESC, ui.lost ASC , u.username ASC\n" +
            "    ) ranking\n" +
            "FROM soccer.user u\n" +
            "         INNER JOIN soccer.user_info ui ON u.id = ui.user_id\n" +
            "ORDER BY :firstField :order1, :SecondField :order2 , u.username ASC")
    List<String[]> mySelect(Pageable pageable, String firstField, String order1, String SecondField, String order2);

    @Query(value = "SELECT COUNT(u)  FROM User u ")
    int getTotalNumber();

    @Query(nativeQuery = true, value = "SElECT a.ranking FROM\n" +
            "    (SELECT   DENSE_RANK() OVER (\n" +
            "       ORDER BY  ui.won DESC, ui.lost ASC , u.username ASC) ranking, u.username\n" +
            "       FROM soccer.user u\n" +
            "         INNER JOIN soccer.user_info ui ON u.id = ui.user_id) a\n" +
            "WHERE a.username = :username")
    Long getRankByUsername(String username);


}
