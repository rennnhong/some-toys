package indi.rennnhong.staterkit.module.messagelist.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Attachment {
    @Id
    @GeneratedValue
    private UUID id;
    private long size;
    private String name;
    @Lob
    private byte[] file;
}
