package br.com.pjinsights.application.service;

import br.com.pjinsights.application.dto.request.BalanceRequest;
import br.com.pjinsights.application.dto.response.BalanceResponse;
import br.com.pjinsights.domain.entity.Balance;
import br.com.pjinsights.infrastructure.repository.BalanceRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BalanceService {
    private final BalanceRepository balanceRepository;

    @Transactional
    public List<BalanceResponse> save(List<BalanceRequest> balanceRequest, UUID companyId) {
        return balanceRequest.stream().map(
              request -> {
                  Balance balance = new Balance();
                  BeanUtils.copyProperties(request,balance);
                  balance.setCompanyId(companyId);
                  balance = balanceRepository.save(balance);

                  BalanceResponse balanceResponse = new BalanceResponse();
                  BeanUtils.copyProperties(balance,balanceResponse);

                  return balanceResponse;
              }
        ).toList();
    }

    public List<BalanceResponse> findByCompanyId(UUID companyId) {
        return balanceRepository.findByCompanyId(companyId).stream().map(
                balance -> {
                    BalanceResponse balanceResponse = new BalanceResponse();
                    BeanUtils.copyProperties(balance,balanceResponse);

                    return  balanceResponse;
                }
        ).toList();
    }

    public Double getBalanceGrowthLastFiveMonths(List<BalanceResponse> balances) {
        if (balances == null || balances.size() < 5) return 0.0;

        var sorted = balances.stream()
                .sorted(Comparator.comparing(BalanceResponse::getReferenceDate))
                .toList();

        BigDecimal oldVal = sorted.get(sorted.size() - 5).getBalanceValue();
        BigDecimal newVal = sorted.get(sorted.size() - 1).getBalanceValue();

        if (oldVal.compareTo(BigDecimal.ZERO) == 0) return 0.0;

        return newVal.subtract(oldVal)
                .divide(oldVal, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();
    }
}
