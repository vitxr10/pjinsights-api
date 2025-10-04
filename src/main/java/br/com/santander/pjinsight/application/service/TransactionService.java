package br.com.santander.pjinsight.application.service;

import br.com.santander.pjinsight.application.model.request.TransactionRequest;
import br.com.santander.pjinsight.application.model.response.TransactionResponse;
import br.com.santander.pjinsight.domain.entity.Transaction;
import br.com.santander.pjinsight.infrastructure.repository.TransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepository repository;
    private final ObjectMapper objectMapper;

    public TransactionResponse save(TransactionRequest req) {
        Transaction tx = objectMapper.convertValue(req, Transaction.class);
        repository.save(tx);
        return objectMapper.convertValue(tx, TransactionResponse.class);
    }

    public TransactionResponse findById(UUID id) {
        Transaction tx = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        return objectMapper.convertValue(tx, TransactionResponse.class);
    }

    public List<TransactionResponse> findAll() {
        return repository.findAll().stream()
                .map(tx -> objectMapper.convertValue(tx, TransactionResponse.class))
                .collect(Collectors.toList());
    }

    public TransactionResponse update(TransactionRequest req) {
        Transaction tx = repository.getReferenceById(req.getId());
        setTransaction(tx, req);
        return objectMapper.convertValue(tx, TransactionResponse.class);
    }

    public TransactionResponse deleteById(UUID id) {
        Transaction tx = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        repository.deleteById(id);
        return objectMapper.convertValue(tx, TransactionResponse.class);
    }

    private void setTransaction(Transaction tx, TransactionRequest req) {
        BeanUtils.copyProperties(req,tx);
    }
}
