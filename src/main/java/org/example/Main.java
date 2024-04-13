package org.example;

import org.example.model.Comment;
import org.example.model.Post;
import org.example.model.Subreddit;
import org.example.model.User;

import java.util.List;
import java.util.Scanner;

import static org.example.model.User.*;

public class Main {

    private static final RedditRepository repository = RedditRepository.getInstance();

    public static void main(String[] args) {
        runMenu();
    }

    private static void runMenu() {
        Scanner command = new Scanner(System.in);
        while (true) {
            System.out.println("\n   ***** REDDIT MENU *****");
            System.out.println(" (1) New to reddit? Sign Up");
            System.out.println(" (2) Already a redditor? Log In");
            System.out.println(" (3) Exit ");
            System.out.println("\nPlease enter your choice by its number: ");
            switch (command.nextLine()) {
                case "1" -> redditMenuSignUp();
                case "2" -> redditMenuLogIn();
                case "3" -> System.exit(0);
                default -> {
                    System.out.println("Invalid input. Please try again...");
                    runMenu();
                }
            }
        }
    }

    public static void redditMenuSignUp() {
        clearScreen();
        Scanner command = new Scanner(System.in);

        boolean validEmail = false;
        while (!validEmail) {
            System.out.println("Enter your email: ");
            String email = command.nextLine();

            if (isEmailValid(email)) {
                validEmail = true; // Set validEmail to true to exit the loop

                boolean validUsername = false;
                while (!validUsername) {
                    System.out.println("Enter your username: ");
                    String username = command.nextLine();

                    if (repository.getUserByUsername(username) == null) {
                        validUsername = true; // Set validUsername to true to exit the loop
                        System.out.println("Enter your password: ");
                        String password = command.nextLine();

                        boolean validConfirmPass = false;
                        while (!validConfirmPass) {
                            System.out.println("Confirm your password: ");
                            String confirmPass = command.nextLine();

                            if (password.equals(confirmPass)) {
                                validConfirmPass = true; // Set validConfirmPass to true to exit the loop
                                System.out.println("Signed up successfully.");
                                User user = new User(username, password, email);
                                repository.addSubscribedUser(user);

                            } else {
                                System.out.println("Passwords do not match. Please try again.");
                            }
                        }
                    } else {
                        System.out.println("Username already taken. Please try again.");
                    }
                }
            } else {
                System.out.println("Email format is invalid. Please try again.");
            }
        }
    }

    public static void redditMenuLogIn() {
        clearScreen();
        Scanner scanner = new Scanner(System.in);
        boolean validUsername = false;
        while (!validUsername) {
            System.out.println("Enter your username: ");
            String username = scanner.nextLine();
            User user = repository.getUserByUsername(username);
            repository.setCurrentUser(user);
            if (user != null) {
                validUsername = true;
                if (user.isLoggedIn()) {
                    System.out.println(user.getUsername() + " is already logged in.");
                    userMenu();
                    break;
                }
                System.out.println("Enter your password: ");
                String password = scanner.nextLine();
                if (user.getPassword().equals(password)) {
                    System.out.println(user.getUsername() + " logged in successfully.");
                    user.setLoggedIn(true);
                    userMenu();
                } else System.out.println("Wrong password.");
                break;
            } else System.out.println("Username not found.");
        }
    }

    private static void userMenu() {
        User user = repository.getCurrentUser();
        Scanner scanner = new Scanner(System.in);
        clearScreen();
        System.out.println("\n \" " + user.getUsername().toUpperCase() + " \" WELCOME TO YOUR DASHBOARD: ");
        System.out.println("(1) Edit personal info");
        System.out.println("(2) View profile");
        System.out.println("(3) Join subreddit");
        System.out.println("(4) Create subreddit");
        System.out.println("(5) Create post");
        System.out.println("(6) Search");
        System.out.println("(7) Logout");
        System.out.println("Enter your choice: ");
        switch (scanner.nextInt()) {
            case 1 -> editPersonalInfo(user);
            case 2 -> viewProfile(user);
            case 3 -> joinSubreddit(user);
            case 4 -> createSubreddit(user);
            case 5 -> createPost(user);
            case 6 -> search();
            case 7 -> logout(user);
            default -> {
                System.out.println("Invalid input. Please try again...");
                userMenu();
            }
        }
    }
    private static void joinSubreddit(User user) {
        Scanner scanner = new Scanner(System.in);
        clearScreen();
        System.out.println("\n--------- Join Subreddit ----------");
        List<Subreddit> allSubreddits = repository.getAllSubreddits();
        for (int i = 0; i < allSubreddits.size() ; i++){
            System.out.println( i + 1 + ") " + allSubreddits.get(i).getName());
        }
        System.out.println("Please enter the NAME of the subreddit you'd like to join: ");
        String name = scanner.nextLine();

        if (repository.getSubredditByName(name) == null) {
            System.out.println("Subreddit not found!");
            userMenu();
        } else {
            Subreddit subreddit = repository.getSubredditByName(name);
            if(subreddit.getCreator().equals(user)) {
                System.out.println("Already creator? No need to join again!");
                userMenu();
            } else {
                for (User u : subreddit.getMembers()) {
                    if (u.equals(user)) {
                        System.out.println("Already a member! No need to join again!");
                        userMenu();
                    }
                }
            }
            user.getSubscribedSubreddits().add(subreddit);
            subreddit.getMembers().add(user);
            System.out.println(user.getUsername() + " subscribed " + subreddit.getName());
            userMenu();
        }
    }

    private static void logout(User user) {
        Scanner scanner = new Scanner(System.in);
        clearScreen();
        System.out.println("\n------- Log Out --------");
        System.out.println("Are you sure you want to log out?[Y/N]");
        String in = scanner.nextLine();
        if (in.toLowerCase().equals("y")) {
            user.setLoggedIn(false);
            repository.setCurrentUser(null);
            System.out.println("Logged out successfully.\nThanks for using the app ^^ .Come back again...");
            runMenu();
        } else if(in.toLowerCase().equals("n")) userMenu();
        else {
            System.out.println("Invalid input. Please try again...");
            userMenu();
        }
    }

    private static void search() {
        Scanner scanner = new Scanner(System.in);
        clearScreen();
        System.out.println("\n--------- Search ----------");
        System.out.println("(1) User\n(2) Subreddit\n(3) Back\nEnter your choice: ");
        switch (scanner.nextInt()) {
            case 1 -> {
                scanner.nextLine();
                System.out.println("Please enter the username you are searching for:");
                String name = scanner.nextLine();
                boolean found = false;
                for (User user : repository.getAllUsers()) {
                    if (user.getUsername().contains(name)) {
                        found = true;
                        System.out.println(user.getUsername() +" was found.\n" +
                                "Would you like to see " + user.getUsername()+"'s profile? [Y/N]");
                        String ans = scanner.nextLine();
                        if (ans.toLowerCase().equals("y")) {
                            System.out.println(viewUserInfo(user));
                            userMenu();
                        }else if (ans.toLowerCase().equals("n"))
                            userMenu();
                        else {
                            System.out.println("Invalid input. Please try again...");
                            search();
                        }
                    }
                }
                if (!found) {
                    System.out.println("username was not found.");
                    userMenu();
                }
            }
            case 2 -> {
                scanner.nextLine();
                System.out.println("Please enter the subreddit's name you are searching for:");
                String name = scanner.nextLine();
                boolean found = false;
                for (Subreddit subreddit : repository.getAllSubreddits()) {
                    if (subreddit.getName().contains(name)) {
                        found = true;
                        System.out.println(subreddit.getName() +" was found.\n" +
                                "Would you like to see " + subreddit.getName() +"'s info? [Y/N]");
                        String ans = scanner.nextLine();
                        if (ans.toLowerCase().equals("y")) {
                            System.out.println(viewSubredditsInfo(subreddit));
                            userMenu();
                        } else if (ans.toLowerCase().equals("n"))
                            userMenu();
                        else {
                            System.out.println("Invalid input. Please try again...");
                            search();
                        }
                    }
                }
                if (!found) {
                    System.out.println("subreddit was not found.");
                    userMenu();
                }
            }
            case 3 -> userMenu();
            default -> {
                System.out.println("Invalid input. Please try again...");
                search();
            }
        }
    }

    private static void leaveComment(User user, Post post) {
        Scanner scanner = new Scanner(System.in);
        clearScreen();
        System.out.println("\n------- Leave comment --------");
        System.out.println("Enter content: ");
        String content = scanner.nextLine();
        Comment comment = new Comment(user, content, post);
        post.addComment(comment);
        viewCommentInfo(comment);
    }
    private static void createPost(User user) {
        Scanner scanner = new Scanner(System.in);
        clearScreen();
        System.out.println("\n--------- Create a Post ----------");
        System.out.println(" Choose subreddit to create your post: ");
        System.out.println("(1) My subscribed subreddits\n(2) My created subreddits\n(3) Back ");
        switch (scanner.nextInt()) {
            case 1 -> {
                scanner.nextLine();
                viewSubscribedSubreddit(user);
                if (user.getSubscribedSubreddits().isEmpty()) {
                    System.out.println("No subreddits has been subscribed yet.");
                    createPost(user);
                }
                System.out.println("Enter subreddits name: ");
                String name = scanner.nextLine();
                Subreddit subreddit = repository.getSubredditByName(name);
                if (repository.getSubredditByName(name) == null) {
                    System.out.println("Subreddit not found!");
                    createPost(user);
                }else {
                    if (subreddit.getCreationType().equals("public")) {
                        System.out.println("you have access to " + subreddit.getName());
                        completePostInfo(user, subreddit);
                    } else {
                        System.out.println("You don't have access to create post.");
                        userMenu();
                    }
                    // request to creator to grant you as admin
                }
            }
            case 2 -> {
                scanner.nextLine();
                viewCreatedSubreddit(user);
                if (user.getCreatedSubreddits().isEmpty()){
                    System.out.println("No subreddits has been created yet.");
                    createPost(user);
                }
                System.out.println("Enter subreddit's name: ");
                String name = scanner.nextLine();
                if (repository.getSubredditByName(name) == null) {
                    System.out.println("Subreddit not found!");
                    createPost(user);
                }else completePostInfo(user, repository.getSubredditByName(name));
            }
            case 3 -> userMenu();
            default -> {
                System.out.println("Invalid input. Please try again...");
                createPost(user);
            }
        }
    }
    private static void completePostInfo(User user,Subreddit subreddit){
        Scanner scanner = new Scanner(System.in);
        clearScreen();
        System.out.println("\n--------- Complete post info ----------");
        System.out.println("Enter your post title: ");
        String title = scanner.nextLine();

        System.out.println("Enter your post content: ");
        String content = scanner.nextLine();

        Post post = new Post(title,content,user,subreddit);
        user.getCreatedPosts().add(post);
        subreddit.addPost(post);

        System.out.println("Would you like to enable comments for this post? [Y/N]");
        String in = scanner.nextLine();

        if (in.equals("y") || in.equals("Y")) {
            post.setLocked(false);
        }else if (in.equals("n") || in.equals("N")) {
            post.setLocked(true);
        }else {
            System.out.println("Invalid input.Please try again...");
            completePostInfo(user,subreddit);
        }

        System.out.println("Your post created successfully.");
        System.out.println(viewPostsInfo(post));
        createPost(user);
    }
    private static void createSubreddit(User user) {
        Scanner scanner = new Scanner(System.in);
        clearScreen();
        System.out.println("\n--------- Create a Community ----------");
        System.out.println("Build and grow a community about something you care about.\n We'll help you set things up.\n");
        System.out.println("Choose wisely. Once you pick a name, it can't be changed.");
        System.out.println("Enter subreddit's name: ");
        String name = scanner.nextLine();
        if (repository.getSubredditByName(name) != null) {
            System.out.println(name + "is already taken.");
            userMenu();
        } else {
            System.out.println("choose creation type, Please Enter the name: \nPublic : Anyone can view, post, and comment to this community.");
            System.out.println("Restricted : Anyone can view, but only approved users can contribute");
            String type = scanner.nextLine();
            Subreddit subreddit = new Subreddit(name, user, type.toLowerCase());
            // creator and add member set in constructor
//            subreddit.getMembers().add(user);

            System.out.println("You can add a few lines about subreddit: ");
            String description = scanner.nextLine();
            subreddit.setDescription(description);

            System.out.println(subreddit.getName() + " created successfully.");
            System.out.println(viewSubredditsInfo(subreddit));
            userMenu();
            //System.out.println("(1) Edit Description\n(2) Create Post\n(3) Delete post => decrease karma too ");
            //
        }
    }

    private static void viewProfile(User user) {
        Scanner scanner = new Scanner(System.in);
        clearScreen();
        System.out.println("\n--------- View profile ----------");
        System.out.println("(1) My subscribed subreddits\n" +
                "(2) My created subreddits\n" +
                "(3) My created posts \n" +
                "(4) My saved posts \n" +
                "(5) My timeline posts list\n" +
                "(6) Upvoted\n" +
                "(7) Downvoted\n" +
                "(8) Back\n" +
                "Enter your choice: ");
        switch (scanner.nextInt()) {
            case 1 -> {
                scanner.nextLine();
                clearScreen();
                viewSubscribedSubreddit(user);
                if (!user.getSubscribedSubreddits().isEmpty()) {
                    System.out.println("Would you like to unsubscribe subreddits?[Y/N]");
                    String in = scanner.nextLine();
                    if (in.toLowerCase().equals("y")){
                        System.out.println("\n--------- Subreddit Reaction ----------");
                        System.out.println("Enter subreddit's name :");
                        String name = scanner.nextLine();
                        if(repository.getSubredditByName(name) == null){
                            System.out.println("Subreddit was not found.");
                        }else{
                            user.getSubscribedSubreddits().remove(repository.getSubredditByName(name));
                            System.out.println("Subreddit was unsubscribed.");
                        }
                    }else if (in.toLowerCase().equals("n")){
                    }else {
                        System.out.println("Invalid input.Please try again...");
                    }
                }
                viewProfile(user);
            }
            case 2 -> {
                scanner.nextLine();
                clearScreen();
                viewCreatedSubreddit(user);
                if (!user.getCreatedSubreddits().isEmpty()) {
                    System.out.println("Would you like to delete your subreddits?[Y/N]");
                    String in = scanner.nextLine();
                    if (in.toLowerCase().equals("y")){
                        System.out.println("\n--------- Subreddit Reaction ----------");
                        System.out.println("Enter subreddit's name :");
                        String name = scanner.nextLine();
                        if(repository.getSubredditByName(name) == null){
                            System.out.println("Subreddit was not found.");
                            }else{
                                user.getCreatedSubreddits().remove(repository.getSubredditByName(name));
                                System.out.println("Subreddit was deleted.");
                            }
                        } else if (in.toLowerCase().equals("n")){
                    }else System.out.println("Invalid input.Please try again...");
                }
                viewProfile(user);
            }
            case 3 -> {
                scanner.nextLine();
                clearScreen();
                viewCreatedPosts(user);
                if (!user.getCreatedPosts().isEmpty()) {
                    System.out.println("Would you like to react the post?[Y/N] ");
                    String in = scanner.nextLine();
                    if (in.toLowerCase().equals("y")){
                        System.out.println("\n--------- Post Reaction ----------");
                        System.out.println("Enter post's title to react : ");
                        String title = scanner.nextLine();
                        if (repository.getPostsByTitle(title) == null) {
                            System.out.println("Post was not found.");
                        } else {
                            Post post = repository.getPostsByTitle(title);
                            System.out.println("(1) Upvote\n" +
                                    "(2) Downvote\n" +
                                    "(3) Delete\n" +
                                    "(4) Leave comments\n" +
                                    "(5) Save post\n"+
                                    "(6) Back\n"+
                                    "Enter your choice: ");
                            int input = scanner.nextInt();
                            switch (input) {
                                case 1 -> upvotedPost(post, user);
                                case 2 -> downvotedPost(post, user);
                                case 3 -> {
                                    user.getCreatedPosts().remove(post);
                                    System.out.println("Post was deleted.");
                                }
                                case 4 -> {
                                    if (post.isLocked())
                                        System.out.println("Leaving a comment for this post is locked.");
                                    else
                                        leaveComment(user, post);
                                }
                                case 5 -> savedPost(post, user);
                                case 6 -> viewProfile(user);
                                default -> {
                                    System.out.println("Invalid input.");
                                    viewProfile(user);
                                }
                            }
                        }
                    } else if (in.toLowerCase().equals("n")){
                        viewProfile(user);
                    }else System.out.println("Invalid input.Please try again...");
                }
                viewProfile(user);
            }
            ///same method
            case 4 -> {
                scanner.nextLine();
                clearScreen();
                viewSavedPosts(user);
                if (!user.getSavedPosts().isEmpty()) {
                    System.out.println("Would you like to react the post?[Y/N] ");
                    String in = scanner.nextLine();
                    if (in.toLowerCase().equals("y")){
                        System.out.println("\n--------- Post Reaction ----------");
                        System.out.println("Enter post's title to react : ");
                        String title = scanner.nextLine();
                        if (repository.getPostsByTitle(title) == null) {
                            System.out.println("Post was not found.");
                        } else {
                            Post post = repository.getPostsByTitle(title);
                            System.out.println("(1) Upvote\n" +
                                    "(2) Downvote\n" +
                                    "(3) Unsaved\n" +
                                    "(4) Leave comments\n" +
                                    "(5) Back\n"+
                                    "Enter your choice: ");
                            int input = scanner.nextInt();
                            switch (input) {
                                case 1 -> upvotedPost(post, user);
                                case 2 -> downvotedPost(post, user);
                                case 3 -> unsavedPost(post, user);
                                case 4 -> {
                                    if (post.isLocked())
                                        System.out.println("Leaving a comment for this post is locked.");
                                    else
                                        leaveComment(user, post);
                                }
                                case 5 -> viewProfile(user);
                                default -> {
                                    System.out.println("Invalid input.");
                                    viewProfile(user);
                                }
                            }
                        }
                    } else if (in.toLowerCase().equals("n")){
                    }else System.out.println("Invalid input.Please try again...");
                }
                viewProfile(user);
            }
            ///same methods
            case 5 -> {
                scanner.nextLine();
                clearScreen();
                viewTimeLinePosts(user);
                String in;
                if (!user.getTimelinePosts().isEmpty()) {
                    System.out.println("Would you like to react the post?[Y/N] ");
                    in = scanner.nextLine();
                    String title;
                    if (in.toLowerCase().equals("y")) {
                        System.out.println("\n--------- Post Reaction ----------");
                        System.out.println("Enter post's title to react : ");
                        title = scanner.nextLine();
                        if (searchPost(user.getTimelinePosts(), title) == null){
                            System.out.println("Post was not found.");
                        }else{
                            Post post = searchPost(user.getTimelinePosts(), title);
                            System.out.println("(1) Upvote\n" +
                                    "(2) Downvote\n" +
                                    "(3) Leave comments\n" +
                                    "(4) Save post\n"+
                                    "(5) Back\n" +
                                    "Enter your choice: ");
                            int input = scanner.nextInt();
                            switch (input) {
                                case 1 -> upvotedPost(post, user);
                                case 2 -> downvotedPost(post, user);
                                case 3 -> {
                                    if (post.isLocked())
                                        System.out.println("Leaving a comment for this post is locked.");
                                    else
                                        leaveComment(user, post);
                                }
                                case 4 -> savedPost(post, user);
                                case 5 -> viewProfile(user);
                                default -> {
                                    System.out.println("Invalid input.");
                                    viewProfile(user);
                                }
                            }
                        }
                    }else if (in.toLowerCase().equals("n")) {
                    }else System.out.println("Invalid input.Please try again...");
                }
                viewProfile(user);
            }
                //actions
            case 6 -> {
                scanner.nextLine();
                System.out.println("\n------- Upvoted --------\n");
                System.out.println("(1) View upvoted comments\n(2) View upvoted posts\n(3) Back");
                int ans = scanner.nextInt();
                switch (ans) {
                    case 1: {
                        System.out.println("\n------- Upvoted Comments --------\n");
                        viewUpvotedComments(user);
                        String in;
                        if (!user.getUpVotedComments().isEmpty()) {
                            System.out.println("Would you like to react the comment?[Y/N] ");
                            in = scanner.nextLine();
                            String author;
                            if (in.toLowerCase().equals("y")) {
                                System.out.println("\n--------- Comment Reaction ----------");
                                System.out.println("Enter comment's author to react : ");
                                author = scanner.nextLine();
                                if (searchComment(user.getUpVotedComments(), author) == null) {
                                    System.out.println("Comment was not found.");
                                } else {
                                    Comment comment = searchComment(user.getUpVotedComments(), author);
                                    System.out.println("""
                                            (1) Upvote
                                            (2) Downvote
                                            (3) Back
                                            Enter your choice:\s""");
                                    int input = scanner.nextInt();
                                    switch (input) {
                                        case 1 -> upvotedComment(comment, user);
                                        case 2 -> downvotedComment(comment, user);
                                        case 3 -> viewProfile(user);
                                        default -> {
                                            System.out.println("Invalid input.");
                                            viewProfile(user);
                                        }
                                    }
                                }
                            } else if (in.toLowerCase().equals("n")) {
                            } else  System.out.println("Invalid input.Please try again...");
                        }
                        viewProfile(user);
                    }
                    //--------------------------------------------------------------
                    case 2: {
                        System.out.println("\n------- Upvoted Posts --------\n");
                        viewUpvotedPosts(user);
                        String in;
                        if (!user.getUpVotedPosts().isEmpty()) {
                            System.out.println("Would you like to react the posts ?[Y/N] ");
                            in = scanner.nextLine();
                            String title;
                            if (in.toLowerCase().equals("y")) {
                                System.out.println("\n--------- Post Reaction ----------");
                                System.out.println("Enter post's title to react : ");
                                title = scanner.nextLine();
                                if (searchPost(user.getUpVotedPosts(), title) == null) {
                                    System.out.println("Post was not found.");
                                } else {
                                    Post post = searchPost(user.getUpVotedPosts(), title);
                                    System.out.println("""
                                            (1) Upvote
                                            (2) Downvote
                                            (3) Back
                                            Enter your choice:\s""");
                                    int input = scanner.nextInt();
                                    switch (input) {
                                        case 1 -> upvotedPost(post, user);
                                        case 2 -> downvotedPost(post, user);
                                        case 3 -> viewProfile(user);
                                        default -> {
                                            System.out.println("Invalid input.");
                                            viewProfile(user);
                                        }
                                    }
                                }
                            } else if (in.toLowerCase().equals("n")) {
                            } else System.out.println("Invalid input.Please try again...");
                        }else System.out.println("No post has been upvoted yet");

                        viewProfile(user);
                    }
                    case 3: userMenu();
                    default : {
                        System.out.println("Invalid input.Please try again...");
                        viewProfile(user);
                    }
                }
            }
            case 7 -> {
                scanner.nextLine();
                System.out.println("\n------- Downvoted --------\n");
                System.out.println("(1) View downvoted comments\n(2) View downvoted posts\n(3) Back");
                int ans = scanner.nextInt();
                switch (ans) {
                    case 1: {
                        System.out.println("\n------- Downvoted Comments --------\n");
                        viewDownvotedComments(user);
                        String in;
                        if (!user.getDownVotedComments().isEmpty()) {
                            System.out.println("Would you like to react the comment?[Y/N]");
                            in = scanner.nextLine();
                            String author;
                            if (in.toLowerCase().equals("y")) {
                                System.out.println("\n--------- Comment Reaction ----------");
                                System.out.println("Enter comment's author to react : ");
                                author = scanner.nextLine();
                                if (searchComment(user.getDownVotedComments(), author) == null) {
                                    System.out.println("Comment was not found.");
                                } else {
                                    Comment comment = searchComment(user.getDownVotedComments(), author);
                                    System.out.println("""
                                            (1) Upvote
                                            (2) Downvote
                                            (3) Back
                                            Enter your choice:\s""");
                                    int input = scanner.nextInt();
                                    switch (input) {
                                        case 1 -> upvotedComment(comment, user);
                                        case 2 -> downvotedComment(comment, user);
                                        case 3 -> viewProfile(user);
                                        default -> {
                                            System.out.println("Invalid input.");
                                            viewProfile(user);
                                        }
                                    }
                                }
                            } else if (in.toLowerCase().equals("n")) {
                                viewProfile(user);
                            } else {
                                System.out.println("Invalid input.Please try again...");
                                viewProfile(user);
                            }
                        }else{
                            System.out.println("No comment has benn downvoted yet");
                            viewProfile(user);
                        }
                    }
                    case 2: {
                        System.out.println("\n------- Downvoted Posts --------\n");
                        viewDownvotedPosts(user);
                        String in;
                        if (!user.getDownVotedPosts().isEmpty()) {
                            System.out.println("Would you like to react the posts ?[Y/N]");
                            in = scanner.nextLine();
                            String title;
                            if (in.toLowerCase().equals("y")) {
                                System.out.println("\n--------- Post Reaction ----------");
                                System.out.println("Enter post's title to react : ");
                                title = scanner.nextLine();
                                if (searchPost(user.getDownVotedPosts(), title) == null) {
                                    System.out.println("Post was not found.");
                                } else {
                                    Post post = searchPost(user.getDownVotedPosts(), title);
                                    System.out.println("""
                                            (1) Upvote
                                            (2) Downvote
                                            (3) Back
                                            Enter your choice:\s""");
                                    int input = scanner.nextInt();
                                    switch (input) {
                                        case 1 -> upvotedPost(post, user);
                                        case 2 -> downvotedPost(post, user);
                                        case 3 -> viewProfile(user);
                                        default -> {
                                            System.out.println("Invalid input.");
                                            viewProfile(user);
                                        }
                                    }
                                }
                            } else if (in.toLowerCase().equals("n")) {
                                viewProfile(user);
                            } else {
                                System.out.println("Invalid input.Please try again...");
                                viewProfile(user);
                            }
                        }else{
                            System.out.println("No post has been downvoted yet");
                            viewProfile(user);
                        }
                    }
                    case 3: userMenu();
                    default : {
                        System.out.println("Invalid input.Please try again...");
                        viewProfile(user);
                    }
                }
            }
            case 8 -> userMenu();
            default -> {
                System.out.println("Invalid input.Please try again...");
                viewProfile(user);
                }
             }
        }

    public static void upvotedPost(Post post, User user){
        Scanner scanner = new Scanner(System.in);
        boolean found = false;
        for (Post post1: user.getUpVotedPosts()) {
            if (post1.equals(post)) {
                found = true;
                System.out.println("Already upvoted.");
                System.out.println("Retract your vote? [Y/N]");
                String choice = scanner.nextLine().toLowerCase();
                if (choice.equals("y")) {
                    user.getDownVotedPosts().remove(post1);
                    break;
                } else if (choice.equals("n")) {
                    viewProfile(user);
                    return;
                }
            }
            if(!found) {
                System.out.println("Post upvoted.");
                post.increasePostKarma();
                user.getUpVotedPosts().add(post);
            }
        }
    }
    public static void downvotedPost(Post post, User user){
        Scanner scanner = new Scanner(System.in);
        boolean found = false;

        for (Post post1: user.getDownVotedPosts()) {
            if (post1.equals(post)) {
                found = true;
                System.out.println("Already downvoted.");
                System.out.println("Retract your vote? [Y/N]");
                String choice = scanner.nextLine().toLowerCase();
                if (choice.equals("y")) {
                    user.getDownVotedPosts().remove(post1);
                    break;
                } else if (choice.equals("n")) {
                    viewProfile(user);
                    return;
                }
            }
        }

        if (!found) {
            System.out.println("Post downvoted.");
            post.decreasePostKarma();
            user.getDownVotedPosts().add(post);
        }
    }
    public static void unsavedPost(Post post, User user){
        user.getSavedPosts().remove(post);
        System.out.println("Post was unsaved.");
    }
    public static void savedPost(Post post, User user){
        user.getSavedPosts().add(post);
        System.out.println("Post was saved.");
    }
    public static void upvotedComment(Comment comment, User user){
        Scanner scanner = new Scanner(System.in);
        boolean found = false;
        for (Comment comment1: user.getUpVotedComments()) {
            if (comment1.equals(comment)) {
                found = true;
                System.out.println("Already upvoted.");
                System.out.println("Retract your vote? [Y/N]");
                String choice = scanner.nextLine().toLowerCase();
                if (choice.equals("y")) {
                    user.getUpVotedComments().remove(comment1);
                    break;
                } else if (choice.equals("n")) {
                    viewProfile(user);
                    return;
                }
            }
            if(!found) {
                System.out.println("Comment upvoted.");
                comment.increaseCommentKarma();
                user.getUpVotedComments().add(comment);
            }
        }
    }
    public static void downvotedComment(Comment comment, User user){
        Scanner scanner = new Scanner(System.in);
        boolean found = false;
        for (Comment comment1: user.getUpVotedComments()) {
            if (comment1.equals(comment)) {
                found = true;
                System.out.println("Already upvoted.");
                System.out.println("Retract your vote? [Y/N]");
                String choice = scanner.nextLine().toLowerCase();
                if (choice.equals("y")) {
                    user.getDownVotedComments().remove(comment1);
                    break;
                } else if (choice.equals("n")) {
                    viewProfile(user);
                    return;
                }
            }
            if(!found) {
                System.out.println("Comment downvoted.");
                comment.decreaseCommentKarma();
                user.getDownVotedComments().add(comment);
            }
        }
    }

    private static Post searchPost(List<Post> posts, String title){
        for (Post post: posts){
            if (post.getTitle().equals(title))
                return post;
        }
        return null;
    }
    private static Comment searchComment(List<Comment> comments, String title){
        for (Comment comment: comments){
            if (comment.getAuthor().getUsername().equals(title))
                return comment;
        }
        return null;
    }
    private static Subreddit searchSubreddit(List<Subreddit> subreddits, String name){
        for (Subreddit subreddit: subreddits){
            if (subreddit.getName().equals(name))
                return subreddit;
        }
        return null;
    }
    private static void editPersonalInfo(User user) {
        Scanner scanner = new Scanner(System.in);
        clearScreen();
        System.out.println("\n---- Edit personal Info ----");
        System.out.println("    Username : " + user.getUsername());
        System.out.println("    Karma : " + user.getKarma());
        System.out.println("    Bio   : " + user.getBio());
        System.out.println("\n(1) Edit bio\n(2) Change username\n(3) Change password\n(4) Back");
        System.out.println("Enter your choice: ");
        switch (scanner.nextInt()) {
            case 1 -> {
                scanner.nextLine();
                System.out.println("You can add a few lines about yourself.");
                String bio = scanner.nextLine();
                // add limit 70 character
                user.setBio(bio);
                System.out.println("Bio edited successfully.");
                editPersonalInfo(user);
            }
            case 2 -> {
                scanner.nextLine();
                System.out.println("Enter your new username: ");
                String newUsername = scanner.nextLine();
                if (repository.getUserByUsername(newUsername) == null) {
                    System.out.println("Are you sure you want to change your username '" + user.getUsername() + "' to " + newUsername + "? [Y/N]");
                    if (scanner.nextLine().toLowerCase().equals("y")) {
                        user.setUsername(newUsername);
                        System.out.println("Username changed successfully.");
                    }
                } else System.out.println("Username already taken.");
                editPersonalInfo(user);
            }
            case 3 -> {
                scanner.nextLine();
                // add method password is weak
                System.out.println("Enter your old password: ");
                String oldPass = scanner.nextLine();
                if (user.getPassword().equals(oldPass)) {
                    System.out.println("Enter your new password: ");
                    String newPass = scanner.nextLine();
                    System.out.println("Confirm your password: ");
                    String confirmPass = scanner.nextLine();
                    if (newPass.equals(confirmPass)) {
                        System.out.println("Your password changed successfully.");
                    } else System.out.println("Passwords do not match.");
                } else System.out.println("Wrong password.");
                editPersonalInfo(user);
            }
            case 4 -> userMenu();
            default -> {
                System.out.println("Invalid input.Please try again...");
                editPersonalInfo(user);
            }
        }
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}