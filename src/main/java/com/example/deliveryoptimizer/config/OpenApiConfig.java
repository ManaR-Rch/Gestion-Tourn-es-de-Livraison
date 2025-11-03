package com.example.deliveryoptimizer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/**
 * Simple Java configuration that exposes an OpenAPI bean and a GroupedOpenApi.
 * We keep it minimal so it can be imported from the XML-focused project.
 * Explanations:
 * - @Configuration: makes this class provide Spring beans when imported.
 * - openAPI(): defines basic API metadata (title, version, description).
 * - groupedOpenApi(): helps scan controller packages and expose endpoints in the UI.
 */
@Configuration
public class OpenApiConfig {

    // Basic OpenAPI metadata shown on the Swagger UI page
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Delivery Optimizer API")
                        .version("v1")
                        .description("API documentation for the Delivery Optimizer project (XML-configured Spring)."));
    }

    /**
     * Optional: when controllers are wired via XML (SimpleUrlHandlerMapping) and
     * not annotated with @RequestMapping, automatic scanning may not pick them up.
     * This customiser adds a minimal description for key endpoints so they appear
     * in the Swagger UI regardless of automatic detection.
     */
    @Bean
    public OpenApiCustomiser manualPaths() {
    return openApi -> {
        // POST /api/tours/optimize
        PathItem optimize = new PathItem();
        Operation postOpt = new Operation()
            .summary("Optimize deliveries into a tour")
            .description("Compute an optimized tour for given delivery IDs and warehouse.");
        ApiResponses responses = new ApiResponses();
        responses.addApiResponse("200", new ApiResponse().description("Ordered list of deliveries (200)"));
        postOpt.responses(responses);
        // very small requestBody description (no schema refs to keep it simple)
        RequestBody rb = new RequestBody()
            .description("{ \"warehouseId\": 1, \"deliveryIds\": [1,2], \"optimizer\": \"nearest\" }")
            .content(new Content().addMediaType("application/json", new MediaType().schema(new Schema<>().type("object"))));
        postOpt.requestBody(rb);
        optimize.post(postOpt);
        openApi.path("/api/tours/optimize", optimize);

        // --- Manual documentation for /api/deliveries ---
        // /api/deliveries (GET)
        PathItem deliveriesRoot = new PathItem();
        Operation getAll = new Operation()
            .summary("List all deliveries")
            .description("Returns all deliveries stored in the system.");
        ApiResponses getAllResponses = new ApiResponses();
        getAllResponses.addApiResponse("200", new ApiResponse().description("Array of Delivery"));
        getAll.responses(getAllResponses);
        deliveriesRoot.get(getAll);

        // /api/deliveries (POST)
        Operation postDelivery = new Operation()
            .summary("Create a delivery")
            .description("Create a new delivery. Provide delivery JSON in request body.");
        ApiResponses postResponses = new ApiResponses();
        postResponses.addApiResponse("201", new ApiResponse().description("Delivery created"));
        postDelivery.responses(postResponses);
        RequestBody postBody = new RequestBody()
            .description("Delivery JSON: { \"latitude\": 48.8, \"longitude\": 2.3, \"weight\": 1.0, \"volume\": 0.1, \"status\": \"PENDING\" }")
            .content(new Content().addMediaType("application/json", new MediaType().schema(new Schema<>().type("object"))));
        postDelivery.requestBody(postBody);
        deliveriesRoot.post(postDelivery);

        openApi.path("/api/deliveries", deliveriesRoot);

        // /api/deliveries/{id} (PUT, DELETE)
        PathItem deliveriesWithId = new PathItem();
        Parameter idParam = new Parameter()
            .name("id")
            .in("path")
            .required(true)
            .description("Delivery id");

        // PUT
        Operation putDelivery = new Operation()
            .summary("Update a delivery")
            .description("Update an existing delivery by id");
        putDelivery.addParametersItem(idParam);
        ApiResponses putResponses = new ApiResponses();
        putResponses.addApiResponse("200", new ApiResponse().description("Delivery updated"));
        putDelivery.responses(putResponses);
        RequestBody putBody = new RequestBody()
            .description("Delivery JSON to update the resource")
            .content(new Content().addMediaType("application/json", new MediaType().schema(new Schema<>().type("object"))));
        putDelivery.requestBody(putBody);
        deliveriesWithId.put(putDelivery);

        // DELETE
        Operation deleteDelivery = new Operation()
            .summary("Delete a delivery")
            .description("Deletes a delivery by id");
        deleteDelivery.addParametersItem(idParam);
        ApiResponses delResponses = new ApiResponses();
        delResponses.addApiResponse("204", new ApiResponse().description("No content - deleted"));
        deleteDelivery.responses(delResponses);
        deliveriesWithId.delete(deleteDelivery);

        openApi.path("/api/deliveries/{id}", deliveriesWithId);

        // --- Manual documentation for /api/warehouses ---
        PathItem warehousesRoot = new PathItem();
        // GET /api/warehouses
        Operation getWarehouses = new Operation()
            .summary("List all warehouses")
            .description("Returns all warehouses.");
        ApiResponses getWareResponses = new ApiResponses();
        getWareResponses.addApiResponse("200", new ApiResponse().description("Array of Warehouse"));
        getWarehouses.responses(getWareResponses);
        warehousesRoot.get(getWarehouses);

        // POST /api/warehouses
        Operation postWarehouse = new Operation()
            .summary("Create a warehouse")
            .description("Create a new warehouse. Provide JSON in request body.");
        ApiResponses postWareResponses = new ApiResponses();
        postWareResponses.addApiResponse("201", new ApiResponse().description("Warehouse created"));
        postWarehouse.responses(postWareResponses);
        RequestBody postWareBody = new RequestBody()
            .description("Warehouse JSON: { \"name\": \"Main\", \"latitude\": 48.8, \"longitude\": 2.3 }")
            .content(new Content().addMediaType("application/json", new MediaType().schema(new Schema<>().type("object"))));
        postWarehouse.requestBody(postWareBody);
        warehousesRoot.post(postWarehouse);

        openApi.path("/api/warehouses", warehousesRoot);

        // You can extend manualPaths() with additional schema/component definitions if desired.
    };
    }

    // Instructs springdoc to include controllers in the specified base package.
    // If your controllers live in a different package, change the packagesToScan value.
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("delivery-optimizer")
                .packagesToScan("com.example.deliveryoptimizer.controller")
                .build();
    }
}
