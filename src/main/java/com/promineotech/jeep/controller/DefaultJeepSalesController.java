package com.promineotech.jeep.controller;

import java.util.List;
import org.springframework.web.bind.annotation.RestController;
import com.promineotech.jeep.entity.Jeep;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class DefaultJeepSalesController implements JeepSalesController {

  @Override
  public List<Jeep> fetchJeeps(String model, String trim) {
    log.info("Model={}, trim={}", model, trim);
    return null;
  }

}
