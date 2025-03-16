package job.tracking.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Tuple;
import job.tracking.User;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

public class UserService {

    private final String FIND_USER_BY_EMAIL = "SELECT u FROM User u WHERE email = :email";
    private final String GET_ALL_USERS  = "SELECT u.id as id, u.name as name, u.email as email FROM User u";

    private final EntityManager em;

    public UserService(EntityManager em) {
        this.em = em;
    }

    public void addUser(String name, String email, String password) {
        Optional<User> user = findUser(email);
        if(findUser(email).isPresent()) {
            System.out.printf("user with email %s already exists.%n", email);
            return;
        }
        executeTransaction(() -> em.persist(new User(name, email, BCrypt.hashpw(password, BCrypt.gensalt()))));
        System.out.printf("%s is now a registered user.%n", name);
    }

    public UserService changeName(String name, int userId) {
        findUser(userId).ifPresentOrElse(user -> executeTransaction(() -> {
            user.setName(name);
            System.out.printf("Name updated for user ID: %d%n", userId);
        }), () -> System.out.printf("User with ID: %d does not exist.%n", userId));
        return this;
    }

    public UserService changeAvatar(String avatarUrl, int userId) {
        findUser(userId).ifPresentOrElse(user -> executeTransaction(() -> {
            user.setAvatarUrl(avatarUrl);
            System.out.printf("Avatar updated for user ID: %d%n", userId);
        }), () -> System.out.printf("User with ID: %d does not exist.%n", userId));
        return this;
    }

    public void changePassword(String oldPassword, String newPassword, int userId) {
        findUser(userId).ifPresentOrElse(user -> {
            String currentPassword = user.getPassword();
            if(oldPassword.equals(currentPassword) && ! oldPassword.equals(newPassword)) {
                executeTransaction(() -> user.setPassword(newPassword));
            } else if (newPassword.equals(oldPassword)) {
                System.out.println("New Password is same as Old Password.");
            } else {
                System.out.println("Old Password is Wrong. Try again.");
            }
                },
                () -> System.out.printf("User with ID: %d does not exist.%n", userId));

    }

    public void deleteAccount(int userId) {
        findUser(userId).ifPresentOrElse(user -> executeTransaction(() -> {
            em.remove(user);
            System.out.printf("User with ID: %d has been Deleted.%n", userId);
        }), () -> System.out.printf("User with ID: %d does not exist.%n", userId));
    }

    public Optional<User> findUser(String email) {
        try {
            return Optional.of(em.createQuery(FIND_USER_BY_EMAIL, User.class)
                    .setParameter("email", email)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }


    public Optional<User> findUser(int userId) {
        return Optional.ofNullable(em.find(User.class, userId));
    }

    public void printUsers() {
        var users = em.createQuery(GET_ALL_USERS, Tuple.class).getResultList();
        System.out.printf("  %-10s%-20s%s%n", "id", "name", "email");
        users.forEach(user -> System.out.printf("  %-10s%-20s%s%n", user.get("id"),
                user.get("name"), user.get("email")));
    }

    public void executeTransaction(Runnable action) {
        var transaction = em.getTransaction();
        try {
            transaction.begin();
            action.run();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            System.out.println("Transaction failed: " + e.getMessage());
        }
    }
}
