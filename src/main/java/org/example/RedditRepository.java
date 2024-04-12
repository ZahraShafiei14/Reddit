package org.example;

import org.example.model.Post;
import org.example.model.Subreddit;
import org.example.model.User;

import java.util.ArrayList;
import java.util.List;

public class RedditRepository {
    private final static List<User> subscribedUsers = new ArrayList<>() {{
        add(new User("moonlike", "mahsash667581", "mahsashafiee6@gmail.com"));
        add(new User("z", "123", "zahrashafiee@gmail.com"));
    }};

    private static RedditRepository instance;
    private User currentUser;

    private RedditRepository() {
    }

    public static RedditRepository getInstance() {
        if (instance == null)
            instance = new RedditRepository();
        return instance;
    }

    public void addSubscribedUser(User user) {
        subscribedUsers.add(user);
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public User getCurrentUser() {
        if (currentUser != null && currentUser.isLoggedIn()) return currentUser;
        else subscribedUsers.forEach(user -> {
            if (user.isLoggedIn()) currentUser = user;
        });
        return currentUser;
    }

    public User getUserByUsername(String username) {
        for (User user : subscribedUsers) {
            if (user.getUsername().equals(username))
                return user;
        }
        return null;
    }

    public List<Subreddit> getAllSubreddits() {
        List<Subreddit> subreddits = new ArrayList<>();
        subscribedUsers.forEach(user -> subreddits.addAll(user.getCreatedSubreddits()));
        return subreddits;
    }

    public Subreddit getSubredditByName(String name) {
        for (Subreddit subreddit : getAllSubreddits()) if (subreddit.getName().equals(name)) return subreddit;
        return null;
    }

    public List<Post> getAllPosts() {
        List<Post> posts = new ArrayList<>();
        subscribedUsers.forEach(user -> posts.addAll(user.getCreatedPosts()));
        return posts;
    }

    public Post getPostsByTitle(String name) {
        for (Post post : getAllPosts()) if (post.getTitle().equals(name)) return post;
        return null;
    }
    public boolean upVotedPost(Post p, User user){
        for (Post post: user.getUpVotedPosts()) if (post.equals(p)) return true;
        return false;
    }
    public boolean downVotedPost(Post p, User user){
        for (Post post: user.getDownVotedPosts()) if (post.equals(p)) return true;
        return false;
    }
}
