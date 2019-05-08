import java.sql.*;
import java.util.ArrayList;

public class CharacterDB {

    static final String DB_URL = "jdbc:sqlite:character.db";

    static final String PLAYER_COL = "player";
    static final String CHARACTER_COL = "character";
    static final String GAME_COL = "game";

    CharacterDB(){
        createTable();
    }

    private static void createTable() {
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {

            String createCharacterTable = "CREATE TABLE IF NOT EXISTS characters (id INTEGER PRIMARY KEY, " +
                    "playerName TEXT, characterName TEXT, gameName TEXT, classBox INTEGER, raceBox INTEGER, " +
                    "alignmentBox INTEGER, levelBox INTEGER, npcBox BOOLEAN, backgroundArea TEXT, " +
                    "equipmentArea TEXT, spellsArea TEXT)";

            statement.executeUpdate(createCharacterTable);

        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
    }

    protected static void addCharacter(Character character) {

        final String addSql = "INSERT INTO characters (playerName, characterName, gameName, classBox, raceBox, " +
                "alignmentBox, levelBox, npcBox, backgroundArea, equipmentArea, spellsArea) " +
                "VALUES ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? )";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = connection.prepareStatement(addSql)) {

            ps.setString(1, character.getPlayerName());
            ps.setString(2, character.getCharacterName());
            ps.setString(3, character.getGameName());

            ps.setInt(4, character.getClassIndex());
            ps.setInt(5, character.getRaceIndex());
            ps.setInt(6, character.getAlignmentIndex());
            ps.setInt(7, character.getLevelIndex());

            ps.setBoolean(8, character.isNpcCheck());

            ps.setString(9, character.getBackground());
            ps.setString(10, character.getEquipment());
            ps.setString(11, character.getSpells());

            ps.execute();

        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
    }

    protected static void deleteCharacter(String name) {

        String deleteSql = "DELETE FROM characters WHERE playerName = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL);
        PreparedStatement deletePs = connection.prepareStatement(deleteSql)) {

            deletePs.setString(1, name);
            deletePs.execute();

        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
    }

    ArrayList<Character> fetchAllCharacters() {

        ArrayList<Character> allCharacters = new ArrayList<Character>();

        try (Connection connection = DriverManager.getConnection(DB_URL);
        Statement statement = connection.createStatement()) {
            String selectAllSQL = "SELECT * FROM characters";
            ResultSet rsAll = statement.executeQuery(selectAllSQL);

            while (rsAll.next()) {
                String player = rsAll.getString(PLAYER_COL);
                String character = rsAll.getString(CHARACTER_COL);
                String game = rsAll.getString(GAME_COL);
                Character characterRecord = new Character(player, character, game);
                allCharacters.add(characterRecord);
            }

            rsAll.close();

            return allCharacters;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
