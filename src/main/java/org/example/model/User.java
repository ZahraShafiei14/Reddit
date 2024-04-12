package org.example.model;



import org.example.exceptions.InvalidEmailException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
    private String username;
    private String password;
    private String email;
    private String bio;
    private UUID userID;
    private int karma;
    private boolean loggedIn = false;
    private final List<Comment> allComments = new ArrayList<>();//leaved by user
    private final List<Comment> upVotedComments = new ArrayList<>();
    private final List<Comment> downVotedComments = new ArrayList<>();
    private final List<Post> allPosts = new ArrayList<>();//leaved by user
    private final List<Post> createdPosts = new ArrayList<>();
    private final List<Post> savedPosts = new ArrayList<>();
    private final List<Post> upVotedPosts = new ArrayList<>();
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
            for (int i = 0; i <= user.subscribedSubreddits.size(); i++) {
                subsub.append(viewSubredditsInfo(user.subscribedSubreddits.get(i)));
            }
        }
        System.out.println(subsub);
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
        StringBuilder createdPosts = new StringBuilder();

        createdPosts.append(user.getUsername()).append(" created these following posts: \n");

        if (user.getCreatedPosts().isEmpty()) {
            createdPosts = new StringBuilder("No posts has been created yet.");
        } else {
            for (int i = 0; i <= user.createdPosts.size(); i++) {
                createdPosts.append(viewPostsInfo(user.createdPosts.get(i)));
            }
        }
        System.out.println(createdPosts);
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

    public List<Subreddit> getSubscribedSubreddits() {
        return subscribedSubreddits;
    }

}
