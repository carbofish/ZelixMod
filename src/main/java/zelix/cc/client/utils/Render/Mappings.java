package zelix.cc.client.utils.Render;

import net.minecraft.entity.EntityLivingBase;

import java.util.List;

public class Mappings {
    List<Mapping> mappingList;
    public void add(Mapping mapping){
        if(!getMappingList().contains(mapping))
        {
            getMappingList().add(mapping);
        }
    }

    public List<Mapping> getMappingList() {
        return mappingList;
    }

    public boolean contains(EntityLivingBase entityLivingBase)
    {
        for (Mapping mapping : mappingList)
        {
            if(mapping.entity.getName().equals(entityLivingBase.getName())) {
                return true;
            }
        }
        return false;
    }
}
