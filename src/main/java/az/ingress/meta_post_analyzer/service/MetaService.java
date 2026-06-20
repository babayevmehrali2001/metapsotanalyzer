package az.ingress.meta_post_analyzer.service;

import az.ingress.meta_post_analyzer.dto.AnalysisResultDto;
import az.ingress.meta_post_analyzer.dto.MetaPostDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MetaService {

    @Value("${meta.access-token}")
    private String accessToken;

    private final RestClient restClient = RestClient.create();

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String fetchLatestPosts() {

        String url =
                "https://graph.facebook.com/v25.0/me/posts" +
                        "?fields=message,created_time,likes.summary(true),comments.summary(true)" +
                        "&limit=20" +
                        "&access_token=" + accessToken;

        try {

            String response = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(String.class);

            if (response == null || response.isBlank()) {
                return getMockData();
            }

            return response;

        } catch (Exception e) {
            return getMockData();
        }
    }

    public AnalysisResultDto analyzePosts() {

        List<MetaPostDto> posts = new ArrayList<>();

        try {

            String jsonResponse = fetchLatestPosts();

            JsonNode root = objectMapper.readTree(jsonResponse);

            JsonNode data = root.path("data");

            for (JsonNode postNode : data) {

                MetaPostDto post = new MetaPostDto();

                post.setMessage(
                        postNode.path("message")
                                .asText("No message")
                );

                post.setCreatedTime(
                        postNode.path("created_time")
                                .asText()
                );

                post.setLikesCount(
                        postNode.path("likes")
                                .path("summary")
                                .path("total_count")
                                .asInt(0)
                );

                post.setCommentsCount(
                        postNode.path("comments")
                                .path("summary")
                                .path("total_count")
                                .asInt(0)
                );

                posts.add(post);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to analyze Meta posts", e);
        }

        List<MetaPostDto> topThreePosts =
                posts.stream()
                        .sorted(
                                Comparator.comparingInt(
                                        MetaPostDto::getEngagement
                                ).reversed()
                        )
                        .limit(3)
                        .collect(Collectors.toList());

        Map<String, Integer> likesByDay = new HashMap<>();

        for (MetaPostDto post : posts) {

            if (post.getCreatedTime() == null ||
                    post.getCreatedTime().length() < 10) {
                continue;
            }

            LocalDate date =
                    LocalDate.parse(
                            post.getCreatedTime()
                                    .substring(0, 10)
                    );

            String dayName =
                    date.getDayOfWeek()
                            .name();

            likesByDay.put(
                    dayName,
                    likesByDay.getOrDefault(dayName, 0)
                            + post.getLikesCount()
            );
        }

        String summary =
                generateSummary(
                        posts,
                        topThreePosts,
                        likesByDay
                );

        return new AnalysisResultDto(
                topThreePosts,
                likesByDay,
                summary
        );
    }

    private String generateSummary(
            List<MetaPostDto> posts,
            List<MetaPostDto> topThreePosts,
            Map<String, Integer> likesByDay
    ) {

        double averageEngagement =
                posts.stream()
                        .mapToInt(MetaPostDto::getEngagement)
                        .average()
                        .orElse(0);

        String bestDay =
                likesByDay.entrySet()
                        .stream()
                        .max(Map.Entry.comparingByValue())
                        .map(Map.Entry::getKey)
                        .orElse("Unknown");

        int bestEngagement =
                topThreePosts.isEmpty()
                        ? 0
                        : topThreePosts.get(0)
                        .getEngagement();

        return String.format(
                "Average engagement: %.2f. Best performing day: %s. Highest engagement: %d.",
                averageEngagement,
                bestDay,
                bestEngagement
        );
    }

    private String getMockData() {
        return """
                {
                  "data": [
                    {
                      "message": "Java Internship Program",
                      "created_time": "2026-06-15T12:00:00+0000",
                      "likes": {
                        "summary": {
                          "total_count": 150
                        }
                      },
                      "comments": {
                        "summary": {
                          "total_count": 45
                        }
                      }
                    },
                    {
                      "message": "Spring Boot Webinar",
                      "created_time": "2026-06-16T14:30:00+0000",
                      "likes": {
                        "summary": {
                          "total_count": 95
                        }
                      },
                      "comments": {
                        "summary": {
                          "total_count": 20
                        }
                      }
                    }
                  ]
                }
                """;
    }
}