package com.example.deliveryoptimizer.controller;

import com.example.deliveryoptimizer.entity.Delivery;
import com.example.deliveryoptimizer.repository.DeliveryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Simple DeliveryController (XML-configured) exposing REST-like endpoints:
 * - GET /api/deliveries -> list all deliveries
 * - POST /api/deliveries -> create a delivery (body: Delivery JSON)
 * - PUT /api/deliveries/{id} -> update an existing delivery
 * - DELETE /api/deliveries/{id} -> delete by id
 *
 * No @Autowired: repositories are injected via constructor and beans are
 * declared
 * in applicationContext.xml.
 */
public class DeliveryController implements Controller {

    private final DeliveryRepository deliveryRepository;
    private final ObjectMapper mapper = new ObjectMapper();

    public DeliveryController(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/json;charset=UTF-8");

        String method = request.getMethod();
        String path = request.getRequestURI();
        // Expect paths like /api/deliveries or /api/deliveries/{id}

        try {
            if ("GET".equalsIgnoreCase(method) && path.equals("/api/deliveries")) {
                List<Delivery> all = deliveryRepository.findAll();
                mapper.writeValue(response.getOutputStream(), all);
                return null;
            }

            if ("POST".equalsIgnoreCase(method) && path.equals("/api/deliveries")) {
                Delivery d = mapper.readValue(request.getInputStream(), Delivery.class);
                Delivery saved = deliveryRepository.save(d);
                response.setStatus(HttpServletResponse.SC_CREATED);
                mapper.writeValue(response.getOutputStream(), saved);
                return null;
            }

            // Operations that require an id in the path
            if (path.startsWith("/api/deliveries/")) {
                String idStr = path.substring("/api/deliveries/".length());
                Long id = tryParseId(idStr);
                if (id == null) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    mapper.writeValue(response.getOutputStream(),
                            java.util.Collections.singletonMap("error", "invalid id"));
                    return null;
                }

                if ("PUT".equalsIgnoreCase(method)) {
                    Optional<Delivery> existing = deliveryRepository.findById(id);
                    if (!existing.isPresent()) {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        mapper.writeValue(response.getOutputStream(),
                                java.util.Collections.singletonMap("error", "not found"));
                        return null;
                    }
                    Delivery upd = mapper.readValue(request.getInputStream(), Delivery.class);
                    Delivery e = existing.get();
                    // simple field-by-field update
                    e.setLatitude(upd.getLatitude());
                    e.setLongitude(upd.getLongitude());
                    e.setWeight(upd.getWeight());
                    e.setVolume(upd.getVolume());
                    e.setStatus(upd.getStatus());
                    Delivery saved = deliveryRepository.save(e);
                    mapper.writeValue(response.getOutputStream(), saved);
                    return null;
                }

                if ("DELETE".equalsIgnoreCase(method)) {
                    deliveryRepository.deleteById(id);
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    return null;
                }
            }

            // If no rule matched
            response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            mapper.writeValue(response.getOutputStream(),
                    java.util.Collections.singletonMap("error", "method not allowed"));
            return null;

        } catch (IOException ex) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(response.getOutputStream(),
                    java.util.Collections.singletonMap("error", "invalid JSON or missing fields"));
            return null;
        }
    }

    private Long tryParseId(String s) {
        try {
            return Long.parseLong(s);
        } catch (Exception e) {
            return null;
        }
    }
}
