package com.example.minisocial.Service.ConnectionManagement;

import com.example.minisocial.Model.PostManagement.Post.Post;
import com.example.minisocial.Model.UserManagement.FriendProfileDTO;
import com.example.minisocial.Model.UserManagement.User;
import com.example.minisocial.Model.ConnectionManagement.FriendRequest;
import com.example.minisocial.Model.UserManagement.UserProfileDTO;
import com.example.minisocial.Service.UserManagement.UserService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.hibernate.Hibernate;
import com.example.minisocial.Model.UserManagement.UserDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class ConnectionService {

    @PersistenceContext(unitName = "myPersistenceUnit")
    private EntityManager em;

    @Inject
    private UserService userService;

    // Send a friend request
    @Transactional
    public void sendFriendRequest(User sender, User receiver) {
        // Check if sender and receiver are already friends
        if (sender.getFriends().contains(receiver)) {
            throw new IllegalStateException("You are already friends with this user.");
        }

        // Check if a pending request exists between them
        List<FriendRequest> pendingRequests = em.createQuery(
                        "SELECT fr FROM FriendRequest fr WHERE (fr.sender = :sender AND fr.receiver = :receiver) OR (fr.sender = :receiver AND fr.receiver = :sender) AND fr.status = 'Pending'",
                        FriendRequest.class)
                .setParameter("sender", sender)
                .setParameter("receiver", receiver)
                .getResultList();

        if (!pendingRequests.isEmpty()) {
            throw new IllegalStateException("A pending friend request already exists between you and this user.");
        }

        // Create and persist the new friend request
        FriendRequest request = new FriendRequest();
        request.setSender(sender);
        request.setReceiver(receiver);
        request.setStatus("Pending");

        em.persist(request);
    }

    // Accept a friend request
    @Transactional
    public void acceptFriendRequest(FriendRequest request) {
        request.setStatus("Accepted");
        em.merge(request);

        // Add each user to the other's friend list
        request.getSender().getFriends().add(request.getReceiver());
        request.getReceiver().getFriends().add(request.getSender());

        em.merge(request.getSender());
        em.merge(request.getReceiver());
    }




    // Reject a friend request
    @Transactional
    public void rejectFriendRequest(FriendRequest request) {
        request.setStatus("Rejected");
        em.merge(request);
    }

    // Get pending friend requests for a user
    @Transactional
    public List<Object[]> getPendingFriendRequests(User user) {
        // Query to get the sender's name and request ID for pending friend requests
        return em.createQuery(
                        "SELECT fr.sender.name, fr.id " +
                                "FROM FriendRequest fr " +
                                "WHERE fr.receiver.id = :userId AND fr.status = 'Pending'",  // Use .id to match the ID
                        Object[].class)
                .setParameter("userId", user.getId())  // Pass user.getId()
                .getResultList();

    }


    public List<Object[]> suggestFriends(User currentUser) {
        List<User> friendsOfCurrentUser = currentUser.getFriends();  // Get current user's friends

        List<Object[]> suggestedFriends = new ArrayList<>();  // List to store Object arrays with id and name

        // Iterate over all friends of the current user
        for (User friend : friendsOfCurrentUser) {
            // Get each friend's friends
            List<User> friendsOfFriend = friend.getFriends();

            // Check for mutual friends and add suggestions
            for (User potentialFriend : friendsOfFriend) {
                // Ensure we're not suggesting the current user or existing friends
                if (!currentUser.equals(potentialFriend) && !currentUser.getFriends().contains(potentialFriend)) {
                    // Add to suggestions if not already present
                    if (!suggestedFriends.stream().anyMatch(friendArr -> friendArr[0].equals(potentialFriend.getId()))) {
                        // Add id and name to the suggestions list
                        suggestedFriends.add(new Object[]{potentialFriend.getId(), potentialFriend.getName()});
                    }
                }
            }
        }

        return suggestedFriends;
    }

        // Find a friend request by ID
    public FriendRequest findFriendRequestById(Long requestId) {
        return em.find(FriendRequest.class, requestId);
    }

    public List<User> getAllFriends(User user) {
        Hibernate.initialize(user.getFriends());
        return user.getFriends(); // Return the list of friends of the user
    }

    @Transactional
    public List<UserDTO> searchUsers(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return new ArrayList<>();
        }

        // Query to find users based on name or email
        List<User> users = em.createQuery("SELECT u FROM User u WHERE u.name LIKE :searchTerm OR u.email LIKE :searchTerm", User.class)
                .setParameter("searchTerm", "%" + searchTerm + "%")
                .getResultList();

        // Map the User entities to UserDTOs
        List<UserDTO> userDTOs = new ArrayList<>();
        for (User user : users) {
            userDTOs.add(new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getBio()));
        }

        return userDTOs;
    }


    @Transactional
    public UserDTO getFriendProfile(Long friendId, User currentUser) {
        // Check if the current user is actually friends with the user they are trying to view
        List<User> friends = currentUser.getFriends();
        User friend = null;

        // Find the friend in the current user's friends list
        for (User u : friends) {
            if (u.getId().equals(friendId)) {
                friend = u;
                break;
            }
        }

        if (friend == null) {
            throw new IllegalArgumentException("This user is not your friend.");
        }

        // Create the UserDTO
        return new UserDTO(friend.getId(), friend.getName(), friend.getEmail(), friend.getBio());
    }

    // Fetch all posts of a friend by friendId
    @Transactional
    public List<Post> getPostsOfFriend(Long friendId) {
        // Fetch the friend user by friendId
        User friend = em.find(User.class, friendId);

        if (friend == null) {
            throw new IllegalArgumentException("Friend not found");
        }

        // Fetch all posts created by this friend
        return em.createQuery("SELECT p FROM Post p WHERE p.author = :author", Post.class)
                .setParameter("author", friend)
                .getResultList();
    }

    @Transactional
    public UserProfileDTO getFriendProfile(Long friendId) {
        // Fetch the friend user by friendId
        User friend = em.find(User.class, friendId);

        if (friend == null) {
            throw new IllegalArgumentException("Friend not found");
        }

        // Return only the necessary details (name, email, bio)
        return new UserProfileDTO(friend.getName(), friend.getEmail(), friend.getBio());
    }



    @Transactional
    public List<String> getAllFriendsNames(Long userId) {
        // Fetch the user by userId
        User user = em.find(User.class, userId);

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        // Get all friends of the user
        List<User> friends = user.getFriends();

        // Extract the names of all friends
        return friends.stream()
                .map(User::getName)  // Only get the name of each friend
                .collect(Collectors.toList());
    }



    @Transactional
    public List<String> getFriendsNames(User currentUser) {
        List<User> friends = currentUser.getFriends(); // Assuming a 'friends' relation in your User entity
        List<String> friendsNames = new ArrayList<>();

        for (User friend : friends) {
            friendsNames.add(friend.getName()); // Add each friend's name to the list
        }

        return friendsNames;
    }

    @Transactional
    public User getUserByEmail(String email) {
        // Attempt to fetch the user by email
        try {
            User user = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();  // This will throw NoResultException if no user is found
            return user;
        } catch (NoResultException e) {
            throw new IllegalArgumentException("User not found with email: " + email);  // Handle case where no user is found
        }
    }







    // Check if two users are friends using native SQL (user_user is not an entity)
    public boolean areFriends(Long userId, Long friendId) {
        String sql = "SELECT COUNT(*) FROM user_user WHERE user_id = ?1 AND friends_id = ?2";
        Number count = (Number) em.createNativeQuery(sql)
                .setParameter(1, userId)
                .setParameter(2, friendId)
                .getSingleResult();
        return count.intValue() > 0;
    }

    // Get all posts from a friend if they are friends
    public List<Post> getFriendPosts(Long requesterId, Long friendId) {
        if (!areFriends(requesterId, friendId)) {
            throw new SecurityException("Access denied: This user is not your friend.");
        }

        String jpql = "SELECT p FROM Post p WHERE p.author.id = :friendId";
        return em.createQuery(jpql, Post.class)
                .setParameter("friendId", friendId)
                .getResultList();
    }

}
