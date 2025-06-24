package at.aau.serg.websocketdemoserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class PlayerDTO {
    private int id;
    private String nickname;

    public PlayerDTO(int id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerDTO)) return false;
        PlayerDTO that = (PlayerDTO) o;
        return id == that.id &&
                Objects.equals(nickname, that.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nickname);
    }
    @Override
    public String toString() {
        return "PlayerDTO{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
