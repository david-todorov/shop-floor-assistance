package com.shopfloor.backend.database.mappers;

import com.shopfloor.backend.api.transferobjects.editors.EditorEquipmentTO;
import com.shopfloor.backend.database.objects.EquipmentDBO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class EquipmentDBOMapper {

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
