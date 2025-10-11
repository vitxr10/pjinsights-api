package br.com.pjinsights.infrastructure.service.ai;

import br.com.pjinsights.domain.entity.Report;
import br.com.pjinsights.infrastructure.dto.request.AIRequest;
import br.com.pjinsights.infrastructure.dto.response.AIResponse;
import br.com.pjinsights.infrastructure.repository.ReportRepository;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@Service
public class AIService {

    @Autowired
    private ReportRepository reportRepository;
    @Value("${ai.model}")
    private String aiModel;
    @Value("${ai.apikey}")
    private String aiApiKey;

    public AIResponse generateCompanyLifeMomentReport(AIRequest request) {
        var reportEntity = reportRepository.findByCnpjAndReportType(request.getCnpj(), request.getReportType());

        if (reportEntity == null) {
            ChatModel aiChat = GoogleAiGeminiChatModel.builder()
                    .apiKey(aiApiKey)
                    .modelName(aiModel)
                    .build();

            String response = aiChat.chat(request.getPrompt());

            var report = new Report();
            report.setCnpj(request.getCnpj());
            report.setContent(compress(response));
            report.setReportType(request.getReportType());

            reportRepository.save(report);
            return new AIResponse(response);
        }

        String decompressedContent = decompress(reportEntity.getContent());
        return new AIResponse(decompressedContent);
    }

    public static String compress(String str) {
        if (str == null || str.isEmpty()) return str;

        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
             GZIPOutputStream gzip = new GZIPOutputStream(byteStream)) {

            gzip.write(str.getBytes(StandardCharsets.UTF_8));
            gzip.close();

            return Base64.getEncoder().encodeToString(byteStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao comprimir string", e);
        }
    }

    public static String decompress(String compressedStr) {
        if (compressedStr == null || compressedStr.isEmpty()) return compressedStr;

        byte[] bytes = Base64.getDecoder().decode(compressedStr);

        try (ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
             GZIPInputStream gzip = new GZIPInputStream(byteStream);
             InputStreamReader reader = new InputStreamReader(gzip, "UTF-8");
             BufferedReader in = new BufferedReader(reader)) {

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                sb.append(line).append("\n");
            }

            return sb.toString().trim();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao descomprimir string", e);
        }
    }
}
