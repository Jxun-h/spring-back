package its.backend.web.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Builder
public class TokenDto {
    @Getter
    private final String grantType;
    @Getter
    private final String accessToken;
    @Getter
    private final String refreshToken;

    public TokenDto(String grantType, String accessToken, String refreshToken) {
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return false;
        if (null == o || getClass() != o.getClass()) return false;
        TokenDto tokenDto = (TokenDto) o;
        return Objects.equals(grantType, tokenDto.grantType) &&
                Objects.equals(accessToken, tokenDto.accessToken) &&
                Objects.equals(refreshToken, tokenDto.refreshToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(grantType, accessToken, refreshToken);
    }

    @Override
    public String toString() {
        return "ToeknDto{" +
                "grantType='" + grantType + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' + "}";
    }
}
