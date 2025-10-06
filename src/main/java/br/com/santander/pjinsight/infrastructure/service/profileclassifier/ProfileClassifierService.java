package br.com.santander.pjinsight.infrastructure.service.profileclassifier;

import br.com.santander.pjinsight.infrastructure.dto.request.ProfileClassifierRequest;
import br.com.santander.pjinsight.infrastructure.dto.response.ProfileClassifierResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileClassifierService {

    @Autowired
    private ProfileClassifierFeignClient client;

    @SneakyThrows
    public List<ProfileClassifierResponse> classifyProfile(List<ProfileClassifierRequest> profileClassifierRequest) {
        return client.classifyProfile(profileClassifierRequest).getBody();
    }
}
