package com.example.minisocial.Service.ConnectionManagement;

import com.example.minisocial.Model.UserManagement.User;
import com.example.minisocial.Model.ConnectionManagement.FriendRequest;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import com.example.minisocial.Model.UserManagement.UserDTO;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class ConnectionService {

    @PersistenceContext(unitName = "myPersistenceUnit")
    private EntityManager em;

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

//        public List<User> searchUsers(String searchTerm) {
//        // Search for users whose name or email matches the searchTerm
//        return em.createQuery("SELECT u FROM User u WHERE u.name LIKE :searchTerm OR u.email LIKE :searchTerm", User.class)
//                .setParameter("searchTerm", "%" + searchTerm + "%")
//                .getResultList();
//    }

//@Transactional
//public List<User> searchUsers(String searchTerm) {
//    // Check if the search term is empty
//    if (searchTerm == null || searchTerm.trim().isEmpty()) {
//        return new ArrayList<>(); // Return empty list if no search term
//    }
//
//    // Query to find users based on name or email
//    return em.createQuery("SELECT u FROM User u WHERE u.name LIKE :searchTerm OR u.email LIKE :searchTerm", User.class)
//            .setParameter("searchTerm", "%" + searchTerm + "%")  // Use wildcard for partial matches
//            .getResultList();
//}
//


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
}
