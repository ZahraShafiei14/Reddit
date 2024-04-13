package org.example.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Comment {
    private String content;
    private LocalDateTime createdAt;
    private int commentKarma;
    private User author;
    private Post post;

    public Comment(User author, String content, Post post) {
        setContent(content);
        setAuthor(author);
        setCreatedAt(LocalDateTime.now());
        setCommentKarma(commentKarma);
        setAuthor(author);
        setPost(post);
    }

    public void increaseCommentKarma() {
        setCommentKarma(this.commentKarma++);
        System.out.println("Comment's karma was increased.");
    }

    public void decreaseCommentKarma() {
        setCommentKarma(this.commentKarma--);
        System.out.println("Comment's karma was decreased.");
    }

    public void editComment(String content) {
        setContent(content);
        System.out.println("Comment was edited.");
    }
    public String getTimeAgo(LocalDateTime time) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(time, now);

        long seconds = duration.getSeconds();
        if (seconds < 60) {
            return "Just now";
        } else if (seconds < 3600) {
            long minutes = seconds / 60;
            return minutes + " minute" + (minutes == 1 ? "" : "s") + " ago";
        } else if (seconds < 86400) {
            long hours = seconds / 3600;
            return hours + " hour" + (hours == 1 ? "" : "s") + " ago";
        } else if (seconds < (86400 * 30)){
            long days = seconds / 86400;
            return days + " day" + (days == 1 ? "" : "s") + " ago";
        }else {
            long months = seconds / (86400 * 30);
            return months + " month" + (months == 1 ? "" : "s") + " ago";
        }
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getCommentKarma() {
        return commentKarma;
    }

    public void setCommentKarma(int commentKarma) {
        this.commentKarma = commentKarma;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

}
