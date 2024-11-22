package com.shopfloor.backend.database.mappers;

import com.shopfloor.backend.api.transferobjects.editors.EditorEquipmentTO;
import com.shopfloor.backend.database.objects.EquipmentDBO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Mapper class for converting between EditorEquipmentTO and EquipmentDBO objects.
 * Provides methods to initialize and update EquipmentDBO entities.
 * @author David Todorov (https://github.com/david-todorov)
 */
@Component
public class EquipmentDBOMapper {

    /**
     * Initializes an EquipmentDBO from an EditorEquipmentTO and creator ID.
     * @param editorEquipmentTO the transfer object containing equipment details
     * @param creatorId the ID of the user creating the equipment
     * @return the initialized EquipmentDBO
     */
    public EquipmentDBO initializeEquipmentDBOFrom(EditorEquipmentTO editorEquipmentTO, Long creatorId) {
        EquipmentDBO equipmentDBO = new EquipmentDBO();
        equipmentDBO.setEquipmentNumber(editorEquipmentTO.getEquipmentNumber());
        equipmentDBO.setName(editorEquipmentTO.getName());
        equipmentDBO.setDescription(editorEquipmentTO.getDescription());
        equipmentDBO.setType(editorEquipmentTO.getType());

        equipmentDBO.setCreatedBy(creatorId);
        equipmentDBO.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        return equipmentDBO;
    }

    /**
     * Updates an existing EquipmentDBO from an EditorEquipmentTO and updater ID.
     * @param target the existing EquipmentDBO to update
     * @param source the transfer object containing updated equipment details
     * @param updaterId the ID of the user updating the equipment
     * @return the updated EquipmentDBO
     */
    public EquipmentDBO updateEquipmentDBOFrom(EquipmentDBO target, EditorEquipmentTO source, Long updaterId) {
        target.setEquipmentNumber(source.getEquipmentNumber());
        target.setName(source.getName());
        target.setType(source.getType());
        target.setDescription(source.getDescription());

        target.setUpdatedBy(updaterId);
        target.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        return target;
    }

}
