package server.WebSocket;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.UnauthorizedException;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;


@WebSocket
public class WebSocketHandler {

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws Exception {
//        try {
//            UserGameCommand command = Serializer.fromJson(message, UserGameCommand.class);
//
//            String username = getUsername(command.getAuthString());
//
//            saveSession(command.getGameID(), session);
//
//            switch (command.getCommandType()) {
//                case CONNECT -> connect(session, username, (ConnectCommand) command);
//                case MAKE_MOVE -> makeMove(session, username, (MakeMoveCommand) command);
//                case LEAVE -> leaveGame(session, username, (LeaveGameCommand) command);
//                case RESIGN -> resign(session, username, (ResignCommand) command);
//            }
//        } catch (UnauthorizedException e) {
//            sendMessage(session.getRemote(), new ErrorMessage("Error: unauthorized"));
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            sendMessage(session.getRemote(), new ErrorMessage("Error: " + ex.getMessage()));
//        }
    }


}
