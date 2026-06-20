package az.ingress.meta_post_analyzer.controller;

import az.ingress.meta_post_analyzer.dto.AnalysisResultDto;
import az.ingress.meta_post_analyzer.service.MetaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final MetaService metaService;

    @GetMapping("/analyze")
    public AnalysisResultDto getAnalysisReport() {
        return metaService.analyzePosts();
    }

    @GetMapping("/raw")
    public String getRawMetaPosts() {
        return metaService.fetchLatestPosts();
    }
}