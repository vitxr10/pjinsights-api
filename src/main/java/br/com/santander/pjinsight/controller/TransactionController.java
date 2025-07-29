package br.com.santander.pjinsight.controller;

import br.com.santander.pjinsight.dto.req.TransactionRequest;
import br.com.santander.pjinsight.dto.res.TransactionResponse;
import br.com.santander.pjinsight.service.TransactionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> findAll() {
        var list = transactionService.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> findById(@PathVariable UUID id) {
        var tx = transactionService.findById(id);
        return new ResponseEntity<>(tx, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> insert(@RequestBody @Valid TransactionRequest request) {
        var tx = transactionService.save(request);
        return new ResponseEntity<>(tx, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        transactionService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<TransactionResponse> update(@RequestBody @Valid TransactionRequest request) {
        var tx = transactionService.update(request);
        return new ResponseEntity<>(tx, HttpStatus.MOVED_PERMANENTLY);
    }
}
