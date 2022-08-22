package com.promineotech.jeep.controller;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.jdbc.JdbcTestUtils;
import com.promineotech.jeep.controller.support.CreateOrderTestSupport;
import com.promineotech.jeep.entity.JeepModel;
import com.promineotech.jeep.entity.Order;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(scripts = {"classpath:flyway/migrations/V1.0__Jeep_Schema.sql",
    "classpath:flyway/migrations/V1.1__Jeep_Data.sql"}, config = @SqlConfig(encoding = "utf-8"))
class CreateOrderTest extends CreateOrderTestSupport {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Test
  void testCreateOrderReturnsSuccess201() {
    
    // Given: an order as JSON
    String body = createOrderBody();
    String uri = getBaseUriForOrders();
    int numRowsOrders = JdbcTestUtils.countRowsInTable(jdbcTemplate, "orders");
    int numRowsOptions = JdbcTestUtils.countRowsInTable(jdbcTemplate, "order_options");
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> bodyEntity = new HttpEntity<>(body, headers);

    // When: The order is sent
    ResponseEntity<Order> response =
        getRestTemplate().exchange(uri, HttpMethod.POST, bodyEntity, Order.class);

    // Then: a 201 status is returned
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    // And: the returned order is correct
    assertThat(response.getBody()).isNotNull(); 
    Order order = response.getBody(); 
    assertThat(order.getCustomer().getCustomerId()).isEqualTo("MORISON_LINA"); 
    assertThat(order.getModel().getModelId()).isEqualTo(JeepModel.WRANGLER); 
    assertThat(order.getModel().getTrimLevel()).isEqualTo("Sport Altitude"); 
    assertThat(order.getModel().getNumDoors()).isEqualTo(4); 
    assertThat(order.getColor().getColorId()).isEqualTo("EXT_NACHO"); 
    assertThat(order.getEngine().getEngineId()).isEqualTo("2_0_TURBO"); 
    assertThat(order.getTire().getTireId()).isEqualTo("35_TOYO"); 
    assertThat(order.getOptions()).hasSize(6); 
    assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "orders"))
    .isEqualTo(numRowsOrders + 1);
    assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "order_options"))
    .isEqualTo(numRowsOptions + 6);
  }

}
