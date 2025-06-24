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
    public String toString() {
        return "PlayerDTO{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
