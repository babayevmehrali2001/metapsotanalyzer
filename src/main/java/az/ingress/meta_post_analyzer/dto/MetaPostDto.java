package az.ingress.meta_post_analyzer.dto;

import lombok.Data;

@Data
public class MetaPostDto {
    private String message;
    private String createdTime;
    private int likesCount;
    private int commentsCount;

    public int getEngagement() {
        return this.likesCount + this.commentsCount;
    }

}