package org.skyweave.service.api.utils.scheduler;

import lombok.RequiredArgsConstructor;
import org.skyweave.service.api.data.TokenRepository;
import org.skyweave.service.api.data.model.Token;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClearTokenScheduler {

  private final TokenRepository tokenRepository;


  @Scheduled(cron = "0 0 0 LW * *")
  private void clearTokens() {
    System.out.println("Clearing tokens");
  }


}
