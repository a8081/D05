
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.TimePeriodRepository;
import domain.Reservation;
import domain.Schedule;
import domain.Teacher;
import domain.TimePeriod;

@Service
@Transactional
public class TimePeriodService {

	@Autowired
	private TimePeriodRepository	timePeriodRepository;

	@Autowired
	private TeacherService			teacherService;

	@Autowired
	private ReservationService		reservationService;

	@Autowired
	private ScheduleService			scheduleService;


	//	public TimePeriod create() {
	//		final TimePeriod tPeriod = new TimePeriod();
	//		final Reservation reservation = new Reservation();
	//		tPeriod.setReservation(reservation);
	//		return tPeriod;
	//	}

	public TimePeriod findOne(final int tPeriodId) {
		Assert.isTrue(tPeriodId != 0);
		final TimePeriod res = this.timePeriodRepository.findOne(tPeriodId);
		Assert.notNull(res);
		return res;
	}

	public Collection<TimePeriod> findAll() {
		Collection<TimePeriod> res = new ArrayList<>();
		res = this.timePeriodRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public TimePeriod save(final TimePeriod timePeriod) {
		Assert.notNull(timePeriod);
		final Teacher principal = this.teacherService.findByPrincipal();
		Assert.isTrue(this.teacherService.findTeacherByReservation(timePeriod.getReservation().getId()).equals(principal), "Esta Acci�n solo la pueden hacer los profesores.");
		final Integer tramoHorario = timePeriod.getEndHour() - timePeriod.getStartHour();
		Assert.isTrue(tramoHorario == 1, "Los tramos horarios deben ser de 1 hora.");
		final Collection<Reservation> reservations = this.reservationService.findAllReservationByTeacher(principal.getUserAccount().getId());
		Assert.isTrue(!this.getTimePeriod(timePeriod) == true, "Este tramo horario ya ha sido escogido.");
		this.findByReservation(timePeriod.getReservation().getId());
		Assert.isTrue(timePeriod.getReservation().getStatus().equals("PENDING") || timePeriod.getReservation().getStatus().equals("REVIEWING"), "No puede a�adir un timePeriod si la reserva est� en Final");
		final Schedule schedule = this.setScheduleTrue(timePeriod);
		if (timePeriod.getId() != 0) {
			final TimePeriod t = this.findOne(timePeriod.getId());
			this.setScheduleFalse(t);
			Assert.isTrue(reservations.contains(timePeriod.getReservation()), "No puedes modificar un periodo de tiempo que no sea de su reserva.");
		}
		final TimePeriod tPeriod = this.timePeriodRepository.save(timePeriod);
		this.scheduleService.save(schedule);
		Assert.notNull(tPeriod);
		return tPeriod;
	}

	public Collection<TimePeriod> findByReservation(final Integer reservationId) {
		final Collection<TimePeriod> res = this.timePeriodRepository.findByReservation(reservationId);
		Assert.notNull(res);
		return res;
	}

	public Collection<TimePeriod> findTimePeriodsByTeacher(final int teacherId) {
		Collection<TimePeriod> res;
		final Teacher teacher = this.teacherService.findOne(teacherId);
		res = this.timePeriodRepository.findTimePeriodsByTeacher(teacher.getUserAccount().getId());
		return res;
	}
	public void deleteInBatch(final Collection<TimePeriod> timePeriods) {
		this.timePeriodRepository.deleteInBatch(timePeriods);

	}
	//	private Schedule setScheduleTrue(final TimePeriod timePeriod) {
	//		final Teacher teacher = this.teacherService.findByPrincipal();
	//		final Schedule schedule = this.scheduleService.findScheduleByTeacher(teacher);
	//		final List<Boolean> newMonday = (List<Boolean>) schedule.getMonday();
	//		final List<Boolean> newTuesday = (List<Boolean>) schedule.getTuesday();
	//		final List<Boolean> newWednesday = (List<Boolean>) schedule.getWednesday();
	//		final List<Boolean> newThursday = (List<Boolean>) schedule.getThursday();
	//		final List<Boolean> newFriday = (List<Boolean>) schedule.getFriday();
	//		if (timePeriod.getDayNumber() == 1) {
	//			for (int i = 0; i < 14; i++)
	//				if (timePeriod.getStartHour() == i + 8)
	//					newMonday.set(i, true);
	//		} else if (timePeriod.getDayNumber() == 2) {
	//			for (int i = 0; i < 14; i++)
	//				if (timePeriod.getStartHour() == i + 8)
	//					newTuesday.set(i - 1, true);
	//		} else if (timePeriod.getDayNumber() == 3) {
	//			for (int i = 0; i < 14; i++)
	//				if (timePeriod.getStartHour() == i + 8)
	//					newWednesday.set(i - 1, true);
	//		} else if (timePeriod.getDayNumber() == 4) {
	//			for (int i = 0; i < 14; i++)
	//				if (timePeriod.getStartHour() == i + 8)
	//					newThursday.set(i - 1, true);
	//		} else if (timePeriod.getDayNumber() == 5)
	//			for (int i = 0; i < 14; i++)
	//				if (timePeriod.getStartHour() == i + 8)
	//					newFriday.set(i - 1, true);
	//		schedule.setMonday(newMonday);
	//		schedule.setTuesday(newTuesday);
	//		schedule.setWednesday(newWednesday);
	//		schedule.setThursday(newThursday);
	//		schedule.setFriday(newFriday);
	//		this.scheduleService.save(schedule);
	//		return schedule;
	//	}

	private Schedule setScheduleTrue(final TimePeriod timePeriod) {
		return this.setScheduleTrueFalse(timePeriod, true);
	}

	private Schedule setScheduleFalse(final TimePeriod timePeriod) {
		return this.setScheduleTrueFalse(timePeriod, false);
	}

	private Schedule setScheduleTrueFalse(final TimePeriod timePeriod, final boolean b) {
		final Teacher teacher = this.teacherService.findByPrincipal();
		final Schedule schedule = this.scheduleService.findScheduleByTeacher(teacher);
		List<Boolean> ls;
		final int day = timePeriod.getDayNumber();
		final int hour = timePeriod.getStartHour();
		switch (day) {
		case 1:
			ls = (List<Boolean>) schedule.getMonday();
			ls.set(hour - 8, b);
			schedule.setMonday(ls);
			break;
		case 2:
			ls = (List<Boolean>) schedule.getTuesday();
			ls.set(hour - 8, b);
			schedule.setTuesday(ls);
			break;
		case 3:
			ls = (List<Boolean>) schedule.getWednesday();
			ls.set(hour - 8, b);
			schedule.setWednesday(ls);
			break;
		case 4:
			ls = (List<Boolean>) schedule.getThursday();
			ls.set(hour - 8, b);
			schedule.setThursday(ls);
			break;
		case 5:
			ls = (List<Boolean>) schedule.getFriday();
			ls.set(hour - 8, b);
			schedule.setFriday(ls);
			break;
		}

		this.scheduleService.save(schedule);
		return schedule;
	}

	public Schedule setScheduleFalse(final Collection<TimePeriod> timePeriods, final Teacher teacher) {
		final Schedule schedule = this.scheduleService.findScheduleByTeacher(teacher);
		final List<Boolean> newMonday = (List<Boolean>) schedule.getMonday();
		final List<Boolean> newTuesday = (List<Boolean>) schedule.getTuesday();
		final List<Boolean> newWednesday = (List<Boolean>) schedule.getWednesday();
		final List<Boolean> newThursday = (List<Boolean>) schedule.getThursday();
		final List<Boolean> newFriday = (List<Boolean>) schedule.getFriday();
		for (final TimePeriod timePeriod : timePeriods) {
			final int day = timePeriod.getDayNumber();
			final int hour = timePeriod.getStartHour();
			switch (day) {
			case 1:
				newMonday.set(hour - 8, false);
				break;
			case 2:
				newThursday.set(hour - 8, false);
				break;
			case 3:
				newWednesday.set(hour - 8, false);
				break;
			case 4:
				newThursday.set(hour - 8, false);
				break;
			case 5:
				newFriday.set(hour - 8, false);
				break;
			}
		}
		schedule.setMonday(newMonday);
		schedule.setTuesday(newTuesday);
		schedule.setWednesday(newWednesday);
		schedule.setThursday(newThursday);
		schedule.setFriday(newFriday);
		this.scheduleService.save2(schedule, teacher);
		return schedule;
	}
	public Boolean getTimePeriod(final TimePeriod timePeriod) {
		Boolean result = false;
		List<Boolean> res;
		final Teacher teacher = this.teacherService.findByPrincipal();
		final Schedule schedule = this.scheduleService.findScheduleByTeacher(teacher);
		if (timePeriod.getDayNumber() == 1) {
			res = (List<Boolean>) schedule.getMonday();
			result = res.get(timePeriod.getStartHour() - 8);
		} else if (timePeriod.getDayNumber() == 2) {
			res = (List<Boolean>) schedule.getTuesday();
			result = res.get(timePeriod.getStartHour() - 8);
		} else if (timePeriod.getDayNumber() == 3) {
			res = (List<Boolean>) schedule.getWednesday();
			result = res.get(timePeriod.getStartHour() - 8);
		} else if (timePeriod.getDayNumber() == 4) {
			res = (List<Boolean>) schedule.getThursday();
			result = res.get(timePeriod.getStartHour() - 8);
		} else if (timePeriod.getDayNumber() == 5) {
			res = (List<Boolean>) schedule.getFriday();
			result = res.get(timePeriod.getStartHour() - 8);
		}
		return result;
	}

	public boolean checkTimePeriodHours(final TimePeriod timePeriod) {
		boolean res = false;
		if (timePeriod.getStartHour() > 7 && timePeriod.getStartHour() < 22)
			if (timePeriod.getEndHour() > 7 && timePeriod.getEndHour() < 22)
				if (timePeriod.getStartHour() < timePeriod.getEndHour())
					if (timePeriod.getEndHour() - timePeriod.getStartHour() == 1)
						res = true;
		return res;
	}
	public Integer timePeriodFree(final Schedule schedule) {
		Integer contador = 0;
		final List<Boolean> newMonday = (List<Boolean>) schedule.getMonday();
		final List<Boolean> newTuesday = (List<Boolean>) schedule.getTuesday();
		final List<Boolean> newWednesday = (List<Boolean>) schedule.getWednesday();
		final List<Boolean> newThursday = (List<Boolean>) schedule.getThursday();
		final List<Boolean> newFriday = (List<Boolean>) schedule.getFriday();
		for (final Boolean b : newMonday)
			if (b == false)
				contador++;
		for (final Boolean b : newTuesday)
			if (b == false)
				contador++;
		for (final Boolean b : newWednesday)
			if (b == false)
				contador++;
		for (final Boolean b : newThursday)
			if (b == false)
				contador++;
		for (final Boolean b : newFriday)
			if (b == false)
				contador++;

		return contador;
	}

	public Collection<TimePeriod> suggestTimePeriod(final int reservationId) {
		final Collection<TimePeriod> suggests = new ArrayList<>();
		final Reservation reservation = this.reservationService.findOne(reservationId);
		final Teacher teacher = this.teacherService.findTeacherByReservation(reservationId);
		final Schedule schedule = this.scheduleService.findScheduleByTeacher(teacher);
		Assert.notNull(schedule);
		final Collection<TimePeriod> timePeriods = this.findByReservation(reservationId);
		Assert.isTrue(reservation.getHoursWeek() > timePeriods.size(), "El n�mero de tramos horarios debe ser menor o igual a las horas semanales solicitadas.");
		Assert.isTrue(this.timePeriodFree(schedule) >= reservation.getHoursWeek(), "El n�mero de tramos horarios libres debe ser mayor o igual a las horas semanales solicitadas.");

		final List<Boolean> newMonday = (List<Boolean>) schedule.getMonday();
		final List<Boolean> newTuesday = (List<Boolean>) schedule.getTuesday();
		final List<Boolean> newWednesday = (List<Boolean>) schedule.getWednesday();
		final List<Boolean> newThursday = (List<Boolean>) schedule.getThursday();
		final List<Boolean> newFriday = (List<Boolean>) schedule.getFriday();

		if (newMonday.contains(false))
			for (int i = 0; i < newMonday.size(); i++)
				if (!newMonday.get(i)) {
					final TimePeriod suggest = new TimePeriod();
					suggest.setDayNumber(1);
					suggest.setStartHour(i + 8);
					suggest.setEndHour(i + 9);
					suggest.setReservation(reservation);
					final TimePeriod saved = this.timePeriodRepository.save(suggest);
					suggests.add(saved);

					newMonday.set(i, true);

					if (reservation.getHoursWeek() == suggests.size())
						break;
				}
		if (newTuesday.contains(false) && reservation.getHoursWeek() > suggests.size())
			for (int i = 0; i < newTuesday.size(); i++)
				if (!newTuesday.get(i)) {
					final TimePeriod suggest = new TimePeriod();
					suggest.setDayNumber(2);
					suggest.setStartHour(i + 8);
					suggest.setEndHour(i + 9);
					suggest.setReservation(reservation);
					final TimePeriod saved = this.timePeriodRepository.save(suggest);
					suggests.add(saved);

					newTuesday.set(i, true);

					if (reservation.getHoursWeek() == suggests.size())
						break;
				}
		if (newWednesday.contains(false) && reservation.getHoursWeek() > suggests.size())
			for (int i = 0; i < newWednesday.size(); i++)
				if (!newWednesday.get(i)) {
					final TimePeriod suggest = new TimePeriod();
					suggest.setDayNumber(3);
					suggest.setStartHour(i + 8);
					suggest.setEndHour(i + 9);
					suggest.setReservation(reservation);
					final TimePeriod saved = this.timePeriodRepository.save(suggest);
					suggests.add(saved);

					newWednesday.set(i, true);

					if (reservation.getHoursWeek() == suggests.size())
						break;
				}
		if (newThursday.contains(false) && reservation.getHoursWeek() > suggests.size())
			for (int i = 0; i < newThursday.size(); i++)
				if (!newThursday.get(i)) {
					final TimePeriod suggest = new TimePeriod();
					suggest.setDayNumber(4);
					suggest.setStartHour(i + 8);
					suggest.setEndHour(i + 9);
					suggest.setReservation(reservation);
					final TimePeriod saved = this.timePeriodRepository.save(suggest);
					suggests.add(saved);

					newThursday.set(i, true);

					if (reservation.getHoursWeek() == suggests.size())
						break;
				}
		if (newFriday.contains(false) && reservation.getHoursWeek() > suggests.size())
			for (int i = 0; i < newFriday.size(); i++)
				if (!newFriday.get(i)) {
					final TimePeriod suggest = new TimePeriod();
					suggest.setDayNumber(5);
					suggest.setStartHour(i + 8);
					suggest.setEndHour(i + 9);
					suggest.setReservation(reservation);
					final TimePeriod saved = this.timePeriodRepository.save(suggest);
					suggests.add(saved);

					newFriday.set(i, true);

					if (reservation.getHoursWeek() == suggests.size())
						break;
				}

		schedule.setMonday(newMonday);
		schedule.setTuesday(newTuesday);
		schedule.setWednesday(newWednesday);
		schedule.setThursday(newThursday);
		schedule.setFriday(newFriday);
		this.scheduleService.save(schedule);

		return suggests;
	}

	public boolean equals(final TimePeriod t, final TimePeriod other) {
		if (t.getDayNumber() == null) {
			if (other.getDayNumber() != null)
				return false;
		} else if (!t.getDayNumber().equals(other.getDayNumber()))
			return false;
		if (t.getEndHour() == null) {
			if (other.getEndHour() != null)
				return false;
		} else if (!t.getEndHour().equals(other.getEndHour()))
			return false;
		if (t.getStartHour() == null) {
			if (other.getStartHour() != null)
				return false;
		} else if (!t.getStartHour().equals(other.getStartHour()))
			return false;
		return true;
	}

	public void flush() {
		this.timePeriodRepository.flush();
	}
}
