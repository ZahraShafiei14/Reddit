package org.example.model;

import java.time.LocalDateTime;

public class Comment {
    private String content;
    private LocalDateTime createdAt;
    private int commentKarma;
    private User author;
    private Post post;

    public Comment(User author, String content, String createdAt, Post post) {
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
