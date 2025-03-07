
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.TimePeriod;

@Repository
public interface TimePeriodRepository extends JpaRepository<TimePeriod, Integer> {

	@Query("select t from TimePeriod t where t.reservation.id=?1")
	Collection<TimePeriod> findByReservation(Integer reservationId);

	@Query("select t from TimePeriod t join t.reservation r join r.lesson l where l.teacher.userAccount.id=?1")
	Collection<TimePeriod> findTimePeriodsByTeacher(int teacherUAId);

}
