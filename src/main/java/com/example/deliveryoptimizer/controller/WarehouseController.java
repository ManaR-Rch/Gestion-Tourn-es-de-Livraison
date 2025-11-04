package com.example.deliveryoptimizer.controller;

import com.example.deliveryoptimizer.entity.Warehouse;
import com.example.deliveryoptimizer.repository.WarehouseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Simple WarehouseController (XML-configured):
 * - GET /api/warehouses -> list all warehouses
 * - POST /api/warehouses -> create a warehouse
 *
 * No @Autowired: repository injected via constructor and bean declared in XML.
 */
public class WarehouseController implements Controller {

    private final WarehouseRepository warehouseRepository;
    private final ObjectMapper mapper = new ObjectMapper();

    public WarehouseController(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        String method = request.getMethod();
        String path = request.getRequestURI();

        try {
            if ("GET".equalsIgnoreCase(method) && path.equals("/api/warehouses")) {
                List<Warehouse> all = warehouseRepository.findAll();
                mapper.writeValue(response.getOutputStream(), all);
                return null;
            }

            if ("POST".equalsIgnoreCase(method) && path.equals("/api/warehouses")) {
                Warehouse w = mapper.readValue(request.getInputStream(), Warehouse.class);
                Warehouse saved = warehouseRepository.save(w);
                response.setStatus(HttpServletResponse.SC_CREATED);
                mapper.writeValue(response.getOutputStream(), saved);
                return null;
            }

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
}
