
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.transaction.support.TransactionTemplate;
import com.example.demo.model.User;

/**
 * Simple repository implementation that uses a shared EntityManager proxy
 * and TransactionTemplate (both configured in applicationContext.xml).
 */
public class UserRepositoryImpl {
    private final EntityManager entityManager;
    private final TransactionTemplate txTemplate;

    public UserRepositoryImpl(EntityManager entityManager, TransactionTemplate txTemplate) {
        this.entityManager = entityManager;
        this.txTemplate = txTemplate;
    }

    /**
     * Retourne tous les utilisateurs.
     * Exécute la requête dans une transaction via TransactionTemplate.
     */
    public List<User> findAll() {
        return txTemplate.execute(status -> {
            return entityManager.createQuery("from User", User.class).getResultList();
        });
    }

    /**
     * Sauvegarde (persist ou merge) un utilisateur dans une transaction.
     */
    public User save(User user) {
        return txTemplate.execute(status -> {
            if (user.getId() == null) {
                entityManager.persist(user);
                return user;
            } else {
                return entityManager.merge(user);
            }
        });
    }
}
