package br.com.santander.pjinsight.infrastructure.service.ai;

import br.com.santander.pjinsight.infrastructure.dto.request.AIRequest;
import br.com.santander.pjinsight.infrastructure.dto.response.AIResponse;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AIService {

    @Value("${ai.model}")
    private String aiModel;

    @Value("${ai.apikey}")
    private String aiApiKey;

    public AIResponse generateCompanyLifeMomentReport(AIRequest request) {
        ChatModel aiChat = GoogleAiGeminiChatModel.builder().
                apiKey(aiApiKey)
                .modelName(aiModel).build();

        String response = aiChat.chat(request.getPrompt());
        return new AIResponse(response);
    }


}
