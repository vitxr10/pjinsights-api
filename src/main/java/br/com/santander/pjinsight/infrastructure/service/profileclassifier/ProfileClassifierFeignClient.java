package br.com.santander.pjinsight.infrastructure.service.profileclassifier;

import br.com.santander.pjinsight.application.model.request.CompanyRequest;
import br.com.santander.pjinsight.application.model.response.CompanyResponse;
import br.com.santander.pjinsight.infrastructure.dto.request.ProfileClassifierRequest;
import br.com.santander.pjinsight.infrastructure.dto.response.ProfileClassifierResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.cloud.openfeign.FeignClient;
import java.util.List;

@FeignClient(
        name = "profileClassifierClient",
        url = "${aws.api.profile-classifier.url}"
)
public interface ProfileClassifierFeignClient {
    @PostMapping(
            value = "/classify",
            consumes = "application/json",
            produces = "application/json"
    )
    ResponseEntity<List<ProfileClassifierResponse>> classify(@RequestBody List<ProfileClassifierRequest> request);
}

