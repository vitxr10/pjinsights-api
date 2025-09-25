package br.com.santander.pjinsight.application.service;

import br.com.santander.pjinsight.application.model.request.BalanceRequest;
import br.com.santander.pjinsight.application.model.response.AddressResponse;
import br.com.santander.pjinsight.application.model.response.BalanceResponse;
import br.com.santander.pjinsight.domain.entity.Address;
import br.com.santander.pjinsight.domain.entity.Balance;
import br.com.santander.pjinsight.infrastructure.repository.BalanceRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

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

}
