package com.project.osc.mapper;
import com.project.osc.model.Campaign;
import com.project.osc.model.OSC;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@Mapper
public interface CampaignMapper {

    List<Campaign> obtainCampaignList (@Param("idEmployee") Integer idEmployee);

}
