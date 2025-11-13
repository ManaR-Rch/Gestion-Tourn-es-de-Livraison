package com.example.deliveryoptimizer.service.impl;

import com.example.deliveryoptimizer.entity.Delivery;
import com.example.deliveryoptimizer.entity.DeliveryHistory;
import com.example.deliveryoptimizer.entity.Warehouse;
import com.example.deliveryoptimizer.repository.DeliveryHistoryRepository;
import com.example.deliveryoptimizer.service.TourOptimizer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Ollama-based optimizer. Sends delivery history to a local Ollama instance
 * and expects a JSON-encoded ordered list in the response.
 */
@Service
@ConditionalOnProperty(name = "optimizer.type", havingValue = "ollama")
public class OllamaOptimizer implements TourOptimizer {

    private static final Logger log = LoggerFactory.getLogger(OllamaOptimizer.class);

    private final DeliveryHistoryRepository historyRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String ollamaHost;
    private final String model;

    public OllamaOptimizer(DeliveryHistoryRepository historyRepository,
                           @Value("${optimizer.ollama.host:http://localhost:11434}") String ollamaHost,
                           @Value("${optimizer.ollama.model:llama3}") String model) {
        this.historyRepository = historyRepository;
        this.ollamaHost = ollamaHost;
        this.model = model;
    }

    @Override
    public List<Delivery> calculateOptimalTour(List<Delivery> deliveries, Warehouse warehouse) {
        if (deliveries == null || deliveries.isEmpty()) {
            return Collections.emptyList();
        }

        // Gather delivery history data (basic example: all histories)
        List<DeliveryHistory> histories = historyRepository.findAll();

        // Build payload for Ollama
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("model", model);
        payload.put("prompt", "Optimize delivery order based on delivery history delays and distances");
        payload.put("stream", false);

        Map<String, Object> data = new HashMap<>();
        data.put("deliveries", deliveries.stream().map(d -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", d.getId());
            m.put("latitude", d.getLatitude());
            m.put("longitude", d.getLongitude());
            m.put("weight", d.getWeight());
            m.put("volume", d.getVolume());
            return m;
        }).collect(Collectors.toList()));
        data.put("histories", histories.stream().map(h -> {
            Map<String, Object> m = new HashMap<>();
            m.put("customerId", h.getCustomer() != null ? h.getCustomer().getId() : null);
            m.put("tourId", h.getTour() != null ? h.getTour().getId() : null);
            m.put("date", h.getDate());
            m.put("plannedTime", h.getPlannedTime());
            m.put("actualTime", h.getActualTime());
            m.put("delay", h.getDelay());
            m.put("dayOfWeek", h.getDayOfWeek());
            return m;
        }).collect(Collectors.toList()));

        payload.put("data", data);

        try {
            // send HTTP request to Ollama
            String url = ollamaHost;
            if (!url.endsWith("/")) url += "/";
            url += "api/generate";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String body = objectMapper.writeValueAsString(payload);

            // HttpEntity not used because we use java.net.HttpURLConnection below

            // use simple java.net.HttpURLConnection to avoid requiring RestTemplate bean
            java.net.URL endpoint = new java.net.URL(url);
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) endpoint.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            try (java.io.OutputStream os = conn.getOutputStream()) {
                os.write(body.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            }

            int status = conn.getResponseCode();
            java.io.InputStream is = (status >= 200 && status < 300) ? conn.getInputStream() : conn.getErrorStream();
            String responseText;
            try (java.util.Scanner s = new java.util.Scanner(is, java.nio.charset.StandardCharsets.UTF_8.name())) {
                s.useDelimiter("\\A");
                responseText = s.hasNext() ? s.next() : "";
            }

            if (status < 200 || status >= 300) {
                log.warn("Ollama returned non-2xx status {}: {}", status, responseText);
                return deliveries; // fallback
            }

            // Try to parse response: it may be plain text or JSON. Common Ollama responses include a JSON object.
            // Attempt to parse as JSON and extract an ordered list.
            try {
                JsonNode root = objectMapper.readTree(responseText);

                // Common pattern: { "text": "[...]" } or { "result": [...] }
                JsonNode listNode = null;
                if (root.has("result")) listNode = root.get("result");
                else if (root.has("text")) {
                    String t = root.get("text").asText();
                    // try parse text as JSON
                    try {
                        listNode = objectMapper.readTree(t);
                    } catch (Exception ex) {
                        // not JSON, ignore
                    }
                } else if (root.isArray()) {
                    listNode = root;
                }

                if (listNode != null && listNode.isArray()) {
                    List<Long> orderedIds = objectMapper.convertValue(listNode, new TypeReference<List<Long>>() {});
                    // Map ids to deliveries preserving only those present
                    Map<Long, Delivery> byId = deliveries.stream().filter(d -> d.getId() != null).collect(Collectors.toMap(Delivery::getId, d -> d));
                    List<Delivery> result = new ArrayList<>();
                    for (Long id : orderedIds) {
                        Delivery d = byId.get(id);
                        if (d != null) result.add(d);
                    }
                    // append any deliveries not mentioned
                    for (Delivery d : deliveries) if (d.getId() == null || !orderedIds.contains(d.getId())) result.add(d);
                    return result;
                }

                // If we can't parse, fallback to returning original list
                log.warn("Ollama response could not be parsed into ordered list; returning input order");
                return deliveries;
            } catch (Exception ex) {
                log.warn("Failed to parse Ollama response: {}", ex.getMessage());
                return deliveries;
            }

        } catch (Exception e) {
            log.warn("Failed to call Ollama API: {}", e.getMessage());
            return deliveries; // fallback on error
        }
    }
}
