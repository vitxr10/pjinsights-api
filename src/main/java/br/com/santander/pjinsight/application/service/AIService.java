package br.com.santander.pjinsight.application.service;

import br.com.santander.pjinsight.application.model.request.IARequest;
import br.com.santander.pjinsight.application.model.response.IAResponse;
import br.com.santander.pjinsight.infrastructure.repository.CompanyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Map;
import java.util.StringJoiner;

@Service
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class AIService {

    private final Dotenv dotenv = Dotenv.load();
    private final ObjectMapper objectMapper;

    @Autowired
    private final CompanyRepository companyRepository;

    public IAResponse generateCompanyLifeMomentReport(IARequest request) {
        ChatModel aiChat = GoogleAiGeminiChatModel.builder().
                apiKey(dotenv.get("API_KEY"))
                .modelName(dotenv.get("AI_MODEL")).build();
        String response = aiChat.chat(request.getPrompt());
        return new IAResponse(response);
    }


}
