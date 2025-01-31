package cz.cvut.fel.omo.semestralka.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class Message {
    private final String author;
    private final String content;
}
