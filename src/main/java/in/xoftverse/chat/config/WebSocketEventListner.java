package in.xoftverse.chat.config;


import in.xoftverse.chat.model.ChatMessage;
import in.xoftverse.chat.model.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListner {

    private final SimpMessageSendingOperations messageSendingOperations;

    @EventListener
    public void handleWebSocketDisconnetListner(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if(username!=null){
            log.info("USER DISCONNECTED : {} ", username);
            var chatMessage = ChatMessage.builder().type(MessageType.LEAVE).sender(username).build();
            messageSendingOperations.convertAndSend("/topic/public", chatMessage);
        }
    }
}
