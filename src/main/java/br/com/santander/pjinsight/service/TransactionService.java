package br.com.santander.pjinsight.service;

import br.com.santander.pjinsight.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TransactionService {

    private TransactionRepository transactionRepository;

}
