package at.aau.serg.websocketdemoserver.model.gamestate;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class GameState {

    private final GameBoard board;
    private List<Player> turnOrder;
    private final Map<Integer, Player> playersById;
    private int currentPlayerIndex;
    private int currentRound;
    private final List<Player> rankingList;

    @JsonIgnore
    private final Set<Integer> tilesBoughtThisTurn = new HashSet<>();

    @JsonIgnore
    private final Set<Integer> tilesBuiltThisTurn = new HashSet<>();

    public GameState() {
        this.board = new GameBoard();
        this.turnOrder = new ArrayList<>();
        this.playersById = new HashMap<>();
        this.currentPlayerIndex = 0;
        this.currentRound = 1;
        this.rankingList = new ArrayList<>();
    }

    /**
     * Initialisiert das Spiel mit zufälliger Spielerreihenfolge.
     */
    public void startGame(List<Player> players) {
        resetGame();
        Collections.shuffle(players);
        for (Player player : players) {
            playersById.put(player.getId(), player);
            turnOrder.add(player);
        }
    }

    public Player getCurrentPlayer() {
        if (turnOrder.isEmpty()) return null;
        return turnOrder.get(currentPlayerIndex);
    }

    public int getCurrentPlayerId() {
        Player current = getCurrentPlayer();
        return current != null ? current.getId() : -1;
    }

    public Player getPlayer(int id) {
        return playersById.get(id);
    }

    public Collection<Player> getAllPlayers() {
        return playersById.values();
    }

    public List<Player> getAlivePlayers() {
        return turnOrder.stream()
                .filter(Player::isAlive)
                .toList();
    }

    public void advanceTurn() {
        if (turnOrder.isEmpty()) return;

        int size = turnOrder.size();
        int initialIndex = currentPlayerIndex;

        // Reset per‐turn fields
        tilesBoughtThisTurn.clear();
        tilesBuiltThisTurn.clear();

        // Try each of the next “size” slots; exit as soon as we find an alive (& possibly suspended) player.
        for (int i = 0; i < size; i++) {
            currentPlayerIndex = (currentPlayerIndex + 1) % size;

            // If we’ve wrapped around back to 0 (and that wasn’t our starting spot), bump the round.
            if (currentPlayerIndex == 0 && currentPlayerIndex != initialIndex) {
                currentRound++;
            }

            Player current = getCurrentPlayer();
            current.setHasRolledDice(false);

            // If the player is dead, skip them and continue the loop.
            if (!current.isAlive()) {
                continue;
            }

            // If they’re alive but suspended, decrease suspension and end here (their turn is skipped).
            if (current.getSuspensionRounds() > 0) {
                current.decreaseSuspension();
            }

            // Alive and no suspension ⇒ this is the next player to act.
            return;
        }

        // If we fall out of the loop, it means we checked all “size” slots without finding an alive player.
        // In that case, nobody’s turn advances further.
    }


    public List<Player> getRankingList() {
        rankingList.clear();
        rankingList.addAll(playersById.values());
        rankingList.sort(Comparator.comparingInt(Player::calculateWealth).reversed());
        return rankingList;
    }

    public boolean isGameOver(int maxRounds, boolean roundsModeEnabled) {
        return roundsModeEnabled && currentRound > maxRounds;
    }

    public void resetGame() {
        Player.resetIdCounter();
        turnOrder.clear();
        playersById.clear();
        currentPlayerIndex = 0;
        currentRound = 1;
        tilesBoughtThisTurn.clear();
        tilesBuiltThisTurn.clear();
    }

    public boolean isPlayersTurn(int playerId) {
        return getCurrentPlayerId() == playerId;
    }

    // ==== Hausbau-Logik ====

    public void markTileBoughtThisTurn(int tilePos) {
        tilesBoughtThisTurn.add(tilePos);
    }

    public boolean wasTileBoughtThisTurn(int tilePos) {
        return tilesBoughtThisTurn.contains(tilePos);
    }

    public void markTileBuiltThisTurn(int tilePos) {
        tilesBuiltThisTurn.add(tilePos);
    }

    public boolean wasTileBuiltThisTurn(int tilePos) {
        return tilesBuiltThisTurn.contains(tilePos);
    }

}
