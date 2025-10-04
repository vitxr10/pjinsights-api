package br.com.santander.pjinsight.application.service;

import br.com.santander.pjinsight.application.model.response.TransactionResponse;
import br.com.santander.pjinsight.domain.entity.Transaction;
import br.com.santander.pjinsight.infrastructure.repository.TransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepository repository;
    private final ObjectMapper objectMapper;

    public List<TransactionResponse> findAllByCnpj(String cnpj) {
        List<Transaction> transactions = repository.findBySenderCnpjOrReceiverCnpj(cnpj, cnpj);
        return transactions.stream()
                .map(t -> objectMapper.convertValue(t, TransactionResponse.class))
                .toList();
    }

    public Integer countTransactionsFirstMonthByCnpj(String cnpj) {
        return repository.countTransactionsFirstMonthByCnpj(cnpj);
    }

    public Integer countTransactionsLastMonthByCnpj(String cnpj) {
        return repository.countTransactionsLastMonthByCnpj(cnpj);
    }

    public Double calculateTransactionsGrowthByCnpj(String cnpj){
        return null;
    }

}
