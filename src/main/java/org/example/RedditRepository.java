package org.example;

import org.example.model.Post;
import org.example.model.Subreddit;
import org.example.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RedditRepository {
    private final static List<User> subscribedUsers = new ArrayList<>() {{
        add(new User("moonlike", "mahsash667581", "mahsashafiee6@gmail.com"));
        add(new User("zahra", "123", "zahra@gmail.com"));
    }};

    private final  List<Subreddit> subreddits = new ArrayList<>(){{
        add(new Subreddit("MoonLight" , getUserByUsername("moonlike") , "restricted"));
        add(new Subreddit("Moony" , getUserByUsername("moonlike") , "public"));

        add(new Subreddit("AP project", getUserByUsername("zahra"), "public"));

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

    // this method prevent duplicate subreddits
    public List<Subreddit> getAllSubreddits() {
        Set<String> subredditNames = new HashSet<>();
        List<Subreddit> allSubreddits = new ArrayList<>(subreddits);

        // Add subreddits created by subscribed users
        subscribedUsers.forEach(user -> {
            user.getCreatedSubreddits().forEach(subreddit -> {
                if (!subredditNames.contains(subreddit.getName())&& !subreddits.contains(subreddit)) { // Check if subreddit name is already added
                    allSubreddits.add(subreddit);
                    subredditNames.add(subreddit.getName());
                }
            });
        });
        return allSubreddits;
    }
    public List<User> getAllUsers() {
        Set<String> usernames = new HashSet<>();
        List<User> allUsers = new ArrayList<>();

        // Add users from subscribedUsers
        subscribedUsers.forEach(user -> {
            if (!usernames.contains(user.getUsername())) {
                allUsers.add(user);
                usernames.add(user.getUsername());
            }
        });

        return allUsers;
    }
    public User getUserByUsername(String username) {
        for (User user : subscribedUsers) {
            if (user.getUsername().equals(username))
                return user;
        }
        return null;
    }

//    public List<Subreddit> getAllSubreddits() {
//        subscribedUsers.forEach(user -> subreddits.addAll(user.getCreatedSubreddits()));
//        return subreddits;
//    }

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
