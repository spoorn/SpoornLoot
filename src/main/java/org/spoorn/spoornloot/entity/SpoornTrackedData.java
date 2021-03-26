package org.spoorn.spoornloot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SpoornTrackedData implements Serializable {

    // UUID of entity that charmed this entity
    private UUID charmOwnerUUID;

    // Last tick time this entity was charmed.  Used to handle charm duration
    private long lastCharmTickTime;
}
