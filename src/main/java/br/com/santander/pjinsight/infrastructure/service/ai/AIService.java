package br.com.santander.pjinsight.infrastructure.service.ai;

import br.com.santander.pjinsight.infrastructure.dto.request.AIRequest;
import br.com.santander.pjinsight.infrastructure.dto.response.AIResponse;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

@Service
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class AIService {

    private final Dotenv dotenv = Dotenv.load();

    public AIResponse generateCompanyLifeMomentReport(AIRequest request) {
        ChatModel aiChat = GoogleAiGeminiChatModel.builder().
                apiKey(dotenv.get("API_KEY"))
                .modelName(dotenv.get("AI_MODEL")).build();

        String response = aiChat.chat(request.getPrompt());
        return new AIResponse(response);
    }


}
