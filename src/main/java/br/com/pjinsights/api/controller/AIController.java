package br.com.pjinsights.api.controller;

import br.com.pjinsights.infrastructure.dto.request.AIRequest;
import br.com.pjinsights.infrastructure.dto.response.AIResponse;
import br.com.pjinsights.infrastructure.service.ai.AIService;
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

    @PostMapping("/generate-from-prompt")
    public ResponseEntity<AIResponse> generateFromPrompt(@RequestBody AIRequest aiRequest) {
        var aiResponse = aiService.generateCompanyLifeMomentReport(aiRequest);
        return ResponseEntity.status(HttpStatus.OK).body(aiResponse);
    }

}
