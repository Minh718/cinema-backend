package com.movie.messagingservice.entities;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ChatBoxPrivateKey implements Serializable {
    private String userId1;
    private String userId2;
}