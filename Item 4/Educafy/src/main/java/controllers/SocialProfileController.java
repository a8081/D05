
package controllers;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.SocialProfileService;
import domain.Actor;
import domain.SocialProfile;

@Controller
@RequestMapping("/socialProfile")
public class SocialProfileController extends AbstractController {

	@Autowired
	private SocialProfileService	socialProfileService;
	@Autowired
	private ActorService			actorService;

	final String					lang	= LocaleContextHolder.getLocale().getLanguage();


	@RequestMapping("/create")
	public ModelAndView create() {
		final ModelAndView result = new ModelAndView("socialProfile/edit");
		final SocialProfile profile = this.socialProfileService.create();
		profile.setActor(this.actorService.findByPrincipal());
		result.addObject("socialProfile", profile);
		return result;
	}

	@RequestMapping("/list")
	public ModelAndView list() {
		final ModelAndView result = new ModelAndView("socialProfile/list");
		final Actor actor = this.actorService.findByPrincipal();
		final Collection<SocialProfile> list = this.socialProfileService.findByActor(actor.getId());
		result.addObject("socialProfiles", list);
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int id) {
		ModelAndView result;
		final SocialProfile profile = this.socialProfileService.findOne(id);
		result = new ModelAndView("socialProfile/edit");
		result.addObject("socialProfile", profile);
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView edit(@Valid final SocialProfile socialProfile, final BindingResult binding) {
		ModelAndView result;
		final Actor actor = this.actorService.findByPrincipal();
		if (binding.hasErrors()) {
			result = new ModelAndView("socialProfile/edit");
			result.addObject("socialProfile", socialProfile);
			result.addObject("errors", binding.getAllErrors());
		} else {
			result = new ModelAndView("socialProfile/list");
			try {
				if (socialProfile.getId() == 0) {
					socialProfile.setActor(this.actorService.findByPrincipal());
					this.socialProfileService.save(socialProfile);
				} else {
					Assert.isTrue(socialProfile.getActor().getId() == this.actorService.findByPrincipal().getId(), "Only the owner of the Social Profile can edit it");
					this.socialProfileService.save(socialProfile);
				}
			} catch (final Exception e) {
				result.addObject("alert", "socialProfile.edit.error");
			}

			final Collection<SocialProfile> list = this.socialProfileService.findByActor(actor.getId());
			result.addObject("socialProfiles", list);
		}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int id) {
		ModelAndView result;
		final Actor actor = this.actorService.findByPrincipal();
		final SocialProfile profile = this.socialProfileService.findOne(id);
		this.socialProfileService.delete(profile);

		result = new ModelAndView("socialProfile/list");
		final Collection<SocialProfile> list = this.socialProfileService.findByActor(actor.getId());
		result.addObject("socialProfiles", list);
		return result;
	}

	// DISPLAY --------------------------------------------------------
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int socialProfileId) {
		final ModelAndView result;
		final SocialProfile socialProfile;

		socialProfile = this.socialProfileService.findOne(socialProfileId);

		if (socialProfile != null) {
			result = new ModelAndView("socialProfile/display");
			result.addObject("socialProfile", socialProfile);
			result.addObject("lang", this.lang);
		} else
			result = new ModelAndView("redirect:misc/403");

		return result;
	}

}
