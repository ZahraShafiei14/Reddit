package org.example.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Subreddit {
    private String name;// can not edit name!
    private String description;
    private String createdAt;
    private User creator;
    private List<User> admins = new ArrayList<>();
    private List<User> members = new ArrayList<>();
    private List<Post> posts = new ArrayList<>();
    private String creationType; // can not change

    public Subreddit(String name, User creator, String creationType) {
        setName(name);
        setDescription("");
        setCreatedAt(formatter(LocalDateTime.now()));
        setCreationType(creationType);
        setCreator(creator);
        creator.getCreatedSubreddits().add(this);
        getMembers().add(creator);
    }

    public static String formatter(LocalDateTime time) {
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public void editDescription(String description) {
        setDescription(description);
        System.out.println("Description was edited.");
    }

    public void followers(User user) {
        members.add(user);
        System.out.println(user.getUsername() + " subscribed " + getName());
        user.getSubscribedSubreddits().add(this);
    }

    public void addPost(Post post) {
        posts.add(post);
        System.out.println("Subreddit's post was added.");
    }

    public void deletedPost(Post post) {
        posts.add(post);
        System.out.println("Subreddit's post was deleted.");
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public List<User> getAdmins() {
        return admins;
    }

    public void setAdmins(List<User> admins) {
        this.admins = admins;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public String getCreationType() {
        return creationType;
    }

    public void setCreationType(String creationType) {
        this.creationType = creationType;
    }

    @Override
    public String toString() {
        return creator.getUsername() + " => " + name.toUpperCase();
    }
}
