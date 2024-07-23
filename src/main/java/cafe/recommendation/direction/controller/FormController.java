package cafe.recommendation.direction.controller;

import cafe.recommendation.cafe.service.CafeRecommendationService;
import cafe.recommendation.direction.dto.InputDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class FormController {

    private final CafeRecommendationService cafeRecommendationService;

    @GetMapping("/")
    public String index() {
        return "main";
    }

    @PostMapping("/search")
    public ModelAndView postDirection(@ModelAttribute InputDto inputDto) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("output");
        modelAndView.addObject("outputFormList",
                cafeRecommendationService.recommendCafeList(inputDto.getAddress()));

        return modelAndView;
    }


}
