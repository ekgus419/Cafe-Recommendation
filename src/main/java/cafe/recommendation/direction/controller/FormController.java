package cafe.recommendation.direction.controller;

import cafe.recommendation.cafe.service.CafeRecommendationService;
import cafe.recommendation.direction.dto.InputDto;
import cafe.recommendation.direction.dto.OutputDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FormController {

    private final CafeRecommendationService cafeRecommendationService;

    @PostMapping("/search")
    @ResponseBody
    public List<OutputDto> postDirection(@RequestBody InputDto inputDto) {
        return cafeRecommendationService.recommendCafeList(inputDto.getAddress());
    }

}
