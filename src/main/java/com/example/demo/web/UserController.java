import com.example.demo.model.User;
import com.example.demo.repository.UserRepositoryImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Controller simple qui n'utilise aucune annotation Spring d'injection.
 * Il est déclaré en tant que bean dans applicationContext.xml et mappé
 * via SimpleUrlHandlerMapping.
 */
public class UserController implements Controller {
    private final UserRepositoryImpl userRepository;
    private final ObjectMapper mapper = new ObjectMapper();

    public UserController(UserRepositoryImpl userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Gère GET (liste) et POST (création) sur /api/users.
     * - GET: renvoie la liste des utilisateurs en JSON
     * - POST: attend un JSON User, le sauvegarde et renvoie l'entité créée
     */
    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String method = request.getMethod();
        response.setContentType("application/json;charset=UTF-8");

        if ("GET".equalsIgnoreCase(method)) {
            List<User> users = userRepository.findAll();
            mapper.writeValue(response.getOutputStream(), users);
            return null;
        }

        if ("POST".equalsIgnoreCase(method)) {
            try {
                User u = mapper.readValue(request.getInputStream(), User.class);
                User saved = userRepository.save(u);
                response.setStatus(HttpServletResponse.SC_CREATED);
                mapper.writeValue(response.getOutputStream(), saved);
            } catch (IOException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                mapper.writeValue(response.getOutputStream(), java.util.Collections.singletonMap("error", "invalid JSON"));
            }
            return null;
        }

        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        return null;
    }
}
