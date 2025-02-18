package org.eclipse.jakarta.config;

import java.util.logging.Logger;

import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.ws.rs.Produces;

public class LoggerConfig {
  @Produces
  public Logger produceLogger(InjectionPoint injectionPoint) {
    return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
  }
}
