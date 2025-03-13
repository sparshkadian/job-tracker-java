import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Tuple;

import java.util.Optional;

public class UserService {

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
        executeTransaction(() -> em.persist(new User(name, email, password)));
        System.out.printf("%s is now a registered user.%n", name);
    }

    public void changeName(String name, int userId) {
        findUser(userId).ifPresentOrElse(user -> executeTransaction(() -> {
            user.setName(name);
            System.out.printf("Name updated for user ID: %d%n", userId);
        }), () -> System.out.printf("User with ID: %d does not exist", userId));
    }

    public void changeAvatar(String avatarUrl, int userId) {
        findUser(userId).ifPresentOrElse(user -> executeTransaction(() -> {
            user.setAvatarUrl(avatarUrl);
            System.out.printf("Avatar updated for user ID: %d%n", userId);
        }), () -> System.out.printf("User with ID: %d does not exist", userId));
    }

    public void deleteAccount(int userId) {
        findUser(userId).ifPresentOrElse(user -> executeTransaction(() -> {
            em.remove(user);
            System.out.printf("User with ID: %d has been Deleted.%n", userId);
        }), () -> System.out.printf("User with ID: %d does not exist", userId));
    }

    public Optional<User> findUser(String email) {
        String query = "SELECT u FROM User u WHERE email = :email";
        try {
            return Optional.of(em.createQuery(query, User.class)
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
        String query = "SELECT u.id as id, u.name as name, u.email as email FROM User u";
        var users = em.createQuery(query, Tuple.class).getResultList();

        System.out.printf("%-10s%-20s%s%n", "id", "name", "email");
        users.forEach(user -> System.out.printf("%-10s%-20s%s%n", user.get("id"),
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
