package br.com.santander.pjinsight.api.controller;

import br.com.santander.pjinsight.infrastructure.dto.request.AIRequest;
import br.com.santander.pjinsight.infrastructure.dto.response.AIResponse;
import br.com.santander.pjinsight.infrastructure.service.ai.AIService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
@AllArgsConstructor
public class AIController {

    private final AIService aiService;
//teste
    @PostMapping("/generate-from-prompt")
    public ResponseEntity<AIResponse> generateFromPrompt(@RequestBody AIRequest promptRequest) {
        var report = aiService.generateCompanyLifeMomentReport(promptRequest);
        return ResponseEntity.status(HttpStatus.OK).body(report);
    }

}
