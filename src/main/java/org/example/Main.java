package org.example;

import org.example.model.Post;
import org.example.model.Subreddit;
import org.example.model.User;

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
                    System.out.println(user.getUsername() + "is already logged in.");
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
        System.out.println("(6) Leave comment");
        System.out.println("(7) Search");
        System.out.println("(8) Logout");
        System.out.println("Enter your choice: ");
        switch (scanner.nextInt()) {
            case 1 -> editPersonalInfo(user);
            case 2 -> viewProfile(user);
            case 3 -> joinSubreddit(user);
            case 4 -> createSubreddit(user);
            case 5 -> createPost(user);
            case 6 -> leaveComment(user);
            case 7 -> search();
            case 8 -> logout(user);
            default -> {
                System.out.println("Invalid input. Please try again...");
                userMenu();
            }
        }
    }

    private static void joinSubreddit(User user) {
        System.out.println();
    }

    private static void logout(User user) {
        Scanner scanner = new Scanner(System.in);
        clearScreen();
        System.out.println("\n---- Log Out ----");
        System.out.println("Are you sure you want to log out?[Y/N]");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            user.setLoggedIn(false);
            repository.setCurrentUser(null);
            System.out.println("Logged out successfully.\nThanks for using the app ^^ .Come back again...");
            runMenu();
        } else {
            System.out.println("Invalid input. Please try again...");
            userMenu();
        }
    }

    private static void search() {

    }

    private static void leaveComment(User user) {
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
                if (user.getSubscribedSubreddits().isEmpty()) createPost(user);
                System.out.println("Enter subreddits name: ");
                String name = scanner.nextLine();
                Subreddit subreddit = repository.getSubredditByName(name);
                if (subreddit.getCreationType().equals("public")) {
                    System.out.println("you have access to " + subreddit.getName());
                    completePostInfo(user, subreddit);
                } else System.out.println("You don't have access to create post.");
            }
            case 2 -> {
                scanner.nextLine();
                viewCreatedSubreddit(user);
                if (user.getCreatedSubreddits().isEmpty()) createPost(user);
                System.out.println("Enter subreddit's name: ");
                String name = scanner.nextLine();
                completePostInfo(user, repository.getSubredditByName(name));
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
        System.out.println("Enter your post title: ");
        String title = scanner.nextLine();

        System.out.println("Enter your post content: ");
        String content = scanner.nextLine();

        Post post = new Post(title,content,user,subreddit);
        user.getCreatedPosts().add(post);
        subreddit.addPost(post);

        System.out.println("Would you like to enable comments for this post? [Y/N]");
        String in = scanner.nextLine();
//
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

//    private static void reactPost(Post post, User user, Subreddit subreddit){
//        Scanner scanner = new Scanner(System.in);
//        // upvote or down vote post
//        System.out.println("Would you like to upvote or downvote this post? [U/D]" );
//        if (scanner.nextLine().equalsIgnoreCase("u"))
//            if (repository.upVotedPost(post,user)) System.out.println("You've already upvoted on this post");
//            else post.increasePostKarma();
//        else if (scanner.nextLine().equalsIgnoreCase("d"))
//            if (repository.downVotedPost(post,user)) System.out.println("You've already downvoted on this post");
//            else post.decreasePostKarma();
//        else System.out.println("Invalid input.Please try again..."); completePostInfo(user,subreddit);
//        // save or unsaved post
//
//    }
//    }

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
            createSubreddit(user);
        } else {
            System.out.println("choose creation type, Please Enter the name: \nPublic : Anyone can view, post, and comment to this community.");
            System.out.println("Restricted : Anyone can view, but only approved users can contribute");
            String type = scanner.nextLine();
            Subreddit subreddit = new Subreddit(name, user, type.toLowerCase());
            subreddit.getMembers().add(user);
            user.getCreatedSubreddits().add(subreddit);
            System.out.println(subreddit.getName() + " created successfully.");
            System.out.println(viewSubredditsInfo(subreddit));
            userMenu();
            //System.out.println("(1) Edit Description\n(2) Create Post");
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
                viewSubscribedSubreddit(user);
                System.out.println("Do you want to unsubscribe subreddit? [Y/N]");
                viewProfile(user);
            }
            case 2 -> {
                scanner.nextLine();
                viewCreatedSubreddit(user);
                viewProfile(user);
            }
            case 3 -> {
                viewCreatedPosts(user);
                if (!user.getCreatedPosts().isEmpty()) {
                    System.out.println("Do you want to see comments? \n");
                }
                viewProfile(user);
            }
            case 8 -> userMenu();
            default -> {
                System.out.println("Invalid input.Please try again...");
                viewProfile(user);
            }
        }


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