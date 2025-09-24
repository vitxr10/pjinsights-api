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

import java.util.UUID;

@Service
@AllArgsConstructor
public class BalanceService {
    private final BalanceRepository balanceRepository;

    @Transactional
    public BalanceResponse save(BalanceRequest balanceRequest, UUID companyId) {
        Balance balance = new Balance();
        BeanUtils.copyProperties(balanceRequest,balance);
        balance.setCompanyId(companyId);
        balance = balanceRepository.save(balance);
        BalanceResponse balanceResponse = new BalanceResponse();
        BeanUtils.copyProperties(balance,balanceResponse);
        return balanceResponse;
    }

    public BalanceResponse findByCompanyId(UUID companyId) {
        BalanceResponse balanceResponse = new BalanceResponse();
        Balance balance = balanceRepository.findByCompanyId(companyId);
        BeanUtils.copyProperties(balance,balanceResponse);
        return  balanceResponse;
    }

}
