package com.shopfloor.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "DB_USER=sfa",
        "DB_PASSWORD=sfa",
        "DB_URL=jdbc:postgresql://localhost:5432/sfaDb",
        "JWT_SECRET_KEY=132f8aa4a79bcb6576634d9e532789e13c81ab05a277d004d97059f619d11b4f",
        "JWT_DURATION_IN_MILLISECONDS=86400000"
})
class ShopFloorAssistanceBackendApplicationTests {

    @Test
    void contextLoads() {
    }

}