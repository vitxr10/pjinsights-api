package br.com.santander.pjinsight.application.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MultipartRequest {
    private MultipartFile multipartFile;
}
