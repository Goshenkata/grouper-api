package com.example.grouperapi.converters;

import com.example.grouperapi.model.entities.Comment;
import com.example.grouperapi.model.entities.Reply;
import org.modelmapper.AbstractConverter;

import java.util.List;

public class PostCommentsListToPostCount extends AbstractConverter<List<Comment>, Integer> {
    @Override
    protected Integer convert(List<Comment> comments) {
        if(comments != null) {
            int sum = 0;
            for (Comment comment : comments) {
                sum += count(comment);
            }
            return sum;
        } else {
            return 0;
        }
    }

    private Integer count(Comment comment) {
        if (comment.getReplies().isEmpty()) {
            return 1;
        } else {
            int sum = 1;
            for (Reply reply : comment.getReplies()) {
                sum += count(reply);
            }
            return sum;
        }
    }
}
