package com.example.deliveryoptimizer.controller;

import com.example.deliveryoptimizer.entity.Vehicle;
import com.example.deliveryoptimizer.service.VehicleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * XML-configured controller exposing GET /api/vehicles/by-type?type=...
 */
public class VehicleController implements Controller {

    private final VehicleService vehicleService;
    private final ObjectMapper mapper = new ObjectMapper();

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/json;charset=UTF-8");

        String method = request.getMethod();
        String path = request.getRequestURI();

        try {
            if ("GET".equalsIgnoreCase(method) && "/api/vehicles/by-type".equals(path)) {
                String type = request.getParameter("type");
                if (type == null || type.trim().isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    mapper.writeValue(response.getOutputStream(), java.util.Collections.singletonMap("error", "missing required parameter 'type'"));
                    return null;
                }
                List<Vehicle> result = vehicleService.getVehiclesByType(type.trim());
                mapper.writeValue(response.getOutputStream(), result);
                return null;
            }

            response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            mapper.writeValue(response.getOutputStream(), java.util.Collections.singletonMap("error", "method not allowed"));
            return null;
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            mapper.writeValue(response.getOutputStream(), java.util.Collections.singletonMap("error", "server error: " + ex.getMessage()));
            return null;
        }
    }
}
