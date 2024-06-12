package websocket.commands;

import chess.ChessMove;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class UserGameCommandTypeAdapter extends TypeAdapter<UserGameCommand> {
    @Override
    public void write(JsonWriter jsonWriter, UserGameCommand userGameCommand) throws IOException {
        Gson gson = new Gson();
        switch(userGameCommand.getCommandType()){
            case CONNECT -> gson.getAdapter(ConnectCommand.class).write(jsonWriter, (ConnectCommand) userGameCommand);
            case MAKE_MOVE -> gson.getAdapter(MakeMoveCommand.class).write(jsonWriter, (MakeMoveCommand) userGameCommand);
            case LEAVE -> gson.getAdapter(LeaveCommand.class).write(jsonWriter, (LeaveCommand) userGameCommand);
            case RESIGN -> gson.getAdapter(ResignCommand.class).write(jsonWriter, (ResignCommand) userGameCommand);
        }
    }

    @Override
    public UserGameCommand read(JsonReader jsonReader) throws IOException {
        UserGameCommand.CommandType commandType = null;
        String authToken = null;
        int gameID = 0;
        ChessMove move = null;

        Gson gson = new Gson();

        jsonReader.beginObject();

        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            switch (name) {
                case "commandType" -> commandType = UserGameCommand.CommandType.valueOf(jsonReader.nextString());
                case "authToken" -> authToken = jsonReader.nextString();
                case "gameID" -> gameID = jsonReader.nextInt();
                case "move" -> move = gson.fromJson(jsonReader, ChessMove.class);
            }
        }

        jsonReader.endObject();

        if(commandType == null) {
            return null;
        } else {
            return switch (commandType) {
                case CONNECT -> new ConnectCommand(authToken, gameID);
                //case MAKE_MOVE -> new MakeMoveCommand(authToken, gameID, move);
                case MAKE_MOVE -> null;
                case LEAVE -> null;
                case RESIGN -> null;
            };
        }
    }
}
