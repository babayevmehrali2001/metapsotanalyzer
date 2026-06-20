package az.ingress.meta_post_analyzer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisResultDto {

    private List<MetaPostDto> topThreePosts;

    private Map<String, Integer> likesByDay;

    private String summary;

}