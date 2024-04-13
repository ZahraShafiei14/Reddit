package org.example.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class Post {
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private User author;
    private Subreddit subreddit; // Reference to the subreddit where the post is posted
    private int postsKarma;
    private List<Comment> comments;
    private LocalDateTime editedAt;
    private boolean isLocked;//leaving comment is locked or not

    public Post(String title, String content, User author, Subreddit subreddit) {
        setTitle(title);
        setContent(content);
        setCreatedAt(LocalDateTime.now());
        setAuthor(author);
        setSubreddit(subreddit);
    }

    public void increasePostKarma(){
        setPostsKarma(this.postsKarma++);
        System.out.println("Post's karma increased.");
    }

    public void decreasePostKarma(){
        setPostsKarma(this.postsKarma--);
        System.out.println("Post's karma decreased.");
    }

    // change the time too
    public void editTitle(String newTitle){
        setTitle(newTitle);
        System.out.println("Post's title edited.");
        setEditedAt(LocalDateTime.now());
    }
    // change the time too
    public void editContent(String newContent){
        setTitle(newContent);
        System.out.println("Post's content edited.");
        setEditedAt(LocalDateTime.now());
    }

    public void addComment(Comment comment){
            comments.add(comment);
            System.out.println("Comment was added.");
    }

    public void deleteComment(Comment comment){
        comments.remove(comment);
        System.out.println("Comment was deleted.");
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

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
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

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Subreddit getSubreddit() {
        return subreddit;
    }

    public void setSubreddit(Subreddit subreddit) {
        this.subreddit = subreddit;
    }

    public int getPostsKarma() {
        return postsKarma;
    }

    public void setPostsKarma(int postsKarma) {
        this.postsKarma = postsKarma;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public LocalDateTime getEditedAt() {
        return editedAt;
    }

    public void setEditedAt(LocalDateTime editedAt) {
        this.editedAt = editedAt;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }
}
