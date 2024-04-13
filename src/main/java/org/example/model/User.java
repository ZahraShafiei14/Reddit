package org.example.model;



import org.example.exceptions.InvalidEmailException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.example.model.Subreddit.formatter;

public class User {
    private String username;
    private String password;
    private String email;
    private String bio;
    private UUID userID;
    private int karma;
    private String createdAt;
    private boolean loggedIn = false;
    private final List<Comment> allComments = new ArrayList<>();//leaved by user
    private final List<Comment> upVotedComments = new ArrayList<>();
    private final List<Comment> downVotedComments = new ArrayList<>();
    private final List<Post> allPosts = new ArrayList<>();//leaved by user
    private final List<Post> createdPosts = new ArrayList<>();
    private final List<Post> savedPosts = new ArrayList<>();
    private final List<Post> upVotedPosts = new ArrayList<>();
    private final List<Post> timelinePosts = new ArrayList<>();
    private final List<Post> downVotedPosts = new ArrayList<>();
    private final List<Subreddit> createdSubreddits = new ArrayList<>();
    private final List<Subreddit> subscribedSubreddits = new ArrayList<>();

    private static final String EMAIL_REGEX = "^[\\w!#$%&'*+/=?^`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^`{|}~-]+)*@(?:\\w(?:[\\w-]*\\w)?\\.)+\\w(?:[\\w-]*\\w)?$";

    public User(String username, String password, String email) {
        setUsername(username);
        setPassword(password);
        setEmail(email);
        setBio("");
        setUserID(UUID.randomUUID());
        setKarma(0);
        setCreatedAt(formatter(LocalDateTime.now()));
    }
    public User(String username, String password, String email, Subreddit subreddit){
        this.username = username;
        this.password = password;
        this.email = email;
        createdSubreddits.add(subreddit);
    }
    public static boolean isEmailValid(String email) {
        return email.matches(EMAIL_REGEX);
    }
    public static void viewSubscribedSubreddit(User user) {
        StringBuilder subsub = new StringBuilder();

        subsub.append(user.getUsername()).append(" subscribed these following subreddits: \n");

        if (user.getSubscribedSubreddits().isEmpty()) {
            subsub = new StringBuilder("No subreddits has been subscribed yet.");
        } else {
            for (int i = 0; i < user.subscribedSubreddits.size(); i++) {
                subsub.append(viewSubredditsInfo(user.subscribedSubreddits.get(i)));
            }
        }
        System.out.println(subsub);
    }
    // add subscribed subreddit's post to timeline.
    public void addTimelinePosts(){
        for (Subreddit subreddit1: getSubscribedSubreddits()) {
            for (Post post : subreddit1.getPosts()) {
                timelinePosts.add(post);
            }
        }
    }
    public static void viewCreatedSubreddit(User user) {
        StringBuilder createdSub = new StringBuilder();

        createdSub.append(user.getUsername()).append(" created these following subreddits: \n");

        if (user.getCreatedSubreddits().isEmpty()) {
            createdSub = new StringBuilder("No subreddits has been created yet.");
        } else {
            for (int i = 0; i < user.createdSubreddits.size(); i++) {
                createdSub.append(viewSubredditsInfo(user.createdSubreddits.get(i)));
            }
        }
        System.out.println(createdSub);
    }
    public static void viewCreatedPosts(User user) {
        String createdPosts = "";

        createdPosts += user.getUsername() + " created these following posts: \n";

        if (user.getCreatedPosts().isEmpty()) {
            createdPosts += "No posts has been created yet.";
        } else {
            for (int i = 0; i < user.createdPosts.size(); i++) {
                createdPosts += viewPostsInfo(user.createdPosts.get(i));
            }
        }
        System.out.println(createdPosts);
    }
    public static void viewSavedPosts(User user) {
        String savedPosts = "";

        savedPosts += user.getUsername() + " saved these following posts: \n";

        if (user.getCreatedPosts().isEmpty()) {
            savedPosts += "No posts has been saved yet.";
        } else {
            for (int i = 0; i < user.savedPosts.size(); i++) {
                savedPosts += viewPostsInfo(user.savedPosts.get(i));
            }
        }
        System.out.println(savedPosts);
    }
    public static void viewTimeLinePosts(User user){
        String tlPosts = "";

        tlPosts += user.getUsername() + " have these trending posts: \n";

        if (user.getTimelinePosts().isEmpty()) {
            tlPosts+= "No posts has been added yet.";
        } else {
            for (int i = 0; i < user.timelinePosts.size(); i++) {
                tlPosts += viewPostsInfo(user.timelinePosts.get(i));
            }
        }
        System.out.println(tlPosts);
    }
    public static String viewSubredditsInfo(Subreddit subreddit) {
        return ("---------- " + subreddit.getName() + " ---------\n"
                + " Description : " + subreddit.getDescription() + "\n"
                + " Created at : " + subreddit.getCreatedAt() + "\n"
                + " Members : " + subreddit.getMembers().size() + "\n"
                + " Posts : " + subreddit.getPosts().size() + "\n"
                + "--------------------------\n");
    }
    public static String viewPostsInfo(Post post) {
        return ("---------- " + post.getTitle() + " ----------\n"
                + " Content : " + post.getContent() + "\n"
                + " Created at : " + post.getTimeAgo(post.getCreatedAt()) + "\n"
                + " Author : " + post.getAuthor().getUsername() + "\n"
                + " Karma : " + post.getPostsKarma() + "\n"
                + " Subreddit : " + post.getSubreddit().getName() + "\n"
                + "------------------------------\n");
    }
    public static String viewCommentInfo(Comment comment) {
        return ("--- Author : " + comment.getAuthor() + " ------- " + comment.getTimeAgo(comment.getCreatedAt())+" ---\n"
                + " Content : " + comment.getContent() + "\n"
                + " Karma : " + comment.getCommentKarma() + "\n"
                + "------------------------------\n");
    }
    public static String viewUserInfo(User user) {
        return ("---------- " + user.getUsername() + " ----------\n"
                + " Created at : " + user.getCreatedAt() + "\n"
                + " Email: " + user.getEmail() + "\n"
                + " Bio : " + user.getBio() + "\n"
                + " Karma : " + user.getKarma() + "\n"
                + " Created subreddits : " + ((user.getSubscribedSubreddits().size() == 0) ? "nothing": user.getSubscribedSubreddits().size() )+ "\n"
                + " Created posts : " + ((user.getCreatedPosts().size() == 0) ? "nothing": user.getCreatedPosts().size() )+ "\n"
                + "------------------------------\n");
    }
    public static void viewUpvotedComments(User user){
        String upComment = "";

        upComment += user.getUsername() + " upvoted these following comments: \n";

        if (user.getUpVotedComments().isEmpty()) {
            upComment+= "No comments has been upvoted yet.";
        } else {
            for (int i = 0; i < user.upVotedComments.size(); i++) {
                upComment += viewCommentInfo(user.getUpVotedComments().get(i));
            }
        }
        System.out.println(upComment);
    }
    public static void viewDownvotedComments(User user){
        String downComment = "";

        downComment += user.getUsername() + " downvoted these following comments: \n";

        if (user.getUpVotedComments().isEmpty()) {
            downComment+= "No comments has been downvoted yet.";
        } else {
            for (int i = 0; i < user.upVotedComments.size(); i++) {
                downComment += viewCommentInfo(user.getUpVotedComments().get(i));
            }
        }
        System.out.println(downComment);
    }
    public static void viewUpvotedPosts(User user){
        String upPost = "";

        upPost += user.getUsername() + " upvoted these following posts: \n";

        if (user.getUpVotedPosts().isEmpty()) {
            upPost+= "No posts has been upvoted yet.";
        } else {
            for (int i = 0; i < user.upVotedPosts.size(); i++) {
                upPost += viewPostsInfo(user.upVotedPosts.get(i));
            }
        }
        System.out.println(upPost);
    }
    public static void viewDownvotedPosts(User user){
        String downPost = "";

        downPost += user.getUsername() + " downvoted these following posts: \n";

        if (user.getDownVotedPosts().isEmpty()) {
            downPost += "No posts has been downvoted yet.";
        } else {
            for (int i = 0; i < user.downVotedPosts.size(); i++) {
                downPost += viewPostsInfo(user.downVotedPosts.get(i));
            }
        }
        System.out.println(downPost);
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

    public List<Post> getCreatedPosts() {
        return createdPosts;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) throws InvalidEmailException {
        /*if (email.matches(EMAIL_REGEX)) this.email = email;
        else throw new InvalidEmailException();*/
        this.email = email;
    }

    public void setKarma(int karma) {
        this.karma = karma;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public List<Post> getTimelinePosts() {
        return timelinePosts;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getBio() {
        return bio;
    }

    public UUID getUserID() {
        return userID;
    }

    public int getKarma() {
        return karma;
    }

    public List<Comment> getAllComments() {
        return allComments;
    }

    public List<Comment> getUpVotedComments() {
        return upVotedComments;
    }

    public List<Comment> getDownVotedComments() {
        return downVotedComments;
    }

    public List<Post> getAllPosts() {
        return allPosts;
    }

    public List<Post> getSavedPosts() {
        return savedPosts;
    }

    public List<Post> getUpVotedPosts() {
        return upVotedPosts;
    }

    public List<Post> getDownVotedPosts() {
        return downVotedPosts;
    }

    public List<Subreddit> getCreatedSubreddits() {
        return createdSubreddits;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<Subreddit> getSubscribedSubreddits() {
        return subscribedSubreddits;
    }

}
