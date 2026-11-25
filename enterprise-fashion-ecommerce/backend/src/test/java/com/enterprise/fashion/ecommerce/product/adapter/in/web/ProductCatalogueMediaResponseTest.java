package com.enterprise.fashion.ecommerce.product.adapter.in.web;

import java.util.List;
import java.util.Map;

import com.enterprise.fashion.ecommerce.product.application.catalogue.ProductCatalogueQueryService;
import com.enterprise.fashion.ecommerce.product.application.catalogue.ProductCatalogueRepresentation;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductCatalogueMediaResponseTest {
    @Test
    void listAndDetailPreserveRequiredFieldsAndOmitOnlyUnassociatedVariantId() throws Exception {
        var representation = new ProductCatalogueRepresentation("product", "Shirt", "Description", "Apparel",
                "1.0.0", List.of(), List.of(new ProductCatalogueRepresentation.Variant("variant", Map.of())),
                List.of(new ProductCatalogueRepresentation.Media("front", "https://example.invalid/front", "Front", null),
                        new ProductCatalogueRepresentation.Media("back", "https://example.invalid/back", "Back", "variant")));
        var queries = mock(ProductCatalogueQueryService.class);
        when(queries.list(20, null)).thenReturn(new ProductCatalogueQueryService.Page(List.of(representation), 20, null));
        when(queries.detail(new ProductId("product"))).thenReturn(new ProductCatalogueQueryService.Found(representation));
        var http = MockMvcBuilders.standaloneSetup(new ProductCatalogueController(queries,
                new ProductCatalogueCursorCodec("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA="))).build();
        for (String path : List.of("/api/v1/products", "/api/v1/products/product")) {
            String root = path.endsWith("/product") ? "$" : "$.items[0]";
            http.perform(get(path)).andExpect(status().isOk())
                    .andExpect(jsonPath(root + ".media[0].variantId").doesNotHaveJsonPath())
                    .andExpect(jsonPath(root + ".media[0].mediaId").value("front"))
                    .andExpect(jsonPath(root + ".media[0].url").value("https://example.invalid/front"))
                    .andExpect(jsonPath(root + ".media[0].alternativeText").value("Front"))
                    .andExpect(jsonPath(root + ".media[1].variantId").value("variant"));
        }
    }
}
