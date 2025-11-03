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

            // You can add other manual paths similarly (deliveries CRUD) if desired.
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
