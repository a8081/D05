
package controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ConfigurationParametersService;
import services.ReservationService;
import services.TimePeriodService;
import controllers.teacher.ReservationTeacherController;
import domain.Reservation;
import domain.TimePeriod;

@Controller
@RequestMapping("/timePeriod")
public class TimePeriodController extends AbstractController {

	@Autowired
	private TimePeriodService				timePeriodService;

	@Autowired
	private ReservationService				reservationService;

	@Autowired
	private ReservationTeacherController	reservationTeacherController;

	@Autowired
	private ConfigurationParametersService	configurationParametersService;


	// CREATE --------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int reservationId) {

		ModelAndView res;
		TimePeriod timePeriod;
		final Reservation reservation = this.reservationService.findOne(reservationId);

		timePeriod = this.timePeriodService.create();
		timePeriod.setReservation(reservation);
		res = this.createEditModelAndView(timePeriod);
		res.addObject("new", false);

		return res;

	}

	// EDIT --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int timePeriodId) {
		ModelAndView result;
		TimePeriod timePeriod;

		timePeriod = this.timePeriodService.findOne(timePeriodId);
		if (timePeriod != null) {
			result = this.createEditModelAndView(timePeriod);
			result.addObject("id", timePeriod.getId());
		} else
			result = new ModelAndView("redirect:misc/403");
		return result;
	}
	// SAVE --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final TimePeriod timePeriod, final BindingResult binding) {

		ModelAndView res = null;
		//		final Actor principal = this.actorService.findByPrincipal();

		if (binding.hasErrors())
			res = this.createEditModelAndView(timePeriod);

		else if (timePeriod != null)

			try {
				this.timePeriodService.save(timePeriod);
				res = this.reservationTeacherController.display(timePeriod.getReservation().getId());
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(timePeriod, "general.commit.error");
			}

		return res;
	}

	// DELETE --------------------------------------------------------

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int timePeriodId) {
		ModelAndView result;
		final TimePeriod timePeriod = this.timePeriodService.findOne(timePeriodId);

		try {
			this.timePeriodService.delete(timePeriod);
			result = this.reservationTeacherController.display(timePeriod.getReservation().getId());
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(timePeriod, "general.commit.error");
			result.addObject("id", timePeriod.getId());
		}

		return result;
	}

	protected ModelAndView createEditModelAndView(final TimePeriod timePeriod) {

		ModelAndView res;

		res = this.createEditModelAndView(timePeriod, null);

		return res;

	}

	protected ModelAndView createEditModelAndView(final TimePeriod timePeriod, final String messageCode) {

		ModelAndView res;

		res = new ModelAndView("timePeriod/edit");
		res.addObject("timePeriod", timePeriod);

		res.addObject("message", messageCode);
		final String banner = this.configurationParametersService.find().getBanner();
		res.addObject("banner", banner);

		return res;

	}

}
