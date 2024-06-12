package websocket.messages;

import chess.ChessMove;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import model.GameData;
import websocket.commands.*;

import java.io.IOException;

public class ServerMessageTypeAdapter extends TypeAdapter<ServerMessage> {
    @Override
    public void write(JsonWriter jsonWriter, ServerMessage serverMessage) throws IOException {
        Gson gson = new Gson();
        switch(serverMessage.getServerMessageType()){
            case LOAD_GAME -> gson.getAdapter(LoadGameMessage.class).write(jsonWriter, (LoadGameMessage) serverMessage);
            case NOTIFICATION -> gson.getAdapter(NotificationMessage.class).write(jsonWriter, (NotificationMessage) serverMessage);
            case ERROR -> gson.getAdapter(ErrorMessage.class).write(jsonWriter, (ErrorMessage) serverMessage);
        }
    }

    @Override
    public ServerMessage read(JsonReader jsonReader) throws IOException {
        ServerMessage.ServerMessageType serverMessageType = null;
        String errorMessage = null;
        GameData game = null;
        String message = null;

        Gson gson = new Gson();

        jsonReader.beginObject();

        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            switch (name) {
                case "serverMessageType" -> serverMessageType = ServerMessage.ServerMessageType.valueOf(jsonReader.nextString());
                case "errorMessage" -> errorMessage = jsonReader.nextString();
                case "game" -> game = gson.fromJson(jsonReader, GameData.class);
                case "message" -> message = jsonReader.nextString();
            }
        }

        jsonReader.endObject();

        if(serverMessageType == null) {
            return null;
        } else {
            return switch (serverMessageType) {
                case LOAD_GAME -> new LoadGameMessage(game);
                case ERROR -> new ErrorMessage(errorMessage);
                case NOTIFICATION -> new NotificationMessage(message);
            };
        }
    }

}
