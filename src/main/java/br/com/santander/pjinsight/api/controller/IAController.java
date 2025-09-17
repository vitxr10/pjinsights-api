package br.com.santander.pjinsight.api.controller;

import br.com.santander.pjinsight.application.model.request.IARequest;
import br.com.santander.pjinsight.application.model.response.IAResponse;
import br.com.santander.pjinsight.application.service.AIService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ia")
@AllArgsConstructor
public class IAController{

    private final AIService iaService;

    @PostMapping("/generateFromPrompt")
    public ResponseEntity<IAResponse> generateFromPrompt(@RequestBody IARequest promptRequest) {
        var report = iaService.generateCompanyLifeMomentReport(promptRequest);
        return ResponseEntity.status(HttpStatus.OK).body(report);
    }

}
