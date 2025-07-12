package br.com.santander.pjinsight.service;

import br.com.santander.pjinsight.dto.req.TransactionRequest;
import br.com.santander.pjinsight.dto.res.TransactionResponse;
import br.com.santander.pjinsight.model.Transaction;
import br.com.santander.pjinsight.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.santander.pjinsight.mapper.ObjectMapper.*;

@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepository repository;

    public TransactionResponse save(TransactionRequest req) {
        Transaction tx = repository.save(parseObject(req, Transaction.class));
        return parseObject(tx, TransactionResponse.class);
    }

    public TransactionResponse findById(String id) {
        Transaction tx = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        return parseObject(tx, TransactionResponse.class);
    }

    public List<TransactionRequest> findAll() {
        return parseListObjects(repository.findAll(), TransactionRequest.class);
    }

    public TransactionResponse update(TransactionRequest req) {
        Transaction tx = repository.getReferenceById(req.getId());
        setTransaction(tx, req);
        return parseObject(tx, TransactionResponse.class);
    }

    public TransactionResponse deleteById(String id) {
        Transaction tx = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        repository.deleteById(id);
        return parseObject(tx, TransactionResponse.class);
    }

    private void setTransaction(Transaction tx, TransactionRequest req) {
        tx.setDateTime(req.getDateTime());
        tx.setAmount(req.getAmount());
        tx.setCategory(req.getCategory());
        tx.setSenderId(req.getSenderId());
        tx.setReceiverId(req.getReceiverId());
        tx.setPreviousBalance(req.getPreviousBalance());
        tx.setNewBalance(req.getNewBalance());
        tx.setPaymentMethod(req.getPaymentMethod());
        tx.setStatus(req.getStatus());
        tx.setType(req.getType());
        tx.setDescription(req.getDescription());
    }
}
