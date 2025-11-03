package com.example.deliveryoptimizer.controller;

import com.example.deliveryoptimizer.dto.DeliveryDto;
import com.example.deliveryoptimizer.entity.Delivery;
import com.example.deliveryoptimizer.entity.Warehouse;
import com.example.deliveryoptimizer.mapper.DeliveryMapper;
import com.example.deliveryoptimizer.repository.DeliveryRepository;
import com.example.deliveryoptimizer.repository.WarehouseRepository;
import com.example.deliveryoptimizer.service.TourService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controller XML-configured that exposes POST /api/tours/optimize
 * Request body example: { "warehouseId": 1, "deliveryIds": [1,2,3],
 * "optimizer": "CLARKE" }
 */
public class TourController implements Controller {

  private final DeliveryRepository deliveryRepository;
  private final WarehouseRepository warehouseRepository;
  private final TourService tourService;
  private final ObjectMapper mapper = new ObjectMapper();

  public TourController(DeliveryRepository deliveryRepository, WarehouseRepository warehouseRepository,
      TourService tourService) {
    this.deliveryRepository = deliveryRepository;
    this.warehouseRepository = warehouseRepository;
    this.tourService = tourService;
  }

  private static class OptimizeRequest {
    public Long warehouseId;
    public List<Long> deliveryIds = new ArrayList<>();
    public String optimizer;
  }

  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
    if (!"POST".equalsIgnoreCase(request.getMethod())) {
      response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
      return null;
    }

    response.setContentType("application/json;charset=UTF-8");

    try {
      OptimizeRequest req = mapper.readValue(request.getInputStream(), OptimizeRequest.class);

      // load warehouse
      Warehouse warehouse = null;
      if (req.warehouseId != null) {
        Optional<Warehouse> wOpt = warehouseRepository.findById(req.warehouseId);
        if (wOpt.isPresent())
          warehouse = wOpt.get();
      }

      // load deliveries
      List<Delivery> deliveries = new ArrayList<>();
      if (req.deliveryIds != null && !req.deliveryIds.isEmpty()) {
        Iterable<Delivery> it = deliveryRepository.findAllById(req.deliveryIds);
        it.forEach(deliveries::add);
      }

      List<Delivery> optimized = tourService.getOptimizedTour(deliveries, req.optimizer, null);

      // map to DTOs
      List<DeliveryDto> dtos = new ArrayList<>();
      for (Delivery d : optimized)
        dtos.add(DeliveryMapper.toDto(d));

      mapper.writeValue(response.getOutputStream(), dtos);
      return null;

    } catch (IOException ex) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      mapper.writeValue(response.getOutputStream(),
          java.util.Collections.singletonMap("error", "invalid JSON or missing fields"));
      return null;
    }
  }
}
