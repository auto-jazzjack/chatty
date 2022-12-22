package io.ranslor.chatty.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String id;
    private String content;
    private String userName;
    private Date date;
    private String roomId;

}
