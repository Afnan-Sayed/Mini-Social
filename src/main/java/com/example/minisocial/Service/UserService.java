//package com.example.minisocial.Service;
//
//import com.example.minisocial.Model.FriendRequest;
//import com.example.minisocial.Model.User;
//
//import java.util.ArrayList;
//import java.util.logging.Logger;
//import jakarta.ejb.Stateless;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jakarta.transaction.Transactional;
//import java.util.List;
//import java.util.Optional;
//import org.hibernate.Hibernate;
//
//@Stateless
//public class UserService {
//
//    @PersistenceContext(unitName = "myPersistenceUnit")  // Inject EntityManager
//    private EntityManager em;
//
//    // Create a new user
//    @Transactional
//    public User createUser(User user) {
//        em.persist(user);
//        return user;
//    }
//
//    // Find a user by ID
//    public User findUserById(Long id) {
//        return em.find(User.class, id);
//    }
//
//    // Find a user by email
//    public Optional<User> findUserByEmail(String email) {
//        try {
//            return Optional.of(em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
//                    .setParameter("email", email)
//                    .getSingleResult());
//        } catch (Exception e) {
//            return Optional.empty();
//        }
//    }
//
//    // Search users by name or email
////    public List<User> searchUsers(String searchTerm) {
////        return em.createQuery("SELECT u FROM User u WHERE u.name LIKE :searchTerm OR u.email LIKE :searchTerm", User.class)
////                .setParameter("searchTerm", "%" + searchTerm + "%")
////                .getResultList();
////    }
//
//
//
//    // Add a logger instance
//    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());
//
//    @Transactional
//    public void sendFriendRequest(User sender, User receiver) {
//        try {
//            // Initialize the friends collection to make sure it is fully loaded
//            Hibernate.initialize(sender.getFriends());
//            Hibernate.initialize(receiver.getFriends());
//
//            // Check if the users are already friends
//            if (sender.getFriends().contains(receiver)) {
//                throw new IllegalStateException("You are already friends with this user.");
//            }
//
//            // Check if there is an existing accepted request between the two users
//            List<FriendRequest> existingRequests = em.createQuery(
//                            "SELECT fr FROM FriendRequest fr WHERE (fr.sender = :sender AND fr.receiver = :receiver OR fr.sender = :receiver AND fr.receiver = :sender) AND fr.status = 'Accepted'",
//                            FriendRequest.class)
//                    .setParameter("sender", sender)
//                    .setParameter("receiver", receiver)
//                    .getResultList();
//
//            if (!existingRequests.isEmpty()) {
//                throw new IllegalStateException("You are already friends with this user.");
//            }
//
//            // Check if there is a pending request already between them
//            List<FriendRequest> pendingRequests = em.createQuery(
//                            "SELECT fr FROM FriendRequest fr WHERE (fr.sender = :sender AND fr.receiver = :receiver OR fr.sender = :receiver AND fr.receiver = :sender) AND fr.status = 'Pending'",
//                            FriendRequest.class)
//                    .setParameter("sender", sender)
//                    .setParameter("receiver", receiver)
//                    .getResultList();
//
//            if (!pendingRequests.isEmpty()) {
//                throw new IllegalStateException("A pending friend request already exists between you and this user.");
//            }
//
//            // Send the friend request if not already friends and no pending request exists
//            // Send the friend request if not already friends and no pending request exists
//            FriendRequest request = new FriendRequest();
//            request.setSender(sender);
//            request.setReceiver(receiver);
//            request.setStatus("Pending");
//
//            em.persist(request);  // Save the request
//            LOGGER.info("Friend request sent from user " + sender.getId() + " to user " + receiver.getId());  // Log the action
//
//        } catch (Exception e) {
//            // Log the exception for debugging purposes
//            LOGGER.severe("Error in sendFriendRequest: " + e.getMessage());
//            e.printStackTrace(); // Print stack trace to logs
//            throw e; // Re-throw the exception to be caught in the controller
//        }
//    }
//
//    // Accept friend request
//    @Transactional
//    public void acceptFriendRequest(FriendRequest request) {
//        request.setStatus("Accepted");
//        em.merge(request);
//
//        // Initialize friends to prevent lazy loading issues
//        Hibernate.initialize(request.getSender().getFriends());
//        Hibernate.initialize(request.getReceiver().getFriends());
//
//        // Add friends to each other's friend list
//        request.getSender().getFriends().add(request.getReceiver());
//        request.getReceiver().getFriends().add(request.getSender());
//
//        em.merge(request.getSender());
//        em.merge(request.getReceiver());
//    }
//
//
//    @Transactional
//    public void rejectFriendRequest(FriendRequest request) {
//        // Set the status of the request to "Rejected"
//        request.setStatus("Rejected");
//
//        // Merge the rejected request into the database
//        em.merge(request);
//
//        // Optionally, delete the friend request from the database (if you want to remove it)
//        // em.remove(request);
//    }
//
//
//    // Get pending friend requests
//    public List<FriendRequest> getPendingRequests(User user) {
//        return em.createQuery(
//                        "SELECT fr FROM FriendRequest fr " +
//                                "LEFT JOIN FETCH fr.sender " +   // Eager fetch sender
//                                "LEFT JOIN FETCH fr.receiver " +  // Eager fetch receiver
//                                "WHERE fr.receiver = :user AND fr.status = :status",
//                        FriendRequest.class)
//                .setParameter("user", user)
//                .setParameter("status", "Pending")
//                .getResultList();
//    }
//
//
//
//    // Get all friends of a user
//    public List<User> getFriends(User user) {
//        // Initialize friends before accessing it
//        Hibernate.initialize(user.getFriends());
//        return user.getFriends();
//    }
//
//    // Find a friend request by ID
//    public FriendRequest findFriendRequestById(Long requestId) {
//        return em.find(FriendRequest.class, requestId);
//    }
//    public List<User> getAllFriends(User user) {
//        Hibernate.initialize(user.getFriends());
//        return user.getFriends(); // Return the list of friends of the user
//    }
//
//    @Transactional
//    public List<Object[]> getPendingFriendRequests(User user) {
//        // Query to get the sender's name and request ID for pending friend requests
//        return em.createQuery(
//                        "SELECT fr.sender.name, fr.id " +
//                                "FROM FriendRequest fr " +
//                                "WHERE fr.receiver = :user AND fr.status = 'Pending'",
//                        Object[].class)
//                .setParameter("user", user)
//
//                .getResultList();
//    }
//
//    public List<User> searchUsers(String searchTerm) {
//        // Search for users whose name or email matches the searchTerm
//        return em.createQuery("SELECT u FROM User u WHERE u.name LIKE :searchTerm OR u.email LIKE :searchTerm", User.class)
//                .setParameter("searchTerm", "%" + searchTerm + "%")
//                .getResultList();
//    }
////
////    public List<User> suggestFriends(User currentUser) {
////        List<User> friendsOfCurrentUser = currentUser.getFriends();  // Get current user's friends
////
////        List<User> suggestedFriends = new ArrayList<>();
////
////        // Iterate over all friends of the current user
////        for (User friend : friendsOfCurrentUser) {
////            // Get each friend's friends
////            List<User> friendsOfFriend = friend.getFriends();
////
////            // Check for mutual friends and add suggestions
////            for (User potentialFriend : friendsOfFriend) {
////                // Ensure we're not suggesting the current user or existing friends
////                if (!currentUser.equals(potentialFriend) && !currentUser.getFriends().contains(potentialFriend)) {
////                    // Add to suggestions if not already present
////                    if (!suggestedFriends.contains(potentialFriend)) {
////                        suggestedFriends.add(potentialFriend);
////                    }
////                }
////            }
////        }
////
////        return suggestedFriends;
////    }
//
//
//    public List<Object[]> suggestFriends(User currentUser) {
//        List<User> friendsOfCurrentUser = currentUser.getFriends();  // Get current user's friends
//
//        List<Object[]> suggestedFriends = new ArrayList<>();  // List to store Object arrays with id and name
//
//        // Iterate over all friends of the current user
//        for (User friend : friendsOfCurrentUser) {
//            // Get each friend's friends
//            List<User> friendsOfFriend = friend.getFriends();
//
//            // Check for mutual friends and add suggestions
//            for (User potentialFriend : friendsOfFriend) {
//                // Ensure we're not suggesting the current user or existing friends
//                if (!currentUser.equals(potentialFriend) && !currentUser.getFriends().contains(potentialFriend)) {
//                    // Add to suggestions if not already present
//                    if (!suggestedFriends.stream().anyMatch(friendArr -> friendArr[0].equals(potentialFriend.getId()))) {
//                        // Add id and name to the suggestions list
//                        suggestedFriends.add(new Object[]{potentialFriend.getId(), potentialFriend.getName()});
//                    }
//                }
//            }
//        }
//
//        return suggestedFriends;
//    }
//
//
//
//}
