package service.utils.aws;

import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.GetCallerIdentityResponse;

public class GetCallerIdentity {

    public static GetCallerIdentityResponse getCallerId() {
        try (StsClient stsClient = StsClient.builder().build()) {
            return stsClient.getCallerIdentity();
        }
    }
}