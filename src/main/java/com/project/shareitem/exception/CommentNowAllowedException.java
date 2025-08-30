package com.project.shareitem.exception;

public class CommentNowAllowedException extends RuntimeException {
    public CommentNowAllowedException() {
        super("Пользователь не может оставить отзыв на вещь, которую он не арендовал");
    }
}
